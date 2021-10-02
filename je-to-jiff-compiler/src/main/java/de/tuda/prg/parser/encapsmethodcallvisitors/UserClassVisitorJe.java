package de.tuda.prg.parser.encapsmethodcallvisitors;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
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

    @Override
    public void visit(MethodDeclaration md, Void arg) {
        if (md.isStatic()) md.removeModifier(Modifier.Keyword.STATIC);   // Removing the static modifier
        //System.out.println("After static check");
        final String methodReturnType = md.getTypeAsString();
        //System.out.println("After String extract check");
        if (!"void".equals(methodReturnType)) {
            //System.out.println("After void check : "+ md.getTypeAsString() + Codes.infReturnTypeCode);
            md.setType(ParserHelper.getStringForParametrizedType(methodReturnType)); // Setting the return type of the inferred method
            //System.out.println("After set type");
        }
        //System.out.println("After return type check");
        md.getParameters().forEach(param -> {
            //param.setType(param.getTypeAsString() + Codes.infMethodParamTypeLabelCode);
            if (ParserHelper.checkJavaTypes(param.getType())) param.setType(ParserHelper.getStringForParametrizedType(param.getType().asString()));
            //else param.setType(param.getTypeAsString() + Codes.infMethodParamTypeLabelParametrizedCode);
        }); // Adding security label to the gateway parameter
        //System.out.println("After parameter type check");
        super.visit(md, arg); // Keeping all the 'super.visit' calls as the last command in the method
    }

    @Override
    public void visit(ConstructorDeclaration cd, Void arg) {
        cd.getParameters().forEach(param -> {
            if (ParserHelper.checkJavaTypes(param.getType())) param.setType(ParserHelper.getStringForParametrizedType(param.getType().asString()));
        });
    }
}