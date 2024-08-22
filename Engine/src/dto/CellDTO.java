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
    private final List<String> dependentCellsID;
    private final List<String> influecningCellsID;
    private final boolean isActive;

    public CellDTO(Cell cell){
        this.dependentCellsID = new ArrayList<>();
        this.influecningCellsID = new ArrayList<>();

        if(cell != null){
            this.cellID = cell.getCellId();
            this.orignalValue = cell.getOrignalValue();
            this.effectiveValue = cell.getEffectiveValue();
            this.version = cell.getVersion();
            this.isActive = true;

            for(Cell dependantCell : cell.getDependsOn()){
                this.dependentCellsID.add(cell.getCellId());
            }

            for(Cell influenceCell : cell.getInfluecningOn()){
                this.dependentCellsID.add(cell.getCellId());
            }
        }
        else{
            this.cellID = null;
            this.orignalValue = "";
            this.effectiveValue = null;
            this.version = 0;
            this.isActive = false;
        }
    }

    public String getCellId() {
        return this.cellID;
    }

    public String getOrignalValue() {
        return this.orignalValue;
    }

    public int getVersion() {
        return this.version;
    }

    public List<String> getDependsOn() {
        return this.dependentCellsID;
    }

    public List<String> getInfluecningOn() {
        return this.influecningCellsID;
    }

    public Returnable getEffectiveValue() {
        return this.effectiveValue;
    }

//    public void createEffectiveValue() {
//        this.effectiveValue = FunctionParser.parseFunction(this.orignalValue).invoke();
//    }

}
