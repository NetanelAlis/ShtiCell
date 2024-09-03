package gui.grid;

import dto.SheetDTO;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class GridBuilder {

    private final int row;
    private final int col;
    private final int rowSpan;
    private final int colSpan;

    public GridBuilder(int row, int col, int rowSpan, int colSpan) {
        this.row = row;
        this.col = col;
        this.rowSpan = rowSpan;
        this.colSpan = colSpan;
    }

    public ScrollPane createGrid() {
        ScrollPane scrollPane = createRootScrollPane();
        GridPane gridPane = createGridPane();
        ObservableList<ColumnConstraints> columnConstraints = createColumnConstraints(gridPane);
        ObservableList<RowConstraints> rowConstraints = createRowConstraints(gridPane);
        ObservableList<Node> children = addColumnsAndRowIndexesToGrid(gridPane);

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

        for (int i = 0; i < this.col - 1; i++) {
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
        firstRow.setMaxHeight(Double.MAX_VALUE);
        rowConstraintsList.add(firstRow);

        for (int i = 0; i < this.row - 1; i++) {
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
            Button button = createRowAndColumnIndexButton((char) ('A' + (i - 1)) + "", i, 0);
            children.add(button);
        }
        for (int i = 0; i < this.row; i++) {
            Button button = createRowAndColumnIndexButton(i < 9 ? "0" + (i + 1) : (i + 1) + "", 0, i + 1);
            children.add(button);
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

}


