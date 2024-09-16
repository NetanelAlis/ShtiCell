package logic;

import dto.CellDTO;
import dto.SheetDTO;
import dto.VersionsChangesDTO;

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
}
