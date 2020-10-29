package de.tuda.stg.Parser.VisitorsRemoteCom;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.stg.Annotations.Enclave;
import de.tuda.stg.Annotations.Gateway;
import de.tuda.stg.Constants.RMIConstants;
import de.tuda.stg.Constants.PathValues;
import de.tuda.stg.Parser.ParserHelper;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EnclaveClassDeclarationVisitorComm extends VoidVisitorAdapter<Set<String>> {
    @Override()
    public void visit(final ClassOrInterfaceDeclaration cOrID, final Set<String> enclaveClassesToBeExposedNames) {
        if (cOrID.isAnnotationPresent(Enclave.class)) {
            String className = cOrID.getNameAsString();
            enclaveClassesToBeExposedNames.add(className);

            // Generating the remote interface
            final CompilationUnit cuRemoteInterface = new CompilationUnit();   //creating the corresponding remote wrapper interface
            cuRemoteInterface.addImport(RMIConstants.javaRMIAll);
            cuRemoteInterface.setPackageDeclaration(RMIConstants.remotePackageName);

            final String remoteInterfaceName  = ParserHelper.getRemoteInterfaceName(className);

            final ClassOrInterfaceDeclaration interfaceDeclaration = cuRemoteInterface.addInterface(remoteInterfaceName).addExtendedType(Remote.class);



            // Generating the wrapper class
            final CompilationUnit cuWrapperClass = new CompilationUnit();
            cuWrapperClass.addImport(RMIConstants.javaRMIAll);
            cuWrapperClass.setPackageDeclaration(RMIConstants.remotePackageName);
            final String wrapperClassName = ParserHelper.getWrapperClassName(className);

            final ClassOrInterfaceDeclaration wrapperClassDeclaration = cuWrapperClass.addClass(wrapperClassName).addExtendedType(RMIConstants.remoteObjectClass).addImplementedType(remoteInterfaceName);
            getAndAddGatewayMethodsToTheRemoteInterface(cOrID, interfaceDeclaration, wrapperClassDeclaration);   // Adding all the Gateway methods into the generated remote class

            final String remoteInterfaceAsString =  cuRemoteInterface.toString();
            System.out.println("--------------Printing the created remote interface ---------------------------");
            System.out.println(remoteInterfaceAsString);

            ParserHelper.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX+remoteInterfaceName+".java", remoteInterfaceAsString);   //Ideally, this file writing should be in some file writing utility class.

            final String wrapperClassAsString = cuWrapperClass.toString();
            System.out.println("--------------Printing the created wrapper class ---------------------------");
            System.out.println(wrapperClassAsString);

            ParserHelper.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX+wrapperClassName+".java", wrapperClassAsString);   //Ideally, this file writing should not be here.

            // super.visit(cOrID, arg); // TODO: Is this needed ?
        }
    }

    public void getAndAddGatewayMethodsToTheRemoteInterface(final ClassOrInterfaceDeclaration cOrID, final ClassOrInterfaceDeclaration interfaceDeclaration, final ClassOrInterfaceDeclaration wrapperClassDeclaration) {
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
                remoteMethodInInterface.removeBody(); // Since it is a method inside an Interface.

                wrapperMethodInClass.addAnnotation(RMIConstants.overrideAnno);
                BlockStmt blockStmt = new BlockStmt();
                blockStmt.addStatement("return "+cOrID.getNameAsString()+"."+gtwMd.getNameAsString()+ParserHelper.getParameterNameListAsString(gtwMd)+";");
                wrapperMethodInClass.setBody(blockStmt);
                wrapperMethodInClass.addThrownException(RemoteException.class);
            }
        }
    }




}

