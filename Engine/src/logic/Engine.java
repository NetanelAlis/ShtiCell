package logic;

import dto.*;
import javafx.scene.paint.Color;
import logic.function.returnable.api.Returnable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public interface Engine {
    void loadData(String path);
    SheetDTO getSheetAsDTO();
    CellDTO getSingleCellData(String cellID);
    void updateSingleCellData(String cellID, String value);
    VersionChangesDTO showVersions();
    ColoredSheetDTO getSheetVersionAsDTO(int version);
    boolean isSheetLoaded();
    void loadFromFile(String path);
    void saveToFile(String path);
    RangeDTO addRange(String rangeName, String range);
    void removeRange(String rangeName);
    RangesDTO getAllRanges();
    void updateCellStyle(String cellID, Color backgroundColor, Color textColor);
    ColoredSheetDTO sortRangeOfCells(String range, List<String> columnsToSortBy);
    ColoredSheetDTO filterRangeOfCells(String rangeToFilterBy, String columnToFilterBy, List<Integer> itemsToFilterBy);
    List<String> getColumnsListOfRange(String rangeToFilter);
    List<Returnable> getUniqueItemsToFilterBy(String columnToFilterBy, String rangeToFilter);

    LinkedHashMap<Returnable, LinkedHashMap<Returnable, Returnable>> getGraphFromRange(String rangeToBuildGraphFrom);
}
