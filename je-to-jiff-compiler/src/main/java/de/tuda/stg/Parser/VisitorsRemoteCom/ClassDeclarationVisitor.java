package de.tuda.stg.Parser.VisitorsRemoteCom;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.stg.Annotations.Enclave;
import de.tuda.stg.Annotations.Gateway;
import de.tuda.stg.Constants.StringConstants;
import de.tuda.stg.Parser.Codes;
import de.tuda.stg.Parser.ParserHelper;

import java.rmi.Remote;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassDeclarationVisitor extends VoidVisitorAdapter<HashSet<String>> {
    @Override
    public void visit(ClassOrInterfaceDeclaration cOrID, HashSet<String> classNameSet) {
        if (cOrID.isAnnotationPresent(Enclave.class)) {
            String className = cOrID.getNameAsString();
            classNameSet.add(className);
            //creating the corresponding remote wrapper interface
            // String remoteInterface = new String();
            CompilationUnit compilationUnitInterface = new CompilationUnit();
            compilationUnitInterface.addImport(StringConstants.javaRMIAll);
            compilationUnitInterface.setPackageDeclaration(StringConstants.remotePackageName);
            String remoteInterfaceName = "Remote"+className;
            ClassOrInterfaceDeclaration interfaceDeclaration = compilationUnitInterface.addInterface(remoteInterfaceName).addExtendedType(Remote.class);

            CompilationUnit compilationUnitWrapperClass = new CompilationUnit();
            compilationUnitWrapperClass.addImport(StringConstants.javaRMIAll);
            compilationUnitWrapperClass.setPackageDeclaration(StringConstants.remotePackageName);
            ClassOrInterfaceDeclaration wrapperClassDeclaration = compilationUnitInterface.addClass(className+"Wrapper").addExtendedType(StringConstants.remoteObjectClass).addImplementedType(remoteInterfaceName);

            // Need to add all the Gateway methods into the generated remote class



            cOrID.getAnnotationByClass(Enclave.class).get().remove();   // Removing the @Enclave annotation
            cOrID.setName(cOrID.getName()+ Codes.classExtensionCode);
            cOrID.addModifier(Modifier.Keyword.FINAL);   // Adding the keyword 'final'
            super.visit(cOrID, classNameSet);
        }
    }

    public void getAndAddGatewayMethods(ClassOrInterfaceDeclaration cOrID, ClassOrInterfaceDeclaration wrapperClassDeclaration) {
        Set<String> annotationsSet = new HashSet<>();
        String strGateway = Gateway.class.getSimpleName();
        annotationsSet.add(strGateway);
        Map<String, List<MethodDeclaration>> annotatedMethodsMap = ParserHelper.getMethodDeclarationWithAnnotations(cOrID, annotationsSet);
        if (annotatedMethodsMap != null && !annotatedMethodsMap.isEmpty()) {
            // Add the methods in the 'annotatedMethodsMap' into the wrapperClassDeclaration
            for (MethodDeclaration gtwMd :  annotatedMethodsMap.get(strGateway)) {
                wrapperClassDeclaration.addMethod(gtwMd);
            }
        }
    }




}

