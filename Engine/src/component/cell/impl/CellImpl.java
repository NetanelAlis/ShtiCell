package component.cell.impl;

import component.cell.api.Cell;
import component.sheet.api.SheetReadOnly;
import logic.parser.FunctionParser;
import logic.function.returnable.Returnable;
import java.util.List;

public class CellImpl implements Cell {

    private final String cellID;
    int row;
    int col;
    private String orignalValue;
    private Returnable effectiveValue;
    private int version;
    List<Cell> dependentCells;
    List<Cell> influecningCells;
    SheetReadOnly sheet;

    CellImpl(int row, int col, String orignalValue, int version, List<Cell> dependentCells, List<Cell> influecningCells, SheetReadOnly sheet){
        this.cellID = "CellID";
        this.orignalValue = orignalValue;
        this.sheet = sheet;
        this.effectiveValue = FunctionParser.parseFunction(this.orignalValue).invoke(this.sheet);
        this.version = version;
        this.dependentCells = dependentCells;
        this.influecningCells = influecningCells;
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

}
