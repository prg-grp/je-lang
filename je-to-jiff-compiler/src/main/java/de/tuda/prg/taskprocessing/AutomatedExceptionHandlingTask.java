package de.tuda.prg.taskprocessing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import org.apache.commons.io.FilenameUtils;

import de.tuda.prg.constants.PathValues;
import de.tuda.prg.exceptions.FileIOException;
import de.tuda.prg.filehandling.FileUtils;
import de.tuda.prg.parser.visitorsje.exceptionvisitors.nullpointervisitors.BlockStatementVisitor;
import de.tuda.prg.parser.visitorsje.exceptionvisitors.nullpointervisitors.TryStmtVisitor;
import de.tuda.prg.parser.ParserHelper;

/* Handles the exceptions statically as required by JIF. */
public class AutomatedExceptionHandlingTask implements CodeXformationTask {

    /**
     * Run method overwritten for exception handling task. This method iterates over
     * all java files in the JE Directory and wraps all possible nullpointerexceptions 
     * within every enclave class in a try-catch statement.
     * 
     * @param jeSrcDir is the path to the JE Directory
     * @param data global data
     * 
     * @return void
     */
    @Override
    public void run(File jeSrcDir, GlobalTaskData data) {
        System.out.println("Task processing started : Task name: AutomatedExceptionHandlingTask.");
        try {
            File[] directoryListing = jeSrcDir.listFiles();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    String currentFileName = file.getName();
                    System.out.println("Currently processing : " + currentFileName);
                    String currentFileBaseName = FilenameUtils.removeExtension(currentFileName);
                    if (FilenameUtils.getExtension(file.getPath()).equals("java")) {  //Change this later
                        final CompilationUnit cu = StaticJavaParser.parse(file); // parse the file

                        if (ParserHelper.isClassAnnotatedWithEnclaveAnnotation(cu)) { // check if enclave class
                            //String beforeVisitClassString = cu.toString(); // For debug
                            //System.out.println(beforeVisitClassString);

                            BlockStatementVisitor visitor = new BlockStatementVisitor(); // Visitor which visits the nodes
                            visitor.startVisiting(cu, null); // Start visiting nodes and receive resulting Hashmap

                            TryStmtVisitor tsVisitor = new TryStmtVisitor(); // TryStatement Visitor to add other possible Exceptions
                            tsVisitor.visit(cu, null);
                            
                            String afterVisitClassString = cu.toString(); // Class after adding Exceptions
                            //System.out.println(afterVisitClassString);

                            //FileUtils.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX + currentFileBaseName + "_beforeExHandling.java", beforeVisitClassString);
                            FileUtils.writeStringToFile(PathValues.JE_FOLDER_PATH + "/" + file.getName(), afterVisitClassString); // Write to file
                        } else {
                            System.out.println("Not an enclave class");
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
