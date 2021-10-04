package de.tuda.prg.constants;

import java.util.HashMap;
import java.util.Map;

public class Codes {

	public static final String endorse = "endorse";
	public static final String declassify = "declassify";


	public static final String principalName = "Alice";
	public static final String parameterPrincipalName = "P";
	
	// ---------------------- ClassName extension --------------------------------------------------------------------
	private static final String classNameExtension = " [principal "+principalName+"] authority ("+principalName+")";
	public static final String classExtensionCode = "CODECLSEXT";

	private static final String classNameExtensionParametrized = " [principal "+parameterPrincipalName+"]";
	public static final String classExtensionCodeParametrized = "CODECLSPEXT";

	// ---------------------- Secret fields ---------------------------------------------------------
	private static final String secFieldTypeRegular = "{"+principalName+"->*; "+principalName+"<-*"+"}";
	public static final String secFieldTypeCodeRegular = "CODESECFIELDTYPEREGULAR";

	private static final String secFieldTypeArray = "{"+principalName+"->*; "+principalName+"<-*"+"}[]{"+principalName+"->*; "+principalName+"<-*"+"}";
	public static final String secFieldTypeCodeArray = "CODESECFIELDTYPEARRAY";

	private static final String secFieldTypeArray2D = "{"+principalName+"->*; "+principalName+"<-*"+"}[][]{"+principalName+"->*; "+principalName+"<-*"+"}";
	public static final String secFieldTypeCodeArray2D = "CODESECFIELDTYPEMATRIX";

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

	// ---------------------- Gateway operator call --------------------------------------------
	private static  final String declassifyToLabel = "{"+principalName+"->*; "+principalName+"<-*"+"}"+" to "+"{"+principalName+"->_; "+principalName+"<-*"+"}";
	public static final String declassifyToLabelCode = "CODEDECLASSIFYTOLABEL";

	// ---------------------- User defined classes ---------------------------------------------------------
	private static final String secFieldTypeParametrizedClass = "{"+parameterPrincipalName+"->*; "+parameterPrincipalName+"<-*"+"}";
	public static final String secFieldTypeCodeParametrizedClass = "CODESECFIELDTYPEPARAMETRIZEDCLASS";

	private static final String secFieldTypeArrayParametrizedClass = "{"+parameterPrincipalName+"->*; "+parameterPrincipalName+"<-*"+"}[]{"+parameterPrincipalName+"->*; "+parameterPrincipalName+"<-*"+"}";
	public static final String secFieldTypeCodeArrayParametrizedClass = "CODESECFIELDPARAMTYPEARRAY";

	private static final String secFieldTypeArray2DParametrizedClass = "{"+parameterPrincipalName+"->*; "+parameterPrincipalName+"<-*"+"}[][]{"+parameterPrincipalName+"->*; "+parameterPrincipalName+"<-*"+"}";
	public static final String secFieldTypeCodeArray2DParametrizedClass = "CODESECFIELDPARAMTYPEMATRIX";


	public static final String javaFileExtension = "java";
	
	public static final Map<String, String> strReplacement;
	
	static {
		strReplacement = new HashMap<>();
		strReplacement.put(classExtensionCode, classNameExtension);
		strReplacement.put(classExtensionCodeParametrized, classNameExtensionParametrized);

		strReplacement.put(secFieldTypeCodeRegular, secFieldTypeRegular);
		strReplacement.put(secFieldTypeCodeArray, secFieldTypeArray);
		strReplacement.put(secFieldTypeCodeArray2D, secFieldTypeArray2D);

		strReplacement.put(secFieldTypeCodeParametrizedClass, secFieldTypeParametrizedClass);
		strReplacement.put(secFieldTypeCodeArrayParametrizedClass, secFieldTypeArrayParametrizedClass);
		strReplacement.put(secFieldTypeCodeArray2DParametrizedClass, secFieldTypeArray2DParametrizedClass);

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
		strReplacement.put(declassifyToLabelCode, declassifyToLabel);

		strReplacement.put(gwMethodParamTypeLabelCode, gwMethodParamTypeLabel);
		strReplacement.put(gwMethodParamTypeLabelParametrizedCode, gwMethodParamTypeLabelParametrized);

		strReplacement.put(objectCreationTypeParameterCode, objectCreationTypeParameter);
	}
}
