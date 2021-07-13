package de.tuda.prg.taskprocessing;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import de.tuda.prg.constants.FileNames;
import de.tuda.prg.constants.PathValues;
import de.tuda.prg.constants.RMIConstants;
import de.tuda.prg.entities.ClassNameMethodDecls;
import de.tuda.prg.exceptions.FileIOException;
import de.tuda.prg.filehandling.FileUtils;
import de.tuda.prg.parser.EnclaveClassTranslationUtils;
import de.tuda.prg.parser.ParserHelper;
import de.tuda.prg.parser.visitorsremotecom.NonEnclaveMethodCallVisitor;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class RMICodeAdderTask implements CodeXformationTask {

    @Override
    public void run(File jeSrcDir, GlobalTaskData interTaskData) {
        // Second try block, adding RMI code to enclave classes
        try {
            System.out.println("Names of the generated wrapper classes = "+interTaskData.enclaveClassesToExposeNames);
            // Scanning the directory
            File[] directoryListing = jeSrcDir.listFiles();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    String currentFileName = file.getName();
                    System.out.println("Currently processing : "+currentFileName);
                    String currentFileBaseName = FilenameUtils.removeExtension(currentFileName);
                    if (FilenameUtils.getExtension(file.getPath()).equals("java")) {  //Change this later

                        // final CompilationUnit cu = StaticJavaParser.parse(new File(file));
                        final CompilationUnit cu = StaticJavaParser.parse(file);

                        if (ParserHelper.isClassAnnotatedWithEnclaveAnnotation(cu)) {
                            EnclaveClassTranslationUtils.addCommCodeToEnclaveClasses(cu, interTaskData.enclaveClassesToExposeNames);

                            String enclaveJavaCodeWithCommString = cu.toString();
                            // System.out.println("----------- Enclave file with the added RMI code -----------------");
                            // System.out.println(enclaveJavaCodeWithCommString);
                            // System.out.println("---------------------Removing JE annotations -------------------------");
                            EnclaveClassTranslationUtils.removeAllJEConstructs(cu);  // check this step, since adding communication is dependent on the annotations such as Gateway, we remove the JE language features after adding the communication code.
                            String enclaveJavaCodeWithCommNoJEAnnoString = cu.toString();

                            FileUtils.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX+currentFileBaseName+".java", enclaveJavaCodeWithCommNoJEAnnoString);

                        } else {
                            System.out.println("Not an enclave class");
                        }
                    }
                    // Do something with child
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (FileIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Third try block, adding RMI code to the non-enclave classes
        try {
            // Scanning the directory
            File[] directoryListing = jeSrcDir.listFiles();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    String currentFileName = file.getName();
                    System.out.println("(NonEnclaveComm) Currently processing : " + currentFileName);
                    String currentFileBaseName = FilenameUtils.removeExtension(currentFileName);
                    if (FilenameUtils.getExtension(file.getPath()).equals("java")) {  //Change this later
                        // final CompilationUnit cu = StaticJavaParser.parse(new File(file));
                        final CompilationUnit cu = StaticJavaParser.parse(file);
                        if (!ParserHelper.isClassAnnotatedWithEnclaveAnnotation(cu)) {
                            cu.addImport(RMIConstants.javaRMIAll);  // Not an optimal place to add RMI imports, the class may not contain any enclave calls at all.
                            NonEnclaveMethodCallVisitor nonEnclaveMCVisitor = new NonEnclaveMethodCallVisitor();
                            nonEnclaveMCVisitor.visit(cu, interTaskData.gatewayMethodNames);  // TODO: We can't add all the remote methods into the single interface, what interfaces to be imported will be decided by this visit method.
                            String nonEnclaveClassString = cu.toString();
                            // System.out.println("------------  NonEnclave Class with added remote comm --------------------");
                            // System.out.println(nonEnclaveClassString);

                            FileUtils.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX+currentFileBaseName+".java", nonEnclaveClassString);
                        } else {
                            System.out.println("An enclave class");
                        }
                    }
                }
            }
            // Adding non-enclave wrapper class
            final CompilationUnit cu = new CompilationUnit();
            cu.addImport(RMIConstants.javaRMIAll);
            final ClassOrInterfaceDeclaration nonEnclWrapperClass = cu.addClass(FileNames.NON_ENCLAVE_WRAPPER_CLASS_BASE_NAME); // This will contain wrapper method definitions of all the gateway methods from all the enclave classes
            for (ClassNameMethodDecls classNameMthdDcl: interTaskData.gatewayMethodDeclarations) {
                String enclaveClassName = classNameMthdDcl.getEnclaveClassName();
                for (MethodDeclaration md: classNameMthdDcl.getMethodDeclarations()) {
                    MethodDeclaration nonEnclWrapperMethodInClass = nonEnclWrapperClass.addMethod(md.getNameAsString(), Modifier.Keyword.PUBLIC, Modifier.Keyword.STATIC);
                    ParserHelper.populateNonEnclaveWrapperMethod(md, enclaveClassName, nonEnclWrapperMethodInClass);
                }
            }
            final String nonEnclWrapperClassAsString =  cu.toString();
            System.out.println("-------------- Printing the created non enclave wrapper class ---------------------------");
            System.out.println(nonEnclWrapperClassAsString);
            FileUtils.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX + FileNames.NON_ENCLAVE_WRAPPER_CLASS_BASE_NAME + ".java", nonEnclWrapperClassAsString);   //Ideally, this file writing should be in some file writing utility class.

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (FileIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
