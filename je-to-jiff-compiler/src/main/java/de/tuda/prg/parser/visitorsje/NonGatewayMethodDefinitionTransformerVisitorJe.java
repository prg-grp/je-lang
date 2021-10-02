package de.tuda.prg.parser.visitorsje;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.annotations.Gateway;
import de.tuda.prg.constants.Codes;
import de.tuda.prg.parser.ParserHelper;

import java.util.HashSet;

public class NonGatewayMethodDefinitionTransformerVisitorJe extends VoidVisitorAdapter  {
    @Override
    public void visit(MethodDeclaration md, Object arg) {
        if (!md.isAnnotationPresent(Gateway.class)) {
            // Process all other Methods that are not GW Methods

            if (md.isStatic()) md.removeModifier(Modifier.Keyword.STATIC);   // Removing the static modifier
            final String methodReturnType = md.getTypeAsString();
            if (!"void".equals(methodReturnType)) {
                if (ParserHelper.checkJavaTypes(md.getType())) md.setType(md.getTypeAsString() + Codes.infReturnTypeCode); // Setting the return type of the gateway method
                else md.setType(md.getTypeAsString() + Codes.infReturnTypeParametrizedCode); 
            }
            md.getParameters().forEach(param -> {
                //param.setType(param.getTypeAsString() + Codes.infMethodParamTypeLabelCode);
                if (ParserHelper.checkJavaTypes(param.getType())) param.setType(param.getTypeAsString() + Codes.infMethodParamTypeLabelCode);
                else param.setType(param.getTypeAsString() + Codes.infMethodParamTypeLabelParametrizedCode);
            }); // Adding security label to the gateway parameter
        }
        super.visit(md, arg); // Keeping all the 'super.visit' calls as the last command in the method
    }
}
