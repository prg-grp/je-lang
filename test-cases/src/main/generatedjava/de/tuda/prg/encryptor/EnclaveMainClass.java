import java.rmi.*;
import java.net.MalformedURLException;
import java.rmi.registry.LocateRegistry;

public class EnclaveMainClass {

    // This can be changed during the translation
    public static final int registryPort = 1099;

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        LocateRegistry.createRegistry(registryPort);
        Naming.rebind("EnclaveRemoteInterfaceEncryptor", new EnclaveWrapperClassEncryptor());
        while (true) {
        }
    /* Here create all the objects to be bound to the RMI registry. */
    }
}
