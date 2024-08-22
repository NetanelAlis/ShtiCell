package dto;

import component.api.Cell;
import component.api.Sheet;
import component.impl.SheetImpl;
import logic.function.returnable.Returnable;
import java.util.HashMap;
import java.util.Map;

public class SheetDTO {
    private final String sheetName;
    private final SheetImpl.Layout layout;
    private final int sheetVersion;
    private final int numberOfCellsThatHaveChanged;
    private final Map<String, Returnable> activeCells;

    public SheetDTO(Sheet sheet) {
        this.sheetName = sheet.getName();
        this.layout = sheet.getLayout();
        this.sheetVersion = sheet.getVersion();
        this.numberOfCellsThatHaveChanged = sheet.getNumberOfCellsThatHaveChanged();
        this.activeCells = new HashMap<>();

        for (Cell cell : sheet.getSheetCells().values()) {
            this.activeCells.put(cell.getCellId(), cell.getEffectiveValue());
        }
    }

    public int getNumberOfRows() {
        return this.layout.getNumberOfRows();
    }

    public int getNumberOfColumns() {
        return this.getNumberOfColumns();
    }

    public String getSheetName() {
        return this.sheetName;
    }

    public SheetImpl.Layout getLayout() {
        return this.layout;
    }

    public int getSheetVersion() {
        return this.sheetVersion;
    }

    public int getNumberOfCellsThatHaveChanged() {
        return this.numberOfCellsThatHaveChanged;
    }

    public Map<String, Returnable> getActiveCells() {
        return this.activeCells;
    }

}

