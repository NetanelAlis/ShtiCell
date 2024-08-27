package logic;

import component.cell.api.Cell;
import component.cell.impl.CellImpl;
import component.sheet.api.Sheet;
import component.sheet.impl.SheetImpl;
import dto.CellDTO;
import dto.SheetDTO;
import jakarta.xml.bind.JAXBException;
import jaxb.converter.XMLToSheetConverter;
import java.io.FileNotFoundException;

public class EngineImpl implements Engine {

    private Sheet sheet = null;

    @Override
    public boolean LoadDataFromXML(String path) {
        try {
            XMLToSheetConverter converter = new jaxb.converter.XMLToSheetConverterImpl();
            this.sheet = converter.convert(path);
            return true;
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException("Error loading data from XML", e);
        }

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
    public void updateSingleCellData(String cellId, String value) {;
        SheetImpl newSheetVersion = this.sheet.copySheet();
        updateCell(cellId, value, newSheetVersion);
        this.sheet = this.sheet.updateSheet(newSheetVersion);
}

    @Override
    public boolean isSheetLoaded() {
        return this.sheet != null;
    }

    private void updateCell(String cellToUpdateID, String value, Sheet newSheetVersion){
    Cell updatedCell = newSheetVersion.getCell(cellToUpdateID);
    if(updatedCell != null){
        updatedCell.setOriginalValue(value, newSheetVersion.getVersion() + 1);
    }
    else{
        updatedCell = new CellImpl(cellToUpdateID, value, newSheetVersion.getVersion() , newSheetVersion);
        newSheetVersion.getSheetCells().put(updatedCell.getCellId(), updatedCell);
    }
}

}
