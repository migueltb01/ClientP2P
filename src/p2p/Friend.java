package p2p;

import javafx.collections.ObservableList;

public class Friend {
    private RemoteClientInterface friendObject;
    private ObservableList<String> friendChat;

    public Friend(RemoteClientInterface friendObject, ObservableList<String> friendChat) {
        this.friendChat = friendChat;
        this.friendObject = friendObject;
    }

    public RemoteClientInterface getFriendObject() {
        return friendObject;
    }

    public void setFriendObject(RemoteClientInterface friendObject) {
        this.friendObject = friendObject;
    }

    public ObservableList<String> getFriendChat() {
        return friendChat;
    }

    public void setFriendChat(ObservableList<String> friendChat) {
        this.friendChat = friendChat;
    }
}
