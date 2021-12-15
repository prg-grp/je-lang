@Enclave
public class StatUtil {

    @Secret
    private static int key = 5;

    @Gateway
    public static StatRecord process(EncRecord rec) {
        EncRecord recE = endorse(rec);
        StatRecord result = decrypt(recE);
        return declassify(result);
    }

    public static StatRecord decrypt(EncRecord record) {
        String cipher = record.encData;
        int len = cipher.length();
        String result = "";
        for (int i = 0; i < len; i++) {
            char character = cipher.charAt(i);
            if (character != ' ') {
                int originalAlphabetPosition = character - 'a';
                int newAlphabetPosition = (originalAlphabetPosition - key) % 26;
                char newCharacter = (char) ('a' + newAlphabetPosition);
                result += newCharacter;
            } else {
                result += character;
            }
        }
        result = checkPeanutAllergy(result);
        return new StatRecord(result);
    }

    private static String checkPeanutAllergy(String toCheck) {
        int p = toCheck.indexOf("Peanut");
        if (p>0) {
            return "Patient is allergic to Peanuts!";
        } else {
            return "Patient is not allergic to Peanuts";
        }
    }
}
