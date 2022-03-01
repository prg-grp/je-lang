import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Integer> events = Generator.generateIntegers(100);
        System.out.println("Events "+events+" with size "+events.size());
        List<EncIntEvent> encEvents = encrypt(events);
        List<EncIntEvent> result = encEvents.stream().filter(encInt -> FilterNode.filter(encInt)).collect(Collectors.toList());
        System.out.println("Results: [");
        for(EncIntEvent eie : result) {
            System.out.print(" "+eie.getVal().getInt()+",");
        }
        
        System.out.println("] with size "+result.size());
    }


    private static List<EncIntEvent> encrypt(List<Integer> events) {
        List<EncIntEvent> lst = new ArrayList();
        for(int i=0; i<events.size(); i++) {
            lst.add(new EncIntEvent(new EncInt(events.get(i))));
        }
        return lst;
    }
}
