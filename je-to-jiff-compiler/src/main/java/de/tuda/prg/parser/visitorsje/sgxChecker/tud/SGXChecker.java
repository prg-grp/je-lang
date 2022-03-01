package de.tuda.prg.parser.visitorsje.sgxChecker.tud;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.tuda.prg.parser.visitorsje.sgxChecker.tud.checker.GatewayCallChecker;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures.CompilationUnitMetaDataContainer;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures.SymbolSolverSingleton;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.visitors.EnclaveClassVisitor;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.visitors.GatewayMethodVisitor;
import javassist.expr.Instanceof;

public class SGXChecker {

    private static final Logger logger = LogManager.getLogger(SGXChecker.class);

    private final EnclaveClassVisitor enclaveClassVisitor = new EnclaveClassVisitor();
    private final GatewayCallChecker gatewayCallChecker = new GatewayCallChecker();

    //Path where the sgxif.config.json is located, set by the constructor
    private String configPath;

    //Path variables which are extracted int the readConfig-function from the sgxif.config.json
    //Path where the packages with the class files are located in the project
    private String packagePath;
    //Repo with all external libraries from the given project
    private String jarRepo;

    //A list containing all paths to external libraries, need to configure the SymbolSolver
    private final List<File> jarList = new ArrayList<>();
    //A List with all CUMDC-Objects (is created for every class-file and contains the CompilationUnit)
    private final List<CompilationUnitMetaDataContainer> cuMdcList = new ArrayList<>();

    /**
     * @param configPath path to sgxif.config.json
     */
    public SGXChecker(String configPath) {
        //this.configPath = configPath; // + "/sgxif.config.json";
        this.readConfig(configPath);
        this.configureStaticJavaParser();
        logger.trace("Collecting Class-Files in %s and create for each a CompilationUnitMetaDataContainer".format(this.packagePath));
        this.fileConsumer(this.packagePath, this::addCuMDC, "java");
    }

    /**
     * Collecting iteratively files from a given path and calling a consumer for a given file-typ
     * @param rootDir root-directory to start iterative search for java-files
     * @param consumer consumer to work with collected files
     * @param fileEnding File-typ, which should be consumed
     */
    private void fileConsumer(String rootDir, Consumer<File> consumer, String fileEnding) {
        System.out.println("root: "+rootDir.toString());
        final File folder = new File(rootDir);

        for (final File file : Objects.requireNonNull(folder.listFiles())) {
            if(file.isDirectory()) { fileConsumer(file.getPath(), consumer, fileEnding); }
            else if(FilenameUtils.getExtension(file.getName()).equals(fileEnding)) { consumer.accept(file); }
        }
    }

    /**
     * Consumer for fileConsumer. Adding jar-Files to the jarList
     * @param file jar-File to be added to jarList
     */
    private void addJarFiles(File file){
        this.jarList.add(file);
    }

    /**
     * Configure the StaticJavaParser for parsing the CompilationUnit in addCuMDC and setting the static SymbolSolver for the class SymbolSolverSingleton.
     */
    private void configureStaticJavaParser(){
        logger.trace("Build SymbolSolver via CombinedTypeSolver");
        this.fileConsumer(this.jarRepo, this::addJarFiles, "jar");

        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new JavaParserTypeSolver(this.packagePath));
        typeSolver.add(new ReflectionTypeSolver());

        this.jarList.forEach(jarPath -> {
            try {
                typeSolver.add(new JarTypeSolver(jarPath));
            } catch ( IOException e){
                logger.error("Can't create JarTypeSolver for "+jarPath.toString());
            }
        });

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        SymbolSolverSingleton.getInstance().setSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
    }

    /**
     * Consumer for fileConsumer. Parsing the CompilationUnit from a given file and create a new CompilationUnitMetaDataContainer for it.
     * @param file Java-File for parsing the CompilationUnit
     */
    private void addCuMDC(File file){
        try{
            CompilationUnit cu = StaticJavaParser.parse(file);
            CompilationUnitMetaDataContainer cuMDC = CompilationUnitMetaDataContainer.createCuMDC(cu);
            if(cuMDC != null){ cuMdcList.add(cuMDC); }

        } catch(FileNotFoundException e){
            logger.error("Failed to build CompilationUnitMetaDataContainer for File %s".format(file.getAbsolutePath()));
        }
    }

    /**
     * Execute Visitors and Checkers for every CompilationUnitMetaDataContainer(class-file) in cuMdcList
     */
    public void analyze() {



        for (CompilationUnitMetaDataContainer cuMDC : cuMdcList) {

            CompilationUnit cu = cuMDC.getCu();
            GatewayMethodVisitor gatewayMethodVisitor = new GatewayMethodVisitor();

            logger.info("Checking file: %s".format(cuMDC.getQualifiedName()));

            try {
                this.enclaveClassVisitor.visit(cu, cuMDC);
                this.gatewayCallChecker.check(cuMDC);
                gatewayMethodVisitor.visit(cu, cuMDC);
                System.out.println("");
            } catch (Exception e) {
                if (e instanceof UnsupportedOperationException) {
                    // As the imported project only checks one files,
                    // Own Dependencies wont be recognized.
                    System.out.println("All good, OwnWrittenClass");
                } else {
                    System.out.println("Failure for file in enclaveClassVisitor: "+e.toString());
                    logger.error("Failure for file: %s\n%s".format(cuMDC.getQualifiedName(), e.getMessage()));
                }
            }
        }
    }

    /**
     * Saving the Fields set in the sgxif.config.json into Member-Variables
     */
    private void readConfig(String configPath) {
        logger.trace("Reading "+configPath.toString());
        this.packagePath = configPath;
        this.configPath = "/Users/robert/je-lang/je-to-jiff-compiler/src/main/java/de/tuda/prg/parser/visitorsje/sgxChecker/tud/sgxif.config.json";
        this.jarRepo = configPath;
        logger.trace("Reading "+configPath.toString());
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(this.configPath));
            JSONObject jsonObject = (JSONObject) obj;
            this.jarRepo = jsonObject.get("jarRepo").toString();
            logger.trace("JarRepo = "+this.jarRepo+", packagePath = "+this.packagePath.toString());
        }
        catch (IOException | ParseException fileNotFoundException) {
            logger.fatal("Need a proper sgxif.config.json-File. Given path: "+this.configPath.toString());
            System.exit(-1);
        }
    }
}
