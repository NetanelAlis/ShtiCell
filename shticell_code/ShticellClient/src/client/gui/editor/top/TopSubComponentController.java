package client.gui.editor.top;

import client.gui.editor.action.line.ActionLineController;
import client.gui.editor.main.view.MainEditorController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class TopSubComponentController {
    
    @FXML private ActionLineController actionLineController;
    @FXML private ChoiceBox<String> versionsChoiceBox;
    @FXML private TitledPane sheetNameTitledPane;
    
    private StringProperty sheetNameProperty;
    private MainEditorController mainViewController;
    private int latestVersion;
    private int currentVersion;

    public TopSubComponentController() {
        this.sheetNameProperty = new SimpleStringProperty("Sheet Name");
        this.latestVersion = 1;
        this.currentVersion = 1;
    }
    
    @FXML
    public void initialize() {
        if (this.actionLineController != null) {
            this.actionLineController.setTopSubComponentController(this);
        }
        
        this.sheetNameTitledPane.textProperty().bind(this.sheetNameProperty);

        this.versionsChoiceBox.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> {
                
                if (newValue != null && oldValue != null && !newValue.equals(oldValue)) {
                    this.currentVersion = this.versionsChoiceBox.getSelectionModel().getSelectedIndex() + 1;
                    this.goToCurrentVersion();
                }
        });
    }
    
    @FXML
    public void onVersionMenuClicked(MouseEvent mouseEvent) {
        mouseEvent.consume();
        
        this.mainViewController.getLatestVersionNumber();
    }
    
    public void goToCurrentVersion() {
        if (this.currentVersion == this.latestVersion) {
            this.versionsChoiceBox.getStyleClass().remove("new-version-available");
        }
        
        this.mainViewController.loadSheetVersion(this.currentVersion);
    }
    
    public void updateVersionsChoiceBox(int numOfVersions) {
        this.latestVersion = numOfVersions;
        String previousSelection = this.versionsChoiceBox.getSelectionModel().getSelectedItem();
        this.versionsChoiceBox.getItems().clear();
        for (int i = 1; i <= numOfVersions; i++) {
            this.versionsChoiceBox.getItems().add("Version " + i + "/" + numOfVersions);
            if (previousSelection.contains("Version " + i)) {
                this.versionsChoiceBox.getSelectionModel().select(i - 1);
            }
        }
        this.versionsChoiceBox.show();
    }
    
    public void setSheetNameAndVersion(String sheetName, int sheetVersion, boolean isEnteringEditor) {
        this.sheetNameProperty.set(sheetName);
        if (isEnteringEditor) {
            this.latestVersion = sheetVersion;
        }
        this.setSheetVersion(sheetVersion);
    }
    
    public void setSheetVersion(int sheetVersion) {
        if (this.latestVersion < sheetVersion) {
            this.latestVersion = sheetVersion;
        }
        
        this.currentVersion = sheetVersion;
        this.versionsChoiceBox.getItems().clear();
        this.versionsChoiceBox.getItems().add("Version " + this.currentVersion + "/" + this.latestVersion);
        this.versionsChoiceBox.getSelectionModel().select(
                "Version " + this.currentVersion + "/" + this.latestVersion);
    }
    
    public void notifyNewVersion(int newLatestVersion) {
        if (!this.versionsChoiceBox.getStyleClass().contains("new-version-available")) {
            this.versionsChoiceBox.getStyleClass().add("new-version-available");
        }
        
        if (newLatestVersion != this.latestVersion) {
            this.latestVersion = newLatestVersion;
            this.versionsChoiceBox.getItems().clear();
            this.versionsChoiceBox.getItems().add("Version " + this.currentVersion + "/" + this.latestVersion);
            this.versionsChoiceBox.getSelectionModel().select(
                    "Version " + this.currentVersion + "/" + this.latestVersion);
        }
    }
    
    public void setMainController(MainEditorController mainViewController) {
        this.mainViewController = mainViewController;
    }
    
    public ActionLineController getActionLIneController() {
        return this.actionLineController;
    }
}
