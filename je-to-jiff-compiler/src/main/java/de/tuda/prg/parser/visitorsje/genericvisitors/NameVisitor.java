package de.tuda.prg.parser.visitorsje.genericvisitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NameVisitor extends VoidVisitorAdapter {
    private boolean fromAccess;
    /**
     * Start Visiting calls visit on the received root node and puts and declares the Lists for the Nodes
     * @param n CompilationUnit (Root Node of AST)
     * @param arg
     * @return visitMap
     */
    public void startVisiting(CompilationUnit n, Map<String, String> arg) {
        fromAccess = false;
        n.accept(this, arg);
    }

    @Override
    public void visit(FieldAccessExpr n, Object arg) {
        fromAccess = true;
        visit(n.getNameAsExpression(), arg);
        fromAccess = false;
    }

    @Override
    public void visit(MethodCallExpr n, Object arg) {
        fromAccess = true;
        visit(n.getNameAsExpression(), arg);
        fromAccess = false;
    }

    @Override
    public void visit(NameExpr n, Object arg) {
        if (!fromAccess) return;
        else {
            Map<String, String> map = (Map) arg;
            if (map.containsKey(n.getNameAsString())) {
                n.setName("(("+map.get(n.getNameAsString())+")"+n.getNameAsString()+")");
            }
        }
    }
}
