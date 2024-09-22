package logic;

import dto.*;
import javafx.scene.paint.Color;
import logic.function.returnable.Returnable;

import java.util.List;

public interface Engine {

    void LoadDataFromXML(String filePath);
    SheetDTO getSheetAsDTO();
    CellDTO geCellAsDTO(String cellId);
    void updateSingleCellData(String cellId, String value);
    boolean isSheetLoaded();
    VersionsChangesDTO getVersionsChangesAsDTO();
    SheetDTO getSheetVersionsAsDTO(int version);
    void saveToFile(String input);
    void LoadDataFromFile(String path);
    void addRange(String rangeName, String range);
    void deleteRange(String rangeName);
    RangeDTO getRangesAsDTO(String rangeName);
    RangesDTO getAllRangesAsDTO();
    void updateBackgroundColor(Color color, String cellID);
    void updateTextColor(Color color, String cellID);
    SheetDTO sortRangeCells(String rangeName, List<String> colsToSortBy);
    List<String> getColumnsToSortBy(String cellsInRange);
    List<Returnable> getItemsToFilterBy(String colToFilterBy,String cellsInRange);
    SheetDTO getFilterSheet(String cellsInRange, List<Integer> itemsToFilterByIndexes, String col);
}
