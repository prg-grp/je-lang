package de.tuda.stg.Parser.VisitorsRemoteCom;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.stg.Annotations.Gateway;
import de.tuda.stg.Parser.Codes;

import java.util.HashSet;

public class MethodDefinitionVisitor extends VoidVisitorAdapter<HashSet<String>> {

    @Override
    public void visit(MethodDeclaration md, HashSet<String> gwMethodsSet) {
        if (md.isAnnotationPresent(Gateway.class)) {
            gwMethodsSet.add(md.getNameAsString());
            md.getAnnotationByClass(Gateway.class).get().remove();   // Removing the @Gateway annotation
            md.setType(md.getTypeAsString()+ Codes.gwReturnTypeCode); // Setting the return type of the gateway method
            md.setName(md.getName()+Codes.gwMethodBeginLabelCode); // Adding the gateway method begin label
            md.getParameters().forEach(param -> param.setType(param.getTypeAsString()+ Codes.gwMethodParamTypeLabelCode)); // Adding security label to the gateway parameter
            md.addParameter(Codes.gwMethodExtensionTypeCode, Codes.gwMethodExtensionParamNameCode);
        }
        super.visit(md, gwMethodsSet);
        // System.out.println("Method Name Printed: " + md.getName());
    }
}
