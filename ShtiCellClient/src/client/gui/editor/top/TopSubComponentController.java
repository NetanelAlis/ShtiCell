package client.gui.editor.top;

import client.gui.editor.action.line.ActionLineController;
import client.gui.editor.main.view.MainEditorController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private StringProperty sheetVersionProperty;
    private MainEditorController mainEditorController;
    private BooleanProperty isFileLoadedProperty;
    private int lastSelectVersion = 1;

    public TopSubComponentController() {
        this.sheetNameProperty = new SimpleStringProperty("Sheet Name");
        this.sheetVersionProperty = new SimpleStringProperty("-");
        this.isFileLoadedProperty = new SimpleBooleanProperty(false);

    }
    
    @FXML
    public void initialize() {
        if (this.actionLineController != null) {
            this.actionLineController.setTopSubComponentController(this);
        }
        this.versionsChoiceBox.getItems().add("Select Version");
        this.versionsChoiceBox.getSelectionModel().select("Select Version");

        this.sheetNameTitledPane.textProperty().bind(this.sheetNameProperty);
        this.versionsChoiceBox.disableProperty().bind(this.isFileLoadedProperty.not());

        this.versionsChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.contains("" + this.lastSelectVersion) && !newValue.contains("Select Version")) {
                this.mainEditorController.loadSheetVersion(
                        Integer.parseInt(this.versionsChoiceBox.getValue().substring(8)));
            }
                this.versionsChoiceBox.getSelectionModel().selectFirst();
        });
    }

    
    @FXML
    public void onVersionMenuClicked(MouseEvent mouseEvent) {
        mouseEvent.consume();

        this.mainEditorController.getLatestVersionNumber();
    }

    public void updateVersionsChoiceBox(int lastSheetVersionNumber) {
        for (int i = 1; i <= lastSheetVersionNumber; i++) {
            if(!this.versionsChoiceBox.getItems().contains("version " + i)) {
                this.versionsChoiceBox.getItems().add("version " + i);
                this.lastSelectVersion = i;
                this.versionsChoiceBox.styleProperty().set("-fx-mark-color: transparent");
            };
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
        this.sheetVersionProperty.set(String.valueOf(sheetVersion));
        this.versionsChoiceBox.getItems().clear();
        this.versionsChoiceBox.getItems().add("Select Version");
        this.versionsChoiceBox.getSelectionModel().select("Select Version");
    }
    
    public void updateSheetVersion(int version) {
        this.sheetVersionProperty.set(String.valueOf(version));
    }

    public void bindFileNotLoaded(BooleanProperty isFileLoaded) {
        this.isFileLoadedProperty.bind(isFileLoaded.not());
    }
}
