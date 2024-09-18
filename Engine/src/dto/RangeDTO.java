package dto;

import component.range.api.Range;

import java.util.ArrayList;
import java.util.List;

public class RangeDTO {
    private String name;
    private List<String> cellsInRangeIDs;

    public RangeDTO(Range range) {
        this.name = range.getName();
        cellsInRangeIDs = new ArrayList<>();
        range.getRangeCells().forEach(cell -> this.cellsInRangeIDs.add(cell.getCellId()));
    }

    @Override
    public String toString() {
        return this.name + " " + this.cellsInRangeIDs.getFirst() + ".." + this.cellsInRangeIDs.getLast();
    }

    public String getName() {
        return name;
    }

    public List<String> getCellsInRangeIDs() {
        return cellsInRangeIDs;
    }
}
