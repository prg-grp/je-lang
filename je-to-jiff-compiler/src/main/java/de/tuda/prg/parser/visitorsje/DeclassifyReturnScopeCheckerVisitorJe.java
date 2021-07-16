package de.tuda.prg.parser.visitorsje;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.constants.Codes;
import de.tuda.prg.constants.FileNames;
import de.tuda.prg.exceptions.TranslationException;

import java.util.Optional;

public class DeclassifyReturnScopeCheckerVisitorJe extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(MethodCallExpr mc, Void arg) {
        if (mc.getName().asString().equals(Codes.endorse)) {
             Optional<Node> parentNodeOpt = mc.getParentNode();
             if (parentNodeOpt.isPresent()) {
                 Node parentNode = parentNodeOpt.get();
                 System.out.println("The parent of the declassify statement = "+parentNode.toString());
                 try {
                     ReturnStmt parentReturn = (ReturnStmt) parentNode;
                 } catch (ClassCastException e) {
                     // throw new TranslationException("The direct parent of the declassify statement is not a \"return\" statement");
                     throw e;
                 }
             }
        }
        super.visit(mc, arg);
    }
}
