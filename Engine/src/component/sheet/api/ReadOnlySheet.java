package component.sheet.api;

import component.cell.api.Cell;
import component.sheet.impl.SheetImpl;
import java.io.Serializable;
import java.util.Map;

public interface ReadOnlySheet extends Serializable {
    int getVersion();
    Cell getCell(String cellID);
    String getName();
    SheetImpl.Layout getLayout();
    int getNumberOfCellsThatHaveChanged();
    Map<String, Cell> getSheetCells();
    SheetImpl copySheet();
     boolean cellInLayout(String cellID);
}
