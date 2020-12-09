public class EnclavePoint {

    int x = 1;

    int y = 2;

    String str = "abc";

    private String key = "def";

    public static void main(String[] args) {
        int i = 4;
    // System.out.println("Hi");
    }

    public void m1(Integer a) {
    // System.out.println("Hi3");
    }

    public void m2(Integer a) {
    // System.out.println("Hi2");
    }

    public void m1(Integer a) {
    // System.out.println("Hi1");
    }

    public String encrypt(String plaintext) {
        String plaintextE = endorse(plaintext);
        String cipher = encode(plaintext, key);
        return declassify(cipher);
    }

    public String encode(String plaintext, String key) {
        return plaintext + key;
    }
}
// public class Point2 {   // can not handle two classes in a single file
// }
