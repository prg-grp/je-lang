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
                int newAlphabetPosition = (originalAlphabetPosition + key) % 26;
                char newCharacter = (char) ('a' + newAlphabetPosition);
                result += newCharacter;
            } else {
                result += character;
            }
        }
        return new StatRecord(result);
    }
}
