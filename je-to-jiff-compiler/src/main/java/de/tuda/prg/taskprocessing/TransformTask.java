package de.tuda.prg.taskprocessing;

import com.github.javaparser.ast.CompilationUnit;

import java.util.ArrayList;
import java.util.Map;

public interface TransformTask {

    public void run(CompilationUnit cu, Map<String, ArrayList> data);

}
