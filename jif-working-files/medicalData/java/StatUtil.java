@Enclave
public class StatUtil {

    @Secret
    private static int key = 5;

    @Gateway
    public static Record process(Record rec) {
        Record recE = endorse(rec);
        Record result = decrypt(recE, key);
        return declassify(result);
    }

    public Record decrypt(Record record, int offset) {
        String cipher = record.data;
        int len = cipher.length();
        String result = "";
        for (int i = 0; i < len; i++) {
            char character = cipher.charAt(i);
            if (character != ' ') {
                int originalAlphabetPosition = character - 'a';
                int newAlphabetPosition = (originalAlphabetPosition + offset) % 26;
                char newCharacter = (char) ('a' + newAlphabetPosition);
                result += newCharacter;
            } else {
                result += character;
            }
        }
        return new Record(result);
    }
}
