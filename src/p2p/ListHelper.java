package p2p;

import controller.ClientBaseUI;
import exceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.util.HashMap;

public class ListHelper {

    private static ListHelper instance = null;
    private ObservableList<String> friends;
    private ObservableList<String> requests;
    private HashMap<String, Friend> chats;

    private ListHelper() {
        this.friends = FXCollections.observableArrayList();
        this.requests = FXCollections.observableArrayList();
        this.chats = new HashMap<String, Friend>();
    }

    public static ListHelper getListHelper() {
        if (instance == null) {
            instance = new ListHelper();
        }
        return instance;
    }


    public ObservableList<String> getFriends() {
        return friends;
    }

    public void setFriends(ObservableList<String> friends) {
        this.friends = friends;
    }

    public void addFriend(String username) {
        this.friends.add(username);
    }

    public ObservableList<String> getRequests() {
        return requests;
    }

    public void addRequest(String requests) {
        this.requests.add(requests);
    }

    public void removeRequest(String user) {
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i).equals(user)) {
                requests.remove(i);
            }
        }
    }

    public void removeFriend(String user) {
        friends.remove(user);
    }

    public void createChat(String username) throws NullPointerException, FriendNotFoundException, OfflineFriendException, UserNotFoundException, IncorrectSessionException, IncorrectPasswordException, RemoteException {
        chats.put(username, new Friend(Connection.getConnection().getServerObject().startChat(Connection.getConnection().getClientObject(), User.getUser().getUsername(), username, User.getUser().getPassword()), FXCollections.observableArrayList()));
    }

    public boolean isChatCreated(String username) {
        return chats.containsKey(username);
    }

    public ObservableList<String> getChat(String username) {
        return chats.get(username).getFriendChat();
    }

    public RemoteClientInterface getFriendObject(String username) {
        return chats.get(username).getFriendObject();
    }

    public void destroyChat(String username) {
        chats.remove(username);
        ClientBaseUI.getBaseUI().clearChat();
    }
}
