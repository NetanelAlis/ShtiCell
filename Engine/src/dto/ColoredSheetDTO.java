package dto;

import component.cell.api.Cell;
import component.sheet.api.Sheet;
import component.sheet.impl.SheetImpl;
import logic.function.returnable.api.Returnable;

import java.util.HashMap;
import java.util.Map;

public class ColoredSheetDTO {
    private final String sheetName;
    private final ColoredSheetDTO.ColoredLayoutDTO layout;
    private final Map<String, ColoredCellDTO> cells;
    private final int version;
    private final int numOfCellsUpdated;
    
    public class ColoredLayoutDTO {
        private final int row;
        private final int column;
        private final int rowHeight;
        private final int columnWidth;
        
        public ColoredLayoutDTO(SheetImpl.Layout layout) {
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
    
    public ColoredSheetDTO(Sheet sheet) {
        this.sheetName = sheet.getSheetName();
        this.layout = new ColoredLayoutDTO(sheet.getLayout());
        this.version = sheet.getVersion();
        this.numOfCellsUpdated = sheet.getNumOfCellsUpdated();
        this.cells = new HashMap<>();
        
        for (Cell cell : sheet.getCells().values()) {
            this.cells.put(cell.getCellId(), new ColoredCellDTO(cell, cell.getCellId()));
        }
    }
    
    public String getSheetName() {
        return sheetName;
    }
    
    public ColoredSheetDTO.ColoredLayoutDTO getLayout() {
        return layout;
    }
    
    public Map<String, ColoredCellDTO> getCells() {
        return cells;
    }
    
    public int getVersion() {
        return version;
    }
    
    public int getNumOfCellsUpdated() {
        return numOfCellsUpdated;
    }
}
