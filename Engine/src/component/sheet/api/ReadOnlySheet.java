package component.sheet.api;

import component.cell.api.Cell;
import component.range.api.Range;
import component.sheet.impl.SheetImpl;

import java.io.Serializable;
import java.util.Map;
import java.util.Random;

public interface ReadonlySheet extends Serializable {
    int getVersion();
    Cell getCell(String cellId);
    SheetImpl.Layout getLayout();
    String getSheetName();
    Map<String, Cell> getCells();
    Map<String, Range> getRanges();
    boolean isExistingRange(String range);
    int getNumOfCellsUpdated();
    SheetImpl copySheet();
    boolean cellInLayout(String cellId);
}
