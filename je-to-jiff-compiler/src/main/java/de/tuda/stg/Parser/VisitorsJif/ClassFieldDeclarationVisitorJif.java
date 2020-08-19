package de.tuda.stg.Parser.VisitorsJif;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.stg.Annotations.Secret;
import de.tuda.stg.Parser.Codes;

public class ClassFieldDeclarationVisitorJif extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(FieldDeclaration classField, Void arg) {
        super.visit(classField, arg);
        if (classField.isAnnotationPresent(Secret.class)) {
            classField.getAnnotationByClass(Secret.class).get().remove();  // Removing the @secret annotation
            classField.getVariables().forEach(vd -> vd.setType(vd.getType().asString()+ Codes.secFieldTypeCode));
        }
      /*  System.out.println("Variable declaration printed: " + classField.getVariables());
        classField.getVariables().forEach(vd -> vd.setType("Random")
        );*/
    }
}
