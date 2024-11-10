package dto.cell;

import component.cell.impl.SerializableColor;
import javafx.scene.paint.Color;

public class CellStyleDTO {
    private final String cellID;
    private final SerializableColor backgroundColor;
    private final SerializableColor textColor;
    
    public CellStyleDTO(String cellID, Color backgroundColor, Color textColor) {
        this.cellID = cellID;
        this.backgroundColor = new SerializableColor(backgroundColor);
        this.textColor = new SerializableColor(textColor);
    }
    
    public String getCellID() { return this.cellID; }
    
    public Color getBackgroundColor() { return this.backgroundColor.getColor(); }
    
    public Color getTextColor() { return this.textColor.getColor(); }
}
