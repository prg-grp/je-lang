package de.tuda.prg.parser.visitorsremotecom;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.annotations.Enclave;
import de.tuda.prg.annotations.Gateway;
import de.tuda.prg.constants.RMIConstants;
import de.tuda.prg.constants.PathValues;
import de.tuda.prg.exceptions.FileIOException;
import de.tuda.prg.filehandling.FileUtils;
import de.tuda.prg.parser.ParserHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

            // cuRemoteInterface.setPackageDeclaration(PathValues.GENERATED_JAVA_PACKAGE_NAME);  // No package declaration, no nested folders as of now

            final String remoteInterfaceName  = ParserHelper.getRemoteInterfaceName(className);

            final ClassOrInterfaceDeclaration interfaceDeclaration = cuRemoteInterface.addInterface(remoteInterfaceName).addExtendedType(Remote.class);

            // Generating the enclave wrapper class
            final CompilationUnit cuEncWrapperClass = new CompilationUnit();
            cuEncWrapperClass.addImport(RMIConstants.javaRMIAll);
            cuEncWrapperClass.addImport(RMIConstants.javaRMIUnicastObj);
            // cuWrapperClass.setPackageDeclaration(PathValues.GENERATED_JAVA_PACKAGE_NAME); // No package declaration, no nested folders as of now

            final String wrapperClassName = ParserHelper.getWrapperClassName(className);

            final ClassOrInterfaceDeclaration wrapperClassDeclaration = cuEncWrapperClass.addClass(wrapperClassName).addExtendedType(RMIConstants.remoteObjectClass).addImplementedType(remoteInterfaceName);
            final ConstructorDeclaration constructor = wrapperClassDeclaration.addConstructor(Modifier.Keyword.PROTECTED);
            constructor.addThrownException(RemoteException.class);
            constructor.getBody().addStatement("super();");

            getAndAddGatewayMethodsToTheRemoteInterfaceAndWrapperClass(cOrID, interfaceDeclaration, wrapperClassDeclaration);   // Adding all the Gateway methods into the generated remote class

            final String remoteInterfaceAsString =  cuRemoteInterface.toString();
            System.out.println("--------------Printing the created enclave remote interface ---------------------------");
            System.out.println(remoteInterfaceAsString);

            try {
                FileUtils.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX + remoteInterfaceName + ".java", remoteInterfaceAsString);   //Ideally, this file writing should be in some file writing utility class.

                final String wrapperClassAsString = cuEncWrapperClass.toString();
                System.out.println("--------------Printing the created enclave wrapper class ---------------------------");
                System.out.println(wrapperClassAsString);
                FileUtils.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX + wrapperClassName + ".java", wrapperClassAsString);   //Ideally, this file writing should not be here.
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (FileIOException e) {
                e.printStackTrace();
            }
            // super.visit(cOrID, arg); // TODO: Is this needed ?
        }
    }

    public void getAndAddGatewayMethodsToTheRemoteInterfaceAndWrapperClass(final ClassOrInterfaceDeclaration jeEnclaveClass, final ClassOrInterfaceDeclaration interfaceDeclaration, final ClassOrInterfaceDeclaration wrapperClassDeclaration) {
        Set<String> annotationsSet = new HashSet<>();
        String strGateway = Gateway.class.getSimpleName();
        annotationsSet.add(strGateway);
        Map<String, List<MethodDeclaration>> annotatedMethodsMap = ParserHelper.getMethodDeclarationWithAnnotations(jeEnclaveClass, annotationsSet);
        if (annotatedMethodsMap != null && !annotatedMethodsMap.isEmpty()) {
            // Add the methods in the 'annotatedMethodsMap' into the wrapperClassDeclaration
            for (MethodDeclaration gtwMd :  annotatedMethodsMap.get(strGateway)) {
                final String currentGtwMethodName = gtwMd.getNameAsString();
                final MethodDeclaration wrapperMethodInClass = wrapperClassDeclaration.addMethod(currentGtwMethodName, Modifier.Keyword.PUBLIC); // can not add a method declaration directly ? seems like need to break it and then again add
                final MethodDeclaration remoteMethodInInterface = interfaceDeclaration.addMethod(currentGtwMethodName, Modifier.Keyword.PUBLIC);

                remoteMethodInInterface.setType(gtwMd.getType());
                remoteMethodInInterface.setParameters(gtwMd.getParameters());
                remoteMethodInInterface.addThrownException(RemoteException.class);
                remoteMethodInInterface.removeBody(); // Since it is a method inside an Interface.

                wrapperMethodInClass.setType(gtwMd.getType());
                wrapperMethodInClass.setParameters(gtwMd.getParameters());
                wrapperMethodInClass.addAnnotation(RMIConstants.overrideAnno);
                BlockStmt blockStmt = new BlockStmt();
                blockStmt.addStatement("return "+jeEnclaveClass.getNameAsString()+"."+gtwMd.getNameAsString()+ParserHelper.getParameterNameListAsString(gtwMd)+";");
                wrapperMethodInClass.setBody(blockStmt);
                // wrapperMethodInClass.addThrownException(RemoteException.class); not needed
            }
        }
    }
}
