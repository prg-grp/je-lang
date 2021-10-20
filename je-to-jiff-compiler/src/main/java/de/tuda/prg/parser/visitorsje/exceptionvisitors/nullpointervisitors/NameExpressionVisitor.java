package de.tuda.prg.parser.visitorsje.exceptionvisitors.nullpointervisitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;

public class NameExpressionVisitor extends VoidVisitorAdapter {

    private ArrayList<NameExpr> variables;

    public ArrayList<NameExpr> startVisiting(CompilationUnit cu, Object arg) {
        variables = new ArrayList<>();
        variables.add(new NameExpr("e"));
        visit(cu, arg);
        return variables;
    }

    public ArrayList<NameExpr> startVisiting(BlockStmt n, Object arg) {
        variables = new ArrayList<>();
        variables.add(new NameExpr("e"));
        visit(n, arg);
        return variables;
    }

    @Override
    public void visit(NameExpr n, Object arg) {
        variables.add(n);
    }
}
