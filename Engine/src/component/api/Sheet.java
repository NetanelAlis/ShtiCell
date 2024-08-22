package component.api;

import component.impl.SheetImpl;
import java.util.Map;

public interface Sheet {

    int getVersion();
    Cell getCell(String cellID);
    void setCell(String cellID, String value);;
    String getName();
    SheetImpl.Layout getLayout();
    int getNumberOfCellsThatHaveChanged();
    Map<String, Cell> getSheetCells();

}
