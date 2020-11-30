public class Encryptor {

    private String key;

    public String encrypt(String plaintext) {
        String plaintextE = endorse(plaintext);
        String result = plaintextE + key;
        String result1 = declassify(result);
        return result1;
    }
}
