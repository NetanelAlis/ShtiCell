package gui.cell;

import javafx.beans.property.StringProperty;

public interface ActionLineCellModel {
    void bind(StringProperty cellIDProperty, StringProperty originalValueProperty, StringProperty lastVersionProperty);
    StringProperty getCellIDProperty();
    StringProperty getOriginalValueProperty();
    StringProperty getLastVersionProperty();
}
