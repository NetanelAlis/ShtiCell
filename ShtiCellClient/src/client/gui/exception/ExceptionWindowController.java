package client.gui.exception;

import client.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

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

    public static void openExceptionPopup(String errorMessage) {
        // Load the FXML for the exception window
        try {
            FXMLLoader loader = new FXMLLoader(ExceptionWindowController.class.getResource("/client/gui/exception/ExceptionWindow.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the error message to the label
            ExceptionWindowController controller = loader.getController();
            controller.setMessage(errorMessage); // Set the error message dynamically

            // Create a new stage (window) for the popup
            Stage popUpStage = new Stage();
            popUpStage.setTitle("Error");
            popUpStage.getIcons().add(
                    new Image(Objects.requireNonNull(
                            Main.class.getResourceAsStream("/client/gui/resources/shticellLogo.png"))));
            popUpStage.setScene(new Scene(root));
            popUpStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
            Button closeButton = controller.getCloseButton();
            closeButton.setOnAction(event -> popUpStage.close());
            popUpStage.showAndWait(); // Show the pop-up window and wait for it to be closed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
