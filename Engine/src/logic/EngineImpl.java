package logic;

import component.archive.api.Archive;
import component.archive.impl.ArchiveImpl;
import component.cell.api.Cell;
import component.cell.impl.CellImpl;
import component.sheet.api.Sheet;
import component.sheet.impl.SheetImpl;
import dto.CellDTO;
import dto.SheetDTO;
import dto.VersionsChangesDTO;
import jakarta.xml.bind.JAXBException;
import jaxb.converter.XMLToSheetConverter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EngineImpl implements Engine {

    private Sheet sheet = null;
    private Archive archive = null;

    @Override
    public void LoadDataFromXML(String path) {
        try {
            XMLToSheetConverter converter = new jaxb.converter.XMLToSheetConverterImpl();
            this.sheet = converter.convert(path);
            this.archive = new ArchiveImpl();
            this.archive.storeInArchive(this.sheet.copySheet());
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
         return new CellDTO(this.sheet.getCell(cellID), cellID);
    }

    @Override
    public void updateSingleCellData(String cellId, String value) {;
        Cell cellToUpdate = this.sheet.getCell(cellId);

        if(cellToUpdate != null && cellToUpdate.getOriginalValue().equals(value)) {
            return;
        }

        SheetImpl newSheetVersion = this.sheet.copySheet();
        updateCell(cellId, value, newSheetVersion);
        this.sheet = this.sheet.updateSheet(newSheetVersion);
        this.archive.storeInArchive(this.sheet.copySheet());
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

    @Override
    public VersionsChangesDTO getVersionsChangesAsDTO() {
        return new VersionsChangesDTO(this.archive.getAllVersionsChanges());
    }

    @Override
    public SheetDTO getSheetVersionsAsDTO(int version) {
        return new SheetDTO(this.archive.retrieveFromArchive(version));
    }

    @Override
    public void saveToFile(String path) {
        this.archive.saveToFile(path);
    }

    @Override
    public void LoadDataFromFile(String path) {
        this.archive = Archive.loadFromFile(path);
        this.sheet = this.archive.retrieveLastSheetVersionFromArchive();
    }
}
