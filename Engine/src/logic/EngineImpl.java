package logic;

import component.sheet.api.Sheet;
import component.sheet.impl.SheetImpl;
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
    public void updateCellData(String cellId) {
        this.sheet = this.sheet.updateCellValueAndCalculate(cellId, "helloWorld");
    }
}
