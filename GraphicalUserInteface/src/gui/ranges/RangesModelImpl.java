package gui.ranges;

import dto.RangeDTO;
import dto.RangesDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class RangesModelImpl implements RangesModel {
    
    private final ObservableList<RangeDTO> ranges;
    private RangeDTO selectedRange;
    
    public RangesModelImpl(ListView<RangeDTO> ranges, RangesDTO rangesDTO) {
        this.ranges = FXCollections.observableArrayList();
        this.ranges.addAll(rangesDTO.getRanges());
        
        ranges.setItems(this.ranges);
        this.selectedRange = ranges.getSelectionModel().getSelectedItem();
    }
    
    @Override
    public ObservableList<RangeDTO> rangesProperty() {
        return this.ranges;
    }
    
    @Override
    public void deleteRange(RangeDTO rangeToDelete) {
        this.ranges.remove(rangeToDelete);
    }
    
    @Override
    public RangeDTO getSelectedRange() {
        return this.selectedRange;
    }
    
    @Override
    public void setSelectedRange(RangeDTO rangeDTO) {
        this.selectedRange = rangeDTO;
    }
}
