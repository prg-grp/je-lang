package de.tuda.prg.taskprocessing;

import java.util.ArrayList;


public class TaskOrderedList {

    public static ArrayList<CodeXformationTask> taskList = new ArrayList<CodeXformationTask>();

    public static ArrayList<CodeXformationTask> getTaskList() {
        /*

            Recommended order:
            1. SyntaxCheckTask
            2. AutomatedExceptionHandlingTask
            3. AutomatedGenericHandling
            3. DeclassifyCheckTask
            4. SecFieldsModificationTask
            5. EnclaveClassXformationTask
            6. RMICodeAdderTask
         */



        // taskList.add(new SyntaxCheckTask());            // No. 1
        taskList.add(new AutomatedExceptionHandlingTask());   // No. 2
        taskList.add(new AutomatedGenericHandling());   // No. 3
        taskList.add(new SanitizeCheckHandlingTask());  // No. 4
        taskList.add(new SerializeUserClassesTask());   // No. 5
        taskList.add(new ParametrizeUserClassesTask()); // No. 6
        taskList.add(new DeclassifyCheckTask());        // No. 7
        taskList.add(new SecFieldsModificationTask());  // No. 8
        taskList.add(new EnclaveClassXformationTask()); // No. 9
        taskList.add(new RMICodeAdderTask()); // No. 10
        taskList.add(new EnclaveInitializationFilesTask()); // No. 11

       // taskList.add(new SyntaxCheckTask());
        // No. 6  further create new tasks to be inserted here

        return taskList;
    }
}
