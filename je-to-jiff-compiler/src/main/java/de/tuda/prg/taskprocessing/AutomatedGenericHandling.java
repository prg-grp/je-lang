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

import de.tuda.prg.parser.visitorsje.genericvisitors.GenericVisitor;
import de.tuda.prg.parser.visitorsje.genericvisitors.NameVisitor;
import de.tuda.prg.parser.visitorsje.genericvisitors.VariableVisitor;

/* Handles the exceptions statically as required by JIF. */
public class AutomatedGenericHandling implements CodeXformationTask {

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
                    if (FilenameUtils.getExtension(file.getPath()).equals("java")) {  //Change this later
                        CompilationUnit cu = StaticJavaParser.parse(file);

                        String beforeVisitClassString = cu.toString();

                        GenericVisitor genericVisitor = new GenericVisitor();
                        List<String> generics = genericVisitor.startVisiting(cu, null);

                        VariableVisitor variableVisitor = new VariableVisitor();
                        Map<String, String> map = variableVisitor.startVisiting(cu, generics);

                        String out = cu.toString();
                        out = out.replaceAll("<>", "");
                        for (String generic : generics) {
                            out = out.replaceAll(generic+" ", "Object ");
                        }
                        cu = StaticJavaParser.parse(out);
                        out = cu.toString();

                        for(String key : map.keySet()) {
                            String val = map.get(key);
                            String regex = key+"\\.";
                            String rep = "\\(\\("+val+"\\)"+key+"\\)\\.";
                            out = out.replaceAll(regex, rep);
                        }

                        cu = StaticJavaParser.parse(out);
                        //NameVisitor nameVisitor = new NameVisitor();
                        //nameVisitor.startVisiting(cu, map);

                        String afterVisitClassString = cu.toString(); // Class after adding Exceptions

                        FileUtils.writeStringToFile(PathValues.GENERATED_JAVA_FOLDER_PREFIX + currentFileBaseName + "_beforeGenericHandling.java", beforeVisitClassString);
                        FileUtils.writeStringToFile(PathValues.JE_FOLDER_PATH + "/" + file.getName(), afterVisitClassString);
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
