import de.tuda.prg.constants.RMIConstants;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class EnclaveMainClass {

    // This can be changed during the translation
    public static final int registryPort = Integer.parseInt(RMIConstants.registryPortValue);

    public static void main(String[] args) throws RemoteException {
        LocateRegistry.createRegistry(registryPort);
        LocateRegistry.createRegistry(1099);
        Naming.rebind("RemoteInterfaceEnclavePoint", new WrapperClassEnclavePoint());
        while (true) {
        }
    /* Here create all the objects to be bound to the RMI registry. */
    }
}
