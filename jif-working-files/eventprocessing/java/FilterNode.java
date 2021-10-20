
@Enclave
public class FilterNode {

    @Secret 
    static String key;
    @Secret
    static GreaterPredicate predicate;

    @Gateway
    public static boolean filter(EncIntEvent event) {
        int val = decrypt(endorse(event).getVal());
        return apply(predicate, val);
    }


    private int decrypt(EncInt val) {
        return val.encryptedInt;
    }

    private boolean apply(GreaterPredicate pred, int val) {
        return pred.test(val);
    }
    
}
