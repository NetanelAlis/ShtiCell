package gui.grid;

import gui.cell.CellSubComponentController;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import java.io.IOException;
import java.net.URL;

public class GridBuilder {

    private final int row;
    private final int col;
    private final int rowSpan;
    private final int colSpan;
    private MainSheetController mainSheetController;

    public GridBuilder(int row, int col, int rowSpan, int colSpan) {
        this.row = row;
        this.col = col;
        this.rowSpan = rowSpan;
        this.colSpan = colSpan;
        mainSheetController = null;
    }

    public ScrollPane build() throws IOException {
        this.mainSheetController = new MainSheetController();

        ScrollPane scrollPane = createRootScrollPane();
        GridPane gridPane = createGridPane();
        ObservableList<ColumnConstraints> columnConstraints = createColumnConstraints(gridPane);
        ObservableList<RowConstraints> rowConstraints = createRowConstraints(gridPane);
        ObservableList<Node> children = addColumnsAndRowIndexesToGrid(gridPane);
        buildCellsComponents(children);

        scrollPane.setContent(gridPane);

        return scrollPane;

    }

    private ScrollPane createRootScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setMaxHeight(Double.MAX_VALUE);
        scrollPane.setMaxWidth(Double.MAX_VALUE);
        scrollPane.setMinHeight(0.0);
        scrollPane.setMinWidth(0.0);
        scrollPane.getStylesheets().add("/gui/grid/MainGridComponent.css");

        return scrollPane;
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();

        gridPane.setAlignment(Pos.CENTER); // Set alignment to center
        gridPane.setGridLinesVisible(true); // Make grid lines visible
        gridPane.setMaxHeight(Double.MAX_VALUE); // Set to use maximum available height
        gridPane.setMaxWidth(Double.MAX_VALUE);  // Set to use maximum available width
        gridPane.setPrefWidth(548.0);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.getStyleClass().add("sheet-grid-pane");

        return gridPane;
    }

    public ObservableList<ColumnConstraints> createColumnConstraints(GridPane grid) {
        ObservableList<ColumnConstraints> columnConstraintsList = grid.getColumnConstraints();

        ColumnConstraints firstColumn = new ColumnConstraints();
        firstColumn.setHgrow(Priority.NEVER);
        firstColumn.setMaxWidth(35.0);
        firstColumn.setMinWidth(35.0);
        firstColumn.setPrefWidth(35.0);
        columnConstraintsList.add(firstColumn);

        for (int i = 0; i < this.col ; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.ALWAYS);
            colConstraints.setMaxWidth(Double.MAX_VALUE);
            colConstraints.setMinWidth(this.colSpan);
            colConstraints.setPrefWidth(this.colSpan);
            columnConstraintsList.add(colConstraints);
        }

        return columnConstraintsList;
    }

    public ObservableList<RowConstraints> createRowConstraints(GridPane grid) {
        ObservableList<RowConstraints> rowConstraintsList = grid.getRowConstraints();

        RowConstraints firstRow = new RowConstraints();

        firstRow.setVgrow(Priority.ALWAYS);
        firstRow.setMaxHeight(35);
        firstRow.setPrefHeight(35);
        firstRow.setMinHeight(35);
        rowConstraintsList.add(firstRow);

        for (int i = 0; i < this.row ; i++) {
            RowConstraints rowConstraints = new RowConstraints();

            rowConstraints.setVgrow(Priority.ALWAYS);
            rowConstraints.setMaxHeight(Double.MAX_VALUE);
            rowConstraints.setPrefHeight(this.rowSpan);
            rowConstraints.setMinHeight(this.rowSpan);
            rowConstraintsList.add(rowConstraints);
        }



        return rowConstraintsList;
    }

    private ObservableList<Node> addColumnsAndRowIndexesToGrid(GridPane gridPane) {
        // Creating buttons with specified properties
        ObservableList<Node> children = gridPane.getChildren();

        for (int i = 1; i < this.col ; i++) {
            Button col = createRowAndColumnIndexButton((char) ('A' + (i - 1)) + "", i, 0);
            children.add(col);
            this.mainSheetController.addColumnHeader(col);
        }
        for (int i = 0; i < this.row; i++) {
            Button row = createRowAndColumnIndexButton(i < 9 ? "0" + (i + 1) : (i + 1) + "", 0, i + 1);
            children.add(row);
            this.mainSheetController.addRowHeader(row);
        }

        return children;
    }

    private Button createRowAndColumnIndexButton(String text, int colIndex, int rowIndex) {
        Button button = new Button(text);
        button.setAlignment(Pos.CENTER);
        button.setMaxHeight(Double.MAX_VALUE);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMnemonicParsing(false);
        GridPane.setColumnIndex(button, colIndex);
        GridPane.setRowIndex(button, rowIndex);
        return button;
    }

    private void buildCellsComponents(ObservableList<Node> children) throws IOException {
        for (int i = 1; i <= this.row; i++) {
            for (int j = 1; j <= this.col; j++) {
                FXMLLoader loader = new FXMLLoader();
                URL url = getClass().getResource("/gui/cell/CellSubComponent.fxml");
                loader.setLocation(url);
                Label cell = loader.load();
                String cellID = createCellID(i,j);
                CellSubComponentController cellController = loader.getController();
                GridPane.setColumnIndex(cell, j);
                GridPane.setRowIndex(cell, i);
                children.add(cell);
                cellController.getCellIDProperty().set(cellID);
                this.mainSheetController.addCellController(cellID, cellController);
            }
        }
    }

    private String createCellID(int row, int col) {
        char column = (char) ('A' + col - 1);
        return "" + column + row;
    }

    public MainSheetController getController(){
        return this.mainSheetController;
    }

}


