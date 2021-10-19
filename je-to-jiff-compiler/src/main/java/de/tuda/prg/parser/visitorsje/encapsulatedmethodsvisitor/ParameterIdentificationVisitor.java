package de.tuda.prg.parser.visitorsje.encapsulatedmethodsvisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import de.tuda.prg.annotations.Gateway;
import de.tuda.prg.annotations.Secret;
import de.tuda.prg.constants.Codes;

public class ParameterIdentificationVisitor extends GenericVisitorAdapter<List<String>, List<MethodCallExpr>> {
    @Override
    public List<String> visit(CompilationUnit cu, List<MethodCallExpr> gwEncapsMethodCalls) {
        List<String> parameterJifType = new ArrayList<String>();
        gwEncapsMethodCalls.forEach(mce -> {
            mce.getArguments().forEach(arg -> {
                if (arg.isNameExpr()) {
                    Optional field = cu.findFirst(FieldDeclaration.class, isFieldDecl(arg.asNameExpr()));
                    if (field.isPresent()) parameterJifType.add("secret");
                    else parameterJifType.add("endorsed");
                }
            });
        });
        return parameterJifType;
    }

    private static Predicate<FieldDeclaration> isFieldDecl(NameExpr n) {
        return fd -> fd.isAnnotationPresent(Secret.class) && fd.getVariable(0).getNameAsExpression().equals(n);
    }
}