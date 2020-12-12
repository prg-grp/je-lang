package de.tuda.prg.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import de.tuda.prg.parser.visitorsje.*;
import de.tuda.prg.parser.visitorsremotecom.EnclaveClassDeclarationVisitorComm;

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

            VoidVisitor<?> endorseDeclassRemoverVisitor = new EndorseDeclassRemoverVisitorJe();
            endorseDeclassRemoverVisitor.visit(cu, null);

        } else {

        }

    }

}
