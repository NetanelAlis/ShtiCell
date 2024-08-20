package component.impl;

import component.api.Cell;
import logic.function.parser.FunctionParser;
import logic.function.returnable.Returnable;
import java.util.List;

public class CellImpl implements Cell {

    private final String cellId;
    int row;
    int col;
    private String orignalValue;
    private Returnable effectiveValue;
    private int version;
    List<Cell> dependentCells;
    List<Cell> influecningCells;

    CellImpl(int row, int col, String orignalValue, Returnable effectivealue, int version, List<Cell> dependentCells, List<Cell> influecningCells){
        this.cellId = Cell.createCellId(row, col);
        this.orignalValue = orignalValue;
        this.effectiveValue = effectivealue;
        this.version = version;
        this.dependentCells = dependentCells;
        this.influecningCells = influecningCells;
    }
    @Override
    public String getCellId() {
        return this.cellId;
    }

    @Override
    public String getOrignalValue() {
        return this.orignalValue;
    }

    @Override
    public int getRow() {
        return this.row;
    }

    @Override
    public int getCol() {
        return this.col;
    }

    @Override
    public int getVersion() {
        return 0;
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
    public void createEffectiveValue() {
        this.effectiveValue = FunctionParser.parseFunction(this.orignalValue).invoke();
    }

}
