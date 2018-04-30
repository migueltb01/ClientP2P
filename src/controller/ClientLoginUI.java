package controller;

import exceptions.PasswordMatchException;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import p2p.Connection;
import p2p.User;

import java.io.IOException;
import java.security.MessageDigest;

public class ClientLoginUI {

    @FXML
    Timeline timeline = new Timeline();
    @FXML
    Timeline timeline2 = new Timeline();

    //LOGIN

    @FXML
    Button buttonLogin;
    @FXML
    Button buttonRegister;
    @FXML
    GridPane gridPaneLogin;
    @FXML
    VBox vBoxLogin;
    @FXML
    TextField textFieldUser;
    @FXML
    PasswordField passwordFieldPassword;
    @FXML
    ProgressIndicator progressIndicator;
    @FXML
    Label labelNoMatchLogin;

    //REGISTRO

    @FXML
    GridPane gridPaneRegister;
    @FXML
    Button buttonCancelRegister;
    @FXML
    Button buttonSubmitRegister;
    @FXML
    VBox vBoxRegister;
    @FXML
    Label labelNoMatchRegister;
    @FXML
    TextField textFieldUserRegister;
    @FXML
    TextField passwordFieldPasswordRegister;
    @FXML
    TextField passwordFieldRepeatPasswordRegister;

    private static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    private void initialize() {

        initializeEvents();
        initializeAnimations();

        vBoxRegister.setDisable(true);
        vBoxRegister.setTranslateX(360);
    }

    private void initializeAnimations() {
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO),
                new KeyFrame(new Duration(1000),
                        new KeyValue(vBoxLogin.translateXProperty(), -360),
                        new KeyValue(vBoxRegister.translateXProperty(), 0)
                )
        );
        timeline2.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO),
                new KeyFrame(new Duration(1000),
                        new KeyValue(vBoxLogin.translateXProperty(), 0),
                        new KeyValue(vBoxRegister.translateXProperty(), 360)
                )
        );
    }

    private void initializeEvents() {

        buttonLogin.setOnAction(event -> {
            progressIndicator.setVisible(true);
            gridPaneLogin.setVisible(false);
            buttonLogin.setDisable(true);
            buttonRegister.setDisable(true);

            try {


                Connection.connect();
                User.setUser(textFieldUser.getText(), sha256(passwordFieldPassword.getText()),
                        Connection.getConnection().getServerObject().logIn(textFieldUser.getText(),
                                sha256(passwordFieldPassword.getText()),
                                Connection.getConnection().getClientObject()));
                System.out.println(User.getUser().getFriends());
                callBaseScreen();

            } catch (Exception e) {
                progressIndicator.setVisible(false);
                gridPaneLogin.setVisible(true);
                buttonLogin.setDisable(false);
                buttonRegister.setDisable(false);

                showError(e.getMessage());
            }

        });

        buttonRegister.setOnAction(event -> {
            vBoxLogin.setDisable(!vBoxLogin.isDisabled());
            vBoxRegister.setDisable(!vBoxRegister.isDisabled());
            timeline.play();
            labelNoMatchRegister.setVisible(false);
            textFieldUser.setText("");
            passwordFieldPassword.setText("");
        });

        buttonCancelRegister.setOnAction(event -> {
            vBoxLogin.setDisable(!vBoxLogin.isDisabled());
            vBoxRegister.setDisable(!vBoxRegister.isDisabled());
            timeline2.play();
            labelNoMatchLogin.setVisible(false);
            textFieldUserRegister.setText("");
            passwordFieldPasswordRegister.setText("");
            passwordFieldRepeatPasswordRegister.setText("");
        });

        buttonSubmitRegister.setOnAction(event -> {
            progressIndicator.setVisible(true);
            gridPaneRegister.setVisible(false);
            buttonCancelRegister.setDisable(true);
            buttonSubmitRegister.setDisable(true);

            try {
                if (!passwordFieldPasswordRegister.getText().equals(passwordFieldRepeatPasswordRegister.getText()))
                    throw new PasswordMatchException();
                Connection.connect();
                Connection.getConnection().getServerObject().registerUser(textFieldUserRegister.getText(), sha256(passwordFieldPasswordRegister.getText()));
                User.setUser(textFieldUserRegister.getText(), sha256(passwordFieldPasswordRegister.getText()),
                        Connection.getConnection().getServerObject().logIn(textFieldUserRegister.getText(),
                                sha256(passwordFieldPasswordRegister.getText()),
                                Connection.getConnection().getClientObject()));
                System.out.println(User.getUser().getFriends());
                callBaseScreen();

            } catch (Exception e) {
                progressIndicator.setVisible(false);
                gridPaneRegister.setVisible(true);
                buttonSubmitRegister.setDisable(false);
                buttonCancelRegister.setDisable(false);

                showError(e.getMessage());
            }
        });

        passwordFieldRepeatPasswordRegister.setOnKeyTyped(event -> {
            if (passwordFieldRepeatPasswordRegister.getText().equals(passwordFieldPasswordRegister.getText())) {
                buttonSubmitRegister.setDisable(false);
            } else {
                buttonSubmitRegister.setDisable(true);
            }
        });
    }

    private void callBaseScreen() throws IOException {

        Stage primaryStage = (Stage) ((Node) buttonSubmitRegister).getScene().getWindow();
        primaryStage.close();
        primaryStage = new Stage();

        double width = 1211;
        double height = 817;
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((screenBounds.getWidth() - width) / 2);
        primaryStage.setY((screenBounds.getHeight() - height) / 2);
        final Scene scene = new Scene(new Group(), width, height);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            try {
                Connection.getConnection().getServerObject().logOut(Connection.getConnection().getClientObject(), User.getUser().getUsername(), User.getUser().getPassword());
            } catch (Exception e) {
                showError(e.getMessage());
            }
            Platform.exit();
            System.exit(0);
        });
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/clientBaseUI.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("dingDing P2P Messaging");
        primaryStage.setScene(new Scene(root, 1211, 817));
        primaryStage.setResizable(false);
        primaryStage.show();

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

        stage.setScene(new Scene(root));
        stage.setTitle("Error");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(buttonLogin.getScene().getWindow());

        controller.setError(error);
        stage.show();
    }
}
