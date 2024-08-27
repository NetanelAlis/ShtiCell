package component.cell.impl;

import component.cell.api.Cell;
import component.sheet.api.ReadOnlySheet;
import component.sheet.api.Sheet;
import jaxb.generated.STLCell;
import logic.parser.FunctionParser;
import logic.function.returnable.Returnable;
import logic.parser.RefParser;

import java.util.ArrayList;
import java.util.List;

public class CellImpl implements Cell {

    private final String cellID;;
    private String originalValue;
    private Returnable effectiveValue;
    private int version;
    List<Cell> dependingOnCells;
    List<Cell> influencingCells;
    ReadOnlySheet sheet;

    public CellImpl(String cellID, String orignalValue, int version, ReadOnlySheet sheet){
        this.cellID =  Character.toUpperCase(cellID.charAt(0)) + cellID.substring(1);
        this.originalValue = orignalValue;
        this.sheet = sheet;
        this.version = version;
        this.dependingOnCells = new ArrayList<>();
        this.influencingCells = new ArrayList<>();

        this.setDependencies();

    }

    private void setDependentAndInfluencingCells(String dependentCellID){
        Cell dependentCell = sheet.getCell(dependentCellID);

        if(dependentCell == null){
            dependentCell = new CellImpl(dependentCellID,"", version, sheet);
            this.sheet.getSheetCells().put(dependentCell.getCellId(), dependentCell);

        }

        this.dependingOnCells.add(dependentCell);
        dependentCell.getInfluecningOn().add(this);

    }

    private void setDependencies(){
        RefParser.PARSE.extractRefs(originalValue).stream()
                .filter(Sheet::isValidCellID)
                .forEach(this::setDependentAndInfluencingCells);
    }

    @Override
    public String getCellId() {
        return this.cellID;
    }

    @Override
    public String getOriginalValue() {
        return this.originalValue;
    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public List<Cell> getDependsOn() {
        return this.dependingOnCells;
    }

    @Override
    public List<Cell> getInfluecningOn() {
        return this.influencingCells;
    }

    @Override
    public void setOriginalValue(String value, int newSheetVersion) {
        this.originalValue = value;

        for(Cell cell : this.dependingOnCells){
            cell.getInfluecningOn().remove(this);
        }

        this.dependingOnCells.clear();
        this.setDependencies();

        if(value.equals(originalValue)){
            this.version = newSheetVersion;
        }

    }

@Override
    public Returnable getEffectiveValue() {
        return this.effectiveValue;
    }

    @Override
    public boolean calculateEffectiveValue(){
        Returnable newEffectiveValue = FunctionParser.parseFunction(this.originalValue).invoke(this.sheet);

        if(newEffectiveValue.equals(this.effectiveValue)){
            return false;
        }

        else{
            this.effectiveValue = newEffectiveValue;
            return true;
        }
    }

    @Override
    public void updateVersion(int sheetUpdatedVersion) {
        this.version = sheetUpdatedVersion;
    }


}
