package de.tuda.stg.Parser.VisitorsJe;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.stg.Annotations.Secret;
import de.tuda.stg.Parser.ParserHelper;

public class ClassFieldDeclarationVisitorJe extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(FieldDeclaration classField, Void arg) {
        super.visit(classField, arg); //TODO: is it needed ?
        if (classField.isAnnotationPresent(Secret.class)) {
            classField.getAnnotationByClass(Secret.class).get().remove();  // Removing the @Secret annotation
            classField.getVariables().forEach(vd -> {
                System.out.println("Type of the field in the Je program = "+vd.getType().asString());

                String replacementTypeString = ParserHelper.getStringForSecType(vd.getType().asString());

                vd.setType(replacementTypeString);
            });
        }
      /*  System.out.println("Variable declaration printed: " + classField.getVariables());
        classField.getVariables().forEach(vd -> vd.setType("Random")
        );*/
    }
}
