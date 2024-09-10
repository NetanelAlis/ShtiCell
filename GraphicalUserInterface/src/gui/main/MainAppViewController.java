package gui.main;

import dto.SheetDTO;
import gui.action.line.ActionLineController;
import gui.cell.CellSubComponentController;
import gui.grid.GridBuilder;
import gui.grid.MainSheetController;
import gui.top.TopSubComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.Engine;
import logic.EngineImpl;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MainAppViewController {
    @FXML
    TopSubComponentController topSubComponentController;
    private MainSheetController mainSheetController;
    private ActionLineController actionLineController;
    private Map<String,CellSubComponentController> cellSubComponentControllers;

    private Stage primaryStage;
    private Engine engine;

    @FXML
    public void initialize() {
        this.engine = new EngineImpl();
        if (topSubComponentController != null) {
            this.topSubComponentController.setMainAppController(this);
            this.setActionLineController(topSubComponentController.getActionLineController());
        }

    }

    public void setMainSheetController(MainSheetController mainSheetController) {
        this.mainSheetController = mainSheetController;
        this.mainSheetController.setMainAppController(this);
    }

    public void setCellSubComponentController() {
        this.cellSubComponentControllers = this.mainSheetController.getCellsControllers();
        for(CellSubComponentController cellController: this.cellSubComponentControllers.values()) {
            cellController.setMainAppController(this);
        }
    }

    public void setActionLineController(ActionLineController actionLineController) {
        this.actionLineController = actionLineController;
        this.actionLineController.setMainAppController(this);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public File getFilePath(FileChooser fileChooser) {
        return fileChooser.showOpenDialog(primaryStage);
    }

    public void loadNewSheetFromXmlFile(String absolutePath)  {
        try{
            this.engine.LoadDataFromXML(absolutePath);
            SheetDTO sheetDTO = this.engine.getSheetAsDTO();
            SheetDTO.LayoutDTO sheetDTOLayout = sheetDTO.getLayout();
            GridBuilder gridBuilder = new GridBuilder(sheetDTOLayout.getNumberOfRows(), sheetDTOLayout.getNumberOfColumns(),
                    sheetDTOLayout.getRowHeight(), sheetDTOLayout.getColumnWidth());

            BorderPane root = (BorderPane)primaryStage.getScene().getRoot();
            root.setCenter(gridBuilder.build());
            this.setMainSheetController(gridBuilder.getConroller());
            this.setCellSubComponentController();
            this.mainSheetController.initAndBindCellsTextProperies(sheetDTO.getActiveCells());
            this.actionLineController.toggleFileLoadedProperty();
            this.topSubComponentController.setSheetNameAndVersion(sheetDTO.getSheetName(), sheetDTO.getSheetVersion());
        }
        catch(RuntimeException | IOException e){
            e.printStackTrace();
        }

    }

    public void cellPressed(CellSubComponentController cellSubComponentController) {
        String cellID = null;

        for (Map.Entry<String, CellSubComponentController> entry : this.cellSubComponentControllers.entrySet()) {
            if (entry.getValue() == cellSubComponentController) {
                cellID = entry.getKey();
                break;
            }
        }

        this.actionLineController.updateCellIDLabel(cellID);
    }
}
