package dto;

import component.cell.api.Cell;
import component.cell.impl.SerializableColor;
import javafx.scene.paint.Color;
import logic.function.returnable.Returnable;
import java.util.ArrayList;
import java.util.List;

public class CellDTO {

    private final String cellID;
    private final String originalValue;
    private final Returnable effectiveValue;
    private final int version;
    private final List<String> dependingOnCellsID;
    private final List<String> influencingCellsID;
    private final boolean isActive;
    private SerializableColor backgroundColor;
    private SerializableColor textColor;

    public CellDTO(Cell cell, String cellID){
        this.backgroundColor = new SerializableColor(Color.WHITE);
        this.textColor = new SerializableColor(Color.BLACK);

        if(cell != null){
            this.cellID = cell.getCellId();
            this.originalValue = cell.getOriginalValue();
            this.effectiveValue = cell.getEffectiveValue();
            this.version = cell.getVersion();
            this.isActive = true;
            this.dependingOnCellsID = new ArrayList<>();
            this.influencingCellsID = new ArrayList<>();
            for(Cell dependantCell : cell.getDependsOn()){
                this.dependingOnCellsID.add(dependantCell.getCellId());
            }

            for(Cell influenceCell : cell.getInfluecningOn()){
                this.influencingCellsID.add(influenceCell.getCellId());
            }
        }
        else{
            this.cellID = cellID;
            this.originalValue = "";
            this.effectiveValue = null;
            this.version = 0;
            this.isActive = false;
            this.dependingOnCellsID = null;
            this.influencingCellsID = null;;
        }
    }

    public String getCellId() {
        return this.cellID;
    }

    public String getOriginalValue() {
        return this.originalValue;
    }

    public int getVersion() {
        return this.version;
    }

    public List<String> getDependsOn() {
        return this.dependingOnCellsID;
    }

    public List<String> getInfluencingOn() {
        return this.influencingCellsID;
    }

    public Returnable getEffectiveValue() {
        return this.effectiveValue;
    }

    public boolean isActive(){
        return this.isActive;
    }

    public SerializableColor getBackgroundColor() {return this.backgroundColor;}
    public SerializableColor getTextColor() {return this.textColor;}

}
