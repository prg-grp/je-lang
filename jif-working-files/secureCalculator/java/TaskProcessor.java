import java.util.List;

@Enclave
public class TaskProcessor {

    @Secret
    static Double salary = new Double(11.7);

    @Gateway
    public static Double process(List taskList) {
        if (!sanitize(taskList)) {
            return null;
        }
        List taskListE = endorse(taskList);
        Double result = salary;
        for (int i = 0; i < taskListE.size(); i++) {
            Task task = taskListE.get(i);
            result = task.run(result);
        }
        return declassify(result);
    }

    private static boolean sanitize(List taskList) {
        for(int i=0; i<taskList.size(); i++) {
            Task task = taskList.get(i);
            boolean z = task.check();
            if (!z) {
                return false;
            }
        }
        return true;
    }
}
