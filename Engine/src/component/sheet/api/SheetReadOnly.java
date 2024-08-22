package component.sheet.api;

import component.cell.api.Cell;
import component.sheet.impl.SheetImpl;

import java.util.Map;

public interface SheetReadOnly {
    int getVersion();
    Cell getCell(String cellID);
    String getName();
    SheetImpl.Layout getLayout();
    int getNumberOfCellsThatHaveChanged();
    Map<String, Cell> getSheetCells();
}
