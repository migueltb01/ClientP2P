package p2p;

import java.util.HashMap;

public class User {
    private static User instance = null;
    private String username;
    private String password;
    private HashMap<String, RemoteClientInterface> openedConnections;

    private User(String username, String password) {
        this.username = username;
        this.password = password;

        this.openedConnections = new HashMap<>();
    }

    public static User setUser(String username, String password) {
        if (instance == null) {
            instance = new User(username, password);
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

    public void addFriendConnection(String username, RemoteClientInterface clientObject) {
        openedConnections.put(username, clientObject);
    }

    public static void destroy() {
        instance = null;
    }

    public RemoteClientInterface getFriendConnection(String username) {
        return openedConnections.get(username);
    }

}
