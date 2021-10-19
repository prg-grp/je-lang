import jif.util.List;

@Enclave
public class TaskProcessor {

    @Secret
    static Double salary = new Double(11.7);

    @Gateway
    public static Double process(List<Task> taskList) {
        List<Task> taskListE = endorse(taskList);
        Double result = salary;
        for (int i = 0; i < taskListE.size(); i++) {
            Task task = taskListE.get(i);
            result = task.run(result);
        }
        return declassify(result);
    }
}
