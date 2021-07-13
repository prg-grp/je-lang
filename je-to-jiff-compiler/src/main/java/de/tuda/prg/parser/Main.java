package de.tuda.prg.parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import de.tuda.prg.constants.Codes;
import de.tuda.prg.constants.FileNames;
import de.tuda.prg.constants.PathValues;
import de.tuda.prg.constants.RMIConstants;
import de.tuda.prg.entities.ClassNameMethodDecls;
import de.tuda.prg.exceptions.FileIOException;
import de.tuda.prg.filehandling.FileUtils;
import de.tuda.prg.parser.visitorsremotecom.NonEnclaveMethodCallVisitor;
import de.tuda.prg.taskprocessing.TaskOrderedList;
import de.tuda.prg.taskprocessing.TaskProcessor;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args == null || args.length != 3) {
            throw new IllegalArgumentException("Incorrect number of arguments to the main method.");

        } else {
            PathValues.JE_FOLDER_PATH = args[0];
            PathValues.GENERATED_JIF_FOLDER_PREFIX = args[1];
            PathValues.GENERATED_JAVA_FOLDER_PREFIX = args[2];
        }

        System.out.println("JE folder path = "+PathValues.JE_FOLDER_PATH);
        System.out.println("Generated Jif folder path = "+PathValues.GENERATED_JIF_FOLDER_PREFIX);
        System.out.println("Generated Java folder path = "+PathValues.GENERATED_JAVA_FOLDER_PREFIX);

        TaskProcessor taskProcessor = new TaskProcessor(TaskOrderedList.getTaskList());
        taskProcessor.process(new File(PathValues.JE_FOLDER_PATH));

        // Fifth try block, adding RemoteObjectProvider and IdentityMethods classes to the generated java folder
        try {
            final File remoteObjectProviderClassFile = new File(PathValues.REMOTE_OBJECT_PROVIDER_CLASS_NAME);
            final CompilationUnit cuRenoteObjectProviderFile = StaticJavaParser.parse(remoteObjectProviderClassFile);
            cuRenoteObjectProviderFile.getPackageDeclaration().get().remove();  // Removing the package declaration from the template file.

            // cuRenoteObjectProviderFile.setPackageDeclaration(PathValues.GENERATED_JAVA_PACKAGE_NAME);  // No package declaration, no nested folders

            final String remoteObjectProviderClassAsString = cuRenoteObjectProviderFile.toString();
            FileUtils.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX+ RMIConstants.RMI_OBJECT_PROVIDER_CLASS+".java", remoteObjectProviderClassAsString);

            // Adding identity methods class
            final File idMethodsClassFile = new File(PathValues.IDENTITY_METHODS_CLASS);
            final CompilationUnit cuIdMethodsFile = StaticJavaParser.parse(idMethodsClassFile);
            cuIdMethodsFile.getPackageDeclaration().get().remove();  // Removing the package declaration from the template file.
            final String idMethodsClassAsString = cuIdMethodsFile.toString();
            FileUtils.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX+ FileNames.IDENTITY_METHODS_CLASS_NAME+".java", idMethodsClassAsString);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (FileIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
