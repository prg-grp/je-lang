package de.tuda.prg.taskprocessing;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import de.tuda.prg.constants.Codes;
import de.tuda.prg.constants.PathValues;
import de.tuda.prg.exceptions.FileIOException;
import de.tuda.prg.filehandling.FileUtils;
import de.tuda.prg.parser.EnclaveClassTranslationUtils;
import de.tuda.prg.parser.ParserHelper;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

public class EnclaveClassXformationTask implements  CodeXformationTask {

    // First try block, translation of Enclave classes to Jif
    @Override
    public void run(File jeSrcDir, GlobalTaskData interTaskData) {
        System.out.println("Task processing started : Task name: EnclaveClassXformationTask.");
        try {
            File[] directoryListing = jeSrcDir.listFiles();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    String currentFileName = file.getName();
                    System.out.println("Currently processing : " + currentFileName);
                    String currentFileBaseName = FilenameUtils.removeExtension(currentFileName);
                    if (FilenameUtils.getExtension(file.getPath()).equals("java")) {  //Change this later

                        // final CompilationUnit cu = StaticJavaParser.parse(new File(file));
                        final CompilationUnit cu = StaticJavaParser.parse(file);

                        if (ParserHelper.isClassAnnotatedWithEnclaveAnnotation(cu)) {

                            //Extracting the names of the Gateway methods inside the Enclave Class.
                            System.out.println("Before populate GW Methods");
                            ParserHelper.populateAllGatewayMethodsInCu(cu, interTaskData.gatewayMethodDeclarations);
                            System.out.println("After populate GW Methods");

                            System.out.println("Before translation");
                            EnclaveClassTranslationUtils.translateEnclaveClass(cu);
                            System.out.println("After translation");

                            String afterVisitClassString = cu.toString();
                            //System.out.println(afterVisitClassString);

                            //System.out.println("--------------------------------");

                            System.out.println("---------------- Now replacing the added codes ----------------");
                            String jifCodeAddedString = afterVisitClassString;

                            for (String key : Codes.strReplacement.keySet()) {
                                jifCodeAddedString = jifCodeAddedString.replace(key, Codes.strReplacement.get(key));
                            }

                            System.out.println("----------------------------------Printing the files with Jif labels --------------------------------");
                            System.out.println(jifCodeAddedString);
                            // Writing generated jif code to the file
                            FileUtils.writeStringToFile(PathValues.GENERATED_JIF_FOLDER_PREFIX + currentFileBaseName + ".jif", jifCodeAddedString);
                        } else {
                            System.out.println("Not an enclave class");
                        }
                    }
                }
            }
            // Populating gateway method names as Strings
            ParserHelper.populateMethodNamesFromDeclarations(interTaskData.gatewayMethodDeclarations, interTaskData.gatewayMethodNames);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileIOException e) {
            e.printStackTrace();
        }

    }
}
