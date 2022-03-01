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


    /**
     * Run method overwritten for generics handling task. This method iterates over
     * all java files in the JE Directory and adds the implementation of the Serializable 
     * Interface to every User class. User classes must implement this interface as 
     * they probably need to be serialized because of the RMI communication.
     * 
     * @param jeSrcDir is the path to the JE Directory
     * @param data global data
     * 
     * @return void
     */
    @Override
    public void run(File jeSrcDir, GlobalTaskData interTaskData) {
        System.out.println("Task processing started : Task name: SerializeUserClassesTask.");
        try {
            File[] directoryListing = jeSrcDir.listFiles();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    String currentFileName = file.getName();
                    System.out.println("Currently processing : " + currentFileName);
                    //String currentFileBaseName = FilenameUtils.removeExtension(currentFileName);
                    if (FilenameUtils.getExtension(file.getPath()).equals("java")) {  //Change this later
                        final CompilationUnit cu = StaticJavaParser.parse(file); // parse tje file

                        if (!ParserHelper.isClassAnnotatedWithEnclaveAnnotation(cu) && !ParserHelper.isMainClass(cu)) { // Is not the Enclave class and is not the Main Class
                            UserClassTranslationUtils.addSerializableToUserClass(cu); // add Serializable to User class
                            String afterVisitClassString = cu.toString();

                            //System.out.println("---------------- OWN USER CLASSES ----------------"); // for debug
                            //System.out.println(afterVisitClassString); // for debug
                            // Writing generated jif code to the file
                            FileUtils.writeStringToFile(PathValues.JE_FOLDER_PATH + "/" + file.getName(), afterVisitClassString);
                            //System.out.println("---------------- OWN USER CLASSES ----------------"); // for debug
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
