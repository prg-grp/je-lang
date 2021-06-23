package de.tuda.prg.taskprocessing;

import com.github.javaparser.ast.CompilationUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskProcessor {

    private ArrayList<TransformTask> taskList;

    public TaskProcessor(ArrayList<TransformTask> taskList) {
        if (taskList != null)
            this.taskList = taskList;
        else
            throw new IllegalArgumentException("The TransformTask list can not be null.");
    }

    public void add(TransformTask task) {
        this.taskList.add(task);
    }

    public void process(final CompilationUnit cu) {
        for (TransformTask task: taskList) {
            task.run(cu, new HashMap<>());
        }

        //TODO: Print the modified 'cu' to the file here ?
    }


}
