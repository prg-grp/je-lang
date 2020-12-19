import java.rmi.*;

public class NonEnclaveMain {

    public static void main(String[] args) {
        String cipher = NonEnclaveRemoteWrapper.encrypt("mystring");
	System.out.println("Received cipher = "+cipher);
    }
}
