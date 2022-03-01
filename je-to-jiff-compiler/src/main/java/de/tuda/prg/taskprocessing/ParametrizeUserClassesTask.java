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

public class ParametrizeUserClassesTask implements CodeXformationTask {

    /**
     * Run method overwritten for parametrizing classes with jif labels that are written by the user. This method iterates over
     * all java files in the JE Directory and handles all non-enclave and non-main classes.
     * 
     * @param jeSrcDir is the path to the JE Directory
     * @param data global data
     * 
     * @return void
     */
    @Override
    public void run(File jeSrcDir, GlobalTaskData interTaskData) {
        System.out.println("Task processing started : Task name: ParametrizeUserClassesTask.");
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
                            UserClassTranslationUtils.translateUserClass(cu); // translates all user classes
                            String afterVisitClassString = cu.toString();
                            String jifCodeAddedString = afterVisitClassString;

                            for (String key : Codes.strReplacement.keySet()) { // replace all Jif keys with the jif value
                                jifCodeAddedString = jifCodeAddedString.replace(key, Codes.strReplacement.get(key));
                            }

                            System.out.println("---------------- OWN USER CLASSES ----------------");
                            System.out.println(jifCodeAddedString);
                            // Writing generated jif code to the file
                            FileUtils.writeStringToFile(PathValues.GENERATED_JIF_FOLDER_PREFIX + currentFileBaseName + ".jif", jifCodeAddedString); // write to jif file
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
