package de.tuda.prg.generatedJava;

import java.rmi.*;

public interface RemoteInterfaceEnclavePoint extends Remote {

    public void m1(Integer a);

    public void m2(Integer a);

    public String encrypt(String plaintext);
}
