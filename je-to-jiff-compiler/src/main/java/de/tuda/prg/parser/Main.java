package de.tuda.prg.parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import de.tuda.prg.constants.Codes;
import de.tuda.prg.constants.FileNames;
import de.tuda.prg.constants.PathValues;
import de.tuda.prg.constants.RMIConstants;
import de.tuda.prg.exceptions.FileIOException;
import de.tuda.prg.filehandling.FileUtils;
import de.tuda.prg.parser.visitorsremotecom.NonEnclaveMethodCallVisitor;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        if (args == null || args.length < 3) {

        } else {
            PathValues.JE_FOLDER_PATH = args[0];
            PathValues.GENERATED_JIF_FOLDER_PREFIX = args[1];
            PathValues.GENERATED_JAVA_FOLDER_PREFIX = args[2];
        }

        System.out.println("JE folder path = "+PathValues.JE_FOLDER_PATH);
        System.out.println("Generated Jif folder path = "+PathValues.GENERATED_JIF_FOLDER_PREFIX);
        System.out.println("Generated Java folder path = "+PathValues.GENERATED_JAVA_FOLDER_PREFIX);


        final HashSet<String> enclaveClassNames = new HashSet<>();
        final HashSet<String> gatewayMethodNames = new HashSet<>();
        final HashSet<String> enclaveClassesToExposeNames = new HashSet<String>();  // Names of the enclave wrapper classes to be bound to the RMI registry.

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
                            FileUtils.writeStringToFile(PathValues.GENERATED_JIF_FOLDER_PREFIX+currentFileBaseName+".jif", jifCodeAddedString);
                        }
                        else {
                            System.out.println("Not an enclave class");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileIOException e) {
            e.printStackTrace();
        }

        // Second try block, adding RMI code to enclave classes
        try {

            System.out.println("Names of the generated wrapper classes = "+enclaveClassesToExposeNames);

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
                            EnclaveClassTranslationUtils.addCommCodeToEnclaveClasses(cu, enclaveClassesToExposeNames);

                            String enclaveJavaCodeWithCommString = cu.toString();
                            System.out.println("----------- Enclave file with added RMI code -----------------");
                            System.out.println(enclaveJavaCodeWithCommString);


                            System.out.println("---------------------Removing JE annotations -------------------------");
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
                            cu.addImport(RMIConstants.javaRMIAll);  // Not an optimal place to add RMI imports, the class may not contain any enclave calls at all.
                            NonEnclaveMethodCallVisitor nonEnclaveMCVisitor = new NonEnclaveMethodCallVisitor();
                            nonEnclaveMCVisitor.visit(cu, gatewayMethodNames);  // TODO: We can't add all the remote methods into the single interface, what interfaces to be imported will be decided by this visit method.
                            String nonEnclaveClassString = cu.toString();
                            System.out.println("------------  NonEnclave Class with added remote comm --------------------");
                            System.out.println(nonEnclaveClassString);

                            FileUtils.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX+currentFileBaseName+".java", nonEnclaveClassString);
                        } else {
                            System.out.println("An enclave class");
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (FileIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Fourth try block, generating the Enclave initialization java file
        try {
            File enclaveMainClassFile = new File(PathValues.ENCLAVE_MAIN_CLASS_TEMPLATE_FILE_NAME);
            final CompilationUnit cu = StaticJavaParser.parse(enclaveMainClassFile);
            cu.getPackageDeclaration().get().remove();  // Removing the package declaration from the template file.
            cu.setPackageDeclaration(RMIConstants.generatedJavaPackageName);
            ParserHelper.addRMIRegistryBindings(cu, enclaveClassesToExposeNames);
            final String enclaveMainClassAsString = cu.toString();
            FileUtils.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX+FileNames.ENCLAVE_MAIN_CLASS_BASE_NAME+".java", enclaveMainClassAsString);
        }  catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (FileIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Fifth try block, adding RemoteObjectProvider class to the generated java folder
        try {
            File remoteObjectProviderClassFile = new File(PathValues.REMOTE_OBJECT_PROVIDER_CLASS_NAME);
            final CompilationUnit cu = StaticJavaParser.parse(remoteObjectProviderClassFile);
            cu.getPackageDeclaration().get().remove();  // Removing the package declaration from the template file.
            cu.setPackageDeclaration(RMIConstants.generatedJavaPackageName);
            final String remoteObjectProviderClassAsString = cu.toString();
            FileUtils.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX+ RMIConstants.RMI_OBJECT_PROVIDER_CLASS+".java", remoteObjectProviderClassAsString);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (FileIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
