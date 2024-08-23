package component.cell.impl;

import component.cell.api.Cell;
import component.sheet.api.ReadOnlySheet;
import logic.parser.FunctionParser;
import logic.function.returnable.Returnable;
import java.util.List;

public class CellImpl implements Cell {

    private final String cellID;;
    private String orignalValue;
    private Returnable effectiveValue;
    private int version;
    List<Cell> dependentCells;
    List<Cell> influecningCells;
    ReadOnlySheet sheet;

    public CellImpl(String cellID, String orignalValue, int version, ReadOnlySheet sheet){
        this.cellID = cellID;
        this.orignalValue = orignalValue;
        this.sheet = sheet;
        this.version = version;
    }
    @Override
    public String getCellId() {
        return this.cellID;
    }

    @Override
    public String getOrignalValue() {
        return this.orignalValue;
    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public List<Cell> getDependsOn() {
        return this.dependentCells;
    }

    @Override
    public List<Cell> getInfluecningOn() {
        return this.influecningCells;
    }

    @Override
    public void setOrignalValue(String value) {
        this.orignalValue = value;
    }

@Override
    public Returnable getEffectiveValue() {
        return this.effectiveValue;
    }

    @Override
    public boolean calculateEffectiveValue(){
        Returnable newEffectiveValue = FunctionParser.parseFunction(this.orignalValue).invoke(this.sheet);

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
