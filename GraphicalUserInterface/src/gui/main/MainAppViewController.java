package gui.main;

import component.api.CellType;
import dto.CellDTO;
import dto.RangeDTO;
import dto.SheetDTO;
import gui.Main;
import gui.cell.CellSubComponentController;
import gui.commands.CommandController;
import gui.customization.CustomizationController;
import gui.file.upload.FileUploadController;
import gui.ranges.RangesController;
import gui.action.line.ActionLineController;
import gui.grid.GridBuilder;
import gui.grid.MainSheetController;
import gui.top.TopSubComponentController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Engine;
import logic.EngineImpl;
import logic.function.returnable.Returnable;
import gui.tasks.FileLoadingTask;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainAppViewController {
    @FXML
    private TopSubComponentController topSubComponentController;
    @FXML
    private RangesController rangesController;
    @FXML
    private CustomizationController customizationController;
    @FXML
    private CommandController commandController;
    private MainSheetController mainSheetController;
    private ActionLineController actionLineController;
    private Map<String, CellSubComponentController> cellSubComponentControllers;

    private BooleanProperty fileNotLoaded;
    private Stage primaryStage;
    private Engine engine;

    @FXML
    public void initialize() {
        this.engine = new EngineImpl();
        if (topSubComponentController != null) {
            this.topSubComponentController.setMainAppController(this);
            this.setActionLineController(topSubComponentController.getActionLineController());
        }
        if (rangesController != null) {
            this.rangesController.setMainAppController(this);
        }
        if (customizationController != null) {
            this.customizationController.setMainAppController(this);
        }
        if(commandController != null) {
            this.commandController.setMainController(this);
        }

        this.rangesController.bindIsFileLoaded(this.fileNotLoaded);
        this.actionLineController.bindIsFileLoaded(this.fileNotLoaded);
        this.customizationController.bindIsFileLoaded(this.fileNotLoaded);
        this.commandController.bindIsFileLoaded(this.fileNotLoaded);

    }

    public MainAppViewController(){
        this.fileNotLoaded = new SimpleBooleanProperty(true);

    }

    private void setMainSheetController(MainSheetController mainSheetController) {
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
        FileUploadController fileUploadController = this.openFileUploadWindow();
        Task<Boolean> fileLoadingTask = new FileLoadingTask(absolutePath, this.engine);

       this.bindFileLoadingTaskToUIComponents(fileUploadController,fileLoadingTask);
        new Thread(fileLoadingTask).start();
    }


    private void bindFileLoadingTaskToUIComponents(FileUploadController fileUploadController, Task<Boolean> fileLoadingTask) {
        fileUploadController.bindProgressComponents(fileLoadingTask, this::initializeSheetLayoutAndControllers);
    }

    private void initializeSheetLayoutAndControllers() {
        try{
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
            this.fileNotLoaded.setValue(false);
            this.rangesController.resetController();
            this.customizationController.resetController();
            this.commandController.resetController();
            this.topSubComponentController.setSheetNameAndVersion(sheetDTO.getSheetName(), sheetDTO.getSheetVersion());
            this.engine.getAllRangesAsDTO().getRanges().forEach(rangeDTO -> {this.rangesController.showRange(rangeDTO);});
            this.rangesController.setRangesAreEmptyProperty();

        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
    }


    public FileUploadController openFileUploadWindow() {
        FileUploadController fileUploadController = null;
        try {
            // Load the FileUploadController and FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/file/upload/fileUploadComponent.fxml"));
            Parent root = loader.load();

            fileUploadController = loader.getController();

            // Set the scene and content
            Stage popUpStage = new Stage();
            Scene scene = new Scene(root);
            popUpStage.setScene(scene);
            fileUploadController.setStage(popUpStage);

            // Make the window modal (blocks interactions with the main window)
            popUpStage.initModality(Modality.APPLICATION_MODAL);

            // Show the window
            popUpStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileUploadController;
    }

    public void showCellDetails(String cellID) {
        CellDTO cellDTO = this.engine.geCellAsDTO(cellID);

        this.actionLineController.showCellDetails(cellDTO);
        this.mainSheetController.showSelectedCellAndDependencies(cellDTO);
        this.customizationController.setCellIDProperty(cellDTO);
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
        this.initSheetForPopUpWindow(gridBuilder, sheetDTO);
    }

    public void showUnTouchableSheetInNewPopUp(SheetDTO sheetDTO) {
        GridBuilder gridBuilder = new GridBuilder(
                sheetDTO.getLayout().getNumberOfRows(),
                sheetDTO.getLayout().getNumberOfColumns(),
                sheetDTO.getLayout().getRowHeight(),
                sheetDTO.getLayout().getColumnWidth());
        this.openGridPopup(gridBuilder, sheetDTO.getSheetVersion(), sheetDTO.getSheetName());
        this.initSheetForPopUpWindow(gridBuilder, sheetDTO);

    }

    public void openGridPopup(GridBuilder gridBuilder, int version, String sheetName) {
        try {
            // Create a new Stage (pop-up window)
            Stage popupStage = new Stage();
                popupStage.setTitle(sheetName + " - version " + version);

            popupStage.getIcons().add(
                    new Image(Objects.requireNonNull(
                            Main.class.getResourceAsStream("/gui/ShtiCell-icon.png"))));

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

    private void initSheetForPopUpWindow(GridBuilder gridBuilder, SheetDTO sheetDTO){
        MainSheetController gridPopupController = gridBuilder.getController();
        gridPopupController.initializeGridModel(sheetDTO.getActiveCells());
        gridPopupController.getCellsControllers().forEach((cellID, cellController) -> {
            cellController.addOldVersionStyleClass();
        });

    }


    public void addRange(String rangeName, String from, String to) {
        try {
            this.engine.addRange(rangeName, from + ".." + to);
            this.rangesController.showRange(this.engine.getRangesAsDTO(rangeName));
        }catch (RuntimeException e){
            this.rangesController.setErrorMessageToSaveButton(e.getMessage());
        }

    }


    public void deleteRange(RangeDTO selectedRange) {
        try{
        this.engine.deleteRange(selectedRange.getName());
        this.rangesController.unShowRange(selectedRange);
        }catch (RuntimeException e){
            this.rangesController.setErrorMessageToDeleteButton(e.getMessage());
        }

    }

    public void showSelectedRange(RangeDTO newValue, RangeDTO oldValue) {
        this.mainSheetController.showSelectedRange(newValue, oldValue);
    }

    public void updateColWidth(Integer newColWidth, int colIndex){
        GridPane gridPane = getGridPane();
        gridPane.getColumnConstraints().get(colIndex).setMinWidth(newColWidth);
        gridPane.getColumnConstraints().get(colIndex).setMaxWidth(newColWidth);
        gridPane.getColumnConstraints().get(colIndex).setPrefWidth(newColWidth);

    }

    public void updateRowHeight(Integer newRowHeight, int rowIndex){
        GridPane gridPane = getGridPane();
        gridPane.getRowConstraints().get(rowIndex).setMinHeight(newRowHeight);
        gridPane.getRowConstraints().get(rowIndex).setMaxHeight(newRowHeight);
        gridPane.getRowConstraints().get(rowIndex).setPrefHeight(newRowHeight);
    }

    private GridPane getGridPane(){
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
        ScrollPane scrollPane = (ScrollPane) root.getCenter();
       return (GridPane) scrollPane.getContent();
    }
    
    public void setSelectedColumn(String columnName) {
        GridPane gridPane = this.getGridPane();
        int colWidth = (int)gridPane.getColumnConstraints().get(columnName.charAt(0) - 'A' + 1).getPrefWidth();
        this.customizationController.showColumn(columnName, colWidth);
    }

    public void setSelectedRow(String rowName) {
        GridPane gridPane = this.getGridPane();
        int rowHeight = (int)gridPane.getRowConstraints().get(Integer.parseInt(rowName)).getPrefHeight();
        this.customizationController.showRow(rowName,rowHeight);
    }

    public void setColTextAlignment(String colIndex, String alignment) {
        this.cellSubComponentControllers.forEach((cellID, cellController) -> {
            if(cellID.contains(colIndex)) {
                cellController.setAlignment(alignment);
            }
        });
    }

    public void setCellDesign(Color backgroundColor, String cellID, Color textColor) {
        this.engine.updateBackgroundColor(backgroundColor,cellID);
        this.engine.updateTextColor(textColor, cellID);
        this.cellSubComponentControllers.get(cellID).updateCellDesign(backgroundColor,textColor);
    }

    public boolean tryToSortRange(String from, String to, List<String> columnsToSortBy) {
        try{
            SheetDTO sortedSheetByRange = this.engine.sortRangeCells(from + ".." + to,columnsToSortBy);
            this.showUnTouchableSheetInNewPopUp(sortedSheetByRange);
        }catch (ClassCastException e){
            this.commandController.setErrorMessageToSortButton("The column to sort by should contain numbers only");
            return false;
        }catch (RuntimeException e){
            this.commandController.setErrorMessageToSortButton(e.getMessage());
            return false;
        }

        return true;

    }

    public List<String> getColumnsToFilterBy(String range) {
        return this.engine.getColumnsToSortBy(range);
    }

    public List<Returnable> getItemsToFilterBy(String colToFilterBy,String range) {
        return this.engine.getItemsToFilterBy(colToFilterBy,range);
    }

    public void filterSheet(String range,List<Integer> itemsToFilterByIndexes, String colToSortBy) {
        try{
            SheetDTO filteredSheetDTO = this.engine.getFilterSheet(range, itemsToFilterByIndexes, colToSortBy);
            this.showUnTouchableSheetInNewPopUp(filteredSheetDTO);
        }catch (IllegalArgumentException e){
            this.commandController.setErrorMessageToFilterButton(e.getMessage());
        }

    }

    public static String effectiveValueFormatter(Returnable effectiveValue){
        CellType type = effectiveValue.getCellType();
        String valueToPrint = effectiveValue.getValue().toString();
        if (type.equals(CellType.BOOLEAN)) {
            valueToPrint = booleanFormatter(valueToPrint);
        } else if (type.equals(CellType.NUMERIC)) {
            valueToPrint = numberFormatter(valueToPrint);
        }

        return valueToPrint;
    }

    private static String numberFormatter(String valueToPrint) {
        try{
            double  number = Double.parseDouble(valueToPrint);
            DecimalFormat formatter = new DecimalFormat("#,###.##");
            formatter.setRoundingMode(RoundingMode.DOWN);
            return formatter.format(number);
        } catch (Exception ignored) {
            return valueToPrint;
        }
    }

    public static String booleanFormatter(String valueToPrint) {
        return valueToPrint.toUpperCase();
    }
}
