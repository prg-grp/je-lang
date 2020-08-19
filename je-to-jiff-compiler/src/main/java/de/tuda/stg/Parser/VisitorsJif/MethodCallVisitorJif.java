package de.tuda.stg.Parser.VisitorsJif;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.stg.Parser.Codes;

public class MethodCallVisitorJif extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodCallExpr mc, Void arg) {
            super.visit(mc, arg);
            System.out.println("Method call detected: "+mc.getName() );

            if (mc.getName().asString().equals(Codes.endorse)) {
                System.out.println("Inside the endorse call : "+mc.getName() );
                mc.addArgument(Codes.endorseToLabelCode);
            }
            if (mc.getName().asString().equals(Codes.declassify)) {
                mc.addArgument(Codes.declassifyToLabelCode);
            }
        }
}

