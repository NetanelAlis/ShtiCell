package client.gui.editor.action.line;

import dto.cell.CellDTO;
import client.gui.editor.cell.ActionLineCellModel;
import client.gui.editor.cell.CellModel;
import client.gui.editor.main.view.MainEditorController;
import client.gui.editor.top.TopSubComponentController;
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
    private BooleanProperty isInReaderModeProperty;
    
    public ActionLineController() {
        this.actionLineCellModel = new CellModel();
        this.isInReaderModeProperty = new SimpleBooleanProperty(true);
    }
    
    @FXML
    private void initialize() {
        this.updateValueButton.disableProperty().bind(
                Bindings.or(this.cellIDLabel.textProperty().isEqualTo("Cell ID "),
                            this.isInReaderModeProperty));
        
        this.originalValueTextField.disableProperty().bind(
                Bindings.or(this.cellIDLabel.textProperty().isEqualTo("Cell ID "),
                        this.isInReaderModeProperty));
        
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
    
    public void setMainController(MainEditorController mainViewController) {
        this.mainEditorController = mainViewController;
    }
    
    public void showCellDetails(CellDTO cellDTO) {
        this.originalValueTextField.setText(cellDTO.getOriginalValue());
        this.actionLineCellModel.getCellIDProperty().set(cellDTO.getCellId());
        this.actionLineCellModel.getLastUpdatedVersionProperty().set(String.valueOf(cellDTO.getVersion()));
        
        if (cellDTO.getUpdatedBy().isEmpty()) {
            this.actionLineCellModel.getUpdatedByProperty().set("");
        } else {
            this.actionLineCellModel.getUpdatedByProperty().set("By " + cellDTO.getUpdatedBy());
        }
    }
    
    public void resetCellModel() {
        this.originalValueTextField.setText("");
        this.actionLineCellModel.getCellIDProperty().set("");
        this.actionLineCellModel.getLastUpdatedVersionProperty().set("");
        this.actionLineCellModel.getUpdatedByProperty().set("");
    }
    
    public void disableEditableActions(boolean disable) {
        this.isInReaderModeProperty.set(disable);
    }
}
