package de.tuda.prg.taskprocessing;

import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public interface CodeXformationTask {

    public void run(File jeSrcDir, GlobalTaskData data);

}
