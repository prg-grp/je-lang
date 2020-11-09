package de.tuda.prg.parser.visitorsje;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.annotations.Enclave;


public class ClassAnnotationRemoverVisitorJe extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(ClassOrInterfaceDeclaration cOrID, Void arg) {
        if (cOrID.isAnnotationPresent(Enclave.class)) {
            cOrID.getAnnotationByClass(Enclave.class).get().remove();   // Removing the @Enclave annotation
        }
    }
}
