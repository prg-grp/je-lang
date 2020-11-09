package de.tuda.prg.templateclasses;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;

public class EnclaveMainClass {

    public static final int registryPort = 1099;  // This can be changed during the translation

    public static void main(String[] args) throws RemoteException {
        LocateRegistry.createRegistry(registryPort);

        /* Here create all the objects to be bound to the RMI registry. */

    }
}
