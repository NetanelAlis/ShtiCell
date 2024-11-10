package client.gui.home.main.view;

import client.gui.app.MainAppViewController;
import client.gui.exception.ExceptionWindowController;
import client.gui.home.Command.CommandsController;
import client.gui.home.Command.PermissionRequestTableEntry;
import client.gui.home.file.upload.FileUploadController;
import client.gui.home.permission.table.PermissionsTableController;
import client.gui.home.sheet.table.SheetTableEntry;
import client.gui.home.sheet.table.SheetsTableController;
import client.gui.util.Constants;
import client.gui.util.http.HttpClientUtil;
import client.main.Main;
import client.task.FileLoadingTask;
import dto.permission.ReceivedPermissionRequestDTO;
import dto.permission.SentPermissionRequestDTO;
import dto.sheet.SheetMetaDataDTO;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
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
    
    @FXML private CommandsController commandsController;
    @FXML private SheetsTableController sheetsTableController;
    @FXML private PermissionsTableController permissionsTableController;
    
    private SheetTableEntry selectedSheet;
    private Stage primaryStage;
    private MainAppViewController mainAppController;
    
    @FXML
    private void initialize() {
        if (this.sheetsTableController != null) {
            this.sheetsTableController.setMainController(this);
        }
        
        if (this.commandsController != null) {
            this.commandsController.setMainController(this);
        }
        
        if (this.permissionsTableController != null) {
            this.permissionsTableController.setMainController(this);
        }
    }
    
    @FXML
    void onLoadSheetClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Sheet file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(this.primaryStage);
        if (selectedFile == null) {
            return;
        }
        
        String absolutePath = selectedFile.getAbsolutePath();
        this.loadNewSheetFromXML(absolutePath);
    }
    
    private void loadNewSheetFromXML(String absolutePath) {
        FileUploadController fileUploadController = this.openFileUploadWindow();
        Task<Void> fileLoadingTask = new FileLoadingTask(
                absolutePath,
                fileUploadController,
                this::addNewSheet);
        
        this.bindFileLoadingTaskToUIComponents(fileUploadController, fileLoadingTask);
        new Thread(fileLoadingTask).start();
    }
    
    private FileUploadController openFileUploadWindow() {
        FileUploadController fileUploadController = null;
        try {
            // Load the FileUploadController and FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FILE_UPLOAD_FXML_RESOURCE_LOCATION));
            Parent root = loader.load();
            
            fileUploadController = loader.getController();
            
            // Set the scene and content
            Stage popUpStage = new Stage();
            Scene scene = new Scene(root);
            popUpStage.setScene(scene);
            fileUploadController.setStage(popUpStage);
            popUpStage.getIcons().add(
                    new Image(Objects.requireNonNull(
                            Main.class.getResourceAsStream(Constants.SHTICELL_LOGO_RESOURCE_LOCATION))));
            // Make the window modal (blocks interactions with the main window)
            popUpStage.initModality(Modality.APPLICATION_MODAL);
            
            // Show the window
            popUpStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return fileUploadController;
    }
    
    private void bindFileLoadingTaskToUIComponents(FileUploadController fileUploadController, Task<Void> fileLoadingTask) {
        fileUploadController.bindProgressComponents(fileLoadingTask);
    }
    
    private void addNewSheet(SheetMetaDataDTO sheetNameAndSize) {
        this.sheetsTableController.addSheetEntry(sheetNameAndSize);
    }
    
    public void sendNewPermissionRequest(String sheetName, String newPermission) {
        SentPermissionRequestDTO requestToSend = new SentPermissionRequestDTO(sheetName, newPermission);
        
        Request request = new Request.Builder()
                .url(Constants.SEND_PERMISSION_REQUEST)
                .post(RequestBody.create(Constants.GSON_INSTANCE.toJson(requestToSend),
                        MediaType.parse("application/json")))
                .build();
        
        HttpClientUtil.runAsync(request, new Callback() {
            
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    if (response.code() != 200) {
                        String responseBodyString = responseBody.string();
                        Platform.runLater(() -> commandsController.updateNewRequestErrorLabel(responseBodyString));
                    } else {
                        Platform.runLater(() -> commandsController.clearNewPermissionRequestsFields());
                    }
                }
            }
            
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        ExceptionWindowController.openExceptionPopup(
                                "Something went wrong: " + e.getMessage())
                );
            }
        });
    }
    
    public void setSelectedSheet(SheetTableEntry newValue) {
        this.selectedSheet = newValue.deepCopy();
        this.permissionsTableController.setSelectedSheet(this.selectedSheet.getSheetName());
    }
    
    public void replyToPermissionRequest(PermissionRequestTableEntry selectedRequest, boolean answer) {
        ReceivedPermissionRequestDTO requestToReplyTo = new ReceivedPermissionRequestDTO(
                selectedRequest.getSender(),
                selectedRequest.getSheetName(),
                selectedRequest.getPermission(),
                selectedRequest.getRequestID()
        );
        
        HttpUrl url = HttpUrl.parse(Constants.ANSWER_PERMISSION_REQUEST)
                .newBuilder()
                .addQueryParameter("answer", String.valueOf(answer))
                .build();
        
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(Constants.GSON_INSTANCE.toJson(requestToReplyTo),
                        MediaType.parse("application/json")))
                .build();
        
        HttpClientUtil.runAsync(request, new Callback() {
            
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    if (response.code() != 200) {
                        String responseBodyString = responseBody.string();
                        Platform.runLater(() ->
                                ExceptionWindowController.openExceptionPopup(
                                        "Something went wrong: " + responseBodyString)
                        );
                    }
                }
            }
            
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        ExceptionWindowController.openExceptionPopup(
                                "Something went wrong: " + e.getMessage())
                );
            }
        });
    }
    
    @Override
    public void close() throws IOException {
        this.commandsController.close();
        this.sheetsTableController.close();
        this.permissionsTableController.close();
    }
    
    public void setAppMainController(MainAppViewController mainAppController) {
        this.mainAppController = mainAppController;
    }
    
    public void setActive() {
        this.sheetsTableController.startTableRefresher();
        this.permissionsTableController.startTableRefresher();
        this.commandsController.startTableRefresher();
    }
    
    public void setInActive() {
        try {
            this.sheetsTableController.close();
            this.permissionsTableController.close();
            this.commandsController.close();
        } catch (IOException ignore) {}
    }
    
    public boolean viewSheet() {
        if (this.selectedSheet == null) {
            this.commandsController.updateViewSheetErrorLabel("No sheet selected");
        } else if (this.selectedSheet.getPermission().equalsIgnoreCase(Constants.NONE_PERMISSION_TYPE)) {
            this.commandsController.updateViewSheetErrorLabel("Unauthorized to view this sheet");
        } else {
            this.mainAppController.switchToEditor(this.selectedSheet.getSheetName());
            return true;
        }
        return false;
    }
}
