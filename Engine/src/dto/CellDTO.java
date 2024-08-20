package dto;

import component.api.Cell;
import logic.function.parser.FunctionParser;
import logic.function.returnable.Returnable;

import java.util.ArrayList;
import java.util.List;

public class CellDTO {

    private final String cellID;
    private final String orignalValue;
    private final Returnable effectiveValue;
    private final int version;
    private final List<String> dependentCells;
    private final List<String> influecningCells;

    public CellDTO(Cell cell){
        this.cellID = cell.getCellId();
        this.orignalValue = cell.getOrignalValue();
        this.effectiveValue = cell.getEffectiveValue();
        this.version = cell.getVersion();
        this.dependentCells = new ArrayList<>();
        this.influecningCells = new ArrayList<>();

        for(Cell dependantCell : cell.getDependsOn()){
            this.dependentCells.add(cell.getCellId());
        }

        for(Cell influenceCell : cell.getInfluecningOn()){
            this.dependentCells.add(cell.getCellId());
        }

    }

    public String getCellId() {
        return this.cellID;
    }

    public String getOrignalValue() {
        return this.orignalValue;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

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
    public void createEffectiveValue() {
        this.effectiveValue = FunctionParser.parseFunction(this.orignalValue).invoke();
    }

}
