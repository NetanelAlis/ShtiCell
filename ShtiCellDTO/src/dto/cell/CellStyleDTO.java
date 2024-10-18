package dto.cell;


import component.cell.impl.SerializableColor;
import javafx.scene.paint.Color;

public class CellStyleDTO {
    private String cellID;
    private SerializableColor backgroundColor;  // Store as hex color string
    private SerializableColor textColor;        // Store as hex color string

    public CellStyleDTO(String cellID, Color backgroundColor, Color textColor) {
        this.cellID = cellID;
        this.backgroundColor = new SerializableColor(backgroundColor);
        this.textColor = new SerializableColor(textColor);
    }

    public String getCellID() {
        return cellID;
    }

    public Color getBackgroundColor() {
        return backgroundColor.getColor();
    }

    public Color getTextColor() {
        return textColor.getColor();
    }
}