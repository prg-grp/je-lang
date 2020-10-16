package de.tuda.stg.Parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.stg.Constants.Codes;
import de.tuda.stg.Constants.StringConstants;
import de.tuda.stg.Parser.VisitorsJe.ClassAnnotationCheckerVisitorJe;
import de.tuda.stg.Parser.VisitorsJe.MethodDefinitionVisitorCollectorJe;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ParserHelper {

    public static boolean isClassAnnotatedWithEnclaveAnnotation(final CompilationUnit cu) {
        VoidVisitorAdapter<ArrayList<Integer>> classAnnotationCheckerVisitorJe = new ClassAnnotationCheckerVisitorJe();
        ArrayList<Integer> annotationStatusIndicator = new ArrayList<Integer>();
        classAnnotationCheckerVisitorJe.visit(cu, annotationStatusIndicator);
        return (annotationStatusIndicator.size() == 1);
    }

    public static Map<String, List<MethodDeclaration>> getMethodDeclarationWithAnnotations(ClassOrInterfaceDeclaration cd, Set<String> annotationSet) {
        Map<String, List<MethodDeclaration>> annotationMethodsMap = new HashMap<>();
        if (annotationSet != null && !annotationSet.isEmpty()) {
            for (MethodDeclaration md : cd.getMethods()) {
                NodeList<AnnotationExpr> annotations = md.getAnnotations();
                for (AnnotationExpr annotationExpr : annotations) {
                    String annotationName = annotationExpr.getNameAsString();
                    if (annotationSet.contains(annotationName)) {
                        List<MethodDeclaration> mdList = annotationMethodsMap.get(annotationName);
                        if (mdList == null) {
                            List newMdList = new ArrayList<MethodDeclaration>();
                            newMdList.add(md);
                            annotationMethodsMap.put(annotationName, newMdList);
                        } else {
                            mdList.add(md);
                        }
                    }
                }
            }
        }
        return annotationMethodsMap;
    }

    public static String getParameterNameListAsString(MethodDeclaration md) {
        String parameterNameList = "(";
        for (Parameter param : md.getParameters()) {
            parameterNameList = parameterNameList.concat(param.getNameAsString()+", ");
        }
        parameterNameList = parameterNameList.replaceAll(", $",")");
        return parameterNameList;
    }


   public static void transformNonEnclaveGatewayCall(final MethodCallExpr mc) {
        Expression receiverExpr = mc.getScope().get(); // extract the receiver  // The receiver has to be the class name. It cannot be an identifier, as we are only dealing with the 'static` methods.
       // Here, the run time type of the 'receiverExpr' is "NameExpr"


       // Skipping any checks on the expression assuming it is a class name expression.
       String receiverName = ((NameExpr) receiverExpr).getNameAsString();
       System.out.println("Receiver of the method call = "+receiverName);
       mc.removeScope();
       mc.setScope(new NameExpr(ParserHelper.getRMICallReceiverString(receiverName)));
    }

    public static void writeStringToFile(final String fileName, final String data) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] strToBytes = data.getBytes();
        try {
            outputStream.write(strToBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getStringForSecType(String javaTypeString) {
        String trimmedString = javaTypeString.trim();
        String str;
        if (trimmedString.endsWith("[]")) {
            str = trimmedString.substring(0, trimmedString.length() - 2)+ Codes.secFieldTypeCodeArray;
        } else {
            str = javaTypeString+Codes.secFieldTypeCodeRegular;
        }
        return str;
    }


    public static void getAllGatewayMethods(final CompilationUnit cu, final HashSet<String> gatewayMethodNames) {
        new MethodDefinitionVisitorCollectorJe().visit(cu, gatewayMethodNames);
    }

    public static String getRMICallReceiverString(String enclaveClassName) {
        String remoteInterfaceName = getRemoteInterfaceName(enclaveClassName);
        String castToRemoteInterface = "(" + remoteInterfaceName + ")";
        String lookUpString = StringConstants.rmiLookUpURLPrefix+
                remoteInterfaceName +
                "\")";
        String receiverString = "(" + castToRemoteInterface + " " +  lookUpString + ")";
        return receiverString;

    }

    public static String getRemoteInterfaceName(String enclaveClassName) {
        return StringConstants.remoteInterfacePrefix+enclaveClassName;
    }

    public static String getWrapperClassName(String enclaveClassName) {
        return StringConstants.remoteWrapperClassSufix+enclaveClassName;
    }
}
