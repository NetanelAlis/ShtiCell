package gui.grid;

import dto.CellDTO;
import gui.cell.CellModel;
import gui.cell.CellSubComponentController;
import gui.cell.DependenciesCellModel;
import gui.main.MainAppViewController;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import logic.function.returnable.Returnable;
import java.util.Optional;

import java.util.*;

public class MainSheetController {
    private MainAppViewController mainAppViewController;
    private List<Button> columnsHeaders = new ArrayList<Button>();
    private List<Button> rowsHeaders = new ArrayList<Button>();
    private Map<String, CellSubComponentController> cellControllers = new HashMap<String, CellSubComponentController>();
    private GridModel gridModel;
    private DependenciesCellModel cellModel = new CellModel();

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



    public void showSelectedCellAndDependencies(CellDTO cellDTO) {
        if(this.cellModel.getSelectedCellID() != null) {
            this.cellControllers.get(this.cellModel.getSelectedCellID())
                    .deselect("selected-cell");
        }
        this.cellModel.getDependsOnPropertyList().forEach((dependsOnID) -> {
                this.cellControllers.get(dependsOnID)
                        .deselect("depending-cell");
            });
        this.cellModel.getInfluencingOnPropertyList().forEach((influencingOnCellID) -> {
            this.cellControllers.get(influencingOnCellID)
                    .deselect("influencing-cell");
        });

        this.cellControllers.get(cellDTO.getCellId())
                .select("selected-cell");
        if(cellDTO.getDependsOn() != null){
            cellDTO.getDependsOn().forEach((dependsOnID) -> {
                this.cellControllers.get(dependsOnID)
                        .select("depending-cell");
            });

            this.cellModel.setDependingOn(cellDTO.getDependsOn());
        }
        if(cellDTO.getInfluencingOn() != null){
            cellDTO.getInfluencingOn().forEach((influencingOnCellID) -> {
                this.cellControllers.get(influencingOnCellID)
                        .select("influencing-cell");
            });

            this.cellModel.setInfluencingOn(cellDTO.getInfluencingOn());
        }

        this.cellModel.setSelectedCell(cellDTO.getCellId());
    }
}

