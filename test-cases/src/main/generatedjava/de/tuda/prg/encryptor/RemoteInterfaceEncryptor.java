package de.tuda.prg.generatedJava;

import java.rmi.*;

public interface RemoteInterfaceEncryptor extends Remote {

    public String encrypt(String plaintext) throws RemoteException;
}
