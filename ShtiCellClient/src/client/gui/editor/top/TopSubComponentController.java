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
    private MainEditorController mainEditorController;
    private int latestVersion = 1;
    public TopSubComponentController() {
        this.sheetNameProperty = new SimpleStringProperty("Sheet Name");

    }
    
    @FXML
    public void initialize() {
        if (this.actionLineController != null) {
            this.actionLineController.setTopSubComponentController(this);
        }
        this.sheetNameTitledPane.textProperty().bind(this.sheetNameProperty);

        this.versionsChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {

                    if (newValue != null && oldValue != null && !newValue.equals(oldValue))
                     {
                        this.mainEditorController.loadSheetVersion(
                                this.versionsChoiceBox.getSelectionModel().getSelectedIndex() + 1);
                    }
                });
            }

    
    @FXML
    public void onVersionMenuClicked(MouseEvent mouseEvent) {
        mouseEvent.consume();

        this.mainEditorController.getLatestVersionNumber();
    }

    public void updateVersionsChoiceBox(int lastSheetVersionNumber) {
        this.latestVersion = lastSheetVersionNumber;
        String previousVersion = this.versionsChoiceBox.getSelectionModel().getSelectedItem();
        this.versionsChoiceBox.getItems().clear();

        for (int i = 1; i <= lastSheetVersionNumber; i++) {
            {
                this.versionsChoiceBox.getItems().add("Version " + i + "/" + lastSheetVersionNumber);
                if(previousVersion.contains("Version " + i)){
                    this.versionsChoiceBox.getSelectionModel().select(i - 1);
                }
            }
        }
        this.versionsChoiceBox.show();
    }
    
    public void setMainController(MainEditorController mainEditorController) {
        this.mainEditorController = mainEditorController;
    }
    
    public ActionLineController getActionLIneController() {
        return this.actionLineController;
    }
    
    public void setSheetNameAndVersion(String sheetName, int sheetVersion) {
        this.sheetNameProperty.set(sheetName);
        this.versionsChoiceBox.getItems().clear();
        if(sheetVersion > this.latestVersion){
            this.latestVersion = sheetVersion;
        }

        this.versionsChoiceBox.getItems().add("Version " + sheetVersion + "/" + this.latestVersion);
        this.versionsChoiceBox.getSelectionModel().select("Version " + sheetVersion + "/" + this.latestVersion);
    }

    public void addNotOnLastVersionClassToVersionsChoiceBox() {
        if (!this.versionsChoiceBox.getStyleClass().contains("not-on-last-version"))
        this.versionsChoiceBox.getStyleClass().add("not-on-last-version");
    }

    public void removeNotOnLastVersionClassFromVersionsChoiceBox(int sheetVersion) {
        if (sheetVersion == this.latestVersion && this.versionsChoiceBox.getStyleClass().contains("not-on-last-version"))
            this.versionsChoiceBox.getStyleClass().remove("not-on-last-version");
    }
}
