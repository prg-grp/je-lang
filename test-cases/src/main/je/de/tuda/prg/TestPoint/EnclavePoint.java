import de.tuda.prg.Annotations.Gateway;
import de.tuda.prg.Annotations.Secret;

@Enclave
public class EnclavePoint {

    int x = 1;
    int y = 2;
    String str = "abc";

    @Secret
    private Integer key = 15;


    public static void main(String[] args) {
        int i = 4;
        System.out.println("Hi");
    }

    @Gateway
    public static void m1(Integer a) {
        System.out.println("Hi3");
    }

    @Gateway
    public static void m2(Integer a) {
        System.out.println("Hi2");
    }


    public void m1(Integer a) {
        System.out.println("Hi1");
    }

    @Gateway
    public static String encrypt(String plaintext) {
        String plaintextE = endorse(plaintext);
        String cipher = encode(plaintext, key);
        return declassify(cipher);
    }
}

// public class Point2 {   // can not handle two classes in a single file	
// }
