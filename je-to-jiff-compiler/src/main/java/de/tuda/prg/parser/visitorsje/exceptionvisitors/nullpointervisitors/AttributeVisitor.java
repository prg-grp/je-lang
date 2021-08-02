package de.tuda.prg.parser.visitorsje.exceptionvisitors.nullpointervisitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;

public class AttributeVisitor extends VoidVisitorAdapter {

    private HashMap<NameExpr, Type> types;

    public HashMap<NameExpr, Type> startVisiting(CompilationUnit n, Object arg) {
        types = new HashMap<>();
        visit(n, arg);
        return types;
    }

    @Override
    public void visit(ClassOrInterfaceType n, Object arg) {
        n.getChildNodes().stream().forEach(c -> {
            if (c instanceof FieldDeclaration) c.accept(this, arg);
        });
    }

    @Override
    public void visit(VariableDeclarator n, Object arg) {
        types.put(n.getNameAsExpression(), n.getType().getElementType());
    }
}
