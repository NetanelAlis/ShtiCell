package dto;

import component.api.Cell;
import component.impl.SheetImpl;
import logic.function.returnable.Returnable;
import java.util.HashMap;
import java.util.Map;

public class SheetDTO {
    private final String sheetName;
    private final SheetImpl.Layout layout;
    private final int sheetVersion;
    private final int numberOfCellsThatHaveChanged;
    private final Map<String, Returnable> cells;

    SheetDTO(String sheetName, SheetImpl.Layout layout, int sheetVersion, int numberOfCellsThatHaveChanged, Map<String, Cell> cells) {
        this.sheetName = sheetName;
        this.layout = layout;
        this.sheetVersion = sheetVersion;
        this.numberOfCellsThatHaveChanged = numberOfCellsThatHaveChanged;
        this.cells = new HashMap<String, Returnable>();

        for (Cell cell : cells.values()) {
            this.cells.put(cell.getCellId(), cell.getEffectiveValue());
        }
    }

    public int getNumberOfRows() {
        return this.layout.getNumberOfRows();
    }

    public int getNumberOfColumns() {
        return this.getNumberOfColumns();
    }

    public String getSheetName() {
        return sheetName;
    }

}

