package gui.action.line;

import dto.CellDTO;
import gui.cell.ActionLineCellModel;
import gui.cell.CellModel;
import gui.main.MainAppViewController;
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
    private TopSubComponentController topSubComponentController;
    private MainAppViewController mainAppViewController;
    private ActionLineCellModel actionLineCellModel;
    @FXML
    private Label cellIDLabel;

    @FXML
    private Label lastUpdatedCellValueVersionLabel;

    @FXML
    private TextField originalValueTextField;

    @FXML
    private Button updateValueButton;

    private BooleanProperty fileNotLoaded;

    public ActionLineController() {
        fileNotLoaded = new SimpleBooleanProperty(true);
        actionLineCellModel = new CellModel();
    }

    @FXML
    public void initialize(){
        this.updateValueButton.disableProperty().bind(Bindings.or(Bindings.or(this.fileNotLoaded, originalValueTextField.textProperty().isEmpty()),
                this.cellIDLabel.textProperty().isEqualTo("Cell ID ")));
        actionLineCellModel.bind(this.cellIDLabel.textProperty(), this.lastUpdatedCellValueVersionLabel.textProperty());

        this.originalValueTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            this.originalValueTextField.setText(newValue);
        });
    }

    public void toggleFileLoadedProperty() {
        fileNotLoaded.setValue(false);
    }

    @FXML
    private void onUpdateValuePressed(ActionEvent event) {
            this.mainAppViewController.updateCellValue(this.actionLineCellModel.getCellIDProperty().getValue(), this.originalValueTextField.textProperty().get());
        }

    public void setTopSubComponentController(TopSubComponentController topSubComponentController) {
        this.topSubComponentController = topSubComponentController;
    }

    public void setMainAppController(MainAppViewController mainAppViewController){
        this.mainAppViewController = mainAppViewController;
    }


    public void showCellDetails(CellDTO cellDTO) {
        this.actionLineCellModel.getCellIDProperty().set(cellDTO.getCellId());
        this.originalValueTextField.setText(cellDTO.getOriginalValue());
        this.actionLineCellModel.getLastVersionProperty().set(String.valueOf(cellDTO.getVersion()));
    }

    public void resetCellModel() {
        this.actionLineCellModel.getCellIDProperty().set("");
        this.originalValueTextField.setText("");
        this.actionLineCellModel.getLastVersionProperty().set("");
    }

}