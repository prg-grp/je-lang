public class NonEnclavePoint {

    public static void main(String[] args) {
        ((RemoteInterfaceEnclavePoint) Naming.lookup("rmi://localhost/RemoteInterfaceEnclavePoint")).m1();
    }
}
