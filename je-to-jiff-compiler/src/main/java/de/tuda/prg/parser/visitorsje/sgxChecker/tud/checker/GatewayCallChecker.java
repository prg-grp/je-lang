package de.tuda.prg.parser.visitorsje.sgxChecker.tud.checker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures.CompilationUnitMetaDataContainer;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures.SymbolSolverSingleton;

public class GatewayCallChecker {

    private static final Logger logger = LogManager.getLogger(GatewayCallChecker.class);

    private final SymbolSolverSingleton symbolSolverSingleton = SymbolSolverSingleton.getInstance();

    /**
     * Checking if objects are passed to Gateway method calls
     * @param cuMDC CompilationUnit Metadata Container from the class file/compilation unit being analyzed
     */
    public void check(CompilationUnitMetaDataContainer cuMDC){
        if(cuMDC.isEnclaveClass()){
            return;
        }

        CompilationUnit cu = cuMDC.getCu();

        //Collecting all Gateway method calls
        List<MethodCallExpr> gatewayCallList = cu.findAll(MethodCallExpr.class)
                .stream()
                .filter(methodCallExpr -> symbolSolverSingleton.gatewayRegister.contains(methodCallExpr.resolve().getQualifiedName()))
                .collect(Collectors.toList());

        List<String> objectParams = new ArrayList<>();

        for(MethodCallExpr methodCallExpr: gatewayCallList){

            JavaParserFacade symbolSolver = symbolSolverSingleton.getSymbolSolver();
            List<Expression> arguments = methodCallExpr.getArguments();

            //If nor arguments are provided, no check needed
            if(arguments.isEmpty()){
                continue;
            }

            SymbolReference<ResolvedMethodDeclaration> maybeMethodDeclaration = symbolSolver.solve(methodCallExpr);
            Optional<MethodDeclaration> methodDeclaration=  maybeMethodDeclaration.getCorrespondingDeclaration().toAst();

            if(methodDeclaration.isPresent()) {
                NodeList<Parameter> params = methodDeclaration.get().getParameters();

                //Collecting object parameters (primitives not important -> call by value)
                for(Parameter param: params){
                    if(!param.getType().isPrimitiveType()){
                        int index = params.lastIndexOf(param);
                        Expression object = methodCallExpr.getArguments().get(index);
                        if(object.getRange().isPresent()){
                            int line = object.getRange().get().begin.line;
                            objectParams.add("[Line: "+line+" "+methodCallExpr.resolve().getQualifiedName());
                            continue;
                        }
                        objectParams.add("[Missing Line] %s(%s)".format( methodCallExpr.resolve().getQualifiedName(), object.toString()));
                    }
                }
            }
        }

        if(!objectParams.isEmpty()){
            String msg = String.join(", ", objectParams);
            logger.warn("Gateway-Method call. Following objects could remain in old state: %s".format(msg));
        }
    }
}
