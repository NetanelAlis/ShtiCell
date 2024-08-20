package component.impl;

import component.api.Cell;
import component.api.Sheet;
import java.util.HashMap;
import java.util.Map;

public class SheetImpl implements Sheet {

    private String sheetName;
    private Layout  layout;
    private int sheetVersion;
    private Map<String, Cell> activeCells = new HashMap<String,Cell>();
    private int numberOfCellsThatHaveChanged = 0;

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

    public SheetImpl(String sheetName, Layout layout, int sheetVersion) {
        this.sheetName = sheetName;
        this.layout = layout;
        this.sheetVersion = sheetVersion;
    }

    @Override
    public int getVersion() {
        return this.sheetVersion;
    }

    @Override
    public Cell getCell(String cellID) {
        return activeCells.get(cellID);
    }

    @Override
    public void setCell(int row, int col, String value) {
        Cell cellToSet = this.activeCells.get((Cell.createCellId(row, col)));
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

    @Override
    public String getName() {
        return this.sheetName;
    }

    @Override
    public Layout getLayout() {
        return this.layout;
    }

    @Override
    public Map<String, Cell>getSheetCells(){
        return this.activeCells;
    }

    @Override
    public int getNumberOfCellsThatHaveChanged(){
        return this.numberOfCellsThatHaveChanged;
    }

}

