package p2p;// Packages


import exceptions.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Remote P2P client interface
public interface RemoteClientInterface extends Remote {

    void notifyOnline(String username) throws RemoteException;

    void notifyOffline(String username) throws RemoteException;

    void requestFriendship(String requester) throws RemoteException;

    void endChat(String username) throws RemoteException;

    void startChat(String username) throws FriendNotFoundException, OfflineFriendException, UserNotFoundException, IncorrectSessionException, IncorrectPasswordException, RemoteException;

    void receiveMessage(String username, String message);

}
