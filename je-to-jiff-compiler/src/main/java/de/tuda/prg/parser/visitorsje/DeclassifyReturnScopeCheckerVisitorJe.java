package de.tuda.prg.parser.visitorsje;

import java.util.Optional;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.exceptions.TranslationException;

import de.tuda.prg.constants.Codes;

public class DeclassifyReturnScopeCheckerVisitorJe extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(MethodCallExpr mc, Void arg) {
        if (mc.getName().asString().equals(Codes.declassify)) {
             Optional<Node> parentNodeOpt = mc.getParentNode();
             if (parentNodeOpt.isPresent()) {
                 Node parentNode = parentNodeOpt.get();
                 try {
                     ReturnStmt parentReturn = (ReturnStmt) parentNode;
                 } catch (ClassCastException e) {
                     // throw e;
                     CompilationUnit cu = (CompilationUnit) mc.findRootNode();
                     String cuFileName = cu.getStorage().get().getFileName();
                     throw new TranslationException("The direct parent of the declassify statement is not a \"return\" statement, in file = "+ cuFileName
                             +", line number = "+mc.getName().getBegin().get().line);
                 }
             }
        }
        super.visit(mc, arg);
    }
}
