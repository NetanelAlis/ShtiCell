package client.gui.home.main.view;

import client.Main;
import client.gui.app.MainAppViewController;
import client.gui.exception.ExceptionWindowController;
import client.gui.home.command.CommandComponentController;
import client.gui.home.command.PermissionRequestTableEntry;
import client.gui.home.file.upload.FileUploadController;
import client.gui.home.permission.table.PermissionTableController;
import client.gui.home.sheet.table.SheetTableController;
import client.gui.home.sheet.table.SheetTableEntry;
import client.task.FileLoadingTask;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import dto.permission.ReceivedRequestForTableDTO;
import dto.permission.SendRequestDTO;
import dto.sheet.SheetMetaDataDTO;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class HomeViewController implements Closeable {


    @FXML private Button loadSheetButton;
    @FXML private SheetTableController sheetTableController;
    @FXML private PermissionTableController permissionTableController;
    @FXML private CommandComponentController commandComponentController;
    private Stage primaryStage;
    private SheetTableEntry lastSelectedSheetEntry;
    private MainAppViewController mainAppViewController;

    @FXML public void initialize() {
        if(this.sheetTableController != null) {
          this.sheetTableController.setHomeViewController(this);
        }

        if(this.permissionTableController != null) {
            this.permissionTableController.setHomeViewController(this);
        }

        if(this.commandComponentController != null){
            this.commandComponentController.setHomeViewController(this);
        }

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
                            Main.class.getResourceAsStream(Constants.SHTICELL_ICON_LOCATION))));
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

    public void sendNewPermissionRequest(String sheetName, String permissionType) {
        SendRequestDTO sendRequestDTO = new SendRequestDTO(permissionType, sheetName);
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
                    if (response.code() != 200) {
                        String responseBodyString = responseBody.string();
                        Platform.runLater(() -> {
                            commandComponentController.updateErrorLabel(responseBodyString);
                        });
                    } else {
                        Platform.runLater(() -> {
                            commandComponentController.clearNewPermissionRequest();
                        });
                    }
                }
            }
        });
    }

    public void updateCurrentSelectedSheet(SheetTableEntry newSheetEntry) {
        this.lastSelectedSheetEntry = newSheetEntry.deepCopy();
        this.permissionTableController.updateSheetNameInRefresher(newSheetEntry);
    }

    public void replyToPermissionRequest(PermissionRequestTableEntry selectedItem, boolean approveRequest) {
        HttpUrl url = HttpUrl.parse(Constants.RESPONSE_TO_PERMISSION_REQUEST)
                .newBuilder()
                .addQueryParameter("request_approved", String.valueOf(approveRequest))
                .build();

        ReceivedRequestForTableDTO permissionRequest = new ReceivedRequestForTableDTO(selectedItem.getPermissions(), selectedItem.getSender(), selectedItem.getSheetName(), selectedItem.getRequestNumber());
        String json = Constants.GSON_INSTANCE.toJson(permissionRequest);

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url) // Use the URL with query parameter
                .post(body)
                .build();

        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    ExceptionWindowController.openExceptionPopup("Something went wrong: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (response.code() != 200) {
                        String responseBodyString = responseBody.string();
                        Platform.runLater(() -> {
                            ExceptionWindowController.openExceptionPopup("Something went wrong: " + responseBodyString);
                        });
                    }
                }
            }
        });
    }

    public void setAppMainController(MainAppViewController mainAppViewController) {
        this.mainAppViewController = mainAppViewController;
    }

    public void setActive() {
        if(this.sheetTableController != null) {
            this.sheetTableController.startSheetTableRefresher();
        }

        if(this.permissionTableController != null) {
            this.permissionTableController.startPermissionTableRefresher();
        }

        if(this.commandComponentController != null){
            this.commandComponentController.startPermissionTableRefresher();
        }
    }

    public void close() {
        this.sheetTableController.close();
        this.permissionTableController.close();
        this.commandComponentController.close();
    }

    public void setInActive() {
        try {
            this.sheetTableController.close();
            this.permissionTableController.close();
            this.commandComponentController.close();
        } catch (Exception ignored) {
        }
    }

    public void viewSheet() {
        if(this.lastSelectedSheetEntry == null){
            this.commandComponentController.updateViewSheetErrorLabel("Please select a sheet");
        } else if(this.lastSelectedSheetEntry.getPermissions().equalsIgnoreCase(Constants.NO_PERMISSION)){
            this.commandComponentController.updateViewSheetErrorLabel("No permission to view sheet");
        }
        else
            this.commandComponentController.updateViewSheetErrorLabel("");
            this.mainAppViewController.switchToEditorPage(this.lastSelectedSheetEntry.getSheetName());
    }
}
