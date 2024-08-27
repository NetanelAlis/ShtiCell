package logic;

import dto.CellDTO;
import dto.SheetDTO;

public interface Engine {

    boolean LoadDataFromXML(String filePath);
    SheetDTO getSheetAsDTO();
    CellDTO geCellAsDTO(String cellId);
    void updateSingleCellData(String cellId, String value);
}
