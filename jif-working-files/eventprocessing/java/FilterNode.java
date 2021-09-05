
@Enclave
public class FilterNode {

    @Secret static String key;
    Predicate predicate;

    @Gateway
    public static boolean filter(EncEvent event) {
        int val = decrypt(endorse(event).getVal(), key);
        return apply(predicate, val);
    }
    
}
