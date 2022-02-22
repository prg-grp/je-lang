package de.tuda.prg.parser.visitorsje.sgxChecker.tud.collectors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tuda.prg.parser.visitorsje.sgxChecker.tud.enums.AnnotationEnum;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.resolver.TypeResolver;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures.CompilationUnitMetaDataContainer;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures.Field;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures.SymbolSolverSingleton;

/**
 * Collecting all relevant information needed for analysis and save them in the given CUMDC object
 */
public class Collector {

    private final SymbolSolverSingleton gatewayRegister = SymbolSolverSingleton.getInstance();
    private static final Logger logger = LogManager.getLogger(Collector.class);

    /**
     * Check if class is enclave class and save the result in CUMDC object.
     * @param cuMDC CompilationUnit Metadata Container from the class file/compilation unit being analyzed
     */
    public void collectIsEnclave(CompilationUnitMetaDataContainer cuMDC) {

        CompilationUnit cu = cuMDC.getCu();
        List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarationList = cu.findAll(ClassOrInterfaceDeclaration.class);
        List<AnnotationExpr> annotations = classOrInterfaceDeclarationList.get(0).getAnnotations();

        //Set the IsEnclaveClass whether there is an enclave annotation or not
        cuMDC.setIsEnclaveClass(annotations.toString().contains(AnnotationEnum.ENCLAVE.toString()));
        logger.trace("Set IsEnclaveClass to "+cuMDC.isEnclaveClass());
    }

    /**
     * Collecting Gateway Methods of the given CUMDC object (Class)
     * @param cuMDC CompilationUnit Metadata Container from the class file/compilation unit being analyzed
     */
    public void collectGatewayMethods(CompilationUnitMetaDataContainer cuMDC){

        logger.trace("Collecting Gateway methods for SymbolSolverSingleton");

        CompilationUnit cu = cuMDC.getCu();
        //Collecting all method declarations of this class if they contain a Gateway annotation
        List<String> gatewayMethods = cu.findAll(MethodDeclaration.class)
                .stream()
                .filter(methodDeclaration -> methodDeclaration.getAnnotations().toString().contains(AnnotationEnum.GATEWAY.toString()))
                .map(methodDeclaration -> methodDeclaration.resolve().getQualifiedName())
                .collect(Collectors.toList());

        gatewayRegister.gatewayRegister.addAll(gatewayMethods);
    }

    /**
     * Collecting all fields with a Secret annotation and saving them back in the CUMDC object
     * @param cuMDC CompilationUnit Metadata Container from the class file/compilation unit being analyzed
     */
    public void collectSecretFields(CompilationUnitMetaDataContainer cuMDC){

        logger.trace("Collecting Secret fields");

        //return if class is no Enclave class (Secret fields should be assigned only in Enclave classes)
        if(!cuMDC.isEnclaveClass()){ return; }

        CompilationUnit cu = cuMDC.getCu();
        List<Field> secretFields = new ArrayList<>();

        //collect all field declaration with a Secret field annotation and create a Field object from it
        cu.findAll(FieldDeclaration.class).forEach( fieldDeclaration -> {
            List<AnnotationExpr> annotations = fieldDeclaration.getAnnotations();
            if(annotations.toString().contains(AnnotationEnum.SECRET.toString())) {
                Field newField = this.extractSecretField(fieldDeclaration, cuMDC);
                secretFields.add(newField);
            }
        });

        cuMDC.setSecretFields(secretFields);
    }

    /**
     * This function extracts all information from a field declaration to create a new Field object
     * @param fieldDeclaration field declaration which contains the information for the creation of the Field object
     * @param cuMDC CUMDC object, needed for the TypeResolver
     * @return Field object for a given Secret field
     */
    private Field extractSecretField(FieldDeclaration fieldDeclaration, CompilationUnitMetaDataContainer cuMDC){
        Field newField = new Field();
        for(VariableDeclarator variable: fieldDeclaration.getVariables()) {
            newField.setModifiers(fieldDeclaration.getModifiers());
            newField.setName(variable.getNameAsString());
            newField.setType(TypeResolver.resolve(variable.getType(), cuMDC));
            newField.setPrimitive(variable.getType().isPrimitiveType());
            newField.setVoidType(variable.getType().isVoidType());
        }
        return newField;

    }
}
