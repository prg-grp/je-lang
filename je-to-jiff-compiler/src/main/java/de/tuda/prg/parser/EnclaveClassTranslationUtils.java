package de.tuda.prg.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.expr.MethodCallExpr;

import de.tuda.prg.constants.Codes;
import de.tuda.prg.parser.userdefinedclassvisitors.*;
import de.tuda.prg.parser.visitorsje.*;
import de.tuda.prg.parser.visitorsje.encapsulatedmethodsvisitor.GatewayMethodCallCollectorJe;
import de.tuda.prg.parser.visitorsje.encapsulatedmethodsvisitor.ParameterIdentificationVisitor;
import de.tuda.prg.parser.visitorsremotecom.EnclaveClassDeclarationVisitorComm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class EnclaveClassTranslationUtils {

    /**
     * translating the Enclave Class with 5 steps
     * @param cu CompilationUnit of Enclave Class
     */
    public static void translateEnclaveClass(final CompilationUnit cu) {

            // Step1: Visiting class declaration inside the cu
            System.out.println("--------- class before visiting -----------------------");
            VoidVisitor<HashSet<String>> classDeclarationVisitor = new ClassDeclarationVisitorJe();
            HashSet<String> enclaveClassNames = new HashSet<String>();
            classDeclarationVisitor.visit(cu,enclaveClassNames);
            System.out.println(enclaveClassNames);
            //System.out.println(cu.toString());
            System.out.println("--------- class after visiting -----------------------");

            // Step2: Visiting all other methods besides GatewayMethods
            System.out.println("--------- non-gw methods before visiting -----------------------");
            List<MethodCallExpr> methodCalls = new ArrayList<MethodCallExpr>();
            VoidVisitor gatewayMethodCaller = new GatewayMethodCallCollectorJe();
            gatewayMethodCaller.visit(cu, methodCalls);
            System.out.println(methodCalls.toString());

            ParameterIdentificationVisitor paramVisitor = new ParameterIdentificationVisitor();
            List<String> parameters = paramVisitor.visit(cu, methodCalls);
            System.out.println(parameters.toString());

            VoidVisitor methodNameVisitor = new NonGatewayMethodDefinitionTransformerVisitorJe();
            methodNameVisitor.visit(cu, parameters);
            //System.out.println(cu.toString());
            System.out.println("--------- non-gw methods after visiting -----------------------");

            // Step3: Visiting methods inside the cu
            System.out.println("--------- gw methods before visiting -----------------------");
            final HashSet<String> gwMethodNames = new HashSet<String>();
            VoidVisitor<HashSet<String>> GatewayMethodNameVisitor = new GatewayMethodDefinitionTransformerVisitorJe();
            GatewayMethodNameVisitor.visit(cu, gwMethodNames);
            //System.out.println(gwMethodNames);
            //System.out.println(cu.toString());
            System.out.println("--------- gw methods after visiting -----------------------");

            // Step4: Visiting declassify and endorse operators
            System.out.println("--------- before visiting methodCalls -----------------------");
            VoidVisitor<Boolean> methodCallVisitor = new MethodCallVisitorJe();
            boolean sanitized = false;
            if (cu.toString().contains(Codes.sanitize)) sanitized = true;
            methodCallVisitor.visit(cu, sanitized);
            //System.out.println(cu.toString());
            System.out.println("--------- after visiting methodCalls -----------------------");

            // Step5: Visiting fields inside the cu (e.g. secret fields)
            System.out.println("--------- before visiting fields -----------------------");
            VoidVisitor<?> variableDeclarationVisitor = new ClassFieldDeclarationVisitorJe();
            variableDeclarationVisitor.visit(cu, null);
            //System.out.println(cu.toString());
            System.out.println("--------- after visiting fields -----------------------");
    }

    /**
     * Helperfunction to add the Communication for the rmi module
     * @param cu
     * @param enclaveClassesToExposeNames
     */
    public static void addCommCodeToEnclaveClasses(final CompilationUnit cu, final Set<String> enclaveClassesToExposeNames) {
        if (ParserHelper.isClassAnnotatedWithEnclaveAnnotation(cu)) {
            VoidVisitor<Set<String>> classDeclarationVisitorComm = new EnclaveClassDeclarationVisitorComm();   // Adding RMI code
            classDeclarationVisitorComm.visit(cu, enclaveClassesToExposeNames);
        }
    }

    /**
     * Helperfunction to remove all JE Constructs such as Annotations
     * @param cu
     */
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
        }
    }
}
