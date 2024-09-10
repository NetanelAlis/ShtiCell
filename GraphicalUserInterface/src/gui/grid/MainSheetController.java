package gui.grid;

import gui.cell.CellSubComponentController;
import gui.main.MainAppViewController;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import logic.function.returnable.Returnable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainSheetController {
    private MainAppViewController mainAppViewController;
    private List<Button> columnsHeaders = new ArrayList<Button>();
    private List<Button> rowsHeaders = new ArrayList<Button>();
    private Map<String, CellSubComponentController> cellControllers = new HashMap<String, CellSubComponentController>();
    private GridModel gridModel;

    public void setMainAppController(MainAppViewController mainAppViewController) {
        this.mainAppViewController = mainAppViewController;
    }

    public void addColumnHeader(Button button) {
        this.columnsHeaders.add(button);
    }

    public void addRowHeader(Button button) {
        this.rowsHeaders.add(button);
    }

    public void addCellController(String cellID, CellSubComponentController cellSubComponentController) {
        this.cellControllers.put(cellID, cellSubComponentController);
    }

    public Map<String, CellSubComponentController> getCellsControllers() {
        return this.cellControllers;
    }

    public void initializeGridModel(Map<String,Returnable> sheetDTOCells){
        this.gridModel = new GridModel(this.cellControllers);
        this.updateGridModel(sheetDTOCells);
    }

    public void updateGridModel(Map<String,Returnable> sheetDTOCells){
        sheetDTOCells.forEach((cellID, cell) -> {
            StringProperty cellTextProperty = this.gridModel.getCellTextProperty(cellID);
            cellTextProperty.setValue(cell.getValue().toString());
        });
    }
}
