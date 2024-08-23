package logic;

import dto.CellDTO;
import dto.SheetDTO;

public interface Engine {

    boolean loadXmlFile(String filePath);
    SheetDTO getSheetAsDTO();
    CellDTO geCellAsDTO(String cellId);
    void updateCellData(String cellId);
}
