package client.gui.editor.grid;

import component.cell.api.Cell;
import client.gui.editor.cell.CellSubComponentController;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GridBuilder {
    private SheetGridController sheetGridController;
    private final int numOfRows;
    private final int numOfCols;
    private final int rowHeight;
    private final int columnWidth;

    public GridBuilder(int row, int col, int rowHeight, int colWidth) {
        this.numOfRows = row;
        this.numOfCols = col;
        this.rowHeight = rowHeight;
        this.columnWidth = colWidth;
        this.sheetGridController = null;
    }
    
    public SheetGridController getSheetGridController() {
        return this.sheetGridController;
    }
    
    public ScrollPane build() throws IOException {
        this.sheetGridController = new SheetGridController();
        GridPane gridPane = new GridPane();
        ScrollPane root = new ScrollPane();

        this.buildScrollPane(root);
        this.buildGridPane(gridPane);
        this.buildRowConstraints(gridPane);
        this.buildColumnConstraints(gridPane);
        
        ObservableList<Node> children = gridPane.getChildren();
        
        this.buildHeadersRow(children);
        this.buildHeadersColumn(children);
        this.buildCellsComponents(children);
        
        this.sheetGridController.initialize();
        
        root.setContent(gridPane);
        
        return root;
    }
    
    private void buildCellsComponents(ObservableList<Node> children) throws IOException {
        for (int i = 1; i <= this.numOfRows; i++) {
            for (int j = 1; j <= this.numOfCols; j++) {
                FXMLLoader loader = new FXMLLoader();
                String cellID = Cell.createCellID(i, j);
                URL url = getClass().getResource("/client/gui/editor/cell/CellSubComponent.fxml");
                loader.setLocation(url);
                Label cell = loader.load();
                CellSubComponentController cellController = loader.getController();
                GridPane.setColumnIndex(cell, j);
                GridPane.setRowIndex(cell, i);
                children.add(cell);
                cellController.cellIDProperty().set(cellID);
                this.sheetGridController.addCellController(cellID, loader.getController());
            }
        }
    }
    
    private void buildHeadersColumn(ObservableList<Node> children) {
        for (int i = 1; i <= this.numOfRows; i++) {
            Button rowHeader = new Button(i < 10 ? "0" + i : i + "");
            rowHeader.setMaxHeight(Double.MAX_VALUE);
            rowHeader.setMaxWidth(Double.MAX_VALUE);
            GridPane.setRowIndex(rowHeader, i);
            children.add(rowHeader);
            this.sheetGridController.addRowHeader(rowHeader);
        }
    }
    
    private void buildHeadersRow(ObservableList<Node> children) {
        for (int i = 0; i < this.numOfCols; i++) {
            Button colHeader = new Button(Character.toString((char) i + 'A'));
            colHeader.setMaxHeight(Double.MAX_VALUE);
            colHeader.setMaxWidth(Double.MAX_VALUE);
            GridPane.setColumnIndex(colHeader, i + 1);
            children.add(colHeader);
            this.sheetGridController.addColumnHeader(colHeader);
        }
    }
    
    private void buildGridPane(GridPane grid) {
        grid.setAlignment(Pos.TOP_LEFT); // Set alignment to center
        grid.setMaxHeight(Double.MAX_VALUE); // Set to use maximum available height
        grid.setMaxWidth(Double.MAX_VALUE);  // Set to use maximum available width
        grid.getStyleClass().add("sheet-grid-pane");
    }
    
    private void buildScrollPane(ScrollPane scrollPane) {
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxHeight(Double.MAX_VALUE);
        scrollPane.setMaxWidth(Double.MAX_VALUE);
        scrollPane.setMinHeight(0);
        scrollPane.setMinWidth(0);
        scrollPane.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource(
                                "/client/gui/editor/grid/style/MainGridComponent.css")).toExternalForm());
    }
    
    private void buildRowConstraints(GridPane grid) {
        ObservableList<RowConstraints> rowConstraints = grid.getRowConstraints();
        
        for (int i = 0; i <= this.numOfRows; i++) {
            RowConstraints currentRow = new RowConstraints();
            
            currentRow.setVgrow(Priority.ALWAYS);
            if (i == 0) {
                currentRow.setMaxHeight(35);
                currentRow.setMinHeight(35);
                currentRow.setPrefHeight(35);
            } else {
                currentRow.setMaxHeight(this.rowHeight);
                currentRow.setMinHeight(this.rowHeight);
                currentRow.setPrefHeight(this.rowHeight);
            }
            
            rowConstraints.add(currentRow);
        }
    }
    
    private void buildColumnConstraints(GridPane grid) {
        ObservableList<ColumnConstraints> columnConstraints = grid.getColumnConstraints();
        
        for (int i = 0; i <= this.numOfCols; i++) {
            ColumnConstraints currentColumn = new ColumnConstraints();
            
            currentColumn.setHgrow(Priority.ALWAYS);
            if (i == 0) {
                currentColumn.setMaxWidth(40);
                currentColumn.setMinWidth(40);
                currentColumn.setPrefWidth(40);
            } else {
                currentColumn.setMaxWidth(this.columnWidth);
                currentColumn.setMinWidth(this.columnWidth);
                currentColumn.setPrefWidth(this.columnWidth);
            }
            
            columnConstraints.add(currentColumn);
        }
    }
}
