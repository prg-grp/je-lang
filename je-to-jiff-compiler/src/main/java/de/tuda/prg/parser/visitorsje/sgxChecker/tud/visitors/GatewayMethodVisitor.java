package de.tuda.prg.parser.visitorsje.sgxChecker.tud.visitors;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.checker.SecretFieldOperationChecker;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.enums.AnnotationEnum;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.helper.MethodCallParameterMatcher;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.resolver.MethodDeclarationSolver;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures.CompilationUnitMetaDataContainer;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures.SymbolSolverSingleton;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures.Method;

import java.util.*;

public class GatewayMethodVisitor extends VoidVisitorAdapter<CompilationUnitMetaDataContainer> {

    private static final Logger logger = LogManager.getLogger(GatewayMethodVisitor.class);

    private final SecretFieldOperationChecker secretFieldOperationChecker = new SecretFieldOperationChecker();
    // Each recursive analyzeBody-call needs its own metaDataContainer since the secret field list can change
    private Stack<CompilationUnitMetaDataContainer> metaDataContainerStack;
    // Stack to detect recursive calls -> prevent endless loop
    private Stack<Method> callStack;
    private final JavaParserFacade symbolSolver = SymbolSolverSingleton.getInstance().getSymbolSolver();

    /**
     * Visit all MethodDeclarations and analyze for illegal @Secret field operations if it is a @Gateway method
     * @param methodDeclaration MethodDeclaration to be analyzed
     * @param cuMDC Metadata about the compilation unit being analyzed
     */
    @Override
    public void visit(MethodDeclaration methodDeclaration, CompilationUnitMetaDataContainer cuMDC) {
        if(cuMDC.isEnclaveClass()) {
            this.metaDataContainerStack = new Stack<>();
            this.metaDataContainerStack.push(cuMDC);
            this.callStack = new Stack<>();
            if(methodDeclaration.getAnnotations().toString().contains(AnnotationEnum.GATEWAY.toString())) {
                this.analyzeBody(methodDeclaration);
            }
        }
    }

    /**
     * This method analyzes the given MethodDeclaration for @Secret field modifications.
     * It further extracts all method calls and resolves its method declaration for each call.
     * If a @Secret variable is passed (via pass by ref) to this method or, it is analyzed recursively.
     * If the resolved method call was made on a @Secret field it is also analyzed recursively.
     * If any of the above cases is true but the method declaration cannot be resolved, a warning is printed.
     * @param methodDeclaration MethodDeclaration to be analyzed
     */
    private void analyzeBody(MethodDeclaration methodDeclaration) {

        CompilationUnitMetaDataContainer cuMDC = this.metaDataContainerStack.peek();
        this.callStack.push(Method.fromDeclaration(methodDeclaration, cuMDC));
        this.secretFieldOperationChecker.check(methodDeclaration, cuMDC.getSecretFields());
        this.analyzeMethodCalls(methodDeclaration);
        callStack.pop();
        metaDataContainerStack.pop();
    }

    /**
     * Loop over all method calls made in this method and call analyzeBody for those we can resolve a method declaration
     * @param methodDeclaration Method declaration to be analyzed
     */
    private void analyzeMethodCalls(MethodDeclaration methodDeclaration) {
        CompilationUnitMetaDataContainer cuMDC = this.metaDataContainerStack.peek();
        List<MethodCallExpr> methodCallExprs = methodDeclaration.findAll(MethodCallExpr.class);

        // For every method call in this method...
        for (MethodCallExpr methodCallExpr : methodCallExprs) {
            MethodDeclaration declaration = MethodDeclarationSolver.solve(this.symbolSolver, methodCallExpr);
            //...check if we can get a declaration
            if (declaration != null) {
                Method method = Method.fromDeclaration(declaration, cuMDC);
                //...and check if we have analyzed it already
                if (this.callStack.stream().noneMatch(call -> call.equals(method))) {
                    //...if not, recursively analyze the called method!
                    CompilationUnitMetaDataContainer topMetaDataContainer = MethodCallParameterMatcher.match(methodCallExpr, cuMDC);
                    this.metaDataContainerStack.push(topMetaDataContainer);
                    this.analyzeBody(declaration.clone());
                }
            }
            //... if no declaration can be resolved, warning!
            else {
                if(methodCallExpr.getRange().isPresent()){
                    int line = methodCallExpr.getRange().get().begin.line;
                    logger.warn("[Line: %s] Cannot resolve and analyze method call: "+methodCallExpr.toString());
                    continue;
                }
                logger.warn("[Missing Line] Cannot resolve and analyze method call: "+methodCallExpr.toString());
            }
        }
    }
}
