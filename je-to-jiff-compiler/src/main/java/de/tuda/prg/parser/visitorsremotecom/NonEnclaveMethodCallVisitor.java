package de.tuda.prg.parser.visitorsremotecom;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.parser.ParserHelper;

import java.util.HashSet;

public class NonEnclaveMethodCallVisitor extends VoidVisitorAdapter<HashSet<String>> {
    @Override
    public void visit(MethodCallExpr mc, HashSet<String> gwMethodNamesSet) {
      super.visit(mc, gwMethodNamesSet);
        System.out.println("Method call detected: "+mc.getName() );

        if (gwMethodNamesSet.contains(mc.getName().asString())) {
            System.out.println("--- A gateway method call detected, name of the method call: ---" + mc.getName().asString());
            ParserHelper.transformNonEnclaveGatewayCall(mc);
        }
    }
}
