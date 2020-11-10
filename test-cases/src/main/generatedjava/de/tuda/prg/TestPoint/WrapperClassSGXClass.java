package de.tuda.stg.generatedJava;

import java.rmi.*;

public class WrapperClassSGXClass extends UnicastRemoteObject implements RemoteInterfaceSGXClass {

    @Override()
    public String encrypt(String plaintext) throws RemoteException {
        return SGXClass.encrypt(plaintext);
    }
}
