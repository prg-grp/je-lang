import java.rmi.*;

public class NonEnclaveMain {

    public static void main(String[] args) {
        ((RemoteInterfaceEncryptor) Naming.lookup("rmi://localhost/RemoteInterfaceEncryptor")).encrypt("mystring");
    }
}
