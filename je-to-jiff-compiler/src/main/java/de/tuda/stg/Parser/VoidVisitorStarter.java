package de.tuda.stg.Parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import de.tuda.stg.Parser.VisitorsJif.ClassDeclarationVisitorJif;
import de.tuda.stg.Parser.VisitorsJif.MethodDefinitionVisitorJif;
import de.tuda.stg.Parser.VisitorsJif.ClassFieldDeclarationVisitorJif;
import de.tuda.stg.Parser.VisitorsJif.MethodCallVisitorJif;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;

public class VoidVisitorStarter {

    private static final String FILE_PATH = "je-to-jiff-compiler/Point.java";


    public static void main(String[] args) {
        try {
            CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));
            System.out.println("--------- class before visiting -----------------------");

            System.out.println(cu.toString());

            System.out.println("--------------------------------");

            HashSet<String> gwMethodNames = new HashSet<String>();
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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }






}
