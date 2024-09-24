package dto;

import component.cell.api.Cell;
import component.sheet.api.Sheet;
import component.sheet.impl.SheetImpl;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ColoredSheetDTO {
    private final String sheetName;
    private final LayoutDTO layout;
    private final int sheetVersion;
    private final int numberOfCellsThatHaveChanged;
    private final Map<String, ColoredCellDTO> activeCells;

    public ColoredSheetDTO(Sheet sheet) {
        this.sheetName = sheet.getName();
        this.layout = new LayoutDTO(sheet.getLayout());
        this.sheetVersion = sheet.getVersion();
        this.numberOfCellsThatHaveChanged = sheet.getNumberOfCellsThatHaveChanged();
        this.activeCells = new HashMap<>();

        for (Cell cell : sheet.getSheetCells().values()) {
            this.activeCells.put(cell.getCellId(), new ColoredCellDTO(cell, cell.getCellId()));
        }
    }

    public class LayoutDTO implements Serializable {
        private int numberOfrows;
        private int numberOfcolumn;
        private int rowHeight;
        private int columnWidth;

        public LayoutDTO(SheetImpl.Layout layout) {
            this.numberOfrows = layout.getNumberOfRows();
            this.numberOfcolumn = layout.getNumberOfColumns();
            this.rowHeight = layout.getRowHeight();
            this.columnWidth = layout.getColumnWidth();
        }

        public int getNumberOfRows () {
            return this.numberOfrows;
        }

        public int getNumberOfColumns () {
            return this.numberOfcolumn;
        }

        public int getRowHeight () {
            return this.rowHeight;
        }

        public int getColumnWidth () {
            return this.columnWidth;
        }
    }

    public String getSheetName() {
        return this.sheetName;
    }

    public LayoutDTO getLayout() {
        return this.layout;
    }

    public int getSheetVersion() {
        return this.sheetVersion;
    }

    public int getNumberOfCellsThatHaveChanged() {
        return this.numberOfCellsThatHaveChanged;
    }

    public Map<String, ColoredCellDTO> getActiveCells() {
        return this.activeCells;
    }

}

