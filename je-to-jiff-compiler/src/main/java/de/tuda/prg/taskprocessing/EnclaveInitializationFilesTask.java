package de.tuda.prg.taskprocessing;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import de.tuda.prg.constants.FileNames;
import de.tuda.prg.constants.PathValues;
import de.tuda.prg.exceptions.FileIOException;
import de.tuda.prg.filehandling.FileUtils;
import de.tuda.prg.parser.ParserHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class EnclaveInitializationFilesTask implements CodeXformationTask {
    @Override
    public void run(File jeSrcDir, GlobalTaskData interTaskData) {
        // Try block, generating the Enclave initialization java file
        try {
            File enclaveMainClassFile = new File(PathValues.ENCLAVE_MAIN_CLASS_TEMPLATE_FILE_NAME);
            final CompilationUnit cu = StaticJavaParser.parse(enclaveMainClassFile);
            cu.getPackageDeclaration().get().remove();  // Removing the package declaration from the template file.

            // cu.setPackageDeclaration(PathValues.GENERATED_JAVA_PACKAGE_NAME);  // No package declaration, no nested folders as of now.

            ParserHelper.addRMIRegistryBindings(cu, interTaskData.enclaveClassesToExposeNames);
            final String enclaveMainClassAsString = cu.toString();
            FileUtils.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX+ FileNames.ENCLAVE_MAIN_CLASS_BASE_NAME+".java", enclaveMainClassAsString);
        }  catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (FileIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
