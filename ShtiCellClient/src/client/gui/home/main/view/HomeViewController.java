package client.gui.home.main.view;

import client.Main;
import client.gui.exception.ExceptionWindowController;
import client.gui.home.command.CommandComponentController;
import client.gui.home.file.upload.FileUploadController;
import client.gui.home.permission.table.PermissionTableController;
import client.gui.home.sheet.table.SheetTableController;
import client.task.FileLoadingTask;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import dto.SendRequestDTO;
import dto.SheetMetaDataDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import user.permission.PermissionType;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class HomeViewController {


    @FXML private Button loadSheetButton;
    @FXML private Label userNameLabel;
    @FXML private SheetTableController sheetTableController;
    @FXML private PermissionTableController permissionTableController;
    @FXML private CommandComponentController commandComponentController;
    private Stage primaryStage;
    private StringProperty userNameProperty;

    @FXML public void initialize() {
        if(this.sheetTableController != null) {
            this.sheetTableController.startSheetTableRefresher();
        }

        if(this.permissionTableController != null) {
            this.commandComponentController.setHomeViewController(this);
        }

        this.userNameLabel.textProperty().bind(this.userNameProperty);
    }

    public HomeViewController() {
        this.userNameProperty = new SimpleStringProperty("User1");
    }

//    public void setUserNameProperty(String userNameProperty){
//        this.userNameProperty.set(userNameProperty);
//    }

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
        Task<Boolean> fileLoadingTask = new FileLoadingTask(absolutePath, fileUploadController, this::addNewSheet, this.userNameProperty.get());

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

    public void addNewSheet(SheetMetaDataDTO sheetMetaDataDTO) {
        this.sheetTableController.addNewSheet(sheetMetaDataDTO);
    }

    private void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void homeViewControllerSendNewPermissionRequest(String sheetName, String permissionType) {
        SendRequestDTO sendRequestDTO = new SendRequestDTO(PermissionType.valueOf(permissionType), this.userNameProperty.get(), sheetName);
        String json = Constants.GSON_INSTANCE.toJson(sendRequestDTO);

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(Constants.REQUEST_PERMISSION)
                .post(body)
                .build();

        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {;
                    ExceptionWindowController.openExceptionPopup("Something went wrong: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    String responseBodyString = responseBody.string();
                    if (response.code() != 200) {
                        Platform.runLater(() ->{
                            ExceptionWindowController.openExceptionPopup(responseBodyString);
                        });
                    }
                }
            }
        });
    }
}
