package de.tuda.prg.parser.visitorsje;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.annotations.Gateway;

public class MethodAnnotationRemoverVisitorJe extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(MethodDeclaration md, Void arg) {
        if (md.isAnnotationPresent(Gateway.class)) {
            md.getAnnotationByClass(Gateway.class).get().remove();   // Removing the @Gateway annotation
        }
        super.visit(md, arg);
    }
}
