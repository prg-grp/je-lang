import java.io.Serializable;

public class Task implements Serializable {

    Double val;

    boolean flag;

    public Task(Double val, boolean flag) {
        this.val = val;
        this.flag = flag;
    }

    public Double run(Double input) {
        return input * val;
    }

    public boolean check() {
        return flag;
    }
}
