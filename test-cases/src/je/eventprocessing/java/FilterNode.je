@Enclave
public class FilterNode {

    @Secret
    static String key;

    @Secret
    static GreaterPredicate predicate = new GreaterPredicate();

    @Gateway
    public static boolean filter(EncIntEvent event) {
        EncIntEvent intEvent = endorse(event);
        EncInt encInt = intEvent.getVal();
        int val = decrypt(encInt);
        return declassify(apply(val));
    }

    private static int decrypt(EncInt val) {
        return val.getInt();
    }

    private static boolean apply(int val) {
        return predicate.test(val);
    }
}
