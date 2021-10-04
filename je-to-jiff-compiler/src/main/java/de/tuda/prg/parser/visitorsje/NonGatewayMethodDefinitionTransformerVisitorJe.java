package de.tuda.prg.parser.visitorsje;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.annotations.Gateway;
import de.tuda.prg.constants.Codes;
import de.tuda.prg.parser.ParserHelper;

import java.util.List;

public class NonGatewayMethodDefinitionTransformerVisitorJe extends VoidVisitorAdapter<List<String>>  {
    @Override
    public void visit(MethodDeclaration md, List<String> parameters) {
        if (!md.isAnnotationPresent(Gateway.class)) {
            // Process all other Methods that are not GW Methods

            if (md.isStatic()) md.removeModifier(Modifier.Keyword.STATIC);   // Removing the static modifier
            final String methodReturnType = md.getTypeAsString();
            if (!"void".equals(methodReturnType)) {
                if (ParserHelper.checkJavaTypes(md.getType())) md.setType(md.getTypeAsString() + Codes.infReturnTypeCode); // Setting the return type of the gateway method
                else md.setType(md.getTypeAsString() + Codes.infReturnTypeParametrizedCode); 
            }
            md.setName(md.getName() + Codes.gwMethodBeginLabelCode); // Adding the gateway method begin label
            md.getParameters().forEach(param -> {
                int i = md.getParameters().indexOf(param);
                System.out.println("Index of parameter is: "+i);
                //param.setType(param.getTypeAsString() + Codes.infMethodParamTypeLabelCode);
                if (ParserHelper.checkJavaTypes(param.getType())) {
                    if (parameters.get(i).equals("endorsed")) param.setType(param.getTypeAsString() + Codes.infMethodParamTypeLabelCode);
                    else if (parameters.get(i).equals("secret")) param.setType(param.getTypeAsString() + Codes.infMethodParamSecretTypeLabelCode);
                } else {
                    if (parameters.get(i).equals("endorsed")) param.setType(param.getTypeAsString() + Codes.infMethodParamTypeLabelParametrizedCode);
                    else if (parameters.get(i).equals("secret")) param.setType(param.getTypeAsString() + Codes.infMethodParamSecretTypeLabelParametrizedCode);
                }
            });
        }
        super.visit(md, parameters); // Keeping all the 'super.visit' calls as the last command in the method
    }

    @Override
    public void visit(ObjectCreationExpr oce, List<String> parameters) {
        if (!ParserHelper.checkJavaTypes(oce.getType())) oce.setType(oce.getTypeAsString()+Codes.objectCreationTypeParameterCode);
    }
}
