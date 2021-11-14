import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Integer> events = Generator.generateIntegers(100);
        List<EncIntEvent> encEvents = encrypt(events);
        List<EncIntEvent> result = encEvents.stream().filter(encInt -> FilterNode.filter(encInt)).collect(Collectors.toList());
    }


    private static List<EncIntEvent> encrypt(List<Integer> events) {
        List<EncIntEvent> lst = new ArrayList();
        for(int i=0; i<events.size(); i++) {
            lst.add(new EncIntEvent());
        }
        return lst;
    }
}
