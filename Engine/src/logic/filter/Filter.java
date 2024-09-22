package logic.filter;

import component.cell.api.Cell;
import component.range.api.Range;
import logic.function.returnable.Returnable;
import java.util.ArrayList;
import java.util.List;

public class Filter {
    private Range rangeToFilter;
    private int topRow;
    private int bottomRow;

    public Filter(Range rangeToFilter) {
        this.rangeToFilter = rangeToFilter;
        this.bottomRow = Integer.parseInt(rangeToFilter.getTo().getCellId().substring(1));
        this.topRow = Integer.parseInt(rangeToFilter.getFrom().getCellId().substring(1));
    }

    public Range filter(String colToSortBy ,List<Returnable> itemsToFilter) {

        List<List<Cell>> rangeCellsRowList = this.getRowsFromRange();
        List<Cell> cellsInColToSortBy = this.getColumnListFromRange(colToSortBy);
        List<List<Cell>> filteredCells = new ArrayList<>();

        for (int i = 0; i < cellsInColToSortBy.size(); i++) {
            for (Returnable item : itemsToFilter) {

                String effectiveValueAsString = String.valueOf(cellsInColToSortBy.get(i)
                        .getEffectiveValue().getValue());

                if (String.valueOf(item.getValue()).equals(effectiveValueAsString)) {
                    filteredCells.add(rangeCellsRowList.get(i));
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
        for (int i = 0; i<rowsList.size(); i++) {
            int finalI = i;
            rowsList.get(i).forEach((cell) -> {
                String columnID = cell.getCellId().substring(0,1);
                String RowId = String.valueOf(
                        finalI + topRow);
                String newID = columnID + RowId;
                cell.updateCellID(newID);
            });
        }
    }

    private List<List<Cell>> getRowsFromRange() {
        List<List<Cell>> rows = new ArrayList<>();
        for (int row = topRow; row <= bottomRow; row++) {
            rows.add(this.getRowListFromRange(row));
        }

        return rows;
    }

    private List<Cell> getRowListFromRange(int rowIndex){
        List<Cell> listToReturn = new ArrayList<>(
                this.rangeToFilter
                        .getRangeCells()
                        .stream()
                        .filter(cell -> cell.getCellId().contains(String.valueOf(rowIndex)))
                        .toList()

        );

        if (listToReturn.isEmpty()) {
            throw new IllegalArgumentException(
                    "Expected Row between " + topRow + " and "
                            + bottomRow + " but found " + rowIndex);
        }

        return listToReturn;
    }

    private List<Cell> getColumnListFromRange(String columnToFilterBy) {
        List<Cell> column = this.rangeToFilter.getRangeCells()
                .stream()
                .filter((cell -> cell.getCellId().contains(columnToFilterBy)))
                .toList();

        if (column.isEmpty()) {
            throw new IllegalArgumentException(
                    "Expected Column between " + this.rangeToFilter.getFrom().getCellId().charAt(0) + " and "
                            + this.rangeToFilter.getTo().getCellId().charAt(0) + " but found " + columnToFilterBy);
        }

        return column;
    }

}



