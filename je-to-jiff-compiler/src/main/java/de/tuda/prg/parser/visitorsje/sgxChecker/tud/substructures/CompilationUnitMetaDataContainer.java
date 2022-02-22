package de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.collectors.Collector;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds all the information from a class-file, which is needed for the analysis.
 */
public class CompilationUnitMetaDataContainer {

    private static final Logger logger = LogManager.getLogger(CompilationUnitMetaDataContainer.class);

    //Object to collect all necessary information for the analysis
    private static final Collector collector = new Collector();

    private String qualifiedName;
    private CompilationUnit cu;
    private List<ImportDeclaration> imports;
    private boolean isEnclaveClass;
    private List<Field> secretFields = new ArrayList<>();

    private CompilationUnitMetaDataContainer(){}

    /**
     * Factory-Methode to create a new cuMDC-Object and initialize it
     * @param cu Compilation Unit need to gain needed information about the class-file
     * @return CompilationUnitMetaDataContainer
     */
    public static CompilationUnitMetaDataContainer createCuMDC(CompilationUnit cu){

        //Return null, if given CompilationUnit is no valid class
        List<ClassOrInterfaceDeclaration> qualifiers = cu.findAll(ClassOrInterfaceDeclaration.class);
        if(qualifiers.isEmpty()){ return null;}

        //Collecting Information which can be extracted from the CompilationUnit
        String qualifiedName = qualifiers.get(0).resolve().getQualifiedName();
        CompilationUnitMetaDataContainer cuMDC = new CompilationUnitMetaDataContainer();
        List<ImportDeclaration> imports = cu.getImports();

        logger.trace("Building CUMDC-Object for "+qualifiedName);

        //Saving these information in to the designated variables
        cuMDC.setQualifiedName(qualifiedName);
        cuMDC.setCu(cu);
        cuMDC.setImports(imports);

        //Extract information not accessible through the CompilationUnit via Collectors:
        //Checking if current CompilationUnit is a Enclave class or not
        collector.collectIsEnclave(cuMDC);
        //Collecting all Secret fields declared in this CompilationUnit
        collector.collectSecretFields(cuMDC);
        //Collecting all Gateway methods for the SymbolSolverSingleton to detect Gateway calls later
        collector.collectGatewayMethods(cuMDC);

        return cuMDC;
    }

    /**
     * Creates a copy of the current CompilationUnitMetaDataContainer.
     * Everything will be copied to avoid call by reference in some cases
     * @return New CompilationUnitMetaDataContainer object
     */
    public CompilationUnitMetaDataContainer copy() {
        CompilationUnitMetaDataContainer metaDataContainer = new CompilationUnitMetaDataContainer();
        metaDataContainer.setSecretFields(new ArrayList<>(this.secretFields));
        metaDataContainer.setImports(new ArrayList<>(this.imports));
        metaDataContainer.setIsEnclaveClass(this.isEnclaveClass);
        metaDataContainer.setCu(this.cu.clone());
        return metaDataContainer;
    }

    //SETTERS:
    public void setCu(CompilationUnit cu) {
        this.cu = cu;
    }

    public void setQualifiedName(String qualifiedName){
        this.qualifiedName = qualifiedName;
    }

    public void setImports(List<ImportDeclaration> imports) {
        this.imports = imports;
    }

    public void setIsEnclaveClass(boolean isEnclaveClass) {
        this.isEnclaveClass = isEnclaveClass;
    }

    public void setSecretFields(List<Field> secretFields) {
        this.secretFields = secretFields;
    }

    //GETTERS:
    public List<Field> getSecretFields() {
        return secretFields;
    }

    public boolean isEnclaveClass() {return isEnclaveClass;}

    public CompilationUnit getCu() {
        return cu;
    }

    public String getQualifiedName(){
        return this.qualifiedName;
    }

    public List<ImportDeclaration> getImports() {
        return imports;
    }
}

