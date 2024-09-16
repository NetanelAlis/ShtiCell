package gui.main;

import dto.CellDTO;
import dto.SheetDTO;
import gui.Main;
import gui.action.line.ActionLineController;
import gui.cell.CellSubComponentController;
import gui.grid.GridBuilder;
import gui.grid.MainSheetController;
import gui.top.TopSubComponentController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.Engine;
import logic.EngineImpl;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class MainAppViewController {
    @FXML
    TopSubComponentController topSubComponentController;
    private MainSheetController mainSheetController;
    private ActionLineController actionLineController;
    private Map<String, CellSubComponentController> cellSubComponentControllers;

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
        for (CellSubComponentController cellController : this.cellSubComponentControllers.values()) {
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

    public void loadNewSheetFromXmlFile(String absolutePath) {
        try {
            this.engine.LoadDataFromXML(absolutePath);
            SheetDTO sheetDTO = this.engine.getSheetAsDTO();
            SheetDTO.LayoutDTO sheetDTOLayout = sheetDTO.getLayout();
            GridBuilder gridBuilder = new GridBuilder(sheetDTOLayout.getNumberOfRows(), sheetDTOLayout.getNumberOfColumns(),
                    sheetDTOLayout.getRowHeight(), sheetDTOLayout.getColumnWidth());

            BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
            root.setCenter(gridBuilder.build());
            this.setMainSheetController(gridBuilder.getController());
            this.setCellSubComponentController();
            this.mainSheetController.initializeGridModel(sheetDTO.getActiveCells());
            this.actionLineController.resetCellModel();
            this.actionLineController.toggleFileLoadedProperty();
            this.topSubComponentController.setSheetNameAndVersion(sheetDTO.getSheetName(), sheetDTO.getSheetVersion());
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }

    }

    public void showCellDetails(String cellID) {
        CellDTO cellDTO = this.engine.geCellAsDTO(cellID);

        this.actionLineController.showCellDetails(cellDTO);
        this.mainSheetController.showSelectedCellAndDependencies(cellDTO);
    }

    public void updateCellValue(String cellID, String cellOriginalValue) {
        try {
            if (this.engine.isSheetLoaded()) {
                this.engine.updateSingleCellData(cellID, cellOriginalValue);
                SheetDTO sheetDTO = this.engine.getSheetAsDTO();
                this.mainSheetController.updateGridModel(sheetDTO.getActiveCells());
                CellDTO cellDTO = this.engine.geCellAsDTO(cellID);
                this.actionLineController.showCellDetails(cellDTO);
                this.mainSheetController.showSelectedCellAndDependencies(cellDTO);
                this.topSubComponentController.setVersion(sheetDTO.getSheetVersion());
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }

    public int getSheetVersions() {
        return this.engine.isSheetLoaded() ? this.engine.getVersionsChangesAsDTO().getVersionChanges().size() : 0;
    }

    public void loadSheetVersion(int version) {
        SheetDTO sheetDTO = this.engine.getSheetVersionsAsDTO(version);
        GridBuilder gridBuilder = new GridBuilder(
                sheetDTO.getLayout().getNumberOfRows(),
                sheetDTO.getLayout().getNumberOfColumns(),
                sheetDTO.getLayout().getRowHeight(),
                sheetDTO.getLayout().getColumnWidth());
        this.openGridPopup(gridBuilder, version, sheetDTO.getSheetName());
        MainSheetController gridPopupController = gridBuilder.getController();
        gridPopupController.initializeGridModel(sheetDTO.getActiveCells());
        gridPopupController.getCellsControllers().forEach((cellID, cellController) -> {
            cellController.addOldVersionStyleClass();
        });
    }

    public void openGridPopup(GridBuilder gridBuilder, int version, String sheetName) {
        try {
            // Create a new Stage (pop-up window)
            Stage popupStage = new Stage();
            popupStage.setTitle(sheetName + " - version " + version);
            popupStage.getIcons().add(
                    new Image(Objects.requireNonNull(
                            Main.class.getResourceAsStream("/gui/style/imgs/ShtiCell-icon.png"))));

            ScrollPane popupGrid = gridBuilder.build();
            popupGrid.getStyleClass().add("grid-popup");
            Scene popupScene = new Scene(popupGrid, 600, 400); // Set preferred width and height
            popupStage.setScene(popupScene);

            // Set modality to make the pop-up window block other windows (optional)

            // Show the pop-up window
            popupStage.show();
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
    }
}
