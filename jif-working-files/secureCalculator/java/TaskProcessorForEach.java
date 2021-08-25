package SecureCalculator;

import jif.util.List;

@Enclave
public class TaskProcessor {

    @Secret
    static Double salary = 11.7;

    @Gateway
    public static Double process(List<Task> taskList) {
        List<Task> taskListE = endorse(taskList);
        Double result = salary;
        for (Task task : taskListE) {
            result += task.run(result);
        }
        return declassify(result);
    }
}
