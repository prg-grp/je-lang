package de.tuda.prg.parser.visitorsje.sgxChecker.tud.resolver;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;

import java.util.Optional;

public class MethodDeclarationSolver {

    public static MethodDeclaration solve(JavaParserFacade symbolSolver, MethodCallExpr methodCallExpr) {
        SymbolReference<ResolvedMethodDeclaration> reference = symbolSolver.solve(methodCallExpr);
        Optional<MethodDeclaration> maybeDeclaredMethod = reference.getCorrespondingDeclaration().toAst();
        return maybeDeclaredMethod.orElse(null);
    }
}
