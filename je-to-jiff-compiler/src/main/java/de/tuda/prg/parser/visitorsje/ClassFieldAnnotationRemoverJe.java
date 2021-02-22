package de.tuda.prg.parser.visitorsje;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.annotations.Secret;

public class ClassFieldAnnotationRemoverJe extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(FieldDeclaration classField, Void arg) {
        if (classField.isAnnotationPresent(Secret.class)) {
            classField.getAnnotationByClass(Secret.class).get().remove();  // Removing the @Secret annotation
        }
        // super.visit(classField, arg);
    }
}
