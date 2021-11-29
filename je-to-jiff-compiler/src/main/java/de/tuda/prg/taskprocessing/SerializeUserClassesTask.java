package de.tuda.prg.taskprocessing;

import java.io.File;
import java.io.IOException;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import org.apache.commons.io.FilenameUtils;

import de.tuda.prg.constants.Codes;
import de.tuda.prg.constants.PathValues;
import de.tuda.prg.exceptions.FileIOException;
import de.tuda.prg.parser.ParserHelper;
import de.tuda.prg.parser.UserClassTranslationUtils;
import de.tuda.prg.filehandling.FileUtils;

public class SerializeUserClassesTask implements CodeXformationTask {

    // First try block, translation of User Classes to jif
    @Override
    public void run(File jeSrcDir, GlobalTaskData interTaskData) {
        System.out.println("Task processing started : Task name: SerializeUserClassesTask.");
        try {
            File[] directoryListing = jeSrcDir.listFiles();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    String currentFileName = file.getName();
                    System.out.println("Currently processing : " + currentFileName);
                    String currentFileBaseName = FilenameUtils.removeExtension(currentFileName);
                    if (FilenameUtils.getExtension(file.getPath()).equals("java")) {  //Change this later

                        // final CompilationUnit cu = StaticJavaParser.parse(new File(file));
                        final CompilationUnit cu = StaticJavaParser.parse(file);

                        if (!ParserHelper.isClassAnnotatedWithEnclaveAnnotation(cu) && !ParserHelper.isMainClass(cu)) {
                            // Process if class is not annoted with Enclave Annotation and is no Main Class
                            UserClassTranslationUtils.addSerializableToUserClass(cu);
                            String afterVisitClassString = cu.toString();

                            System.out.println("---------------- OWN USER CLASSES ----------------");
                            System.out.println(afterVisitClassString);
                            // Writing generated jif code to the file
                            FileUtils.writeStringToFile(PathValues.JE_FOLDER_PATH + "/" + file.getName(), afterVisitClassString);
                            System.out.println("---------------- OWN USER CLASSES ----------------");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileIOException e) {
            e.printStackTrace();
        }

    }
}
