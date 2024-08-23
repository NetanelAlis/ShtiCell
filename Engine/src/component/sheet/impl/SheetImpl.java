package component.sheet.impl;

import component.cell.api.Cell;
import component.cell.impl.CellImpl;
import component.sheet.api.Sheet;
import component.sheet.topological.order.TopologicalOrder;

import java.io.*;
import java.util.*;


public class SheetImpl implements Sheet {

    private String sheetName;
    private Layout  layout;
    private int version;
    private Map<String, Cell> activeCells;
    private int numberOfCellsThatHaveChanged;

    public class Layout implements Serializable {
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

    private SheetImpl copySheet() {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            return (SheetImpl) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e); // catch in the ui
        }
    }

    @Override
    public Sheet updateCellValueAndCalculate(String cellID, String value) {

        SheetImpl newSheetVersion = copySheet();
        Cell newCell = new CellImpl(cellID, value, newSheetVersion.getVersion() + 1, newSheetVersion);
        newSheetVersion.activeCells.put(value, newCell);

        try {
            List<Cell> cellsThatHaveChanged =
                    newSheetVersion
                            .orderCellsForCalculation()
                            .stream()
                            .filter(Cell::calculateEffectiveValue)
                            .toList();

            // successful calculation. update sheet and relevant cells version
             int newVersion = newSheetVersion.increaseVersion();
             cellsThatHaveChanged.forEach(cell -> cell.updateVersion(newVersion));
             this.numberOfCellsThatHaveChanged = cellsThatHaveChanged.size();

            return newSheetVersion;
        } catch (Exception e) {
            // deal with the runtime error that was discovered as part of invocation
            return this;
        }
    }

    private List<Cell> orderCellsForCalculation() {
        return TopologicalOrder.SORT.topologicalSort(this.activeCells);
    }

    private int increaseVersion(){
        return ++this.version;
    }

}

