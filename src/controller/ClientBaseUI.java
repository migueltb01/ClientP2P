package controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
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
import p2p.User;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;


public class ClientBaseUI {

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
    Button buttonAttach;
    @FXML
    TextField textFieldMessage;
    @FXML
    ListView listViewChat;
    @FXML
    Label labelChatUsername;
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
    Label labelNumberOfFriends;
    @FXML
    Button buttonDeleteAccount;
    @FXML
    Button buttonChangePassword;

    @FXML
    private void initialize() {
        setFriends();
        vBoxAccount.setTranslateX(-500);
        vBoxRequests.setTranslateX(-500);
        labelUsername.setText(User.getUser().getUsername());
        listViewFriends.getSelectionModel().clearSelection();
        buttonSend.setDisable(true);
        buttonAttach.setDisable(true);
        listViewChat.setDisable(true);
        textFieldMessage.setDisable(true);

        ArrayList<String> users = null;
        ArrayList<String> requests = null;
        try {
            users = Connection.getConnection().getServerObject().searchUsers("");
            listViewResults.setItems(FXCollections.observableArrayList(users));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        initializeAnimations();
        initializeEvents();
        timelineFriends.play();
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

        buttonSend.setOnAction(event -> {
            ObservableList<String> messages = listViewChat.getItems();
            messages.add(textFieldMessage.getText());
        });

        //-----------------------------------------------------------------------------------------------FRIENDS EVENTS

        listViewFriends.setOnMouseClicked(event -> {
            if (User.getUser().getFriendConnection(listViewFriends.getSelectionModel().getSelectedItem()) == null) {
                try {
                    User.getUser().addFriendConnection(listViewFriends.getSelectionModel().getSelectedItem(),
                            Connection.getConnection().getServerObject().startChat(Connection.getConnection().getClientObject(),
                                    User.getUser().getUsername(), listViewFriends.getSelectionModel().getSelectedItem(),
                                    User.getUser().getPassword())
                    );

                } catch (Exception e) {
                    showError(e.getMessage());
                }
            }
        });

        //----------------------------------------------------------------------------------------------REQUESTS EVENTS

        textFieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                buttonAcceptRequest.setDisable(true);
                listViewResults.getSelectionModel().clearSelection();
                ArrayList<String> users = Connection.getConnection().getServerObject().searchUsers(newValue);
                listViewResults.setItems(FXCollections.observableArrayList(users));

            } catch (Exception e) {
                showError(e.getMessage());
            }
        });

        listViewResults.setOnMouseClicked(event -> {
            if (!listViewResults.getSelectionModel().isEmpty())
                buttonSendRequest.setDisable(false);
        });

        buttonSendRequest.setOnAction(event -> {
            try {
                Connection.getConnection().getServerObject().addFriend(Connection.getConnection().getClientObject(), User.getUser().getUsername(), User.getUser().getPassword(), listViewResults.getSelectionModel().getSelectedItem());
            } catch (Exception e) {
                showError(e.getMessage());
            }
        });

        //-----------------------------------------------------------------------------------------------ACCOUNT EVENTS

        buttonChangePassword.setOnAction(event -> {

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

    public void setFriends() {
        ObservableList<String> friends = listViewFriends.getItems();
        friends.addAll(User.getUser().getFriends());
        labelNumberOfFriends.setText(String.valueOf(listViewResults.getItems().size()));
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
}
