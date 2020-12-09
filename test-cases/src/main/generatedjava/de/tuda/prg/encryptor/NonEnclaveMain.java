import java.rmi.*;

public class NonEnclaveMain {

    public static void main(String[] args) {
        String cipher = ((RemoteInterfaceEncryptor) RemoteObjectProvider.getRemoteObject(RemoteInterfaceEncryptor)).encrypt("mystring");
    }
}
