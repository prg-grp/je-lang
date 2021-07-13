package de.tuda.prg.taskprocessing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskProcessor {

    private ArrayList<CodeXformationTask> taskList;

    public TaskProcessor(ArrayList<CodeXformationTask> taskList) {
        if (taskList != null)
            this.taskList = taskList;
        else
            throw new IllegalArgumentException("The TransformTask list can not be null.");
    }

    public void add(CodeXformationTask task) {
        this.taskList.add(task);
    }

    public void process(final File jeSrcDir) {
        final Map<String, ArrayList> interTaskData = new HashMap<>();
        for (CodeXformationTask task: taskList) {
            task.run(jeSrcDir, interTaskData);
        }
    }


}
