package client.gui.editor.cell;

import client.gui.editor.main.view.MainEditorController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class CellSubComponentController {
    
    @FXML private Label cellComponent;
    
    private MainEditorController mainEditorController;
    private StringProperty cellID;
    private Color backgroundColor;
    private Color textColor;
    
    public CellSubComponentController() {
        this.backgroundColor = Color.WHITE;
        this.textColor = Color.BLACK;
    }
    
    @FXML
    private void initialize() {
        this.cellID = new SimpleStringProperty();
    }
    
    @FXML
    void onCellPressed(MouseEvent event) {
        if(mainEditorController != null) {
            this.mainEditorController.showCellDetails(this);
        }
    }
    
    public StringProperty cellIDProperty() {
        return this.cellID;
    }
    
    public StringProperty getCellValueProperty() {
        return this.cellComponent.textProperty();
    }
    
    public void setMainController(MainEditorController mainViewController) {
        this.mainEditorController = mainViewController;
    }
    
    public void deselect(String className) {
        this.cellComponent.getStyleClass().remove(className);
    }
    
    public void select(String className) {
        this.cellComponent.getStyleClass().add(className);
    }
    
    public void addOldVersionStyleClass() {
        this.cellComponent.getStyleClass().add("in-old-version");
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
    
    public void setCellStyle(Color backgroundColor, Color textColor) {
        this.cellComponent.setStyle(formatCellStyleString(backgroundColor, textColor));
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }
    
    private String formatCellStyleString(Color backgroundColor, Color textColor) {
        return String.format("%s: rgb(%d, %d, %d);\n%s: rgb(%d, %d, %d);",
                "-fx-background-color",
                (int) (backgroundColor.getRed() * 255),
                (int) (backgroundColor.getGreen() * 255),
                (int) (backgroundColor.getBlue() * 255),
                "-fx-text-fill",
                (int) (textColor.getRed() * 255),
                (int) (textColor.getGreen() * 255),
                (int) (textColor.getBlue() * 255));
    }
    
    public Color getBackgroundColor() { return this.backgroundColor; }
    
    public Color getTextColor() { return this.textColor; }
}
