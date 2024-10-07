package client.gui.editor.action.line;

import client.gui.editor.cell.ActionLineCellModel;
import client.gui.editor.cell.CellModel;
import client.gui.editor.top.TopSubComponentController;
import dto.CellDTO;
import client.gui.editor.main.view.MainEditorController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ActionLineController {
    
    @FXML private Label cellIDLabel;
    @FXML private Label lastUpdatedCellValueVersionLabel;
    @FXML private TextField originalValueTextField;
    @FXML private Button updateValueButton;
    
    private TopSubComponentController topSubComponentController;
    private MainEditorController mainEditorController;
    private ActionLineCellModel actionLineCellModel;
    private BooleanProperty isFileLoadedProperty;
    
    public ActionLineController() {
        actionLineCellModel = new CellModel();
        isFileLoadedProperty = new SimpleBooleanProperty(true);
    }
    
    @FXML
    private void initialize() {
        this.updateValueButton.disableProperty().bind(
                Bindings.or(this.cellIDLabel.textProperty().isEqualTo("Cell ID "),
                            this.isFileLoadedProperty));
        
        this.originalValueTextField.disableProperty().bind(
                Bindings.or(this.cellIDLabel.textProperty().isEqualTo("Cell ID "),
                        this.isFileLoadedProperty));
        
        this.actionLineCellModel.bind(
                this.cellIDLabel.textProperty(),
                this.lastUpdatedCellValueVersionLabel.textProperty());
    }
    
    @FXML
    private void onUpdateValuePressed(ActionEvent event) {
        this.mainEditorController.updateCellValue(
                this.actionLineCellModel.getCellIDProperty().get(),
                this.originalValueTextField.textProperty().get());
    }
    
    public void setTopSubComponentController(TopSubComponentController topSubComponentController) {
        this.topSubComponentController = topSubComponentController;
    }
    
    public void setMainController(MainEditorController mainEditorController) {
        this.mainEditorController = mainEditorController;
    }
    
    public void showCellDetails(CellDTO cellDTO) {
        this.actionLineCellModel.getCellIDProperty().set(cellDTO.getCellId());
        this.originalValueTextField.setText(cellDTO.getOriginalValue());
        this.actionLineCellModel.getLastUpdatedVersionProperty().set(String.valueOf(cellDTO.getVersion()));
    }
    
    
    public void resetCellModel() {
        this.actionLineCellModel.getCellIDProperty().set("");
        this.originalValueTextField.setText("");
        this.actionLineCellModel.getLastUpdatedVersionProperty().set("");
    }
    
    public void bindFileNotLoaded(BooleanProperty isFileLoaded) {
        this.isFileLoadedProperty.bind(isFileLoaded);
    }

}
