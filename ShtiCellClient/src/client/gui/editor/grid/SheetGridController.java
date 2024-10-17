package client.gui.editor.grid;

import dto.cell.CellDTO;
import dto.cell.ColoredCellDTO;
import dto.range.RangeDTO;
import client.gui.editor.cell.CellModel;
import client.gui.editor.cell.CellSubComponentController;
import client.gui.editor.cell.DependenciesCellModel;
import client.gui.editor.main.view.MainEditorController;
import dto.returnable.EffectiveValueDTO;
import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static client.gui.editor.main.view.MainEditorController.effectiveValueFormatter;

public class SheetGridController {

    private GridModel gridModel;
    private MainEditorController mainEditorController;
    private DependenciesCellModel dependenciesCellModel = new CellModel();
    private Button selectedColumn;
    private Button selectedRow;
    private final List<Button> columnHeaders = new ArrayList<>();
    private final List<Button> rowHeaders = new ArrayList<>();
    private final Map<String, CellSubComponentController> cellsControllers = new HashMap<>();

    public void initialize() {
        this.rowHeaders.forEach((button -> {
            button.setOnAction(event -> {
                if (this.mainEditorController != null) {
                    this.mainEditorController.setSelectedRow(button.getText());
                    if (this.selectedRow != null) {
                        this.selectedRow.getStyleClass().remove("selected-row");
                    }
                    button.getStyleClass().add("selected-row");
                    this.selectedRow = button;
                }
            });
        }));
        
        this.columnHeaders.forEach((button -> {
            button.setOnAction(event -> {
                if (this.mainEditorController != null) {
                    this.mainEditorController.setSelectedColumn(button.getText());
                    if (this.selectedColumn != null) {
                        this.selectedColumn.getStyleClass().remove("selected-column");
                    }
                    button.getStyleClass().add("selected-column");
                    this.selectedColumn = button;
                }
            });
        }));
    }
    
    public void addColumnHeader(Button button) {
        this.columnHeaders.add(button);
    }
    
    public void addRowHeader(Button button) {
        this.rowHeaders.add(button);
    }
    
    public void addCellController(String cellID ,CellSubComponentController controller) {
        this.cellsControllers.put(cellID, controller);
    }
    
    public void setMainController(MainEditorController mainEditorController) {
        this.mainEditorController = mainEditorController;
    }
    
    public Map<String, CellSubComponentController> getCellsControllers() {
        return this.cellsControllers;
    }
    
    public void initializeGridModel(Map<String, EffectiveValueDTO> cells) {
        this.gridModel = new GridModel(cellsControllers);
        this.updateGridModel(cells);
    }
    
    public void updateGridModel(Map<String, EffectiveValueDTO> cells) {
        cells.forEach((cellID, returnable) -> {
            this.gridModel.getCellValueProperty(cellID).set(effectiveValueFormatter(returnable));
        });
    }
    
    public void initializePopupGridModel(Map<String, ColoredCellDTO> cells) {
        this.gridModel = new GridModel(cellsControllers);
        this.updatePopupGridModel(cells);
    }
    
    public void updatePopupGridModel(Map<String, ColoredCellDTO> cells) {
        cells.forEach((cellID, cell) -> {
            this.gridModel.getCellValueProperty(cellID).set(effectiveValueFormatter(cell.getEffectiveValue()));
        });
    }
    
    public void showSelectedCellAndDependencies(CellDTO cellDTO) {
        String previousSelected = this.dependenciesCellModel.getSelectedCellProperty().get();
        
        this.updateSelectedCell(cellDTO.getCellId(), previousSelected);
        this.updateDependingOn(cellDTO);
        this.updateInfluencingOn(cellDTO);
    }
    
    private void updateInfluencingOn(CellDTO cellDTO) {
        this.dependenciesCellModel.getInfluencingOn().forEach((currentInfluence) -> {
            this.cellsControllers.get(currentInfluence).deselect("influencing-cell");
        });
        
        if(cellDTO.getInfluencingOn() != null){
            cellDTO.getInfluencingOn().forEach((currentInfluence) -> {
                this.cellsControllers.get(currentInfluence).select("influencing-cell");
            });
            this.dependenciesCellModel.setInfluencingOn(cellDTO.getInfluencingOn());
        }
    }
    
    private void updateDependingOn(CellDTO cellDTO) {
        this.dependenciesCellModel.getDependingOn().forEach((currentDependency) -> {
            this.cellsControllers.get(currentDependency).deselect("depending-cell");
        });
        
        if(cellDTO.getDependingOn() != null){
            cellDTO.getDependingOn().forEach((currentDependingOn) -> {
                this.cellsControllers.get(currentDependingOn).select("depending-cell");
            });
            this.dependenciesCellModel.setDependingOn(cellDTO.getDependingOn());
        }
    }
    
    private void updateSelectedCell(String CellID, String previousSelected) {
        if (previousSelected != null) {
            this.cellsControllers.get(previousSelected).deselect("selected-cell");
        }
        this.cellsControllers.get(CellID).select("selected-cell");
        this.dependenciesCellModel.setSelectedCell(CellID);
    }
    
    public void toggleSelectedRange(RangeDTO selectedRange, RangeDTO previousSelectedRange) {
        if (previousSelectedRange != null) {
            previousSelectedRange.getCells().forEach((cellID) -> {
                this.cellsControllers.get(cellID).deselect("selected-range");
            });
        }
        
        if (selectedRange != null) {
            selectedRange.getCells().forEach((cellID) -> {
                this.cellsControllers.get(cellID).select("selected-range");
            });
        }
    }
    
    public boolean isAlreadySelected(String cellID) {
        return this.dependenciesCellModel.isSelectedCell(cellID);
    }
    
    public void resetCellModel(String selectedCellID) {
        this.cellsControllers.get(selectedCellID).deselect("selected-cell");
        this.dependenciesCellModel.setSelectedCell(null);
        
        this.dependenciesCellModel.getDependingOn().forEach((currentDependency) -> {
            this.cellsControllers.get(currentDependency).deselect("depending-cell");
        });
        
        this.dependenciesCellModel.getInfluencingOn().forEach((currentInfluence) -> {
            this.cellsControllers.get(currentInfluence).deselect("influencing-cell");
        });
    }
}
