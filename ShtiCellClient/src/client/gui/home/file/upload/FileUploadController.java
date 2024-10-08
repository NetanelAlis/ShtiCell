package client.gui.home.file.upload;

import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import java.util.Optional;
import java.util.function.Consumer;

public class FileUploadController {

    @FXML
    private Button cancelButton;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressPercentLabel;

    private Stage fileUploadStage;

    public void bindProgressComponents(Task<Boolean> aTask) {
        // task progress bar
        progressBar.progressProperty().bind(aTask.progressProperty());

        // task percent label
        progressPercentLabel.textProperty().bind(
                Bindings.when(aTask.progressProperty().greaterThan(0))
                        .then(
                                Bindings.concat(
                                        Bindings.format("%.0f", Bindings.multiply(aTask.progressProperty(), 100)),
                                        " %"
                                )
                        )
                        .otherwise("")
        );

        cancelButton.setOnAction(event -> this.onTaskClosed(aTask));
        fileUploadStage.setOnCloseRequest(event -> this.onTaskClosed(aTask));
    }

    public void onTaskFinished() {
        this.progressPercentLabel.textProperty().unbind();
        this.progressBar.progressProperty().unbind();
        this.progressPercentLabel.setText("");
        this.progressBar.setProgress(0);
        this.fileUploadStage.close();
    }

    private void onTaskClosed(Task<Boolean> aTask) {
        if (aTask.isRunning()) {
            aTask.cancel();
            onTaskFinished();
        }
    }

    public void setStage(Stage stage){
        this.fileUploadStage = stage;
    }
}
