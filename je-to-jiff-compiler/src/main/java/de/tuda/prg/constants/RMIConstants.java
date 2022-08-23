package de.tuda.prg.constants;

public class RMIConstants {

    public static final String JAVA_RMI_ALL = "java.rmi.*";

    public static final String JAVA_RMI_UNICAST_OBJ = "java.rmi.server.UnicastRemoteObject";  // This shouldn't be needed

    public static final String JAVA_NET_ALL = "java.net.*";

    public static final String REMOTE_OBJECT_CLASS_NAME = "UnicastRemoteObject";

    public static final String REMOTE_INTERFACE_PREFIX = "EnclaveRemoteInterface";

    public static final String REMOTE_WRAPPER_CLASS_SUFFIX = "EnclaveWrapperClass";

    public static final String OVERRIDE_ANNO = "Override";

    public static final String RMI_LOOK_UP_URL_PREFIX = "Naming.lookup"+"("+"\""+"rmi://localhost/";

    public static final String RMI_OBJECT_LOOKUP_CALL = "";

    public static final String REGISTRY_PORT_VALUE = "1099";

    public static String RMI_OBJECT_PROVIDER_CLASS = "RemoteObjectProvider";

    public static String RMI_OBJECT_LOOKUP_CALL_PREFIX = RMI_OBJECT_PROVIDER_CLASS+".getRemoteObject";
}
