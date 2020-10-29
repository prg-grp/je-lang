package de.tuda.stg.Parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import de.tuda.stg.Parser.VisitorsJe.*;
import de.tuda.stg.Parser.VisitorsRemoteCom.EnclaveClassDeclarationVisitorComm;

import java.util.HashSet;
import java.util.Set;

public class EnclaveClassTranslationUtils {

    public static void translateEnclaveClass(final CompilationUnit cu) {


            // Step1: Visiting class declaration inside the cu
            VoidVisitor<HashSet<String>> classDeclarationVisitor = new ClassDeclarationVisitorJe();
            HashSet<String> enclaveClassNames = new HashSet<String>();
            classDeclarationVisitor.visit(cu,enclaveClassNames);
            System.out.println(enclaveClassNames);
            System.out.println("--------- class after visiting -----------------------");


            // Step2: Visiting methods inside the cu
            final HashSet<String> gwMethodNames = new HashSet<String>();
            VoidVisitor<HashSet<String>> methodNameVisitor = new GatewayMethodDefinitionVisitorTransformerJe();
            methodNameVisitor.visit(cu, gwMethodNames);

            // Step3: Visiting declassify and endorse operators
            VoidVisitor<?> methodCallVisitor = new MethodCallVisitorJe();
            methodCallVisitor.visit(cu, null);

            // Step4: Visiting fields inside the cu (e.g. secret fields)
            VoidVisitor<?> variableDeclarationVisitor = new ClassFieldDeclarationVisitorJe();
            variableDeclarationVisitor.visit(cu, null);
    }

    public static void addCommCodeToEnclaveClasses(final CompilationUnit cu, final Set<String> enclaveClassesToExposeNames) {
        if (ParserHelper.isClassAnnotatedWithEnclaveAnnotation(cu)) {
            // Code for adding Enclave RMI code

            //Still need to get rid of annotations and endorse and declassify operators (All JE features)
            VoidVisitor<Set<String>> classDeclarationVisitorComm = new EnclaveClassDeclarationVisitorComm();   // Adding RMI code
            classDeclarationVisitorComm.visit(cu,enclaveClassesToExposeNames);
        } else {

        }
    }

    public static void removeAllJEConstructs(final CompilationUnit cu) {
        if (ParserHelper.isClassAnnotatedWithEnclaveAnnotation(cu)) {

            VoidVisitor<?> classAnnotationRemoverVisitorJe = new ClassAnnotationRemoverVisitorJe();
            classAnnotationRemoverVisitorJe.visit(cu, null);

            VoidVisitor<?> classFieldAnnotationRemoverJe = new ClassFieldAnnotationRemoverJe();
            classFieldAnnotationRemoverJe.visit(cu, null);

            VoidVisitor<?> methodAnnotationRemoverVisitorJe = new MethodAnnotationRemoverVisitorJe();
            methodAnnotationRemoverVisitorJe.visit(cu, null);

            //Remove declassify and endorse operators, either remove or treat them as identity operators.

        } else {

        }

    }

}
