package de.tuda.prg.parser.visitorsje.genericvisitors;

import java.util.Optional;
import java.util.function.Predicate;
import java.lang.StringIndexOutOfBoundsException;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class ClassCastExceptionVisitor extends VoidVisitorAdapter<Node> {

    @Override
    public void visit(TryStmt ts, Node arg) {
        //System.out.println("Starting MCE VISIT");
        Optional mce = ts.findFirst(CastExpr.class);
        if (mce.isPresent()) {
            NodeList nl = ts.getCatchClauses();

            CatchClause cc = new CatchClause(); // initialize cc
            cc.setParameter(new Parameter().setName("e").setType(ClassCastException.class)); // Set Parameter of Exception to handle to NullPointerException
            nl.add(cc);

            ts.setCatchClauses(nl);
        }
        //System.out.println("Ending MCE VISIT");
    }
    
}