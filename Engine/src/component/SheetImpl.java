package component;

import java.util.HashMap;
import java.util.Map;

public class SheetImpl implements Sheet {

    private String sheetName;
    private String  xmlPath;
    private Layout  layout;
    private int sheetVersion;
    private Map<String,Cell> activeCells = new HashMap<String,Cell>();

    private class Layout {
        private int numberOfrows;
        private int numberOfcolumn;
        private int rowHeight;
        private int columnWidth;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public Cell getCell(int row, int col) {
        return activeCells.get(Cell.createCellId(row, col));
    }

    @Override
    public void setCell(int row, int col, String value) {
        Cell cellToSet = this.activeCells.get((Cell.createCellId(row, col)));
        cellToSet.setOrignalValue(value);

    }

}

