package gui.action.line;

import dto.cell.CellDTO;
import gui.cell.ActionLineCellModel;
import gui.cell.CellModel;
import gui.main.view.MainViewController;
import gui.top.TopSubComponentController;
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
    private MainViewController mainViewController;
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
        this.mainViewController.updateCellValue(
                this.actionLineCellModel.getCellIDProperty().get(),
                this.originalValueTextField.textProperty().get());
    }
    
    public void setTopSubComponentController(TopSubComponentController topSubComponentController) {
        this.topSubComponentController = topSubComponentController;
    }
    
    public void setMainController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
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
