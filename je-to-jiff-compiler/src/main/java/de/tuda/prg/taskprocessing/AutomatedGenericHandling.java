package de.tuda.prg.taskprocessing;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import org.apache.commons.io.FilenameUtils;

import de.tuda.prg.constants.PathValues;
import de.tuda.prg.exceptions.FileIOException;
import de.tuda.prg.filehandling.FileUtils;
import de.tuda.prg.parser.visitorsje.genericvisitors.ClassCastExceptionVisitor;
import de.tuda.prg.parser.visitorsje.genericvisitors.GenericVisitor;
import de.tuda.prg.parser.visitorsje.genericvisitors.NameVisitor;
import de.tuda.prg.parser.visitorsje.genericvisitors.VariableVisitor;
import de.tuda.prg.parser.ParserHelper;

/* Handles the exceptions statically as required by JIF. */
public class AutomatedGenericHandling implements CodeXformationTask {

    /**
     * Run method overwritten for generics handling task. This method iterates over
     * all java files in the JE Directory and replaces all Generics with the corresponding typecast.
     * e.g. iterating over List<Task> would cast every object List.get(1) to a Task object.
     * 
     * @param jeSrcDir is the path to the JE Directory
     * @param data global data
     * 
     * @return void
     */
    @Override
    public void run(File jeSrcDir, GlobalTaskData data) {
        System.out.println("Task processing started : Task name: AutomatedGenericHandling .");
        try {
            File[] directoryListing = jeSrcDir.listFiles();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    String currentFileName = file.getName();
                    System.out.println("Currently processing : " + currentFileName);
                    String currentFileBaseName = FilenameUtils.removeExtension(currentFileName);
                    if (FilenameUtils.getExtension(file.getPath()).equals("java")) {
                        CompilationUnit cu = StaticJavaParser.parse(file); // parse file
                        if (ParserHelper.isClassAnnotatedWithEnclaveAnnotation(cu)) { // Handle only enclave classes
                            //String beforeVisitClassString = cu.toString(); // For debug
                            //System.out.println(beforeVisitClassString);

                            GenericVisitor genericVisitor = new GenericVisitor(); // Identify and remove all generic calls
                            List<String> generics = genericVisitor.startVisiting(cu, null); // Store them in a List

                            VariableVisitor variableVisitor = new VariableVisitor(); // Find all variables based on the generic Type
                            Map<String, String> map = variableVisitor.startVisiting(cu, generics); // Store variables with the corresponding generic type in map

                            //System.out.println(generics.toString()); // For debug
                            //System.out.println(map.toString()); // For debug

                            String out = cu.toString();
                            out = out.replaceAll("<>", ""); // Remove <> from the file as Generic Types had been removed already
                            for (String generic : generics) { 
                                out = out.replaceAll(generic+" ", "Object "); // Store variable as Object
                            }
                            cu = StaticJavaParser.parse(out); // parse the file with new types
                            out = cu.toString();

                            for(String key : map.keySet()) { // add typecast for all used variables 
                                String val = map.get(key); 
                                String regex = key+"\\.";

                                String rep = "\\(\\("+val+"\\)"+key+"\\)\\.";
                                out = out.replaceAll(regex, rep);
                            }

                            cu = StaticJavaParser.parse(out);
                            
                            ClassCastExceptionVisitor ccev = new ClassCastExceptionVisitor(); // For every class cast, add class cast exception
                            ccev.visit(cu, null);

                            String afterVisitClassString = cu.toString(); // Class after adding Exceptions

                            //FileUtils.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX + currentFileBaseName + "_beforeGenericHandling.java", beforeVisitClassString);
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
