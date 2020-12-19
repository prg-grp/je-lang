package de.tuda.prg.parser.visitorsje;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.annotations.Gateway;
import de.tuda.prg.constants.Codes;

import java.util.HashSet;

public class GatewayMethodDefinitionTransformerVisitorJe extends VoidVisitorAdapter<HashSet<String>>  {
    @Override
    public void visit(MethodDeclaration md, HashSet<String> gwMethodsSet) {
        if (md.isAnnotationPresent(Gateway.class)) {
            gwMethodsSet.add(md.getNameAsString());
            md.getAnnotationByClass(Gateway.class).get().remove();   // Removing the @Gateway annotation
            final String methodReturnType = md.getTypeAsString();
            if (!"void".equals(methodReturnType)) {
                md.setType(md.getTypeAsString()+ Codes.gwReturnTypeCode); // Setting the return type of the gateway method
            }
            md.setName(md.getName()+Codes.gwMethodBeginLabelCode); // Adding the gateway method begin label
            md.getParameters().forEach(param -> param.setType(param.getTypeAsString()+ Codes.gwMethodParamTypeLabelCode)); // Adding security label to the gateway parameter
            md.addParameter(Codes.gwMethodExtensionTypeCode, Codes.gwMethodExtensionParamNameCode);
        }
        super.visit(md, gwMethodsSet); //TODO: is it needed ?
        // System.out.println("Method Name Printed: " + md.getName());
    }
}
