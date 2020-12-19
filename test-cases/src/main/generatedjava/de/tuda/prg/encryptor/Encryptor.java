public class Encryptor {

    private static String key = "KEY";

    public static String encrypt(String plaintext) {
        String plaintextE = IdentityMethods.endorse(plaintext);
        String result = plaintextE + key;
        String result1 = IdentityMethods.declassify(result);
        return result1;
    }
}
