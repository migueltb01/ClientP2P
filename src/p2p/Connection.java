package p2p;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.*;

public class Connection {
    private static Connection instance = null;
    private RemoteServerInterface serverObject;
    private RemoteClientImplementation clientObject;

    private Connection(RemoteServerInterface serverObject, RemoteClientImplementation clientObject) {
        this.serverObject = serverObject;
        this.clientObject = clientObject;
    }

    public static Connection getConnection() {
        if (instance == null) {
            return null;
        } else return instance;
    }

    public static Connection connect() throws RemoteException, NotBoundException, MalformedURLException {
        System.setSecurityManager(new RMISecurityManager());

        String registryURL =
                "rmi://localhost:" + 1099 + "/P2PServer";
        RemoteServerInterface serverObject =
                (RemoteServerInterface) Naming.lookup(registryURL);
        RemoteClientImplementation clientObject =
                new RemoteClientImplementation();

        if (instance == null) {
            instance = new Connection(serverObject, clientObject);
        }

        return instance;
    }

    public RemoteServerInterface getServerObject() {
        return serverObject;
    }

    public static void destroy() {
        instance = null;
    }

    public RemoteClientInterface getClientObject() {
        return clientObject;
    }
}
