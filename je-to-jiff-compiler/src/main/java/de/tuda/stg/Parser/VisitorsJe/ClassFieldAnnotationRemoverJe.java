package de.tuda.stg.Parser.VisitorsJe;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.stg.Annotations.Secret;

public class ClassFieldAnnotationRemoverJe extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(FieldDeclaration classField, Void arg) {
        super.visit(classField, arg); //TODO: is it needed ?
        if (classField.isAnnotationPresent(Secret.class)) {
            classField.getAnnotationByClass(Secret.class).get().remove();  // Removing the @Secret annotation
        }
    }
}
