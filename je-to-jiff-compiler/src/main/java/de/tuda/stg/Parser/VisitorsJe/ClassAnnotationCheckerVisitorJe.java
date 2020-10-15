package de.tuda.stg.Parser.VisitorsJe;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.stg.Annotations.Enclave;
import de.tuda.stg.Parser.Codes;
import java.util.ArrayList;

public class ClassAnnotationCheckerVisitorJe extends VoidVisitorAdapter<ArrayList<Integer>> {

    @Override
    public void visit(ClassOrInterfaceDeclaration cOrID, ArrayList<Integer> annotationPresenceIndicator) {
        if (annotationPresenceIndicator != null && annotationPresenceIndicator.isEmpty()) {
            if (cOrID.isAnnotationPresent(Enclave.class)) {
                annotationPresenceIndicator.add(1);
                cOrID.getAnnotationByClass(Enclave.class).get().remove();   // Removing the @Enclave annotation
                cOrID.setName(cOrID.getName()+ Codes.classExtensionCode);
                cOrID.addModifier(Modifier.Keyword.FINAL);   // Adding the keyword 'final'
                // super.visit(cOrID, classNameSet); // TODO : is this needed ?
            }
        } else {
            throw new IllegalArgumentException("List 'annotationPresenceIndicator' should not be null and should be empty");
        }

    }
}
