package gui.cell;

import gui.main.MainAppViewController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class CellSubComponentController {

    @FXML
    private Label cellComponent;
    private MainAppViewController mainAppViewController;
    private StringProperty cellID;

    public CellSubComponentController() {
        this.cellID = new SimpleStringProperty();
    }

    public StringProperty getCellValueProperty() {
        return this.cellComponent.textProperty();
    }

    public void setMainAppController(MainAppViewController mainAppController) {
        this.mainAppViewController = mainAppController;
    }

    @FXML
    public void onCellPressed(MouseEvent mouseEvent) {
        this.mainAppViewController.showCellDetails(this.cellID.get());
    }

    public StringProperty getCellIDProperty() {
        return this.cellID;
    }

    public void deselect(String className) {
        this.cellComponent.getStyleClass().remove(className);
    }

    public void select(String className) {
        this.cellComponent.getStyleClass().add(className);

    }

    public void addOldVersionStyleClass() {
        this.cellComponent.getStyleClass().add("old-version");
    }


    public void setAlignment(String alignment) {
        switch (alignment) {
            case "left":
                this.cellComponent.setAlignment(Pos.CENTER_LEFT);
                break;
            case "right":
                this.cellComponent.setAlignment(Pos.CENTER_RIGHT);
                break;
            case "center":
                this.cellComponent.setAlignment(Pos.CENTER);
                break;
            default:
                break;
        }
    }

    public void updateCellDesign(Color backgroundColor, Color textColor) {
        String colorStyle = String.format("-fx-background-color: rgb(%d, %d, %d);\n-fx-text-fill: rgb(%d, %d, %d);",
                (int) (backgroundColor.getRed() * 255),
                (int) (backgroundColor.getGreen() * 255),
                (int) (backgroundColor.getBlue() * 255),
                (int) (textColor.getRed() * 255),
                (int) (textColor.getGreen() * 255),
                (int) (textColor.getBlue() * 255));
        this.cellComponent.setStyle(colorStyle);
    }

}