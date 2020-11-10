import java.rmi.*;

public class NonEnclavePoint {

    public static void main(String[] args) {
        var a = ((RemoteInterfaceEnclavePoint) Naming.lookup("rmi://localhost/RemoteInterfaceEnclavePoint")).m1();
    }
}
