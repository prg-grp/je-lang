package de.tuda.prg.templateclasses;

import java.net.MalformedURLException;
import java.rmi.*;

public class RemoteObjectProvider {
    public static Remote getRemoteObject(String objectName) {
        Remote remoteObj = null;
        try {
            remoteObj = Naming.lookup("rmi://localhost:1099/" + objectName);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new RuntimeException("RMI lookup failed.");
        }
        return remoteObj;
    }
}
