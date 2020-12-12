import java.rmi.*;

public class NonEnclaveMain {

    public static void main(String[] args) throws RemoteException {
        String cipher = ((RemoteInterfaceEncryptor) RemoteObjectProvider.getRemoteObject("RemoteInterfaceEncryptor")).encrypt("mystring");
	System.out.println("--- [NonEnclaveMain] Remote call made ---");
	System.out.println("--- [NonEnclaveMain] Cipher --- = "+cipher);
    }
}
