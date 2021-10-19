package de.tuda.prg.parser.visitorsje.exceptionvisitors.nullpointervisitors;

import java.util.Optional;
import java.util.function.Predicate;
import java.lang.StringIndexOutOfBoundsException;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class TryStmtVisitor extends VoidVisitorAdapter<Node> {

    @Override
    public void visit(TryStmt ts, Node arg) {
        //System.out.println("Starting MCE VISIT");
        Optional mce = ts.findFirst(MethodCallExpr.class, isCharAtMethod());
        if (mce.isPresent()) {
            NodeList nl = ts.getCatchClauses();

            CatchClause cc = new CatchClause(); // initialize cc
            cc.setParameter(new Parameter().setName("e").setType(StringIndexOutOfBoundsException.class)); // Set Parameter of Exception to handle to NullPointerException
            nl.add(cc);

            ts.setCatchClauses(nl);
        }
        //System.out.println("Ending MCE VISIT");
        mce = ts.findFirst(MethodCallExpr.class, isGetMethod());
        if (mce.isPresent()) {
            NodeList nl = ts.getCatchClauses();

            CatchClause cc = new CatchClause(); // initialize cc
            cc.setParameter(new Parameter().setName("e").setType(IndexOutOfBoundsException.class)); // Set Parameter of Exception to handle to NullPointerException
            nl.add(cc);

            ts.setCatchClauses(nl);
        }
    }

    private static Predicate<MethodCallExpr> isCharAtMethod() {
        return mce -> mce.getNameAsString().equals("charAt");
    }

    private static Predicate<MethodCallExpr> isGetMethod() {
        return mce -> mce.getNameAsString().equals("get");
    }
    
}
