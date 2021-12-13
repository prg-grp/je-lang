package de.tuda.prg.taskprocessing;

import java.io.File;
import java.io.IOException;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import org.apache.commons.io.FilenameUtils;

import de.tuda.prg.constants.PathValues;
import de.tuda.prg.exceptions.FileIOException;
import de.tuda.prg.filehandling.FileUtils;
import de.tuda.prg.parser.ParserHelper;
import de.tuda.prg.parser.visitorsje.sanitizecheckvisitors.*;

public class SanitizeCheckHandlingTask implements CodeXformationTask {

    @Override
    public void run(File jeSrcDir, GlobalTaskData data) {
        // TODO Auto-generated method stub
        System.out.println("Task processing started : Task name: SanitizeCheckHandling.");
        try {
            File[] directoryListing = jeSrcDir.listFiles();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    String currentFileName = file.getName();
                    System.out.println("Currently processing : " + currentFileName);
                    String currentFileBaseName = FilenameUtils.removeExtension(currentFileName);
                    if (FilenameUtils.getExtension(file.getPath()).equals("java")) {  //Change this later
                        CompilationUnit cu = StaticJavaParser.parse(file);
                        if (ParserHelper.isClassAnnotatedWithEnclaveAnnotation(cu)) {
                            SanitizeVisitor sv = new SanitizeVisitor();
                            sv.visit(cu, false);

                            String afterVisitClassString = cu.toString(); // Class after adding Exceptions

                            System.out.println(afterVisitClassString);
                            //FileUtils.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX + currentFileBaseName + "_beforeExHandling.java", beforeVisitClassString);
                            FileUtils.writeStringToFile(PathValues.JE_FOLDER_PATH + "/" + file.getName(), afterVisitClassString);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileIOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
}
