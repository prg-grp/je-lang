package de.tuda.prg.parser.visitorsje;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.annotations.Enclave;

import java.util.ArrayList;

public class ClassAnnotationCheckerVisitorJe extends VoidVisitorAdapter<ArrayList<Integer>> {

    @Override
    public void visit(ClassOrInterfaceDeclaration cOrID, ArrayList<Integer> annotationPresenceIndicator) {  // Only works when you have only one class definition in the file
        if (annotationPresenceIndicator != null && annotationPresenceIndicator.isEmpty()) {
            if (cOrID.isAnnotationPresent(Enclave.class)) {
                annotationPresenceIndicator.add(1);
            }
        } else {
            throw new IllegalArgumentException("List 'annotationPresenceIndicator' should not be null and should be empty");
        }
        // super.visit(cOrID, classNameSet); // Commented out, since this can cause some issues when there are multiple classes defined in a file.
    }
}
