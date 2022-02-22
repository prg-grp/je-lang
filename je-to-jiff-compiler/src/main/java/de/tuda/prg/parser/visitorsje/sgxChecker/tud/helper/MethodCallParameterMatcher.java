package de.tuda.prg.parser.visitorsje.sgxChecker.tud.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tuda.prg.parser.visitorsje.sgxChecker.tud.resolver.MethodDeclarationSolver;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures.CompilationUnitMetaDataContainer;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures.Field;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures.Method;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures.SymbolSolverSingleton;

/**
 * This class contains a static match-Method which matches the parameters of a given method call expression with the
 * parameters declared in its corresponding declaration.
 * It further updates the secret list contained in the passed CompilationUniMetaDataContainer if any @Secret variable
 * was passed into the called method.
 */
public class MethodCallParameterMatcher {

    private final static Logger logger = LogManager.getLogger(MethodCallParameterMatcher.class);

    private final static JavaParserFacade symbolSolver = SymbolSolverSingleton.getInstance().getSymbolSolver();

    /**
     * This method matches the arguments of a given method call expression and its corresponding method declaration.
     * If any @Secret variable is passed in the method call, the @Secret list will be updated using the following rules:
     *  1. If the called method resides in the same class as the caller, the secret list is extended in case of a
     *      passed @Secret variable.
     *  2. If the called method is an external method, a copy of the passed MetaDataContainer will be made and the
     *      current @Secret list will be replaced with the @Secret list which holds all @Secret variables which were
     *      passed to the called method (can be empty).
     * @param methodCallExpr Method call expression for which parameters should be matched.
     * @param metaDataContainer CompilationUniMetaDataContainer containing the CU where the method is located in.
     * @return New CompilationUniMetaDataContainer.
     */
    public static CompilationUnitMetaDataContainer match(MethodCallExpr methodCallExpr,
                                                         CompilationUnitMetaDataContainer metaDataContainer) {

        MethodDeclaration methodDeclaration = MethodDeclarationSolver.solve(symbolSolver, methodCallExpr);
        if(methodDeclaration != null) {
            List<MethodDeclaration> clsMethodDeclarations = metaDataContainer.getCu().findAll(MethodDeclaration.class);
            List<Method> clsMethods = clsMethodDeclarations.stream()
                    .map(clsMethodDeclaration -> Method.fromDeclaration(clsMethodDeclaration, metaDataContainer))
                    .collect(Collectors.toList());

            Method analyzedMethod = Method.fromDeclaration(methodDeclaration, metaDataContainer);

            if(clsMethods.stream().anyMatch(clsMethod -> clsMethod.equals(analyzedMethod))) {
                metaDataContainer.setSecretFields(getUpdatedSecretsForClassCall(methodCallExpr, metaDataContainer));
                return metaDataContainer;
            }
            else {
                CompilationUnitMetaDataContainer metaDataContainerCopy = metaDataContainer.copy();
                metaDataContainerCopy.setSecretFields(getUpdatedSecretsForExternalCall(methodCallExpr, metaDataContainer));
                return metaDataContainerCopy;
            }
        }
        else {
            logger.error("Method cannot be resolved: %s".format(methodCallExpr.toString()));
            return null;
        }
    }

    /**
     * Update secrets array in case of inner-class call
     * @param methodCallExpr Method call
     * @return Updated secrets
     */
    private static List<Field> getUpdatedSecretsForClassCall(MethodCallExpr methodCallExpr,
                                                             CompilationUnitMetaDataContainer metaDataContainer) {
        //To Prevent java.util.ConcurrentModificationException a new List is created
        List<Field> currentSecrets = new ArrayList<>(metaDataContainer.getSecretFields());
        return updateSecretList(methodCallExpr, currentSecrets, metaDataContainer);
    }

    /**
     * Replace secrets array in case of external call
     * @param methodCallExpr Method call
     * @return Updated secrets
     */
    private static List<Field> getUpdatedSecretsForExternalCall(MethodCallExpr methodCallExpr,
                                                                CompilationUnitMetaDataContainer metaDataContainer) {
        // in case of an external call the current secret list has to be empty.
        List<Field> currentSecrets = new ArrayList<>();
        return updateSecretList(methodCallExpr, currentSecrets, metaDataContainer);
    }

    /**
     * This method returns an array of the argument names as they are set in the declaration of the called method.
     * @param methodCallExpr Method call for which parameter are matched.
     * @return Array of argument names in method declaration
     */
    private static List<String> getDeclarationArguments(MethodCallExpr methodCallExpr) {
        List<String> paramNameList = new ArrayList<>();
        SymbolReference<ResolvedMethodDeclaration> rmd = symbolSolver.solve(methodCallExpr);
        Optional<MethodDeclaration> maybeMethodDeclaration = rmd.getCorrespondingDeclaration().toAst();
        maybeMethodDeclaration.ifPresent(methodDeclaration -> paramNameList.addAll(methodDeclaration.getParameters()
                .stream()
                .map(NodeWithSimpleName::getNameAsString)
                .collect(Collectors.toList())
        ));
        return paramNameList;
    }

    /**
     * This method returns an array of names of the arguments passed at the call.
     * @param methodCallExpr Method call expression for which parameters are matched
     * @return Array of passed argument names
     */
    private static List<String> getCallArguments(MethodCallExpr methodCallExpr) {
        return methodCallExpr
                .getArguments()
                .stream()
                .map(arguments -> {
                    if(arguments.isFieldAccessExpr()){
                        return arguments.asFieldAccessExpr().getNameAsString();
                    }
                    return arguments.asNameExpr().getNameAsString();
                })
                .collect(Collectors.toList());
    }

    /**
     * Iterate over all arguments of a given method call expression and check whether a secret variable is passed.
     * In case one is passed, add it to the given list to be updated.
     * @param methodCallExpr Method call
     * @param listToUpdate List to be updated
     */
    private static List<Field> updateSecretList(MethodCallExpr methodCallExpr, List<Field> listToUpdate,
                                         CompilationUnitMetaDataContainer metaDataContainer) {

        List<String> argumentNameList = getCallArguments(methodCallExpr);
        List<String> paramNameList = getDeclarationArguments(methodCallExpr);


        for(Field oldField: metaDataContainer.getSecretFields()){
            String fieldName = oldField.getName();
            if(argumentNameList.toString().contains(fieldName)){
                int index = argumentNameList.indexOf(fieldName);
                Field newField = oldField.clone();
                newField.setName(paramNameList.get(index));
                listToUpdate.add(newField);
            }
        }

        return listToUpdate;
    }
}
