package de.tuda.prg.parser.visitorsje;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.annotations.Gateway;

import java.util.HashSet;

public class MethodDefinitionVisitorCollectorJe extends VoidVisitorAdapter<HashSet<String>> {
    @Override
    public void visit(MethodDeclaration md, HashSet<String> gwMethodsSet) {
        if (md.isAnnotationPresent(Gateway.class)) {
            gwMethodsSet.add(md.getNameAsString());
        }
        // super.visit(md, gwMethodsSet); //TODO: is it needed ?
        // System.out.println("Method Name Printed: " + md.getName());
    }
}
