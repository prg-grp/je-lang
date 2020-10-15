package de.tuda.stg.Parser.VisitorsJe;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.stg.Annotations.Gateway;


public class MethodAnnotationRemoverVisitorJe extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(MethodDeclaration md, Void arg) {
        if (md.isAnnotationPresent(Gateway.class)) {
            md.getAnnotationByClass(Gateway.class).get().remove();   // Removing the @Gateway annotation
        }
        super.visit(md, arg); //TODO: is it needed ?
    }
}
