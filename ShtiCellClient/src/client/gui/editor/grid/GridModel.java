package client.gui.editor.grid;

import client.gui.editor.cell.CellSubComponentController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Map;

public class GridModel {
    
    private final Map<String, StringProperty> cellsProperties;
    
    public GridModel(Map<String, CellSubComponentController> cellsControllers) {
        this.cellsProperties = new HashMap<>();
        
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
