package de.tuda.stg.Parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import de.tuda.stg.Parser.VisitorsJe.ClassDeclarationVisitorJe;
import de.tuda.stg.Parser.VisitorsJe.MethodDefinitionVisitorJe;
import de.tuda.stg.Parser.VisitorsJe.ClassFieldDeclarationVisitorJe;
import de.tuda.stg.Parser.VisitorsJe.MethodCallVisitorJe;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;

public class VoidVisitorDriver {

    // private static final String FILE_PATH = "test-cases/src/main/je/de/tuda/stg/TestPoint/Point.java";
    // private static final String JE_FOLDER_PATH = "test-cases/src/main/je/de/tuda/stg/battleship/";
    // private static final String GENERATED_JIF_FOLDER_PREFIX = "test-cases/src/main/generated-jif/de/tuda/stg/battleship/";

    /*private static String JE_FOLDER_PATH = "test-cases/src/main/je/de/tuda/stg/battleshipLinkedList/";
    private static String GENERATED_JIF_FOLDER_PREFIX = "test-cases/src/main/generated-jif/de/tuda/stg/battleshipLinkedList/";*/

    private static String JE_FOLDER_PATH = "test-cases/src/main/je/de/tuda/stg/TestPoint/";
    private static String GENERATED_JIF_FOLDER_PREFIX = "test-cases/src/main/generated-jif/de/tuda/stg/TestPoint/";



    public static void main(String[] args) {
        // JE_FOLDER_PATH = args[0];
        // GENERATED_JIF_FOLDER_PREFIX = args[1];

        // First try block, translation of Enclave classes into Jif
        try {
            // Scanning the directory
            File jeSrcDir = new File(JE_FOLDER_PATH);
            File[] directoryListing = jeSrcDir.listFiles();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    String currentFileName = file.getName();
                    System.out.println("Currently processing : "+currentFileName);
                    String currentFileBaseName = FilenameUtils.removeExtension(currentFileName);
                    if (FilenameUtils.getExtension(file.getPath()).equals("java")) {  //Change this later

                        // final CompilationUnit cu = StaticJavaParser.parse(new File(file));
                        final CompilationUnit cu = StaticJavaParser.parse(file);

                        EnclaveClassTranslationUtils.translateEnclaveClasses(cu);

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
                        ParserHelper.writeStringToFile(GENERATED_JIF_FOLDER_PREFIX+currentFileBaseName+".jif", jifCodeAddedString);
                        // Convert gateway method calls from the non-sgx environment into remote calls.
                        //
                    }
                    // Do something with child
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // Second try block, adding RMI code to enclave classes
        try {
            // Scanning the directory
            File jeSrcDir = new File(JE_FOLDER_PATH);
            File[] directoryListing = jeSrcDir.listFiles();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    String currentFileName = file.getName();
                    System.out.println("Currently processing : "+currentFileName);
                    String currentFileBaseName = FilenameUtils.removeExtension(currentFileName);
                    if (FilenameUtils.getExtension(file.getPath()).equals("java")) {  //Change this later

                        // final CompilationUnit cu = StaticJavaParser.parse(new File(file));
                        final CompilationUnit cu = StaticJavaParser.parse(file);

                        EnclaveClassTranslationUtils.addCommCodeToEnclaveClasses(cu);

                       // EnclaveClassTranslationUtils.removeAllJEConstructs(cu);  check this step, since adding communication is dependent on the annotations Gateway, we remove the JE language features after adding the communication code.
                    }
                    // Do something with child
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }






}
