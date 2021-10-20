package de.tuda.prg.parser.visitorsje;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.annotations.Secret;
import de.tuda.prg.parser.ParserHelper;

public class ClassFieldDeclarationVisitorJe extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(FieldDeclaration classField, Void arg) {
        if (classField.isAnnotationPresent(Secret.class)) {
            if (classField.isStatic()) {
                classField.removeModifier(Modifier.Keyword.STATIC);  // Removing the Static modifier
                classField.getAnnotationByClass(Secret.class).get().remove();  // Removing the @Secret annotation
                /*int len = classField.getVariables().size();
                for (int i=0; i<len; i++) {
                    VariableDeclarator vd = classField.getVariable(i);

                    System.out.println("Type of the field in the Je program = " + vd.getType().asString());

                    String replacementTypeString = ParserHelper.getStringForSecType(vd.getType().asString());

                    System.out.println("replacementTypeString = " + replacementTypeString);

                    vd.setType(replacementTypeString);
                }*/
                classField.getVariables().forEach(vd -> {
                    System.out.println("Type of the field in the Je program = " + vd.getType().asString());

                    String replacementTypeString = ParserHelper.getStringForSecType(vd.getType().asString());

                    System.out.println("replacementTypeString = " + replacementTypeString);

                    vd.setType(replacementTypeString);
                    System.out.println("After replace type");
                });
            } else {
                throw new IllegalArgumentException("Secret field is not static, all secret fields must be static.");
            }
        }
        super.visit(classField, arg);
      /*
        classField.getVariables().forEach(vd -> vd.setType("Random")
        );*/
    }
}
