@Enclave
public class TaskProcessor {

    @Secret
    private static Integer salary = 5;


    @Gateway
    public static Integer process (List<Task> taskList) {

        List<Task> taskListE = endorse(taskList);

        Integer result = salary;

        for (int i = 0; i < taskList.size(); i++) {
            result = task.run(result);
        }

        return result;
    }
}