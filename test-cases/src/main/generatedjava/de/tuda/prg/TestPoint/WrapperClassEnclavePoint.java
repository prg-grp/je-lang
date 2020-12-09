package de.tuda.stg.generatedJava;

import java.rmi.*;

public class WrapperClassEnclavePoint extends UnicastRemoteObject implements RemoteInterfaceEnclavePoint {

    @Override()
    public void m1(Integer a) throws RemoteException {
        return EnclavePoint.m1(a);
    }

    @Override()
    public void m2(Integer a) throws RemoteException {
        return EnclavePoint.m2(a);
    }

    @Override()
    public String encrypt(String plaintext) throws RemoteException {
        return EnclavePoint.encrypt(plaintext);
    }
}
