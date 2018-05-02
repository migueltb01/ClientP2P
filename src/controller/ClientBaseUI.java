package controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import p2p.Connection;
import p2p.ListHelper;
import p2p.User;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;


public class ClientBaseUI {

    private static ClientBaseUI instance = null;

    @FXML
    Timeline timelineFriends = new Timeline();
    @FXML
    Timeline timelineAccount = new Timeline();
    @FXML
    Timeline timelineRequests = new Timeline();

    //-----------------------------------------------------------------------------------------------------------COMMON
    @FXML
    Button buttonSend;
    @FXML
    TextField textFieldMessage;
    @FXML
    ListView listViewChat;
    @FXML
    Label labelChatUsername;
    ObservableList chat;
    //----------------------------------------------------------------------------------------------------------FRIENDS
    @FXML
    Button buttonFriends;
    @FXML
    VBox vBoxFriends;
    @FXML
    Button buttonAddFriend;
    @FXML
    Button buttonRemoveFriend;
    @FXML
    ListView<String> listViewFriends;
    //---------------------------------------------------------------------------------------------------------REQUESTS
    @FXML
    Button buttonRequests;
    @FXML
    VBox vBoxRequests;
    @FXML
    Button buttonAcceptRequest;
    @FXML
    Button buttonRejectRequest;
    @FXML
    ListView<String> listViewRequests;
    @FXML
    ListView<String> listViewResults;
    @FXML
    TextField textFieldSearch;
    @FXML
    Button buttonSendRequest;
    //----------------------------------------------------------------------------------------------------------ACCOUNT
    @FXML
    Button buttonAccount;
    @FXML
    VBox vBoxAccount;
    @FXML
    Label labelUsername;
    @FXML
    TextField textFieldOldPassword;
    @FXML
    TextField textFieldNewPassword;
    @FXML
    TextField textFieldRepeatPassword;
    @FXML
    Button buttonAcceptPassword;
    @FXML
    Button buttonDeleteAccount;
    @FXML
    Button buttonChangePassword;

    public static ClientBaseUI getBaseUI() {
        return instance;
    }

    private void initializeAnimations() {
        timelineFriends.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO),
                new KeyFrame(new Duration(1000),
                        new KeyValue(vBoxFriends.translateXProperty(), 0),
                        new KeyValue(vBoxAccount.translateXProperty(), -500),
                        new KeyValue(vBoxRequests.translateXProperty(), -500)
                )
        );
        timelineAccount.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO),
                new KeyFrame(new Duration(1000),
                        new KeyValue(vBoxFriends.translateXProperty(), -500),
                        new KeyValue(vBoxAccount.translateXProperty(), 0),
                        new KeyValue(vBoxRequests.translateXProperty(), -500)
                )
        );
        timelineRequests.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO),
                new KeyFrame(new Duration(1000),
                        new KeyValue(vBoxFriends.translateXProperty(), -500),
                        new KeyValue(vBoxAccount.translateXProperty(), -500),
                        new KeyValue(vBoxRequests.translateXProperty(), 0)
                )
        );
    }

    @FXML
    private void initialize() {
        instance = this;
        vBoxAccount.setTranslateX(-500);
        vBoxRequests.setTranslateX(-500);
        labelUsername.setText(User.getUser().getUsername());
        listViewFriends.getSelectionModel().clearSelection();
        buttonSend.setDisable(true);
        textFieldMessage.setDisable(true);
        listViewChat.setMouseTransparent(true);
        listViewChat.setFocusTraversable(false);

        listViewRequests.setItems(ListHelper.getListHelper().getRequests());
        listViewFriends.setItems(ListHelper.getListHelper().getFriends());

        ArrayList<String> users;
        try {
            users = Connection.getConnection().getServerObject().searchUsers("", User.getUser().getUsername());
            listViewResults.setItems(FXCollections.observableArrayList(users));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        initializeAnimations();
        initializeEvents();
        timelineFriends.play();
    }

    public void showError(String error) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ui/clientErrorUI.fxml"));

        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClientErrorUI controller = fxmlLoader.getController();
        controller.setError(error);

        stage.setScene(new Scene(root));
        stage.setTitle("Error");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node) buttonFriends).getScene().getWindow());
        stage.show();
    }

    private void initializeEvents() {

        //------------------------------------------------------------------------------------------------COMMON EVENTS

        buttonFriends.setOnAction(event -> {
            timelineFriends.play();
        });

        buttonRequests.setOnAction(event -> {
            timelineRequests.play();
        });

        buttonAccount.setOnAction(event -> {
            timelineAccount.play();
        });

        textFieldMessage.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == "") {
                    buttonSend.setDisable(true);
                } else {
                    buttonSend.setDisable(false);
                }
            }
        });

        buttonSend.setOnAction(event -> {
            ObservableList<String> messages = listViewChat.getItems();
            messages.add(User.getUser().getUsername() + " dijo:\n       " + textFieldMessage.getText());
            try {
                ListHelper.getListHelper().getFriendObject(labelChatUsername.getText()).receiveMessage(User.getUser().getUsername(),
                        User.getUser().getUsername() + " dijo:\n       " + textFieldMessage.getText());
            } catch (Exception e) {
                showError(e.getMessage());
            }
            textFieldMessage.setText("");
        });

        //-----------------------------------------------------------------------------------------------FRIENDS EVENTS

        ListHelper.getListHelper().getFriends().addListener((ListChangeListener<String>) c -> listViewFriends.setItems(ListHelper.getListHelper().getFriends()));

        listViewFriends.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    labelChatUsername.setText(newValue);
                    if (!ListHelper.getListHelper().isChatCreated(newValue)) {
                        try {
                            ListHelper.getListHelper().createChat(newValue);
                            ListHelper.getListHelper().getFriendObject(labelChatUsername.getText()).startChat(User.getUser().getUsername());
                        } catch (Exception e) {
                            showError(e.getMessage());
                        }
                    }
                    chat = ListHelper.getListHelper().getChat(newValue);
                    listViewChat.setItems(chat);
                } else {
                    labelChatUsername.setText("");
                }
                buttonSend.setDisable(false);
                textFieldMessage.setDisable(false);
            }
        });
        //----------------------------------------------------------------------------------------------REQUESTS EVENTS

        textFieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                buttonSendRequest.setDisable(true);
                listViewResults.getSelectionModel().clearSelection();
                ArrayList<String> users = Connection.getConnection().getServerObject().searchUsers(newValue, User.getUser().getUsername());
                listViewResults.setItems(FXCollections.observableArrayList(users));

            } catch (Exception e) {
                showError(e.getMessage());
            }
        });

        ListHelper.getListHelper().getRequests().addListener((ListChangeListener<String>) c -> {
            listViewRequests.setItems(ListHelper.getListHelper().getRequests());
        });

        listViewRequests.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                buttonAcceptRequest.setDisable(false);
                buttonRejectRequest.setDisable(false);
            }
        });

        buttonAcceptRequest.setOnAction(event -> {
            try {
                Connection.getConnection().getServerObject().resolveRequest(Connection.getConnection().getClientObject(),
                        User.getUser().getPassword(), listViewRequests.getSelectionModel().getSelectedItem(),
                        User.getUser().getUsername(), true);
                ListHelper.getListHelper().removeRequest(listViewRequests.getSelectionModel().getSelectedItem());
                buttonAcceptRequest.setDisable(true);
                buttonRejectRequest.setDisable(true);
                listViewRequests.getSelectionModel().clearSelection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        buttonRejectRequest.setOnAction(event -> {
            try {
                Connection.getConnection().getServerObject().resolveRequest(Connection.getConnection().getClientObject(),
                        User.getUser().getPassword(), listViewRequests.getSelectionModel().getSelectedItem(),
                        User.getUser().getUsername(), false);
                ListHelper.getListHelper().removeRequest(listViewRequests.getSelectionModel().getSelectedItem());
                buttonAcceptRequest.setDisable(true);
                buttonRejectRequest.setDisable(true);
                listViewRequests.getSelectionModel().clearSelection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        listViewResults.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> buttonSendRequest.setDisable(false));

        buttonSendRequest.setOnAction(event -> {
            try {
                Connection.getConnection().getServerObject().addFriend(Connection.getConnection().getClientObject(), User.getUser().getUsername(), User.getUser().getPassword(), listViewResults.getSelectionModel().getSelectedItem());
                listViewResults.getSelectionModel().clearSelection();
                buttonSendRequest.setDisable(true);
                ArrayList<String> users = Connection.getConnection().getServerObject().searchUsers("", User.getUser().getUsername());
                listViewResults.setItems(FXCollections.observableArrayList(users));

            } catch (Exception e) {
                showError(e.getMessage());
            }
        });

        //-----------------------------------------------------------------------------------------------ACCOUNT EVENTS

        buttonChangePassword.setOnAction(event -> {
            textFieldOldPassword.setVisible(true);
            textFieldNewPassword.setVisible(true);
            textFieldRepeatPassword.setVisible(true);

            buttonAcceptPassword.setVisible(true);
        });

        textFieldOldPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (equals(User.getUser().getPassword())) {

                }
            }
        });

        buttonDeleteAccount.setOnAction(event -> {
            try {
                Connection.getConnection().getServerObject().deleteUser(Connection.getConnection().getClientObject(), User.getUser().getUsername(), User.getUser().getPassword());
                User.destroy();
                Connection.destroy();

                Stage primaryStage = (Stage) ((Node) buttonFriends).getScene().getWindow();
                primaryStage.close();
                primaryStage = new Stage();

                double width = 367;
                double height = 399;
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                primaryStage.setX((screenBounds.getWidth() - width) / 2);
                primaryStage.setY((screenBounds.getHeight() - height) / 2);
                final Scene scene = new Scene(new Group(), width, height);
                primaryStage.setScene(scene);
                primaryStage.setOnCloseRequest(null);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/clientLoginUI.fxml"));
                Parent root = loader.load();
                primaryStage.setTitle("dingDing P2P Messaging: Login");
                primaryStage.setScene(new Scene(root, 367, 399));
                primaryStage.setResizable(false);
                primaryStage.show();

            } catch (Exception e) {
                showError(e.getMessage());
            }
        });


    }

    public void clearChat() {
        chat = null;
        listViewChat.setItems(null);
    }
}
