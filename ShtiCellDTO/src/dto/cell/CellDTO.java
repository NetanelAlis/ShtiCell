package dto.cell;

import component.cell.api.Cell;
import component.cell.impl.SerializableColor;
import dto.returnable.EffectiveValueDTO;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class CellDTO {
    private final String cellId;
    private final String originalValue;
    private final EffectiveValueDTO effectiveValue;
    private final String updatedBy;
    private final int version;
    private SerializableColor backgroundColor;
    private SerializableColor textColor;
    private final List<String> dependingOn;
    private final List<String> influencingOn;
    private final boolean isActive;


    public CellDTO(Cell cell, String cellID) {
        if (cell != null) {
            this.cellId = cell.getCellId();
            this.originalValue = cell.getOriginalValue();
            this.effectiveValue = new EffectiveValueDTO(cell.getEffectiveValue());
            this.updatedBy = cell.getUpdatedBy();
            this.version = cell.getVersion();
            this.backgroundColor = cell.getBackgroundColor();
            this.textColor = cell.getTextColor();
            this.dependingOn = new ArrayList<>();
            this.influencingOn = new ArrayList<>();
            this.isActive = true;

            for (Cell dependantCell : cell.getDependentCells()) {
                this.dependingOn.add(dependantCell.getCellId());
            }

            for (Cell influencedCell : cell.getInfluencedCells()) {
                this.influencingOn.add(influencedCell.getCellId());
            }
        } else {
            this.cellId = cellID;
            this.originalValue = "";
            this.effectiveValue = null;
            this.version = 1;
            this.updatedBy = "";
            this.dependingOn = null;
            this.influencingOn = null;
            this.isActive = false;
            this.backgroundColor = new SerializableColor(Color.WHITE);
            this.textColor = new SerializableColor(Color.BLACK);
        }
    }

    public boolean isActive() {
        return this.isActive;
    }

    public String getCellId() {
        return this.cellId;
    }

    public String getOriginalValue() {
        return this.originalValue;
    }

    public EffectiveValueDTO getEffectiveValue() {
        return this.effectiveValue;
    }

    public int getVersion() {
        return this.version;
    }
    
    public String getUpdatedBy() { return this.updatedBy; }
    
    public Color getBackgroundColor() {
        return this.backgroundColor.getColor();
    }

    public Color getTextColor() {
        return this.textColor.getColor();
    }
    
    public List<String> getDependingOn() {
        return this.dependingOn;
    }

    public List<String> getInfluencingOn() {
        return this.influencingOn;
    }
}
