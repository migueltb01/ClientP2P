package p2p;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private static User instance = null;
    private String username;
    private String password;
    private ArrayList<String> friends;
    private HashMap<String, RemoteClientInterface> openedConnections;

    private User(String username, String password, ArrayList<String> friends) {
        this.username = username;
        this.password = password;
        this.friends = friends;
        this.openedConnections = new HashMap<>();
    }

    public static User setUser(String username, String password, ArrayList<String> friends) {
        if (instance == null) {
            instance = new User(username, password, friends);
        }
        return instance;
    }

    public static User getUser() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public void addFriend(String username) {
        this.friends.add(username);
    }

    public void addFriendConnection(String username, RemoteClientInterface clientObject) {
        openedConnections.put(username, clientObject);
    }

    public RemoteClientInterface getFriendConnection(String username) {
        return openedConnections.get(username);
    }
}
