package de.tuda.prg.generatedJava;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;

public class EnclaveMainClass {

    // This can be changed during the translation
    public static final int registryPort = 1099;

    public static void main(String[] args) throws RemoteException {
        LocateRegistry.createRegistry(registryPort);
        LocateRegistry.createRegistry(1099);
        Naming.rebind("RemoteInterfaceEncryptor", new WrapperClassEncryptor());
        while (true) {
        }
    /* Here create all the objects to be bound to the RMI registry. */
    }
}
