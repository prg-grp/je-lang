import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class WrapperClassEncryptor extends UnicastRemoteObject implements RemoteInterfaceEncryptor {

    protected WrapperClassEncryptor() throws RemoteException {
        super();
    }

    @Override()
    public String encrypt(String plaintext) {
        return Encryptor.encrypt(plaintext);
    }
}
