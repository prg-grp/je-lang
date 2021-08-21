package medicalData;

@Enclave
public class StatUtil {

    @Secret
    static int key = 5;

    @Gateway
    public static Record process(Record rec) {
        Record recE = endorse(rec);
        Record record = decrypt(rec, key);
        return declassify(record);
    }

    public static Record decrypt(Record record, int offset) {
        char[] cipher;
        {
            char[] z = null;
            try {
                z = record.getData();
            } catch (NullPointerException e) {
            }
            cipher = z;
        }
        int len;
        {
            int o = 0;
            try {
                o = cipher.length;
            } catch (NullPointerException e) {
            }
            len = o;
        }
        char[] result = new char[len];
        {
            int sq = 0;
            try {
                sq = cipher.length;
            } catch (NullPointerException e) {
            }
            for (int i = 0; i < sq; i++) {
                char character;
                {
                    char n = ' ';
                    try {
                        n = cipher[i];
                    } catch (NullPointerException e) {
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                    character = n;
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
        }
        return new Record(result);
    }
}
