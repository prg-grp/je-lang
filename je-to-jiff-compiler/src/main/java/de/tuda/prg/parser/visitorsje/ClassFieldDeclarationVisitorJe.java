package de.tuda.prg.parser.visitorsje;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.annotations.Secret;
import de.tuda.prg.parser.ParserHelper;

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