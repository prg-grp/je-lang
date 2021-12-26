package de.tuda.prg.parser.visitorsje;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.constants.Codes;
import de.tuda.prg.parser.ParserHelper;

public class MethodCallVisitorJe extends VoidVisitorAdapter<Boolean> {

        String endorseVariable = "";

        @Override
        public void visit(MethodCallExpr mc, Boolean isSanitized) {
            // super.visit(mc, arg);
            // System.out.println("Method call detected: "+mc.getName() );

            if (mc.getName().asString().equals(Codes.endorse)) {
                endorseVariable = mc.getArgument(0).toString();
                // System.out.println("Inside the endorse call : "+mc.getName() );
                if (!isSanitized) mc.addArgument(Codes.endorseToLabelCode);
            }
            if (mc.getName().asString().equals(Codes.declassify)) {
                mc.addArgument(Codes.declassifyToLabelCode);
            }
            super.visit(mc, isSanitized);
        }

        @Override
        public void visit(VariableDeclarator vd, Boolean isSanitized) {
            if (vd.toString().contains(Codes.endorse) && !ParserHelper.checkJavaTypes(vd.getType())) {
                vd.setType(vd.getType().toString()+Codes.principalParameterCode);
            }
            super.visit(vd, isSanitized);
        }

        /*
        @Override
        public void visit(IfStmt s, Boolean isSanitized) {
            if (isSanitized && s.toString().contains(Codes.sanitize)) {
                System.out.println(s.toString());
                s.getCondition().toMethodCallExpr().get().setArgument(0, new NameExpr(endorseVariable));
            }
        }*/
}

