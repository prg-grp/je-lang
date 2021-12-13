import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List taskList = getTaskSeq(4);
        Double tax = TaskProcessor.process(taskList);
        System.out.println(tax);
    }

    private static List getTaskSeq(int n) {
        List tasks = new ArrayList();
        for (int i = 0; i < n; i++) {
            tasks.add(new Task(i, true));
        }
        return tasks;
    }
}
