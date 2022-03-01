package de.tuda.prg.taskprocessing;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.printer.DotPrinter;
import com.github.javaparser.printer.YamlPrinter;
import de.tuda.prg.exceptions.FileIOException;
import de.tuda.prg.parser.ParserHelper;
import de.tuda.prg.parser.visitorsje.ClassFieldDeclarationVisitorJe;
import de.tuda.prg.parser.visitorsje.DeclassifyReturnScopeCheckerVisitorJe;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

public class DeclassifyCheckTask implements CodeXformationTask {

    /**
     * Run method overwritten for declassify check.
     * @param jeSrcDir is the path to the JE Directory
     * @param data global data
     * 
     * @return void
     */
    @Override
    public void run(File jeSrcDir, GlobalTaskData data) {
        System.out.println("Task processing started : Task name: DeclassifyCheckTask.");
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
                        if (ParserHelper.isClassAnnotatedWithEnclaveAnnotation(cu)) {
                            VoidVisitor<?> declassifyReturnMcVisitor = new DeclassifyReturnScopeCheckerVisitorJe();
                            declassifyReturnMcVisitor.visit(cu, null);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
