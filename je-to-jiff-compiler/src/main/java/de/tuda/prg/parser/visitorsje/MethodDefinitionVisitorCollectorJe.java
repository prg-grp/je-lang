package de.tuda.prg.parser.visitorsje;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.annotations.Gateway;
import java.util.List;

public class MethodDefinitionVisitorCollectorJe extends VoidVisitorAdapter<List<MethodDeclaration>> {
    @Override
    public void visit(MethodDeclaration md, List<MethodDeclaration> gtwMethodDeclarationSet) {
        if (md.isAnnotationPresent(Gateway.class)) {
            gtwMethodDeclarationSet.add(md);
        }
        // super.visit(md, gwMethodsSet); //TODO: is it needed ?
        // System.out.println("Method Name Printed: " + md.getName());
    }
}
