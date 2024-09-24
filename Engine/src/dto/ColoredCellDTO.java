package dto;

import component.cell.api.Cell;
import component.cell.impl.SerializableColor;
import javafx.scene.paint.Color;
import logic.function.returnable.Returnable;
import java.util.ArrayList;
import java.util.List;

public class ColoredCellDTO{

    private final String cellID;
    private final Returnable effectiveValue;
    private final boolean isActive;
    private SerializableColor backgroundColor;
    private SerializableColor textColor;

    public ColoredCellDTO(Cell cell, String cellID){
        this.backgroundColor = new SerializableColor(Color.WHITE);
        this.textColor = new SerializableColor(Color.BLACK);

        if(cell != null){
            this.cellID = cell.getCellId();
            this.effectiveValue = cell.getEffectiveValue();
            this.isActive = true;
        }
        else{
            this.cellID = cellID;
            this.effectiveValue = null;
            this.isActive = false;
        }
    }

    public String getCellId() {
        return this.cellID;
    }
    public Returnable getEffectiveValue() {
        return this.effectiveValue;
    }
    public SerializableColor getBackgroundColor() {return this.backgroundColor;}
    public SerializableColor getTextColor() {return this.textColor;}

}
