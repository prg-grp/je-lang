package medicalData;

public class StatUtil {

    @SECRET
    static int key = 5;

    @GATEWAY
    public static Record process(Record rec) {
        Record recE = endorse(rec);
        Record record = decrypt(recE, key);
        return declassify(record);
    }

    public static Record decrypt(Record record, int offset) {
        char[] cipher = record.getData();
        int len = cipher.length;
        char[] result = new char[len];
        for (int i=0; i<cipher.length; i++) {
            char character = cipher[i];
            if (character != ' ') {
                int originalAlphabetPosition = character - 'a';
                int newAlphabetPosition = (originalAlphabetPosition + offset) % 26;
                char newCharacter = (char) ('a' + newAlphabetPosition);
                result[i] = newCharacter;
            } else {
                result[i] = character;
            }
        }

        return new Record(result);
    }
}
