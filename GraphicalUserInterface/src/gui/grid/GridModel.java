package gui.grid;

import gui.cell.CellSubComponentController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.HashMap;
import java.util.Map;

public class GridModel {

    private Map<String, StringProperty> cellProperties = new HashMap<>();

    public GridModel(Map<String, CellSubComponentController> cells){

        cells.forEach((cellID,cellSubComponentController)->{
            StringProperty currentCellProperty = new SimpleStringProperty();
            this.cellProperties.put(cellID,currentCellProperty);
            cellSubComponentController.getCellValueProperty().bind(currentCellProperty);
        });
    }

    public StringProperty getCellTextProperty(String cellID){// add Exception?
        return this.cellProperties.get(cellID);
    }
}
