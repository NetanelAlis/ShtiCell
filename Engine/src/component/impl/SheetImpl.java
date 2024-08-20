package component.impl;

import component.api.Cell;
import component.api.Sheet;
import java.util.HashMap;
import java.util.Map;

public class SheetImpl implements Sheet {

    private String sheetName;
    private Layout  layout;
    private int sheetVersion;
    private Map<String, Cell> cells = new HashMap<String,Cell>();
    private int numberOfCellsThatHaveChanged;

    public class Layout {
        private int numberOfrows;
        private int numberOfcolumn;
        private int rowHeight;
        private int columnWidth;

        public Layout(int numberOfrows, int numberOfcolumn, int rowHeight, int columnWidth) {
            this.numberOfrows = numberOfrows;
            this.numberOfcolumn = numberOfcolumn;
            this.rowHeight = rowHeight;
            this.columnWidth = columnWidth;
        }
        public int getNumberOfRows(){
            return this.numberOfrows;
        }
        public int getNumberOfColumns(){
            return this.numberOfcolumn;
        }
    }

    @Override
    public int getVersion() {
        return this.sheetVersion;
    }

    @Override
    public Cell getCell(int row, int col) {
        return cells.get(Cell.createCellId(row, col));
    }

    @Override
    public void setCell(int row, int col, String value) {
        Cell cellToSet = this.cells.get((Cell.createCellId(row, col)));
        cellToSet.setOrignalValue(value);

    }

    @Override
    public int getNumberOfSheetRows() {
        return this.layout.numberOfrows;
    }

    @Override
    public int getNumberOfSheetColumns() {
        return this.layout.numberOfcolumn;
    }

}

