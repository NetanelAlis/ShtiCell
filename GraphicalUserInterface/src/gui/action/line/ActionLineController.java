package gui.action.line;

import gui.grid.MainSheetController;
import gui.main.MainAppViewController;
import gui.top.TopSubComponentController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.naming.Binding;

public class ActionLineController {
    private TopSubComponentController topSubComponentController;
    private MainAppViewController mainAppViewController;
    @FXML
    private Label cellIDLabel;

    @FXML
    private Label lastUpdatedCellValueVersionLabel;

    @FXML
    private TextField originalValueTextField;

    @FXML
    private Button updateValueButton;

    private BooleanProperty fileNotLoaded;
    private StringProperty cellID;

    public ActionLineController() {
        fileNotLoaded = new SimpleBooleanProperty(true);
        cellID = new SimpleStringProperty("Cell ID ??");
    }

    @FXML
    public void initialize(){
        this.updateValueButton.disableProperty().bind(Bindings.or(fileNotLoaded, originalValueTextField.textProperty().isEmpty()));
        this.cellIDLabel.textProperty().bind(cellID);
    }

    public void toggleFileLoadedProperty() {
        fileNotLoaded.setValue(!fileNotLoaded.getValue());
    }

    @FXML
    private void onUpdateValuePressed(ActionEvent event) {
        lastUpdatedCellValueVersionLabel.setText("next version");
    }

    public void setTopSubComponentController(TopSubComponentController topSubComponentController) {
        this.topSubComponentController = topSubComponentController;
    }

    public void setMainAppController(MainAppViewController mainAppViewController){
        this.mainAppViewController = mainAppViewController;
    }

    public void updateCellIDLabel(String key) {
        this.cellID.set("cell ID " + key);
    }
}