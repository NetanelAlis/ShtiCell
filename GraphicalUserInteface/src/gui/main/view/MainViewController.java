package gui.main.view;

import component.cell.api.CellType;
import dto.*;
import gui.Main;
import gui.action.line.ActionLineController;
import gui.cell.CellSubComponentController;
import gui.command.CommandsController;
import gui.customization.CustomizationController;
import gui.exception.ExceptionWindowController;
import gui.file.upload.FileUploadController;
import gui.graph.GraphType;
import gui.grid.GridBuilder;
import gui.grid.SheetGridController;
import gui.ranges.RangesController;
import gui.top.TopSubComponentController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.engine.Engine;
import logic.engine.EngineImpl;
import logic.function.returnable.api.Returnable;
import tasks.FileLoadingTask;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class MainViewController {

    @FXML
    private TopSubComponentController topSubComponentController;
    @FXML
    private CustomizationController customizationsController;
    @FXML
    private CommandsController commandsController;
    @FXML
    private RangesController rangesController;
    private ActionLineController actionLineController;
    private SheetGridController sheetGridController;
    private Map<String, CellSubComponentController> cellSubComponentControllerMap;

    private BooleanProperty fileNotLoadedProperty;
    private Engine engine;
    private Stage primaryStage;

    public MainViewController() {
        this.fileNotLoadedProperty = new SimpleBooleanProperty(true);
    }

    @FXML
    public void initialize() {
        this.engine = new EngineImpl();

        if (this.topSubComponentController != null) {
            this.topSubComponentController.setMainController(this);
            this.setActionLineController(this.topSubComponentController.getActionLIneController());
        }

        if (this.rangesController != null) {
            this.rangesController.setMainController(this);
        }

        if (this.customizationsController != null) {
            this.customizationsController.setMainController(this);
        }

        if (this.commandsController != null) {
            this.commandsController.setMainController(this);
        }

        this.actionLineController.bindFileNotLoaded(this.fileNotLoadedProperty);
        this.rangesController.bindFileNotLoaded(this.fileNotLoadedProperty);
        this.customizationsController.bindFileNotLoaded(this.fileNotLoadedProperty);
        this.commandsController.bindFileNotLoaded(this.fileNotLoadedProperty);
        this.topSubComponentController.bindFileNotLoaded(this.fileNotLoadedProperty);
    }

    public void setActionLineController(ActionLineController actionLineController) {
        this.actionLineController = actionLineController;
        this.actionLineController.setMainController(this);
    }

    public void setSheetGridController(SheetGridController sheetGridController) {
        this.sheetGridController = sheetGridController;
        this.sheetGridController.setMainController(this);
    }

    public void setCellSubComponentControllerMap(
            Map<String, CellSubComponentController> cellSubComponentControllerMap) {

        this.cellSubComponentControllerMap = cellSubComponentControllerMap;
        this.cellSubComponentControllerMap.forEach((cellID, cellController) -> {
            cellController.setMainController(this);
        });
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public File getFilePath(FileChooser fileChooser) {
        return fileChooser.showOpenDialog(this.primaryStage);
    }

    public void loadNewSheetFromXML(String absolutePath) {
        FileUploadController fileUploadController = this.openFileUploadWindow();
        Task<Boolean> fileLoadingTask = new FileLoadingTask(absolutePath, this.engine);

        this.bindFileLoadingTaskToUIComponents(fileUploadController, fileLoadingTask);


        fileLoadingTask.setOnFailed(event -> {
            Platform.runLater(() -> {
                fileUploadController.onTaskFinished(Optional.empty());
                openExceptionPopup(fileLoadingTask.getException().getMessage());
            });
        });
        new Thread(fileLoadingTask).start();
    }

    private void bindFileLoadingTaskToUIComponents(FileUploadController fileUploadController, Task<Boolean> fileLoadingTask) {
        fileUploadController.bindProgressComponents(fileLoadingTask, this::initializeSheetLayoutAndControllers);
    }

    private void initializeSheetLayoutAndControllers() {
        try {
            SheetDTO sheetDTO = this.engine.getSheetAsDTO();
            RangesDTO rangesDto = this.engine.getAllRanges();
            GridBuilder gridBuilder = new GridBuilder(sheetDTO.getLayout().getRow(),
                    sheetDTO.getLayout().getColumn(),
                    sheetDTO.getLayout().getRowHeight(),
                    sheetDTO.getLayout().getColumnWidth());

            BorderPane root = (BorderPane) this.primaryStage.getScene().getRoot();
            root.setCenter(gridBuilder.build());
            this.setSheetGridController(gridBuilder.getSheetGridController());
            this.setCellSubComponentControllerMap(this.sheetGridController.getCellsControllers());
            this.sheetGridController.initializeGridModel(sheetDTO.getCells());
            this.rangesController.initializeRangesModel(rangesDto);
            this.fileNotLoadedProperty.set(false);
            this.actionLineController.resetCellModel();
            this.rangesController.resetController();
            this.customizationsController.resetController();
            this.commandsController.resetController();
            this.topSubComponentController.setSheetNameAndVersion(sheetDTO.getSheetName(), sheetDTO.getVersion());

        } catch (RuntimeException | IOException e) {
            this.openExceptionPopup(e.getMessage());
        }
    }


    public void showCellDetails(CellSubComponentController cellSubComponentController) {
        String selectedCellID = cellSubComponentController.cellIDProperty().get();
        if (this.sheetGridController.isAlreadySelected(selectedCellID)) {
            this.sheetGridController.resetCellModel(selectedCellID);
            this.actionLineController.resetCellModel();
            this.customizationsController.deselectCell();
        } else {
            CellDTO cellDTO = this.engine.getSingleCellData(cellSubComponentController.cellIDProperty().get());
            this.actionLineController.showCellDetails(cellDTO);
            this.sheetGridController.showSelectedCellAndDependencies(cellDTO);
            this.customizationsController.setSelectedCell(cellDTO);
        }
    }

    public void updateCellValue(String cellToUpdate, String newValue) {
        try {
            this.engine.updateSingleCellData(cellToUpdate, newValue);
            SheetDTO sheetDTO = this.engine.getSheetAsDTO();
            CellDTO cellDTO = this.engine.getSingleCellData(cellToUpdate);
            this.sheetGridController.updateGridModel(sheetDTO.getCells());
            this.actionLineController.showCellDetails(cellDTO);
            this.sheetGridController.showSelectedCellAndDependencies(cellDTO);
            this.topSubComponentController.updateSheetVersion(sheetDTO.getVersion());
        } catch (RuntimeException e) {
            this.openExceptionPopup(e.getMessage());
        }
    }

    public int getSheetVersions() {
        return this.engine.isSheetLoaded() ? this.engine.showVersions().getVersionChanges().size() : 0;
    }

    public void loadSheetVersion(int version) {
        ColoredSheetDTO sheetDTO = this.engine.getSheetVersionAsDTO(version);
        createReadonlyGrid(sheetDTO, " - version " + version);
    }

    public void openGridPopup(GridBuilder gridBuilder, String title, String sheetName) {
        try {
            // Create a new Stage (pop-up window)
            Stage popupStage = new Stage();
            popupStage.setTitle(sheetName + title);

            ScrollPane popupGrid = gridBuilder.build();
            popupGrid.getStyleClass().add("grid-popup");

            // Create a new scene for the pop-up window
            Scene popupScene = new Scene(popupGrid, 600, 400); // Set preferred width and height
            popupStage.setScene(popupScene);
            popupStage.getIcons().add(
                    new Image(Objects.requireNonNull(
                            Main.class.getResourceAsStream("/gui/main/view/style/shticellLogo.png"))));

            // Show the pop-up window
            popupStage.show();
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
    }

    private FileUploadController openFileUploadWindow() {
        FileUploadController fileUploadController = null;
        try {
            // Load the FileUploadController and FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/file/upload/FileUploadComponent.fxml"));
            Parent root = loader.load();

            fileUploadController = loader.getController();

            // Set the scene and content
            Stage popUpStage = new Stage();
            Scene scene = new Scene(root);
            popUpStage.setScene(scene);
            fileUploadController.setStage(popUpStage);
            popUpStage.getIcons().add(
                    new Image(Objects.requireNonNull(
                            Main.class.getResourceAsStream("/gui/main/view/style/shticellLogo.png"))));
            // Make the window modal (blocks interactions with the main window)
            popUpStage.initModality(Modality.APPLICATION_MODAL);

            // Show the window
            popUpStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileUploadController;
    }

    private void openExceptionPopup(String errorMessage) {
        // Load the FXML for the exception window
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/exception/ExceptionWindow.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the error message to the label
            ExceptionWindowController controller = loader.getController();
            controller.setMessage(errorMessage); // Set the error message dynamically

            // Create a new stage (window) for the popup
            Stage popUpStage = new Stage();
            popUpStage.setTitle("Error");
            popUpStage.getIcons().add(
                    new Image(Objects.requireNonNull(
                            Main.class.getResourceAsStream("/gui/main/view/style/shticellLogo.png"))));
            popUpStage.setScene(new Scene(root));
            popUpStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
            Button closeButton = controller.getCloseButton();
            closeButton.setOnAction(event -> popUpStage.close());
            popUpStage.showAndWait(); // Show the pop-up window and wait for it to be closed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public RangeDTO addNewRange(String rangeName, String from, String to) {
        try {
            return this.engine.addRange(rangeName, from + ".." + to);
        } catch (RuntimeException e) {
            this.rangesController.updateSaveErrorLabel(e.getMessage());
            return null;
        }
    }

    public boolean deleteRange(RangeDTO rangeToDelete) {
        try {
            this.engine.removeRange(rangeToDelete.getName());
            return true;
        } catch (RuntimeException e) {
            this.rangesController.updateDeleteErrorLabel(e.getMessage());
            return false;
        }
    }

    public void toggleSelectedRange(RangeDTO selectedRange, RangeDTO previousSelectedRange) {
        this.sheetGridController.toggleSelectedRange(selectedRange, previousSelectedRange);
    }

    public void updateColumnWidth(Integer newColumnWidth, int columnToUpdate) {
        GridPane gridPane = this.getSheetGrid();
        gridPane.getColumnConstraints().get(columnToUpdate).setMinWidth(newColumnWidth);
        gridPane.getColumnConstraints().get(columnToUpdate).setPrefWidth(newColumnWidth);
        gridPane.getColumnConstraints().get(columnToUpdate).setMaxWidth(newColumnWidth);

    }

    public void updateRowHeight(Integer newRowHeight, int rowToUpdate) {
        GridPane gridPane = this.getSheetGrid();
        gridPane.getRowConstraints().get(rowToUpdate).setMinHeight(newRowHeight);
        gridPane.getRowConstraints().get(rowToUpdate).setPrefHeight(newRowHeight);
        gridPane.getRowConstraints().get(rowToUpdate).setMaxHeight(newRowHeight);
    }

    private GridPane getSheetGrid() {
        BorderPane root = (BorderPane) this.primaryStage.getScene().getRoot();
        ScrollPane scrollPane = (ScrollPane) root.getCenter();
        return (GridPane) scrollPane.getContent();
    }


    public void setSelectedColumn(String columnName) {
        int columnIndex = (columnName.charAt(0) - 'A') + 1;
        int currentPrefWidth = (int) this.getSheetGrid().getColumnConstraints().get(columnIndex).getPrefWidth();
        this.customizationsController.setSelectedColumn(columnName, currentPrefWidth);
    }

    public void setSelectedRow(String rowName) {
        int rowIndex = Integer.parseInt(rowName);
        int currentPrefHeight = (int) this.getSheetGrid().getRowConstraints().get(rowIndex).getPrefHeight();
        this.customizationsController.setSelectedRow(rowIndex, currentPrefHeight);
    }

    public void setColumnTextAlignment(String columnName, String alignment) {
        this.cellSubComponentControllerMap.forEach((cellID, cellController) -> {
            if (cellID.contains(columnName)) {
                cellController.setAlignment(alignment);
            }
        });
    }

    public void setCellStyle(String cellID, Color backgroundColor, Color textColor) {
        this.cellSubComponentControllerMap.get(cellID).setCellStyle(backgroundColor, textColor);
        this.engine.updateCellStyle(cellID, backgroundColor, textColor);
    }

    public boolean sortRange(String rangeToSort, List<String> columnsToSortBy) {
        try {
            ColoredSheetDTO sortedSheet = this.engine.sortRangeOfCells(rangeToSort, columnsToSortBy);
            createReadonlyGrid(sortedSheet, " - Sorted");
            return true;
        } catch (ClassCastException e) {
            this.commandsController.updateSortErrorLabel("Cannot sort by non-numeric column");
            return false;
        } catch (RuntimeException e) {
            this.commandsController.updateSortErrorLabel(e.getMessage());
            return false;
        }
    }

    public boolean filterRange(String rangeToFilterBy, String columnToFilterBy, List<Integer> itemsToFilterBy) {
        try {
            ColoredSheetDTO filteredSheet = this.engine.filterRangeOfCells(rangeToFilterBy, columnToFilterBy, itemsToFilterBy);
            createReadonlyGrid(filteredSheet, " - Filtered");
            return true;
        } catch (RuntimeException e) {
            this.commandsController.updateFilterErrorLabel(e.getMessage());
            return false;
        }

    }

    private void createReadonlyGrid(ColoredSheetDTO sheetToShow, String popupName) {
        GridBuilder gridBuilder = new GridBuilder(
                sheetToShow.getLayout().getRow(),
                sheetToShow.getLayout().getColumn(),
                sheetToShow.getLayout().getRowHeight(),
                sheetToShow.getLayout().getColumnWidth());

        this.openGridPopup(gridBuilder, popupName, sheetToShow.getSheetName());
        SheetGridController gridPopupController = gridBuilder.getSheetGridController();
        gridPopupController.initializePopupGridModel(sheetToShow.getCells());

        gridPopupController.getCellsControllers().forEach((cellID, cellController) -> {
            cellController.addOldVersionStyleClass();

            ColoredCellDTO currentCell = sheetToShow.getCells().get(cellID);
            if (currentCell != null) {
                cellController.setCellStyle(currentCell.getBackgroundColor(), currentCell.getTextColor());
            }

        });
    }

    public List<String> getColumnsOfRange(String rangeToFilter) {
        try {
            return this.engine.getColumnsListOfRange(rangeToFilter);
        } catch (RuntimeException e) {
            this.commandsController.updateFilterErrorLabel(e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Returnable> getUniqueItems(String columnToFilterBy, String rangeToFilter) {
        try {
            return this.engine.getUniqueItemsToFilterBy(columnToFilterBy, rangeToFilter);
        } catch (RuntimeException e) {
            this.commandsController.updateFilterErrorLabel(e.getMessage());
        }

        return Collections.emptyList();
    }

    public static String effectiveValueFormatter(Returnable effectiveValue) {
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
        try {
            double number = Double.parseDouble(valueToPrint);
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

    public boolean buildGraph(String rangeToBuildGraphFrom, String graphType) {
        try {
            LinkedHashMap<Returnable, LinkedHashMap<Returnable, Returnable>> graph = this.engine.getGraphFromRange(rangeToBuildGraphFrom);
            this.showGraphPopup(graphType, graph);
            return true;
        } catch (RuntimeException e) {
            this.commandsController.updateGraphErrorLabel(e.getMessage());
            return false;
        }
    }

    private void showGraphPopup(String i_GraphType, LinkedHashMap<Returnable, LinkedHashMap<Returnable, Returnable>> graphData) {
        GraphType graphType = GraphType.valueOf(i_GraphType.toUpperCase().replace(" ", "_"));
        Chart graphChart = graphType.createChart(graphData);

        Stage graphStage = new Stage();
        graphStage.setTitle(i_GraphType);
        graphStage.getIcons().add(
                new Image(Objects.requireNonNull(
                        Main.class.getResourceAsStream("/gui/main/view/style/shticellLogo.png"))));
        ScrollPane scrollPane = new ScrollPane();
        (graphChart).setPadding(new Insets(20, 20, 60, 20));
        scrollPane.setContent(graphChart);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        Scene scene = new Scene(scrollPane, 800, 600);
        graphStage.setScene(scene);
        graphStage.showAndWait();  // Show the popup and wait for it to close
    }

}
