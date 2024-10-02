package gui.ranges;

import dto.RangeDTO;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public interface RangesModel {
    ObservableList<RangeDTO> rangesProperty();
    void deleteRange(RangeDTO rangeDTO);
    RangeDTO getSelectedRange();
    void setSelectedRange(RangeDTO rangeDTO);
}
