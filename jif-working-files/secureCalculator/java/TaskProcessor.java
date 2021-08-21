package SecureCalculator;

import jif.util.List;

@Enclave
public class TaskProcessor {

    @Secret
    static Double salary = 11.7;

    @Gateway
    public static Double process(List taskList) {
        List taskListE = endorse(taskList);
        Double result = salary;
        {
            int t = 0;
            try {
                t = taskListE.size();
            } catch (NullPointerException e) {
            }
            for (int i = 0; i < t; i++) {
                Object task;
                {
                    Object v = null;
                    try {
                        v = taskListE.get(i);
                    } catch (NullPointerException e) {
                    }
                    task = v;
                }
                {
                    Double z = null;
                    try {
                        z = ((Task) task).run(result);
                    } catch (NullPointerException e) {
                    }
                    result = z;
                }
            }
        }
        return declassify(result);
    }
}
