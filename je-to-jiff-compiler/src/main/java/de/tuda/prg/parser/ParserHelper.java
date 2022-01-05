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
import de.tuda.prg.constants.PathValues;
import de.tuda.prg.constants.RMIConstants;
import de.tuda.prg.entities.ClassNameMethodDecls;
import de.tuda.prg.exceptions.TranslationException;
import de.tuda.prg.parser.generalvisitors.ClassNameGetterVisitor;
import de.tuda.prg.parser.visitorsje.ClassAnnotationCheckerVisitorJe;
import de.tuda.prg.parser.visitorsje.MethodDefinitionVisitorCollectorJe;
import de.tuda.prg.parser.visitorsje.encapsulatedmethodsvisitor.GatewayMethodCallCollectorJe;
import de.tuda.prg.taskprocessing.JARSeperator;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.*;

/**
 * static ParserHelper class including helper functions
 */
public class ParserHelper {

    /**
     * checks if class is annotated with an @Enclave annotation
     * @param cu CompilationUnit of input program
     * @return boolean whethere class is an enclave class or not
     */
    public static boolean isClassAnnotatedWithEnclaveAnnotation(final CompilationUnit cu) {
        VoidVisitorAdapter<ArrayList<Integer>> classAnnotationCheckerVisitorJe = new ClassAnnotationCheckerVisitorJe();
        ArrayList<Integer> annotationStatusIndicator = new ArrayList<Integer>();
        classAnnotationCheckerVisitorJe.visit(cu, annotationStatusIndicator);
        return (annotationStatusIndicator.size() == 1);
    }

    /**
     * checks if class is Main or Generator Class
     * @param cu CompilationUnit of input program
     * @return boolen whethere class is a main class or not
     */
    public static boolean isMainClass(final CompilationUnit cu) {
        System.out.println("---------isMainClass-----------");
        String[] strArray = new String[1];
        new ClassNameGetterVisitor().visit(cu, strArray);
        String className = strArray[0];
        if (className.equals("Main") || className.equals("Generator")) return true; // TODO: How to handle subclasses of MAIN not calles from Enclave?
        else return false;
        /*JARSeperator jarsep = new JARSeperator();
        jarsep.computeSeperation(new File(PathValues.JE_FOLDER_PATH));


        String[] strArray = new String[1];
        new ClassNameGetterVisitor().visit(cu, strArray);
        String className = strArray[0];

        System.out.println(((ArrayList) jarsep.getNonEnclaveJar()).toString());
        System.out.println(((ArrayList) jarsep.getBothJar()).toString());
        System.out.println("---------isMainClass-----------");
        if (jarsep.getNonEnclaveJar().contains(className) && !jarsep.getBothJar().contains(className)) {
            return true;
        } else return false;
        */
    }

    /**
     * checks if a Class or Interface has Methods containing specific annotations from the annotationSet
     * @param cd classOrInterfaceDeclaration that should be checked
     * @param annotationSet Set of annotations for the methods that are searched for
     * @return Map of annotation and corresponding List of methoddeclarations that are annotated with it
     */
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

    /**
     * Computes a parameterList as a String for a methodDeclaration
     * @param md of which the parameters should be written to a String List
     * @return String list of parameters
     */
    public static String getParameterNameListAsString(MethodDeclaration md) {
        String parameterNameList = "(";
        for (Parameter param : md.getParameters()) {
            parameterNameList = parameterNameList.concat(param.getNameAsString()+", ");
        }
        parameterNameList = parameterNameList.replaceAll(", $",")");
        return parameterNameList;
    }


    /**
     * Method for transforming the NonEnclave Gateway call with the rmi call
     * @param mc MethodCall that should be modified
     */
   public static void transformNonEnclaveGatewayCall(final MethodCallExpr mc) {
        System.out.println(mc.toString());
        Expression receiverExpr = mc.getScope().get(); // extract the receiver  // The receiver has to be the class name. It cannot be an identifier, as we are only dealing with the 'static` methods.
        // Here, the run time type of the 'receiverExpr' is "NameExpr"

        // Skipping any checks on the expression assuming it is a class name expression.
        // Added Name Expression check because of filter method of streams
        if (receiverExpr.isNameExpr()) {
            String receiverName = ((NameExpr) receiverExpr).getNameAsString();
            System.out.println("Receiver of the method call = "+receiverName);
            mc.removeScope();
            mc.setScope(new NameExpr(FileNames.NON_ENCLAVE_WRAPPER_CLASS_BASE_NAME));
        }
    }

    /**
     * Identifies the String for the Security Type, depending whetere it is a Java Type, a Jif Type or a User class
     * @param javaType Type that should be checked
     * @return String for replacement of type
     */
    public static String getStringForSecType(Type javaType) {
        if (checkJavaTypes(javaType)) return getStringForSecPrimitiveType(javaType.toString(), false);
        else if (checkJifTypes(javaType)) return getStringForSecJifType(javaType.toString(), false);
        else return getStringForUserType(javaType.toString(), false);
    }

    /**
     * Computes the string for Java Primitive Types and their arrays/2d-arrays
     * @param javaTypeString String of the java Type
     * @param parametrized condition wheter it is parametrized or not
     * @return String for Security Primitve Type
     */
    private static String getStringForSecPrimitiveType(String javaTypeString, boolean parametrized) {
        String secFieldTypeReg, secFieldTypeArray, secFieldTypeArray2D;
        if (!parametrized) {
            secFieldTypeReg = Codes.secFieldTypeCodeRegular;
            secFieldTypeArray = Codes.secFieldTypeCodeArray;
            secFieldTypeArray2D = Codes.secFieldTypeCodeArray2D;
        } else {
            secFieldTypeReg = Codes.secFieldPrimitiveTypeCodeParametrizedClass;
            secFieldTypeArray = Codes.secFieldPrimitiveTypeCodeArrayParametrizedClass;
            secFieldTypeArray2D = Codes.secFieldPrimitiveTypeCodeArray2DParametrizedClass;
        }
        String trimmedString = javaTypeString.trim();
        String str;
        if (trimmedString.endsWith("[][]")) {
            str = trimmedString.substring(0, trimmedString.length() - 4)+ secFieldTypeArray2D;
        } else if (trimmedString.endsWith("[]")) {
            str = trimmedString.substring(0, trimmedString.length() - 2)+ secFieldTypeArray;
        } else {
            str = javaTypeString+secFieldTypeReg;
        }
        return str;
    }

    /**
     * Computes the string for Jif Primitive Types and their arrays/2d-arrays
     * @param javaTypeString String of the jif Type
     * @param parametrized condition wheter it is parametrized or not
     * @return String for Security Type
     */
    private static String getStringForSecJifType(String javaTypeString, boolean parametrized) {
        String trimmedString = javaTypeString.trim();
        String str;
        if (trimmedString.endsWith("[][]")) {
            str = trimmedString.substring(0, trimmedString.length() - 4)+ Codes.secFieldJifTypeCodeArray2D;
        } else if (trimmedString.endsWith("[]")) {
            str = trimmedString.substring(0, trimmedString.length() - 2)+ Codes.secFieldJifTypeCodeArray;
        } else {
            str = javaTypeString+Codes.secFieldJifTypeCodeRegular;
        }
        return str;
    }

    /**
     * Computes the string for User Class Types and their arrays/2d-arrays
     * @param javaTypeString String of the user Type
     * @param parametrized condition wheter it is parametrized or not
     * @return String for Security Type
     */
    private static String getStringForUserType(String javaTypeString, boolean parametrized) {
        String secFieldTypeReg, secFieldTypeArray, secFieldTypeArray2D;
        if (!parametrized) {
            secFieldTypeReg = Codes.secFieldUserTypeCodeRegular;
            secFieldTypeArray = Codes.secFieldUserTypeCodeArray;
            secFieldTypeArray2D = Codes.secFieldUserTypeCodeArray2D;
        } else {
            secFieldTypeReg = Codes.secFieldUserTypeCodeParametrizedClass;
            secFieldTypeArray = Codes.secFieldUserTypeCodeArrayParametrizedClass;
            secFieldTypeArray2D = Codes.secFieldUserTypeCodeArray2DParametrizedClass;
        }
        String trimmedString = javaTypeString.trim();
        String str;
        if (trimmedString.endsWith("[][]")) {
            str = trimmedString.substring(0, trimmedString.length() - 4)+ secFieldTypeArray;
        } else if (trimmedString.endsWith("[]")) {
            str = trimmedString.substring(0, trimmedString.length() - 2)+ secFieldTypeArray2D;
        } else {
            str = javaTypeString+secFieldTypeReg;
        }
        return str;
    }

    /**
     * Identifies the String for the parametrized Security Type, depending whetere it is a Java Type or a User class type
     * @param javaType Type that should be checked
     * @return String for replacement of type
     */
    public static String getStringForParametrizedType(Type javaType) {
        if (checkJavaTypes(javaType)) return getStringForSecPrimitiveType(javaType.toString(), true);
        else return getStringForUserType(javaType.toString(), true);
    }

    /**
     * collects all gateway methods inside a class
     * @param cu CompilationUnit that should be parsed
     * @param gatewayMethodDeclarations List where the gateway declarations should be added
     */
    public static void populateAllGatewayMethodsInCu(final CompilationUnit cu, final List<ClassNameMethodDecls> gatewayMethodDeclarations) {
        String[] strArray = new String[1];
        new ClassNameGetterVisitor().visit(cu, strArray);
        String className = strArray[0];

        ClassNameMethodDecls cMd = new ClassNameMethodDecls(className);
        new MethodDefinitionVisitorCollectorJe().visit(cu, cMd.getMethodDeclarations());
        gatewayMethodDeclarations.add(cMd);
    }

    /**
     * collects all encapsulated methods inside a gateway method
     * @param cu CompilationUnit that should be parsed
     * @param gatewayMethodCalls List where the methodcalls should be added
     */
    public static void populateAllEncapsulatedMethodsInsideGatewayMethod(final CompilationUnit cu, final List<MethodCallExpr> gatewayMethodCalls) {
        String[] strArray = new String[1];
        new ClassNameGetterVisitor().visit(cu, strArray);
        String className = strArray[0];

        GatewayMethodCallCollectorJe gwmcec = new GatewayMethodCallCollectorJe();
        gwmcec.visit(cu, gatewayMethodCalls);
    }

    /**
     * computes the RMI receiverString for the enclave class
     * @param enclaveClassName name of the enclave class
     * @return receiverString
     */
    public static String getRMICallReceiverString(String enclaveClassName) {
        String remoteInterfaceName = getRemoteInterfaceName(enclaveClassName);
        String castToRemoteInterface = "(" + remoteInterfaceName + ")";
        String lookUpString = RMIConstants.RMI_OBJECT_LOOKUP_CALL_PREFIX+
                "(\""+remoteInterfaceName+"\")";
        String receiverString = "(" + castToRemoteInterface + " " +  lookUpString + ")";
        return receiverString;

    }

    /**
     * computes the interface name for the Remote Object
     * @param enclaveClassName name of enclave
     * @return remoteInterfaceName
     */
    public static String getRemoteInterfaceName(String enclaveClassName) {
        return RMIConstants.remoteInterfacePrefix+enclaveClassName;
    }

    /**
     * computes the name for the wrapper class
     * @param enclaveClassName name of enclave
     * @return wrapperClassName
     */
    public static String getWrapperClassName(String enclaveClassName) {
        return RMIConstants.remoteWrapperClassSufix+enclaveClassName;
    }

    /**
     * 
     * @param cu
     * @param enclaveClassesToExposeNames
     */
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

    /**
     * 
     * @param mdGtwMethod
     * @param enclaveClassName
     * @param wrapperVersion
     */
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
                        + argString+"; } catch (RemoteException e) { e.printStackTrace();\nthrow new RuntimeException(\"RemoteException from non-enclave to enclave RMI call: \");}")
                .addStatement("return tempVar;");
            wrapperVersion.setBody(blockStmt);
        }
    }

    /**
     * 
     * @param gatewayMethodDeclarations
     * @param gatewayMethodNames
     */
    public static void populateMethodNamesFromDeclarations(List<ClassNameMethodDecls> gatewayMethodDeclarations, HashSet<String> gatewayMethodNames) {
        for (ClassNameMethodDecls clNameMethodDeclare : gatewayMethodDeclarations) {
            Set<String> currClassGtwMethodNames = new HashSet<>();
            for (MethodDeclaration md : clNameMethodDeclare.getMethodDeclarations()) {
                currClassGtwMethodNames.add(md.getNameAsString());
            }
            gatewayMethodNames.addAll(currClassGtwMethodNames);
        }
    }

    /**
     * 
     * @param t
     * @return
     */
    public static boolean checkJavaTypes(Type t) {
        if (t.isPrimitiveType()) return true;
        else if (t.getElementType().isPrimitiveType()) return true;
        else {
            System.out.println(t.asString());
            switch(t.asString()) {
                case "String" : return true;
                case "Double" : return true;
            }
            switch(t.getElementType().asString()) {
                case "String" : return true;
                case "Double" : return true;
                default : return false;
            }
        }
    }

    /**
     * 
     * @param t
     * @return
     */
    public static boolean checkJifTypes(Type t) {
        switch(t.getElementType().asString()) {
            case "List" : return true;
            default : return false;
        }
    }
}
