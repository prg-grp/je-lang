public class Main {


    public static void main(String[] args) {
        int offset = 5;
        EncRecord record = encrypt("This is a message", 26-5);
        StatUtil.process(record);
    }

    public static EncRecord encrypt(String message, int key) {
        int len = message.length();
        String result = "";
        for (int i = 0; i < len; i++) {
            char character = message.charAt(i);
            if (character != ' ') {
                int originalAlphabetPosition = character - 'a';
                int newAlphabetPosition = (originalAlphabetPosition + key) % 26;
                char newCharacter = (char) ('a' + newAlphabetPosition);
                result += newCharacter;
            } else {
                result += character;
            }
        }
        return new EncRecord(result);
    }
}
