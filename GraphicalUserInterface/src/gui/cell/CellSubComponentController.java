package gui.cell;

import gui.main.MainAppViewController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class CellSubComponentController {

    @FXML
    private Label cellComponent;
    private MainAppViewController mainAppViewController;
    private StringProperty cellID;

    public CellSubComponentController() {
        this.cellID = new SimpleStringProperty();
    }

    public StringProperty getCellValueProperty(){
        return this.cellComponent.textProperty();
    }

    public void setMainAppController(MainAppViewController mainAppController){
        this.mainAppViewController = mainAppController;
    }
    @FXML
    public void onCellPressed(MouseEvent mouseEvent) {
        this.mainAppViewController.showCellDetails(this.cellID.get());
    }

    public StringProperty getCellIDProperty(){
        return this.cellID;
    }

    public void deselect(String className){
        this.cellComponent.getStyleClass().remove(className);
    }

    public void select(String className){
        this.cellComponent.getStyleClass().add(className);

    }



}