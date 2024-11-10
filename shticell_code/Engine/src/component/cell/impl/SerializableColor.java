package component.cell.impl;

import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.Objects;

public class SerializableColor implements Serializable {

    private double red;
    private double green;
    private double blue;
    private double opacity;

    public SerializableColor(Color color) {
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.opacity = color.getOpacity();
    }

    public Color getColor() {
        return new Color(red, green, blue, opacity);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerializableColor that = (SerializableColor) o;
        return Double.compare(red, that.red) == 0 && Double.compare(green, that.green) == 0 && Double.compare(blue, that.blue) == 0 && Double.compare(opacity, that.opacity) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue, opacity);
    }
}
