package de.tuda.stg.Parser;

import java.util.HashMap;
import java.util.Map;

public class Codes {

	public static final String endorse = "endorse";
	public static final String declassify = "declassify";


	public static final String principalName = "Enclave";
	
	// ---------------------- ClassName extension --------------------------------------------------------------------
	private static final String classNameExtension = " [principal "+principalName+"] authority ("+principalName+")";
	public static final String classExtensionCode = "CODECLSEXT";
	
	// ---------------------- Secret fields ---------------------------------------------------------
	private static final String secFieldType = "{"+principalName+"->*; "+principalName+"<-*"+"}";
	public static final String secFieldTypeCode = "CODESECFIELDTYPE";
	
	// ---------------------- Gateway methods related ----------------------------------------------
	private static final String gwReturnType = "{"+principalName+"->_; "+principalName+"<-*"+"}";
	public static final String gwReturnTypeCode = "CODEGWRETURNTYPE";

	private static final String gwMethodBeginLabel = "{"+principalName+"<-*"+"}";
	public static final String gwMethodBeginLabelCode = "CODEGWMETHODBEGINLABEL";

	private static final String gwMethodExtension = "where authority("+principalName+")";
	public static final String gwMethodExtensionTypeCode = "CODEGWMETHODEXTENSION";

	private static final String gwMethodExtensionParamName = "";
	public static final String gwMethodExtensionParamNameCode = "CODEGWMETHODEXTENSIONPARAMNAME";

	private static final String gwMethodParamTypeLabel = "{}";
	public static final String gwMethodParamTypeLabelCode = "CODEGWMETHODPARAMTYPELABEL";

	// ---------------------- Endorse operator call --------------------------------------------
	private static  final String endorseToLabel = "{}"+" to "+"{"+principalName+"<-*"+"}";
	public static final String endorseToLabelCode = "CODEENDORSETOLABEL";

	// ---------------------- Gateway operator call --------------------------------------------
	private static  final String declassifyToLabel = "{"+principalName+"->*; "+principalName+"<-*"+"}"+" to "+"{"+principalName+"->_; "+principalName+"<-*"+"}";
	public static final String declassifyToLabelCode = "CODEDECLASSIFYTOLABEL";
	
	
	public static final Map<String, String> strReplacement;
	
	static
    { 
		strReplacement = new HashMap<>(); 
		strReplacement.put(classExtensionCode, classNameExtension); 
		strReplacement.put(secFieldTypeCode, secFieldType); 
		strReplacement.put(gwReturnTypeCode, gwReturnType);
		strReplacement.put(gwMethodBeginLabelCode, gwMethodBeginLabel);

		strReplacement.put(", "+gwMethodExtensionTypeCode+" "+gwMethodExtensionParamNameCode+")", ") "+gwMethodExtension);   // Can this be improved ?, as of now the extension is added as an extra parameter with a type and name.

		strReplacement.put(endorseToLabelCode, endorseToLabel);
		strReplacement.put(declassifyToLabelCode, declassifyToLabel);
		strReplacement.put(gwMethodParamTypeLabelCode, gwMethodParamTypeLabel);

		
    } 
	
	public static final String javaFileExtension = "java";

}
