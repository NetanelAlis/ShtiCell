package component.range.api;

import component.cell.api.Cell;
import component.sheet.api.ReadOnlySheet;

import java.io.Serializable;
import java.util.List;

public interface Range extends Serializable {
    List<Cell> getRangeCells();
    Cell getFrom();
    Cell getTo();
    String getName();
    void reduceUsage();
    void increaseUsage();
    boolean inUse();
    void populateRange(ReadOnlySheet sheet);
    List<String> getColumnsInRange();
    Range copyRange();
}
