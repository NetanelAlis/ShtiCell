package gui.cell;

import gui.main.MainAppViewController;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class CellSubComponentController {

    @FXML
    private Label cellComponent;
    private MainAppViewController mainAppViewController;

    public StringProperty getCellValueProperty(){
        return this.cellComponent.textProperty();
    }

    public void setMainAppController(MainAppViewController mainAppController){
        this.mainAppViewController = mainAppController;
    }
    @FXML
    public void onCellPressed(MouseEvent mouseEvent) {
        this.mainAppViewController.cellPressed(this);
    }

}