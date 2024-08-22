package component.impl;

import component.api.Cell;
import component.api.Sheet;
import java.util.HashMap;
import java.util.Map;

public class SheetImpl implements Sheet {

    private String sheetName;
    private Layout  layout;
    private int version;
    private Map<String, Cell> activeCells;
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

        public int getRowHeight(){
            return this.rowHeight;
        }

        public int getColumnWidth(){
            return this.columnWidth;
        }
    }

    public SheetImpl(String sheetName) {
        this.sheetName = sheetName;
        this.layout = new Layout(4, 4, 1, 8);
        this.version = 0;
        this.activeCells = new HashMap<String,Cell>();
        this.numberOfCellsThatHaveChanged = 0;

    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public Cell getCell(String cellID) {
        if(cellInLayout(cellID)){
            return activeCells.get(cellID);
        }
            throw new IllegalArgumentException("the Sheet size is " + this.layout.getNumberOfRows() + "row and " +
                     this.layout.getNumberOfColumns() + "columns, the entered cell ID (" + cellID +") is " +
                    "out of bounds.");
    }

    @Override
    public void setCell(String cellID, String value) {
        this.activeCells.get(value).setOrignalValue(value);
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

    private int parseCellIDRow(String cellID) {
        return Integer.parseInt(cellID.substring(1)) - 1;
    }

    private int parseCellIDCol(String cellID){
        char colLetter = cellID.charAt(0);
        return colLetter - 'A';
    }

    boolean cellInLayout(String cellID){
        int row = parseCellIDRow(cellID);
        int col = parseCellIDCol(cellID);
        boolean rowValid = (row >= 0 && row <= this.layout.getNumberOfRows());
        boolean colValid = (col >= 0 && col <= this.layout.getNumberOfColumns());

        return rowValid && colValid;
    }

}

