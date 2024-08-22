package logic;

import component.api.Sheet;
import component.impl.SheetImpl;
import dto.CellDTO;
import dto.SheetDTO;

public class EngineImpl implements Engine {

    private Sheet sheet = null;

    public EngineImpl() {
        this.sheet = new SheetImpl("Brown Sheet");
    }

    @Override
    public boolean loadXmlFile(String filePath) {
        return true;
    }

    @Override
    public SheetDTO getSheetAsDTO (){
        return new SheetDTO(this.sheet);
    }

    @Override
    public CellDTO geCellAsDTO(String cellID){
         return new CellDTO(this.sheet.getCell(cellID));
    }

    @Override
    public CellDTO updateCellData(String cellId) {
        return null;
    }
}
