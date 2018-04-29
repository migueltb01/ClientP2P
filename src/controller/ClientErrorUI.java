package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ClientErrorUI {
    String error;

    @FXML
    Button buttonAccept;
    @FXML
    Label labelMessage;

    @FXML
    private void initialize() {
        buttonAccept.setOnAction(event -> ((Stage) buttonAccept.getScene().getWindow()).close());
    }

    public void setError(String error) {
        labelMessage.setText(error);
        System.out.println(error);
    }
}
