package de.tuda.prg.constants;

import java.util.HashMap;
import java.util.Map;

public class Codes {

	public static final String endorse = "endorse";
	public static final String declassify = "declassify";
	public static final String sanitize = "sanitize";


	public static final String principalName = "Alice";
	public static final String parameterPrincipalName = "P";
	
	// ---------------------- ClassName extension --------------------------------------------------------------------
	private static final String classNameExtension = " [principal "+principalName+"] authority ("+principalName+")";
	public static final String classExtensionCode = "CODECLSEXT";

	private static final String classNameExtensionParametrized = " [principal "+parameterPrincipalName+"]";
	public static final String classExtensionCodeParametrized = "CODECLSPEXT";

	private static final String extendClassNameExtensionParametrized = "["+parameterPrincipalName+"]";
	public static final String extendClassExtensionCodeParametrized = "CODEEXTCLSPEXT";

	// ---------------------- Secret fields ---------------------------------------------------------
	private static final String secFieldTypeRegular = "{"+principalName+"->*; "+principalName+"<-*"+"}";
	public static final String secFieldTypeCodeRegular = "CODESECFIELDPRIMTYPEREGULAR";

	private static final String secFieldTypeArray = "{"+principalName+"->*; "+principalName+"<-*"+"}[]{"+principalName+"->*; "+principalName+"<-*"+"}";
	public static final String secFieldTypeCodeArray = "CODESECFIELDPRIMTYPEARRAY";

	private static final String secFieldTypeArray2D = "{"+principalName+"->*; "+principalName+"<-*"+"}[][]{"+principalName+"->*; "+principalName+"<-*"+"}";
	public static final String secFieldTypeCodeArray2D = "CODESECFIELDPRIMTYPEMATRIX";

	private static final String secFieldJifTypeRegular = "[{"+principalName+"->*; "+principalName+"<-*"+"}]";
	public static final String secFieldJifTypeCodeRegular = "CODESECFIELDJIFTYPEREGULAR";

	private static final String secFieldJifTypeArray = "[{"+principalName+"->*; "+principalName+"<-*"+"}][]{"+principalName+"->*; "+principalName+"<-*"+"}";
	public static final String secFieldJifTypeCodeArray = "CODESECFIELDJIFTYPEARRAY";

	private static final String secFieldJifTypeArray2D = "[{"+principalName+"->*; "+principalName+"<-*"+"}][][]{"+principalName+"->*; "+principalName+"<-*"+"}";
	public static final String secFieldJifTypeCodeArray2D = "CODESECFIELDJIFTYPEMATRIX";

	private static final String secFieldUserTypeRegular = "["+principalName+"]{"+principalName+"->*; "+principalName+"<-*"+"}";
	public static final String secFieldUserTypeCodeRegular = "CODESECFIELDUSERTYPEREGULAR";

	private static final String secFieldUserTypeArray = "["+principalName+"]{"+principalName+"->*; "+principalName+"<-*"+"}[]{"+principalName+"->*; "+principalName+"<-*"+"}";
	public static final String secFieldUserTypeCodeArray = "CODESECFIELDUSERTYPEARRAY";

	private static final String secFieldUserTypeArray2D = "["+principalName+"]{"+principalName+"->*; "+principalName+"<-*"+"}[][]{"+principalName+"->*; "+principalName+"<-*"+"}";
	public static final String secFieldUserTypeCodeArray2D = "CODESECFIELDUSERTYPEMATRIX";

	// ---------------------- Gateway methods related ----------------------------------------------
	private static final String gwReturnType = "{"+principalName+"->_; "+principalName+"<-*"+"}";
	public static final String gwReturnTypeCode = "CODEGWRETURNTYPE";

	private static final String gwReturnTypeParametrized = "["+principalName+"]{"+principalName+"->_; "+principalName+"<-*"+"}";
	public static final String gwReturnTypeCodeParametrized = "CODEGWRETURNPARAMTYPE";

	private static final String gwMethodBeginLabel = "{"+principalName+"<-*"+"}";
	public static final String gwMethodBeginLabelCode = "CODEGWMETHODBEGINLABEL";

	private static final String gwMethodExtension = "where authority("+principalName+")";
	public static final String gwMethodExtensionTypeCode = "CODEGWMETHODEXTENSION";

	private static final String gwMethodExtensionParamName = "";
	public static final String gwMethodExtensionParamNameCode = "CODEGWMETHODEXTENSIONPARAMNAME";

	private static final String gwMethodParamTypeLabel = "{}";
	public static final String gwMethodParamTypeLabelCode = "CODEGWMETHODPARAMTYPELABEL";

	private static final String gwMethodParamTypeLabelParametrized = "[Alice]{}";
	public static final String gwMethodParamTypeLabelParametrizedCode = "CODEGWMETHODPARAMTYPEPARAMETRIZEDLABEL";

	private static final String gwMethodParamTypeLabelGeneric = "[{}]";
	public static final String gwMethodParamTypeLabelGenericCode = "CODEGWMETHODPARAMTYPEGENERICLABEL";

	// ---------------------- Encapsulated methods related --------------------------------------------
	private static final String infReturnType = "{"+principalName+"->*; "+principalName+"<-*"+"}";
	public static final String infReturnTypeCode = "CODEINFRETURNTYPELABEL";

	private static final String infReturnTypeParametrized = "["+principalName+"]{"+principalName+"->*; "+principalName+"<-*"+"}";
	public static final String infReturnTypeParametrizedCode = "CODEINFRETURNTYPEPARAMETRIZEDLABEL";

	private static final String infMethodParamTypeLabel = "{"+principalName+"<-*"+"}";
	public static final String infMethodParamTypeLabelCode = "CODEINFMETHODPARAMTYPELABEL";

	private static final String infMethodParamTypeLabelParametrized = "["+principalName+"]{"+principalName+"<-*"+"}";
	public static final String infMethodParamTypeLabelParametrizedCode = "CODEINFMETHODPARAMSECRETTYPEPARAMETRIZEDLABEL";

	private static final String infMethodParamSecretTypeLabel = "{"+principalName+"->*; "+principalName+"<-*"+"}";
	public static final String infMethodParamSecretTypeLabelCode = "CODEINFMETHODPARAMSECRETTYPELABEL";

	private static final String infMethodParamSecretTypeLabelParametrized = "["+principalName+"]{"+principalName+"->*; "+principalName+"<-*"+"}";
	public static final String infMethodParamSecretTypeLabelParametrizedCode = "CODEINFMETHODPARAMTYPEPARAMETRIZEDLABEL";

	private static final String objectCreationTypeParameter = "["+principalName+"]";
	public static final String objectCreationTypeParameterCode = "CODEOBJECTCREATIONEXPRLABEL";

	// ---------------------- Endorse operator call --------------------------------------------
	private static  final String endorseToLabel = "{}"+" to "+"{"+principalName+"<-*"+"}";
	public static final String endorseToLabelCode = "CODEENDORSETOLABEL";

	private static  final String principalParameter = "["+principalName+"]";
	public static final String principalParameterCode = "PRINCIPALPARAMETER";

	// ---------------------- Gateway operator call --------------------------------------------
	private static  final String declassifyToLabel = "{"+principalName+"->*; "+principalName+"<-*"+"}"+" to "+"{"+principalName+"->_; "+principalName+"<-*"+"}";
	public static final String declassifyToLabelCode = "CODEDECLASSIFYTOLABEL";

	// ---------------------- User defined classes ---------------------------------------------------------
	private static final String secFieldPrimitiveTypeParametrizedClass = "{"+parameterPrincipalName+"->*; "+parameterPrincipalName+"<-*"+"}";
	public static final String secFieldPrimitiveTypeCodeParametrizedClass = "CODESECFIELDPRIMTYPEPARAMETRIZEDCLASS";

	private static final String secFieldPrimitiveTypeArrayParametrizedClass = "{"+parameterPrincipalName+"->*; "+parameterPrincipalName+"<-*"+"}[]{"+parameterPrincipalName+"->*; "+parameterPrincipalName+"<-*"+"}";
	public static final String secFieldPrimitiveTypeCodeArrayParametrizedClass = "CODESECFIELDPARAMPRIMTYPEARRAY";

	private static final String secFieldPrimitiveTypeArray2DParametrizedClass = "{"+parameterPrincipalName+"->*; "+parameterPrincipalName+"<-*"+"}[][]{"+parameterPrincipalName+"->*; "+parameterPrincipalName+"<-*"+"}";
	public static final String secFieldPrimitiveTypeCodeArray2DParametrizedClass = "CODESECFIELDPARAMPRIMTYPEMATRIX";

	private static final String secFieldUserTypeParametrizedClass = "["+parameterPrincipalName+"]{"+parameterPrincipalName+"->*; "+parameterPrincipalName+"<-*"+"}";
	public static final String secFieldUserTypeCodeParametrizedClass = "CODESECFIELDUSERTYPEPARAMETRIZEDCLASS";

	private static final String secFieldUserTypeArrayParametrizedClass = "["+parameterPrincipalName+"]{"+parameterPrincipalName+"->*; "+parameterPrincipalName+"<-*"+"}[]{"+parameterPrincipalName+"->*; "+parameterPrincipalName+"<-*"+"}";
	public static final String secFieldUserTypeCodeArrayParametrizedClass = "CODESECFIELDPARAMUSERTYPEARRAY";

	private static final String secFieldUserTypeArray2DParametrizedClass = "["+parameterPrincipalName+"]{"+parameterPrincipalName+"->*; "+parameterPrincipalName+"<-*"+"}[][]{"+parameterPrincipalName+"->*; "+parameterPrincipalName+"<-*"+"}";
	public static final String secFieldUserTypeCodeArray2DParametrizedClass = "CODESECFIELDPARAMUSERTYPEMATRIX";


	public static final String javaFileExtension = "java";
	
	public static final Map<String, String> strReplacement;
	
	static {
		strReplacement = new HashMap<>();
		strReplacement.put(classExtensionCode, classNameExtension);
		strReplacement.put(classExtensionCodeParametrized, classNameExtensionParametrized);
		strReplacement.put(extendClassExtensionCodeParametrized, extendClassNameExtensionParametrized);

		strReplacement.put(secFieldTypeCodeRegular, secFieldTypeRegular);
		strReplacement.put(secFieldTypeCodeArray, secFieldTypeArray);
		strReplacement.put(secFieldTypeCodeArray2D, secFieldTypeArray2D);
		strReplacement.put(secFieldJifTypeCodeRegular, secFieldJifTypeRegular);
		strReplacement.put(secFieldJifTypeCodeArray, secFieldJifTypeArray);
		strReplacement.put(secFieldJifTypeCodeArray2D, secFieldJifTypeArray2D);
		strReplacement.put(secFieldUserTypeCodeRegular, secFieldUserTypeRegular);
		strReplacement.put(secFieldUserTypeCodeArray, secFieldUserTypeArray);
		strReplacement.put(secFieldUserTypeCodeArray2D, secFieldUserTypeArray2D);

		strReplacement.put(secFieldPrimitiveTypeCodeParametrizedClass, secFieldPrimitiveTypeParametrizedClass);
		strReplacement.put(secFieldPrimitiveTypeCodeArrayParametrizedClass, secFieldPrimitiveTypeArrayParametrizedClass);
		strReplacement.put(secFieldPrimitiveTypeCodeArray2DParametrizedClass, secFieldPrimitiveTypeArray2DParametrizedClass);
		strReplacement.put(secFieldUserTypeCodeParametrizedClass, secFieldUserTypeParametrizedClass);
		strReplacement.put(secFieldUserTypeCodeArrayParametrizedClass, secFieldUserTypeArrayParametrizedClass);
		strReplacement.put(secFieldUserTypeCodeArray2DParametrizedClass, secFieldUserTypeArray2DParametrizedClass);

		strReplacement.put(gwReturnTypeCode, gwReturnType);
		strReplacement.put(gwReturnTypeCodeParametrized, gwReturnTypeParametrized);
		strReplacement.put(gwMethodBeginLabelCode, gwMethodBeginLabel);

		strReplacement.put(", " + gwMethodExtensionTypeCode + " " + gwMethodExtensionParamNameCode + ")", ") " + gwMethodExtension);   // Can this be improved ?, as of now the extension is added as an extra parameter with a type and name.

		strReplacement.put(infReturnTypeCode, infReturnType);
		strReplacement.put(infMethodParamTypeLabelCode, infMethodParamTypeLabel);

		strReplacement.put(infReturnTypeParametrizedCode, infReturnTypeParametrized);
		strReplacement.put(infMethodParamTypeLabelParametrizedCode, infMethodParamTypeLabelParametrized);

		strReplacement.put(infMethodParamSecretTypeLabelCode, infMethodParamSecretTypeLabel);
		strReplacement.put(infMethodParamSecretTypeLabelParametrizedCode, infMethodParamSecretTypeLabelParametrized);
		
		strReplacement.put(endorseToLabelCode, endorseToLabel);
		strReplacement.put(principalParameterCode, principalParameter);
		strReplacement.put(declassifyToLabelCode, declassifyToLabel);

		strReplacement.put(gwMethodParamTypeLabelCode, gwMethodParamTypeLabel);
		strReplacement.put(gwMethodParamTypeLabelParametrizedCode, gwMethodParamTypeLabelParametrized);
		strReplacement.put(gwMethodParamTypeLabelGenericCode, gwMethodParamTypeLabelGeneric);

		strReplacement.put(objectCreationTypeParameterCode, objectCreationTypeParameter);
	}
}
