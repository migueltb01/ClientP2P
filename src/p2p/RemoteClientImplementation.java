package p2p;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteClientImplementation extends UnicastRemoteObject implements RemoteClientInterface {
    public RemoteClientImplementation() throws RemoteException {

    }

    @Override
    public void notifyOnline(String username) throws RemoteException {
        User.getUser().addFriend(username);
    }

    @Override
    public void requestFriendship(String requester) throws RemoteException {

    }

    @Override
    public void endChat(String username) throws RemoteException {

    }
}
