package dto.sheet;

import component.cell.api.Cell;
import component.sheet.api.Sheet;
import component.sheet.impl.SheetImpl;
import dto.returnable.EffectiveValueDTO;

import java.util.HashMap;
import java.util.Map;

public class SheetDTO {
    private final String sheetName;
    private final SheetDTO.LayoutDTO layout;
    private final Map<String, EffectiveValueDTO> cells;
    private final int version;
    private final int numOfCellsUpdated;

    public class LayoutDTO {
        private final int row;
        private final int column;
        private final int rowHeight;
        private final int columnWidth;

        public LayoutDTO(SheetImpl.Layout layout) {
            this.row = layout.getRow();
            this.column = layout.getColumn();
            this.rowHeight = layout.getRowHeight();
            this.columnWidth = layout.getColumnWidth();
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

    public SheetDTO(Sheet sheet) {
        this.sheetName = sheet.getSheetName();
        this.layout = new LayoutDTO(sheet.getLayout());
        this.version = sheet.getVersion();
        this.numOfCellsUpdated = sheet.getNumOfCellsUpdated();
        this.cells = new HashMap<>();

        for (Cell cell : sheet.getCells().values()) {
            this.cells.put(cell.getCellId(), new EffectiveValueDTO(cell.getEffectiveValue()));
        }
    }

    public String getSheetName() {
        return sheetName;
    }

    public SheetDTO.LayoutDTO getLayout() {
        return layout;
    }

    public Map<String, EffectiveValueDTO> getCells() {
        return cells;
    }

    public int getVersion() {
        return version;
    }

    public int getNumOfCellsUpdated() {
        return numOfCellsUpdated;
    }
}
