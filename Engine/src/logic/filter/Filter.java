package logic.filter;

import component.cell.api.Cell;
import component.range.api.Range;
import dto.returnable.EffectiveValueDTO;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    
    private final Range rangeToFilter;
    private final String leftColumn;
    private final String rightColumn;
    private final int topRow;
    private final int bottomRow;
    
    
    public Filter(Range rangeToFilter) {
        this.rangeToFilter = rangeToFilter;
        this.bottomRow = Integer.parseInt(rangeToFilter.getTo().getCellId().substring(1));
        this.topRow = Integer.parseInt(rangeToFilter.getFrom().getCellId().substring(1));
        this.leftColumn = this.rangeToFilter.getFrom().getCellId().substring(0,1);
        this.rightColumn = this.rangeToFilter.getTo().getCellId().substring(0,1);
    }
    
    public Range filter(String columnToFilterBy, List<EffectiveValueDTO> itemsToFilter) {
        List<Cell> columnToFilter = this.getColumnListFromRange(columnToFilterBy);
        List<List<Cell>> rangeInRows = this.getRowsFromRange();
        List<List<Cell>> filteredCells = new ArrayList<>();
        
        for (int i = 0; i < columnToFilter.size(); i++) {
            for (EffectiveValueDTO item : itemsToFilter) {
                
                String effectiveValueAsString = String.valueOf(columnToFilter.get(i)
                        .getEffectiveValue().getValue());
                
                if (String.valueOf(item.getEffectiveValue()).equals(effectiveValueAsString)) {
                    filteredCells.add(rangeInRows.get(i));
                    break;
                }
            }
        }
        
        this.updateCellIDs(filteredCells);
        this.reorderRange(filteredCells);
        
        return this.rangeToFilter;
    }
    
    private void reorderRange(List<List<Cell>> rowsList){
        this.rangeToFilter.getRangeCells().clear();
        rowsList.forEach(row -> {
            row.forEach(cell -> this.rangeToFilter.getRangeCells().add(cell));
        });
    }
    
    private void updateCellIDs(List<List<Cell>> rowsList) {
        int i = 0;
        while (rowsList.size() > i) {
            int finalI = i;
            rowsList.get(i).forEach((cell) -> {
                String columnID = cell.getCellId().substring(0,1);
                String RowId = String.valueOf(this.topRow + finalI);
                String newID = columnID + RowId;
                cell.updateCellID(newID);
            });
            i++;
        }
    }
    
    private List<List<Cell>> getRowsFromRange() {
        List<List<Cell>> rows = new ArrayList<>();
        for (int row = this.topRow; row <= this.bottomRow; row++) {
            rows.add(this.getRowListFromRange(row));
        }
        
        return rows;
    }
    
    private List<Cell> getRowListFromRange(int rowIndex) {
        return getCells(rowIndex, this.rangeToFilter, this.topRow, this.bottomRow);
    }
    
    public static List<Cell> getCells(int rowIndex, Range range, int topRow, int bottomRow) {
        List<Cell> row = range.getRangeCells()
                .stream()
                .filter((cell -> cell.getCellId().contains(String.valueOf(rowIndex))))
                .toList();
        
        if (row.isEmpty()) {
            throw new IllegalArgumentException(
                    "Expected Row between " + topRow + " and "
                            + bottomRow + " but found " + rowIndex);
        }
        
        return row;
    }
    
    private List<Cell> getColumnListFromRange(String columnToFilterBy) {
        List<Cell> column = this.rangeToFilter.getRangeCells()
                .stream()
                .filter((cell -> cell.getCellId().contains(columnToFilterBy)))
                .toList();
        
        if (column.isEmpty()) {
            throw new IllegalArgumentException(
                    "Expected Column between " + this.leftColumn + " and "
                            + this.rightColumn + " but found " + columnToFilterBy);
        }
        
        return column;
    }
}
