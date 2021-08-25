package medicalData;


public class Main {


    public static void main(String[] args) {
        int offset = 5;
        Record record = new Record("this is some data".toCharArray());
        Record recordE = encrypt(record, 26-5);
        StatUtil.process(recordE);
    }

    public static Record encrypt(Record record, int offset) {
        char[] cipher = record.data;
        char[] result = new char[cipher.length];
        for (int i=0; i<cipher.length; i++) {
            char character = cipher[i];
            if (character != ' ') {
                int originalAlphabetPosition = character - 'a';
                int newAlphabetPosition = (originalAlphabetPosition + offset) % 26;
                char newCharacter = (char) ('a' + newAlphabetPosition);
                result[i] = newCharacter;
                System.out.print(newCharacter);
            } else {
                result[i] = character;
            }
        }
        System.out.println("");
        return new Record(result);
    }

}
