package de.tuda.prg.generatedJava;

import java.rmi.*;

public class WrapperClassEncryptor extends UnicastRemoteObject implements RemoteInterfaceEncryptor {

    @Override()
    public String encrypt(String plaintext) throws RemoteException {
        return Encryptor.encrypt(plaintext);
    }
}
