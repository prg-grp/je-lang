package de.tuda.prg.parser.visitorsje;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.annotations.Gateway;
import de.tuda.prg.constants.Codes;
import de.tuda.prg.parser.ParserHelper;

import java.util.HashSet;

public class GatewayMethodDefinitionTransformerVisitorJe extends VoidVisitorAdapter<HashSet<String>>  {
    @Override
    public void visit(MethodDeclaration md, HashSet<String> gwMethodsSet) {
        if (md.isAnnotationPresent(Gateway.class)) {
            if (md.isStatic()) {
                gwMethodsSet.add(md.getNameAsString());

                md.removeModifier(Modifier.Keyword.STATIC);   // Removing the static modifier
                md.getAnnotationByClass(Gateway.class).get().remove();   // Removing the @Gateway annotation
                final String methodReturnType = md.getTypeAsString();
                if (!"void".equals(methodReturnType)) {
                    md.setType(md.getTypeAsString() + Codes.gwReturnTypeCode); // Setting the return type of the gateway method
                }
                md.setName(md.getName() + Codes.gwMethodBeginLabelCode); // Adding the gateway method begin label
                md.getParameters().forEach(param -> {
                    if (ParserHelper.checkJavaTypes(param.getType())) param.setType(param.getTypeAsString() + Codes.gwMethodParamTypeLabelCode);
                    else param.setType(param.getTypeAsString() + Codes.gwMethodParamTypeLabelParametrizedCode);
                }); // Adding security label to the gateway parameter
                md.addParameter(Codes.gwMethodExtensionTypeCode, Codes.gwMethodExtensionParamNameCode);
            } else {
                throw new IllegalArgumentException("Gateway method is not static, all Gateway methods should be static.");
            }
        } 
        super.visit(md, gwMethodsSet); // Keeping all the 'super.visit' calls as the last command in the method
    }
}