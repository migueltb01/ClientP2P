package p2p;

import exceptions.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteClientImplementation extends UnicastRemoteObject implements RemoteClientInterface {

    public RemoteClientImplementation() throws RemoteException {

    }

    @Override
    public void notifyOnline(String username) throws RemoteException {
        ListHelper.getListHelper().addFriend(username);
    }

    @Override
    public void notifyOffline(String username) throws RemoteException {
        ListHelper.getListHelper().removeFriend(username);
        ListHelper.getListHelper().destroyChat(username);
    }

    @Override
    public void requestFriendship(String requester) throws RemoteException {
        ListHelper.getListHelper().addRequest(requester);
    }

    public void startChat(String username) throws FriendNotFoundException, OfflineFriendException, UserNotFoundException, IncorrectSessionException, IncorrectPasswordException, RemoteException {
        if (!ListHelper.getListHelper().isChatCreated(username)) {
            ListHelper.getListHelper().createChat(username);
        }
    }

    public void receiveMessage(String username, String message) {
        ListHelper.getListHelper().getChat(username).add(message);
    }

    @Override
    public void endChat(String username) throws RemoteException {

    }
}
