package SecureCalculator;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List taskList = getTaskSeq(4);
        Double tax;
        {
            Double c = null;
            try {
                c = TaskProcessor.process(taskList);
            } catch (NullPointerException e) {
            }
            tax = c;
        }
        try {
            System.out.println(tax);
        } catch (NullPointerException e) {
        }
    }

    private static List getTaskSeq(int n) {
        List tasks = new ArrayList();
        for (int i = 0; i < n; i++) {
            try {
                tasks.add(new Task());
            } catch (NullPointerException e) {
            }
        }
        return tasks;
    }
}
