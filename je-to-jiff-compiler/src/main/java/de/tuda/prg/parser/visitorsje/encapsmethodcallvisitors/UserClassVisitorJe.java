package de.tuda.prg.parser.visitorsje.encapsmethodcallvisitors;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.annotations.Enclave;
import de.tuda.prg.constants.Codes;
import de.tuda.prg.parser.ParserHelper;

import java.util.HashSet;

public class UserClassVisitorJe extends VoidVisitorAdapter<Void>  {
    @Override
    public void visit(ClassOrInterfaceDeclaration cOrID, Void arg) {
            cOrID.setName(cOrID.getName()+ Codes.classExtensionCodeParametrized);
            //cOrID.addModifier(Modifier.Keyword.FINAL);   // Adding the keyword 'final'
            // super.visit(cOrID, classNameSet); removed super 'visit' call, no support for nested classes
            super.visit(cOrID, arg);
    }

    @Override
    public void visit(FieldDeclaration field, Void arg) {
        if (field.isStatic()) field.removeModifier(Modifier.Keyword.STATIC);  // Removing the Static modifier
        field.getVariables().forEach(vd -> {
            System.out.println("Type of the field in the Je program = " + vd.getType().asString());

            String replacementTypeString = ParserHelper.getStringForParametrizedType(vd.getType().asString());

            System.out.println("replacementTypeString = " + replacementTypeString);

            vd.setType(replacementTypeString);
            System.out.println("After replace type");
        });
        super.visit(field, arg);
    }
}