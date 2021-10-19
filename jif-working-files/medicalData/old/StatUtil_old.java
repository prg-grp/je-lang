package medicalData;

@Enclave
public class StatUtil {

    @Secret
    static int key = 5;

    @Gateway
    public static Record process(Record rec) {
        Record recE = endorse(rec);
        Record record = decrypt(recE, key);
        return declassify(record);
    }

    public static Record decrypt(Record record, int offset) {
        char[] cipher;
        {
            char[] s = null;
            try {
                s = record.getData();
            } catch (NullPointerException e) {
            }
            cipher = s;
        }
        int len;
        {
            int v = 0;
            try {
                v = cipher.length;
            } catch (NullPointerException e) {
            }
            len = v;
        }
        char[] result = new char[len];
        for (int i = 0; i < len; i++) {
            char character;
            {
                char b = ' ';
                try {
                    b = cipher[i];
                } catch (NullPointerException e) {
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                character = b;
            }
            if (character != ' ') {
                int originalAlphabetPosition = character - 'a';
                int newAlphabetPosition = (originalAlphabetPosition + offset) % 26;
                char newCharacter = (char) ('a' + newAlphabetPosition);
                try {
                    result[i] = newCharacter;
                } catch (NullPointerException e) {
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            } else {
                try {
                    result[i] = character;
                } catch (NullPointerException e) {
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
        return new Record(result);
    }
}
