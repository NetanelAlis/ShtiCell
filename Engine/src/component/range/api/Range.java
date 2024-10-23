package component.range.api;

import component.cell.api.Cell;
import component.sheet.api.ReadOnlySheet;
import java.io.Serializable;
import java.util.List;

public interface Range extends Serializable {
    void populateRange(ReadOnlySheet sheet);
    List<Cell> getRangeCells();
    Cell getFrom();
    Cell getTo();
    String getName();
    void addUsage();
    void reduceUsage();
    boolean isInUse();
    List<String> getColumnsListOfRange();
}
