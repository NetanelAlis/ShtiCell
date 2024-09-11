package gui.cell;

import javafx.beans.property.StringProperty;

public interface ActionLineCellModel {
    void bind(StringProperty cellIDProperty,StringProperty lastVersionProperty);
    StringProperty getCellIDProperty();
    StringProperty getLastVersionProperty();
}
