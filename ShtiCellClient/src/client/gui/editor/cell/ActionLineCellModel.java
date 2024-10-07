package client.gui.editor.cell;

import javafx.beans.property.StringProperty;

public interface ActionLineCellModel {
    void bind(StringProperty cellIDProperty, StringProperty lastUpdatedVersionProperty);
    StringProperty getCellIDProperty();
    StringProperty getLastUpdatedVersionProperty();
}
