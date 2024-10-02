package dto;

import component.cell.api.Cell;
import component.cell.impl.SerializableColor;
import javafx.scene.paint.Color;
import logic.function.returnable.api.Returnable;

import java.util.ArrayList;
import java.util.List;

public class ColoredCellDTO {
    private final String cellId;
    private final Returnable effectiveValue;
    private SerializableColor backgroundColor;
    private SerializableColor textColor;
    private final boolean isActive;
    
    
    public ColoredCellDTO(Cell cell, String cellID) {
        if (cell != null) {
            this.cellId = cell.getCellId();
            this.effectiveValue = cell.getEffectiveValue();
            this.backgroundColor = cell.getBackgroundColor();
            this.textColor = cell.getTextColor();
            this.isActive = true;
        } else {
            this.cellId = cellID;
            this.effectiveValue = null;
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
    
    public Returnable getEffectiveValue() {
        return this.effectiveValue;
    }
    
    public Color getBackgroundColor() {
        return this.backgroundColor.getColor();
    }
    
    public Color getTextColor() {
        return this.textColor.getColor();
    }
}
