package logic.sort;

import component.cell.api.Cell;
import component.range.api.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static logic.filter.Filter.getCells;

public class Sorter {
    private final String leftColumn;
    private final String rightColumn;
    private final Range rangeToSort;
    private final List<String> columnsToSortBy;
    private final int topRow;
    private final int bottomRow;
    
    public Sorter(Range rangeToSort, List<String> columnsToSortBy) {
        this.rangeToSort = rangeToSort;
        this.columnsToSortBy = columnsToSortBy;
        this.bottomRow = Integer.parseInt(rangeToSort.getTo().getCellId().substring(1));
        this.topRow = Integer.parseInt(rangeToSort.getFrom().getCellId().substring(1));
        this.leftColumn = this.rangeToSort.getFrom().getCellId().substring(0,1);
        this.rightColumn = this.rangeToSort.getTo().getCellId().substring(0,1);
        
        if (columnsToSortBy.isEmpty()) {
            throw new IllegalArgumentException("Cannot sort without columns");
        }
        
        this.columnsToSortBy.forEach(column -> {
            try {
                if (column.isBlank()) {
                    throw new IllegalArgumentException("Cannot sort by blank column");
                }
                Integer.parseInt(column);
                throw new IllegalArgumentException("Expected Column between " + this.leftColumn +
                        " and " + this.rightColumn + " but found " + column);
            } catch (NumberFormatException ignored) {}
        });
    }
    
    public Range sort() {
        List<List<Cell>> columnsToSort = this.getColumnsFromRange();
        List<List<Cell>> rangeInRows = this.getRowsFromRange();
        List<Integer> indices =
                new ArrayList<>(IntStream.range(0, columnsToSort.getFirst().size()).boxed().toList());
        
        // Sort the indices based on custom comparator
        indices.sort((i1, i2) -> {
            // Compare the first list
            int cmp = Double.compare(
                    columnsToSort.getFirst().get(i1).getEffectiveValue().tryConvertTo(Double.class),
                    columnsToSort.getFirst().get(i2).getEffectiveValue().tryConvertTo(Double.class));
            if (cmp != 0) {
                return cmp;
            }
            // If values are equal in the first list, compare the other lists
            for (int i = 1; i < columnsToSort.size(); i++) {
                List<Cell> otherList = columnsToSort.get(i);
                cmp = Double.compare(
                        columnsToSort.get(i).get(i1).getEffectiveValue().tryConvertTo(Double.class),
                        columnsToSort.get(i).get(i2).getEffectiveValue().tryConvertTo(Double.class));
                if (cmp != 0) {
                    return cmp;
                }
            }
            return 0; // Fully equal if no differences found in all lists
        });
        
        this.updateCellIDs(
                rangeInRows,
                this.calculateDelta(
                        columnsToSort.getFirst(),
                        reorderListByIndices(columnsToSort.getFirst(), indices)));
        
        this.reorderRange(this.sortRows(rangeInRows, indices));
        
        return this.rangeToSort;
    }
    
    private void reorderRange(List<List<Cell>> rowsList){
        this.rangeToSort.getRangeCells().clear();
        rowsList.forEach(row -> {
            row.forEach(cell -> this.rangeToSort.getRangeCells().add(cell));
        });
    }
    
    private List<List<Cell>> sortRows(List<List<Cell>> rowsList, List<Integer> sortedIndices) {
        List<List<Cell>> temp = new ArrayList<>(rowsList);
        for (int i = 0; i < sortedIndices.size(); i++) {
            rowsList.set(i, temp.get(sortedIndices.get(i)));
        }
        
        return rowsList;
    }
    
    private void updateCellIDs(List<List<Cell>> rowsList, List<Integer> deltasList) {
        for (int i = 0; i<rowsList.size(); i++) {
            int finalI = i;
            rowsList.get(i).forEach((cell) -> {
                String columnID = cell.getCellId().substring(0,1);
                String RowId = String.valueOf(
                        Integer.parseInt(cell.getCellId().substring(1)) + deltasList.get(finalI));
                String newID = columnID + RowId;
                cell.updateCellID(newID);
            });
        }
    }
    
    private List<Integer> calculateDelta(List<Cell> originalOrder, List<Cell> sortedOrder) {
        List<Integer> delta = new ArrayList<>();
        for (int i = 0; i < originalOrder.size(); i++) {
            for (int j = 0; j < sortedOrder.size(); j++) {
                if (originalOrder.get(i).equals(sortedOrder.get(j))) {
                    delta.add(j - i);
                    break;
                }
            }
        }
        
        return delta;
    }
    
    // Reorder a list based on the provided sorted indices
    private List<Cell> reorderListByIndices(List<Cell> list, List<Integer> sortedIndices) {
        List<Cell> sortedList = new ArrayList<>();
        for (int i = 0; i < sortedIndices.size(); i++) {
            sortedList.add(list.get(sortedIndices.get(i)));
        }
        
        return sortedList;
    }
    
    private List<List<Cell>> getRowsFromRange() {
        List<List<Cell>> rows = new ArrayList<>();
        for (int row = topRow; row <= bottomRow; row++) {
            rows.add(this.getRowListFromRange(row));
        }
        
        return rows;
    }
    
    private List<List<Cell>> getColumnsFromRange() {
        List<List<Cell>> columnsToSort = new ArrayList<>();
        for (int i = 0; i < this.columnsToSortBy.size(); i++) {
            columnsToSort.add(this.getColumnListFromRange(i));
        }
        
        return columnsToSort;
    }

    private List<Cell> getRowListFromRange(int rowIndex) {
        return getCells(rowIndex, this.rangeToSort, this.topRow, this.bottomRow);
    }

    private List<Cell> getColumnListFromRange(int ColIndex) {
        List<Cell> column = this.rangeToSort.getRangeCells()
                .stream()
                .filter((cell -> cell.getCellId().contains(this.columnsToSortBy.get(ColIndex))))
                .toList();

        if (column.isEmpty()) {
            throw new IllegalArgumentException(
                            "Expected Column between " + this.leftColumn + " and "
                            + this.rightColumn + " but found " + this.columnsToSortBy.get(ColIndex));
        }
        
        return column;
    }
}



