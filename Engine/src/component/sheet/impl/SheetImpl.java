package component.sheet.impl;

import component.cell.api.Cell;
import component.range.api.Range;
import component.range.impl.RangeImpl;
import component.sheet.api.Sheet;
import component.sheet.topological.order.TopologicalOrder;
import jaxb.generated.STLSheet;
import java.io.*;
import java.util.*;

public class SheetImpl implements Sheet {
    private final String sheetName;
    private final Layout layout;
    private final Map<String, Cell> cells;
    private final Map<String, Range> ranges;
    private int version;
    private int numOfCellsUpdated;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SheetImpl sheet = (SheetImpl) o;
        return version == sheet.version && numOfCellsUpdated == sheet.numOfCellsUpdated && Objects.equals(sheetName, sheet.sheetName) && Objects.equals(layout, sheet.layout) && Objects.equals(cells, sheet.cells) && Objects.equals(ranges, sheet.ranges);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(sheetName, layout, cells, ranges, version, numOfCellsUpdated);
    }
    
    public class Layout implements Serializable {
        private final static int MAX_NUM_OF_ROWS = 50;
        private final static int MAX_NUM_OF_COLUMNS = 20;
        private final int row;
        private final int column;
        private final int rowHeight;
        private final int columnWidth;
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Layout layout = (Layout) o;
            return row == layout.row && column == layout.column && rowHeight == layout.rowHeight && columnWidth == layout.columnWidth;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(row, column, rowHeight, columnWidth);
        }
        
        public Layout(int row, int column, int rowHeight, int columnWidth) {
            this.row = row;
            this.column = column;
            this.rowHeight = rowHeight;
            this.columnWidth = columnWidth;

            if (row > MAX_NUM_OF_ROWS || column > MAX_NUM_OF_COLUMNS || row <= 0 || column <= 0) {
                throw new IllegalArgumentException("Row or column are out of range,\n" +
                        "expected rows: (1-" + MAX_NUM_OF_ROWS + ") and columns: (1-" + MAX_NUM_OF_COLUMNS +
                        ")\nbut got rows=" + row + " and columns=" + column);
            }
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public int getRowHeight() {
            return rowHeight;
        }

        public int getColumnWidth() {
            return columnWidth;
        }
    }
    
    public SheetImpl(STLSheet stlSheet) {
        this.sheetName = stlSheet.getName();
        this.cells = new HashMap<>();
        this.ranges = new HashMap<>();
        this.version = 0;
        this.numOfCellsUpdated = 0;
        this.layout = new Layout(stlSheet.getSTLLayout().getRows(),
                stlSheet.getSTLLayout().getColumns(),
                stlSheet.getSTLLayout().getSTLSize().getRowsHeightUnits(),
                stlSheet.getSTLLayout().getSTLSize().getColumnWidthUnits());
    }
    
    @Override
    public Cell getCell(String cellId) {
        cellId = Character.toUpperCase(cellId.charAt(0)) + cellId.substring(1);

        if (cellInLayout(cellId)){
            return this.cells.get(cellId);
        }

        throw new IllegalArgumentException("The sheet size is " + this.layout.getRow() + " rows and " +
                this.layout.getColumn() + " columns, The Cell or Referenced Cell " + cellId + " is out of bounds.");
    }
    
    @Override
    public boolean cellInLayout(String cellId) {
        int row = this.parseCellIdRow(cellId);
        int column = this.parseCellIdColumn(cellId);

        return row <= this.layout.getRow()
                && row >= 0
                && column <= this.layout.getColumn()
                && column >= 0;
    }
    
    private int parseCellIdRow(String cellId) {
        return Integer.parseInt(cellId.substring(1)) - 1;
    }
    
    private int parseCellIdColumn(String cellId) {
        return Character.toUpperCase(cellId.charAt(0)) - 'A';
    }
    
    @Override
    public Sheet updateSheet(SheetImpl newSheetVersion, boolean isOriginalValueChanged) {
        List<Cell> cellsThatHaveChanged =
                TopologicalOrder.SORT.topologicalSort(newSheetVersion.getCells())
                        .stream()
                        .filter(Cell::calculateEffectiveValue)
                        .toList();
        
        if (cellsThatHaveChanged.isEmpty() && !isOriginalValueChanged) {
            return this;
        }

        // successful calculation. update sheet and relevant cells version
        int newVersion = newSheetVersion.increaseVersion();
        cellsThatHaveChanged.forEach(cell -> cell.updateVersion(newVersion));
        newSheetVersion.numOfCellsUpdated = cellsThatHaveChanged.size();

        return newSheetVersion;
    }
    
    @Override
    public void createRange(String rangeName, String range) {
        if(!this.getRanges().containsKey(rangeName)) {
                this.getRanges().put(rangeName, new RangeImpl(rangeName, range, this));
        } else {
            throw new IllegalArgumentException("The Range " + rangeName + " already exists");
        }
    }
    
    @Override
    public void deleteRange(String rangeName) {
        if (this.ranges.get(rangeName).isInUse()) {
            throw new IllegalArgumentException("Cannot Delete Range " + rangeName + " while it is in use");
        } else {
            this.ranges.remove(rangeName);
        }
    }
    
    private int increaseVersion() {
         return ++this.version;
    }
    
    @Override
    public SheetImpl copySheet() {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            return (SheetImpl) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Layout getLayout() {
        return layout;
    }
    
    @Override
    public int getVersion() {
        return this.version;
    }
    
    @Override
    public String getSheetName(){
        return this.sheetName;
    }
    
    @Override
    public Map<String, Cell> getCells(){
        return this.cells;
    }
    
    @Override
    public Map<String, Range> getRanges() {
        return this.ranges;
    }
    
    @Override
    public boolean isExistingRange(String range) {
        return this.getRanges().containsKey(range);
    }
    
    @Override
    public int getNumOfCellsUpdated(){
        return this.numOfCellsUpdated;
    }
}
