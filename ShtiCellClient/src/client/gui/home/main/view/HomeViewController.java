package client.gui.home.main.view;

import client.Main;
import client.gui.home.file.upload.FileUploadController;
import client.gui.home.permission.table.PermissionTableController;
import client.gui.home.sheet.table.SheetTableController;
import client.tasks.FileLoadingTask;
import dto.SheetNameAndSizeDTO;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class HomeViewController {


    @FXML private Button loadSheetButton;
    @FXML private Label userNameLabel;
    @FXML private SheetTableController sheetTableController;
    @FXML private PermissionTableController permissionTableController;
    private Stage primaryStage;

    @FXML
    void onLoadSheetClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Sheet file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        this.loadNewSheetFromXML(absolutePath);
    }

    private void loadNewSheetFromXML(String absolutePath) {
        FileUploadController fileUploadController = this.openFileUploadWindow();
        Task<Boolean> fileLoadingTask = new FileLoadingTask(absolutePath, fileUploadController, this::addNewSheet);

        this.bindFileLoadingTaskToUIComponents(fileUploadController, fileLoadingTask);

        new Thread(fileLoadingTask).start();
    }

    private FileUploadController openFileUploadWindow() {
        FileUploadController fileUploadController = null;
        try {
            // Load the FileUploadController and FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/home/file/upload/FileUploadComponent.fxml"));
            Parent root = loader.load();

            fileUploadController = loader.getController();

            // Set the scene and content
            Stage popUpStage = new Stage();
            Scene scene = new Scene(root);
            popUpStage.setScene(scene);
            fileUploadController.setStage(popUpStage);
            popUpStage.getIcons().add(
                    new Image(Objects.requireNonNull(
                            Main.class.getResourceAsStream("/client/gui/resources/shticellLogo.png"))));
            // Make the window modal (blocks interactions with the main window)
            popUpStage.initModality(Modality.APPLICATION_MODAL);

            // Show the window
            popUpStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileUploadController;
    }

    private void bindFileLoadingTaskToUIComponents(FileUploadController fileUploadController, Task<Boolean> fileLoadingTask) {
        fileUploadController.bindProgressComponents(fileLoadingTask);
    }

    public void addNewSheet(SheetNameAndSizeDTO sheetNameAndSizeDTO) {
        this.sheetTableController.updateTable(sheetNameAndSizeDTO.getSheetName(), sheetNameAndSizeDTO.numberOfRows(), sheetNameAndSizeDTO.getNumberOfCols());
    }

    private void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

}
