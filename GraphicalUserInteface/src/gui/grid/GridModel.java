package gui.grid;

import gui.cell.CellSubComponentController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class GridModel {
    
    private final Map<String, StringProperty> cellsProperties;
    private final Map<String, Color> backgroundColors;
    private final Map<String, Color> textColors;
    
    public GridModel(Map<String, CellSubComponentController> cellsControllers) {
        this.cellsProperties = new HashMap<>();
        this.backgroundColors = new HashMap<>();
        this.textColors = new HashMap<>();
        
        cellsControllers.forEach((cellID, cellController) -> {
            StringProperty currentCellProperty = new SimpleStringProperty();
            this.cellsProperties.put(cellID, currentCellProperty);
            cellController.getCellValueProperty().bind(currentCellProperty);
        });
    }
    
    public StringProperty getCellValueProperty(String cellID) {
        return this.cellsProperties.get(cellID);
    }
}
