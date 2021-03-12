package de.tuda.prg.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.constants.Codes;
import de.tuda.prg.constants.FileNames;
import de.tuda.prg.constants.RMIConstants;
import de.tuda.prg.entities.ClassNameMethodDecls;
import de.tuda.prg.exceptions.TranslationException;
import de.tuda.prg.parser.generalvisitors.ClassNameGetterVisitor;
import de.tuda.prg.parser.visitorsje.ClassAnnotationCheckerVisitorJe;
import de.tuda.prg.parser.visitorsje.MethodDefinitionVisitorCollectorJe;

import java.io.FileNotFoundException;
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
       mc.setScope(new NameExpr(FileNames.NON_ENCLAVE_WRAPPER_CLASS_BASE_NAME));
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


    public static void getAllGatewayMethods(final CompilationUnit cu, final List<ClassNameMethodDecls> gatewayMethodDeclarations) {
        String[] strArray = new String[1];
        new ClassNameGetterVisitor().visit(cu, strArray);
        String className = strArray[0];

        ClassNameMethodDecls cMd = new ClassNameMethodDecls(className);
        new MethodDefinitionVisitorCollectorJe().visit(cu, cMd.getMethodDeclarations());
        gatewayMethodDeclarations.add(cMd);
    }

    public static String getRMICallReceiverString(String enclaveClassName) {
        String remoteInterfaceName = getRemoteInterfaceName(enclaveClassName);
        String castToRemoteInterface = "(" + remoteInterfaceName + ")";
        String lookUpString = RMIConstants.RMI_OBJECT_LOOKUP_CALL_PREFIX+
                "(\""+remoteInterfaceName+"\")";
        String receiverString = "(" + castToRemoteInterface + " " +  lookUpString + ")";
        return receiverString;

    }

    public static String getRemoteInterfaceName(String enclaveClassName) {
        return RMIConstants.remoteInterfacePrefix+enclaveClassName;
    }

    public static String getWrapperClassName(String enclaveClassName) {
        return RMIConstants.remoteWrapperClassSufix+enclaveClassName;
    }

    public static void addRMIRegistryBindings(CompilationUnit cu, HashSet<String> enclaveClassesToExposeNames) {
        ClassOrInterfaceDeclaration classDeclaration = cu.getClassByName(FileNames.ENCLAVE_MAIN_CLASS_BASE_NAME).get();
        MethodDeclaration mainMethodDeclaration = null;
        List<MethodDeclaration> methodDefinitionList = classDeclaration.getMethodsByName("main");
        if (methodDefinitionList.isEmpty() || (methodDefinitionList.size() > 1) ) {
            throw new IllegalArgumentException("Zero or more than one main methods");
        }
        else {
            mainMethodDeclaration  = methodDefinitionList.get(0);
            final BlockStmt mainMethodBody = mainMethodDeclaration.getBody().get();
            for (String enclaveClassToExposeName : enclaveClassesToExposeNames) {
                final String remoteInterfaceName = getRemoteInterfaceName(enclaveClassToExposeName);
                final String wrapperClassName = getWrapperClassName(enclaveClassToExposeName);
                mainMethodBody.addStatement("Naming.rebind(\""+remoteInterfaceName+"\", new "+wrapperClassName+"());");
            }
            mainMethodBody.addStatement("while(true) {}");
        }
    }

    public static void populateNonEnclaveWrapperMethod(final MethodDeclaration mdGtwMethod, final String enclaveClassName, final MethodDeclaration wrapperVersion) {
        if (mdGtwMethod == null) {
            throw new TranslationException("Method declaration received in null.");
        } else {
            wrapperVersion.setType(mdGtwMethod.getType());
            wrapperVersion.setParameters(mdGtwMethod.getParameters());

            StringBuilder arguments = new StringBuilder();
            for (Parameter currParam : mdGtwMethod.getParameters()) {
                arguments.append(currParam.getNameAsString() + ", ");
            }
            arguments.replace(arguments.length() - 2, arguments.length(), "");

            String argString = "(" + arguments.toString() + ")";
            Type returnType = mdGtwMethod.getType();
            final BlockStmt blockStmt = new BlockStmt();

            if (returnType.isPrimitiveType()) {
                blockStmt.addStatement(returnType+" tempVar;");
            } else {
                blockStmt.addStatement(returnType+" tempVar = null;");
            }
            blockStmt.addStatement("try { tempVar = "+getRMICallReceiverString(enclaveClassName)+"."
                        + mdGtwMethod.getNameAsString()
                        + argString+"; } catch (RemoteException e) { throw new RuntimeException(\"RemoteException from non-enclave to enclave RMI call\");}")
                .addStatement("return tempVar;");
            wrapperVersion.setBody(blockStmt);
        }
    }

    public static void populateGtwMethodNames(List<ClassNameMethodDecls> gatewayMethodDeclarations, HashSet<String> gatewayMethodNames) {
        for (ClassNameMethodDecls clNameMethodDeclare : gatewayMethodDeclarations) {
            Set<String> currClassGtwMethodNames = new HashSet<>();
            for (MethodDeclaration md : clNameMethodDeclare.getMethodDeclarations()) {
                currClassGtwMethodNames.add(md.getNameAsString());
            }
            gatewayMethodNames.addAll(currClassGtwMethodNames);
        }
    }
}
