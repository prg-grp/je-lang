package de.tuda.prg.eventProcessing;

@Enclave
public class FilterNode {
    @Secret private static String key;
    static private Predicate predicate;

    public static Boolean filter(EncIntEvent event) {
        if(!sanitize(event)) {
            return null;
        }
        boolean result;
        try {
            EncInt encInt = endorse(event).getVal();
            Integer val = decrypt(encInt, key);
            result = apply(predicate, val);
        } catch (Exception e) {
            return null;
        }
        return declassify(result);
    }
}
