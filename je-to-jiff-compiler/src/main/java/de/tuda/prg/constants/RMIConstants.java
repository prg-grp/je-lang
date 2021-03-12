package de.tuda.prg.constants;

public class RMIConstants {

    public static final String javaRMIAll = "java.rmi.*";

    public static final String javaRMIUnicastObj = "java.rmi.server.UnicastRemoteObject";  // This shouldn't be needed

    public static final String javaNetAll = "java.net.*";

    public static final String remoteObjectClass = "UnicastRemoteObject";

    public static final String remoteInterfacePrefix = "EnclaveRemoteInterface";

    public static final String remoteWrapperClassSufix = "EnclaveWrapperClass";

    public static final String overrideAnno = "Override";

    public static final String rmiLookUpURLPrefix = "Naming.lookup"+"("+"\""+"rmi://localhost/";

    public static final String rmiObjectLookupCall = "";

    public static final String registryPortValue = "1099";

    public static String RMI_OBJECT_PROVIDER_CLASS = "RemoteObjectProvider";

    public static String RMI_OBJECT_LOOKUP_CALL_PREFIX = RMI_OBJECT_PROVIDER_CLASS+".getRemoteObject";
}
