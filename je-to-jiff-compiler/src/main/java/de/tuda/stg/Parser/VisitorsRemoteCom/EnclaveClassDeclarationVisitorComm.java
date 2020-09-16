package de.tuda.stg.Parser.VisitorsRemoteCom;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.stg.Annotations.Enclave;
import de.tuda.stg.Annotations.Gateway;
import de.tuda.stg.Constants.StringConstants;
import de.tuda.stg.Parser.Codes;
import de.tuda.stg.Parser.ParserHelper;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EnclaveClassDeclarationVisitorComm extends VoidVisitorAdapter<Void> {
    @Override()
    public void visit(ClassOrInterfaceDeclaration cOrID, Void arg) {
        if (cOrID.isAnnotationPresent(Enclave.class)) {
            String className = cOrID.getNameAsString();
            //creating the corresponding remote wrapper interface
            // String remoteInterface = new String();
            final CompilationUnit cuRemoteInterface = new CompilationUnit();
            cuRemoteInterface.addImport(StringConstants.javaRMIAll);
            cuRemoteInterface.setPackageDeclaration(StringConstants.remotePackageName);
            final String remoteInterfaceName = StringConstants.remoteInterfacePrefix+className;
            final ClassOrInterfaceDeclaration interfaceDeclaration = cuRemoteInterface.addInterface(remoteInterfaceName).addExtendedType(Remote.class);

            final CompilationUnit cuWrapperClass = new CompilationUnit();
            cuWrapperClass.addImport(StringConstants.javaRMIAll);
            cuWrapperClass.setPackageDeclaration(StringConstants.remotePackageName);
            final ClassOrInterfaceDeclaration wrapperClassDeclaration = cuWrapperClass.addClass(className+StringConstants.remoteWrapperClassSufix).addExtendedType(StringConstants.remoteObjectClass).addImplementedType(remoteInterfaceName);
            getAndAddGatewayMethods(cOrID, interfaceDeclaration, wrapperClassDeclaration);   // Adding all the Gateway methods into the generated remote class

            final String remoteInterfaceAsString =  cuRemoteInterface.toString();
            System.out.println("--------------Printing the created remote interface ---------------------------");
            System.out.println(remoteInterfaceAsString);

            final String wrapperClassAsString = cuWrapperClass.toString();
            System.out.println("--------------Printing the created wrapper class ---------------------------");
            System.out.println(wrapperClassAsString);

            super.visit(cOrID, arg); // Is this needed ?
        }
    }

    public void getAndAddGatewayMethods(final ClassOrInterfaceDeclaration cOrID, final ClassOrInterfaceDeclaration interfaceDeclaration, final ClassOrInterfaceDeclaration wrapperClassDeclaration) {
        Set<String> annotationsSet = new HashSet<>();
        String strGateway = Gateway.class.getSimpleName();
        annotationsSet.add(strGateway);
        Map<String, List<MethodDeclaration>> annotatedMethodsMap = ParserHelper.getMethodDeclarationWithAnnotations(cOrID, annotationsSet);
        if (annotatedMethodsMap != null && !annotatedMethodsMap.isEmpty()) {
            // Add the methods in the 'annotatedMethodsMap' into the wrapperClassDeclaration
            for (MethodDeclaration gtwMd :  annotatedMethodsMap.get(strGateway)) {
                MethodDeclaration wrapperMethodInClass = wrapperClassDeclaration.addMethod(gtwMd.getNameAsString(), Modifier.Keyword.PUBLIC); // can not add a method declaration directly ? seems like need to break it and then again add
                MethodDeclaration remoteMethodInInterface = interfaceDeclaration.addMethod(gtwMd.getNameAsString(), Modifier.Keyword.PUBLIC);

                wrapperMethodInClass.setType(gtwMd.getType());
                remoteMethodInInterface.setType(gtwMd.getType());

                wrapperMethodInClass.setParameters(gtwMd.getParameters());
                remoteMethodInInterface.setParameters(gtwMd.getParameters());
                remoteMethodInInterface.removeBody(); // Because it is a method inside an Interface.

                wrapperMethodInClass.addAnnotation(StringConstants.overrideAnno);
                BlockStmt blockStmt = new BlockStmt();
                blockStmt.addStatement("return "+cOrID.getNameAsString()+"."+gtwMd.getNameAsString()+ParserHelper.getParameterNameListAsString(gtwMd)+";");
                wrapperMethodInClass.setBody(blockStmt);
                wrapperMethodInClass.addThrownException(RemoteException.class);
            }
        }
    }




}

