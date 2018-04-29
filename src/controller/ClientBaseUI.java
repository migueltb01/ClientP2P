package controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import p2p.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


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
                    System.out.println(e.getMessage());//----------------------------------------------------------------------------------------------------------------------REVISE THIS!
                }
            }
        });

        //----------------------------------------------------------------------------------------------REQUESTS EVENTS


        //-----------------------------------------------------------------------------------------------ACCOUNT EVENTS

        buttonChangePassword.setOnAction(event -> {

        });

        buttonDeleteAccount.setOnAction(event -> {
            try {
                Connection.getConnection().getServerObject().deleteUser(Connection.getConnection().getClientObject(),User.getUser().getUsername(),User.getUser().getPassword());
            } catch (Exception e) {
                try {
                    showError(e.getMessage(),event);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }

    public void setFriends() {
        ObservableList<String> friends = listViewFriends.getItems();
        friends.addAll(User.getUser().getFriends());
    }

    public void showError(String error, Event event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ui/clientErrorUI.fxml"));

        Parent root = fxmlLoader.load();
        ClientErrorUI controller = fxmlLoader.getController();
        controller.setError(error);

        stage.setScene(new Scene(root));
        stage.setTitle("Error");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
}
