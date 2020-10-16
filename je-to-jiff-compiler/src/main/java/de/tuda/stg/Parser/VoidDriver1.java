package de.tuda.stg.Parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import de.tuda.stg.Constants.Codes;
import de.tuda.stg.Constants.PathValues;
import de.tuda.stg.Parser.VisitorsRemoteCom.NonEnclaveMethodCallVisitor;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;

public class VoidDriver1 {

    public static void main(String[] args) {
        // JE_FOLDER_PATH = args[0];
        // GENERATED_JIF_FOLDER_PREFIX = args[1];
        final HashSet<String> enclaveClassNames = new HashSet<>();
        final HashSet<String> gatewayMethodNames = new HashSet<>();


        // First try block, translation of Enclave classes to Jif
        try {
            // Scanning the directory
            File jeSrcDir = new File(PathValues.JE_FOLDER_PATH);
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

                            //Extracting the names of the Gateway methods inside the Enclave Class.
                            ParserHelper.getAllGatewayMethods(cu, gatewayMethodNames);

                            EnclaveClassTranslationUtils.translateEnclaveClass(cu);

                            String afterVisitClassString = cu.toString();
                            System.out.println(afterVisitClassString);

                            System.out.println("--------------------------------");

                            System.out.println("---------------- Now replacing the added codes ----------------");
                            String jifCodeAddedString = afterVisitClassString;

                            for (String key : Codes.strReplacement.keySet()) {
                                jifCodeAddedString = jifCodeAddedString.replace(key, Codes.strReplacement.get(key));
                            }

                            System.out.println("----------------------------------Printing the files with Jif labels --------------------------------");
                            System.out.println(jifCodeAddedString);
                            // Writing generated jif code to the file
                            ParserHelper.writeStringToFile(PathValues.GENERATED_JIF_FOLDER_PREFIX+currentFileBaseName+".jif", jifCodeAddedString);
                        }
                        else {
                            System.out.println("Not an enclave class");
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Second try block, adding RMI code to enclave classes
        try {
            // Scanning the directory
            File jeSrcDir = new File(PathValues.JE_FOLDER_PATH);
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
                            EnclaveClassTranslationUtils.addCommCodeToEnclaveClasses(cu);

                            String enclaveJavaCodeWithCommString = cu.toString();
                            System.out.println("----------- Enclave file with added RMI code -----------------");
                            System.out.println(enclaveJavaCodeWithCommString);


                            System.out.println("---------------------Removing JE annotations -------------------------");
                            EnclaveClassTranslationUtils.removeAllJEConstructs(cu);  // check this step, since adding communication is dependent on the annotations such as Gateway, we remove the JE language features after adding the communication code.
                            String enclaveJavaCodeWithCommNoJEAnnoString = cu.toString();

                            ParserHelper.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX+currentFileBaseName+".java", enclaveJavaCodeWithCommNoJEAnnoString);

                        } else {
                            System.out.println("Not an enclave class");
                        }
                    }
                    // Do something with child
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Third try block, adding RMI code to the non-enclave classes
        try {
            // Scanning the directory
            File jeSrcDir = new File(PathValues.JE_FOLDER_PATH);
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
                            NonEnclaveMethodCallVisitor nonEnclaveMCVisitor = new NonEnclaveMethodCallVisitor();
                            nonEnclaveMCVisitor.visit(cu, gatewayMethodNames);
                            String nonEnclaveClassString = cu.toString();
                            System.out.println("------------  NonEnclave Class with added remote comm --------------------");
                            System.out.println(nonEnclaveClassString);

                            ParserHelper.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX+currentFileBaseName+".java", nonEnclaveClassString);
                        } else {
                            System.out.println("An enclave class");
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
