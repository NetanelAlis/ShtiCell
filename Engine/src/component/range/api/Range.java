package component.range.api;

import component.cell.api.Cell;
import java.util.List;

public interface Range {
    List<Cell> getRangeCells();
    Cell getFrom();
    Cell getTo();
    String getName();
    void reduceUsage();
    void increaseUsage();
    boolean inUse();
}
