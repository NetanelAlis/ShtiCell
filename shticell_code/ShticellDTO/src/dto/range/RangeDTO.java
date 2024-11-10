package dto.range;

import component.range.api.Range;

import java.util.ArrayList;
import java.util.List;

public class RangeDTO {
    private String name;
    private List<String> cells;
    
    public RangeDTO(Range range) {
        this.name = range.getName();
        this.cells = new ArrayList<>();
        range.getRangeCells().forEach(cell -> this.cells.add(cell.getCellId()));
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<String> getCells() {
        return this.cells;
    }
    
    @Override
    public String toString() {
        return name + " " + cells.getFirst() + ".." + cells.getLast();
    }
}
