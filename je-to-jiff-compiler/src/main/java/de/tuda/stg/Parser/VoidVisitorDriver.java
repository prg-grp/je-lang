package de.tuda.stg.Parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import de.tuda.stg.Parser.VisitorsJif.ClassDeclarationVisitorJif;
import de.tuda.stg.Parser.VisitorsJif.MethodDefinitionVisitorJif;
import de.tuda.stg.Parser.VisitorsJif.ClassFieldDeclarationVisitorJif;
import de.tuda.stg.Parser.VisitorsJif.MethodCallVisitorJif;
import de.tuda.stg.Parser.VisitorsRemoteCom.EnclaveClassDeclarationVisitorComm;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;

public class VoidVisitorDriver {

    // private static final String FILE_PATH = "je-to-jiff-compiler/Point.java";
    private static final String JE_FOLDER_PATH = "test-cases/src/main/je/de/tuda/stg/battleship/";
    private static final String GENERATED_JIF_FOLDER_PREFIX = "test-cases/src/main/generated-jif/de/tuda/stg/battleship/";


    public static void main(String[] args) {
        try {

            // Scanning the directory
            File jeSrcDir = new File(JE_FOLDER_PATH);
            File[] directoryListing = jeSrcDir.listFiles();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    String currentFileName = file.getName();
                    System.out.println("Currently processing : "+currentFileName);
                    String currentFileBaseName = FilenameUtils.removeExtension(currentFileName);
                    if (FilenameUtils.getExtension(file.getPath()).equals("je")) {  //Change this later

                        // final CompilationUnit cu = StaticJavaParser.parse(new File(file));
                        final CompilationUnit cu = StaticJavaParser.parse(file);
                        System.out.println("--------- class before visiting -----------------------");

                        System.out.println(cu.toString());

                        System.out.println("--------------------------------");

                        final HashSet<String> gwMethodNames = new HashSet<String>();
                        VoidVisitor<HashSet<String>> methodNameVisitor = new MethodDefinitionVisitorJif();
                        methodNameVisitor.visit(cu, gwMethodNames);

                        VoidVisitor<?> methodCallVisitor = new MethodCallVisitorJif();
                        methodCallVisitor.visit(cu, null);

                        VoidVisitor<?> variableDeclarationVisitor = new ClassFieldDeclarationVisitorJif();
                        variableDeclarationVisitor.visit(cu, null);

                        VoidVisitor<HashSet<String>> classDeclarationVisitor = new ClassDeclarationVisitorJif();
                        HashSet<String> enclaveClassNames = new HashSet<String>();
                        classDeclarationVisitor.visit(cu,enclaveClassNames);
                        System.out.println(enclaveClassNames);

                        System.out.println("--------- class after visiting -----------------------");

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
                        ParserHelper.writeStringToTheFile(GENERATED_JIF_FOLDER_PREFIX+currentFileBaseName+".jif", jifCodeAddedString);


                        // Convert gateway method calls from the non-sgx environment into remote calls.
                        //
                    }
                    // Do something with child
                }
            }




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // Code for adding Enclave RMI code
        /*final CompilationUnit cuForCom = StaticJavaParser.parse(new File(JE_FOLDER_PATH));
        VoidVisitor<?> classDeclarationVisitorCom = new EnclaveClassDeclarationVisitorComm();   // Adding RMI code
        classDeclarationVisitorCom.visit(cuForCom, null);*/
    }






}
