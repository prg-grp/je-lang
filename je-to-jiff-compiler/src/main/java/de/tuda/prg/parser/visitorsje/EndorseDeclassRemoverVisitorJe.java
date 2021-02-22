package de.tuda.prg.parser.visitorsje;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.constants.Codes;
import de.tuda.prg.constants.FileNames;

public class EndorseDeclassRemoverVisitorJe extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(MethodCallExpr mc, Void arg) {
        if (mc.getName().asString().equals(Codes.endorse)) {
            mc.setName(FileNames.IDENTITY_METHODS_CLASS_NAME+ "." + Codes.endorse);
        }
        if (mc.getName().asString().equals(Codes.declassify)) {
            mc.setName(FileNames.IDENTITY_METHODS_CLASS_NAME+ "." +Codes.declassify);
        }
        // super.visit(mc, arg); // TODO: is it needed
    }
}
