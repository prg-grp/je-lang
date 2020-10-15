package de.tuda.stg.Parser.VisitorsJe;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.stg.Annotations.Enclave;


public class ClassAnnotationRemoverVisitorJe extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(ClassOrInterfaceDeclaration cOrID, Void arg) {
        if (cOrID.isAnnotationPresent(Enclave.class)) {
            cOrID.getAnnotationByClass(Enclave.class).get().remove();   // Removing the @Enclave annotation
        }
    }
}
