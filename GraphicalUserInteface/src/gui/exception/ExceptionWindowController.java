package gui.exception;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ExceptionWindowController {

    @FXML
    private Button closeButton;

    @FXML
    private Label messageLabel;

    public void setMessage(String errorMessage) {
        messageLabel.setText(errorMessage);
    }

    public Button getCloseButton() {
        return closeButton;
    }
}
