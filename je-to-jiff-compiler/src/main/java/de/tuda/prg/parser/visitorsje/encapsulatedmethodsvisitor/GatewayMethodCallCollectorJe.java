package de.tuda.prg.parser.visitorsje.encapsulatedmethodsvisitor;

import java.util.List;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import de.tuda.prg.annotations.Gateway;
import de.tuda.prg.constants.Codes;

public class GatewayMethodCallCollectorJe extends VoidVisitorAdapter<List<MethodCallExpr>> {
    @Override
    public void visit(MethodCallExpr mce, List<MethodCallExpr> gwEncapsMethodCalls) {
        System.out.println("Start Collecting mces");
        if (!mce.getNameAsString().equals(Codes.declassify) && !mce.getNameAsString().equals(Codes.endorse)) {
            gwEncapsMethodCalls.add(mce);
            super.visit(mce, gwEncapsMethodCalls);
        }
        System.out.println("End Collecting mces");
    }

    @Override
    public void visit(MethodDeclaration md, List<MethodCallExpr> gwEncapsMethodCalls) {
        if (md.isAnnotationPresent(Gateway.class)) {
            System.out.println("Is an enclave class : Collect all Methodcalls");
            super.visit(md, gwEncapsMethodCalls);
            System.out.println("End of Methoddeclaration");
        } else {
            System.out.println("Not an enclave class");
            return;
        }
    }
}
