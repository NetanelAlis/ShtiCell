package logic;

import component.api.Sheet;
import component.impl.SheetImpl;
import dto.CellDTO;
import dto.SheetDTO;

public class EngineImpl implements Engine {

    private Sheet sheet = null;

    @Override
    public boolean loadXmlFile(String filePath) {
        return true;
    }

    @Override
    public SheetDTO getSheetAsDTO (){
        return new SheetDTO(this.sheet);
    }

    @Override
    public CellDTO geCellAsDTO(String cellId){
        return new CellDTO(this.sheet.getCell());
    }

    @Override
    public void updateCellData(String cellId) {

    }

}
