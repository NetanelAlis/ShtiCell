package gui.ranges;

import dto.RangeDTO;
import dto.RangesDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.util.List;

public class RangeModel {
    private ObservableList<RangeDTO> rangesPropertiesDTO;
    private BooleanProperty rangesAreEmptyProperty;
    private BooleanProperty listViewItemNotSelectedProperty;
    private RangeDTO selectedRange;

    public RangeModel() {
        this.rangesPropertiesDTO = FXCollections.observableArrayList();
        this.rangesAreEmptyProperty = new SimpleBooleanProperty(true);
        this.listViewItemNotSelectedProperty  = new SimpleBooleanProperty(true);
        selectedRange = null;

    }

    public void bindAll(ListView<RangeDTO> listView, BooleanProperty rangesAreEmptyProperty,
                        BooleanProperty listViewItemSelectedProperty){
        listView.setItems(rangesPropertiesDTO);
        rangesAreEmptyProperty.bind(this.rangesAreEmptyProperty);
        listViewItemSelectedProperty.bind(this.listViewItemNotSelectedProperty);;
    }

    void addRange(RangeDTO range) {
        if(this.getSelectedRangesSize() == 0){
        this.rangesAreEmptyProperty.set(false);
        }
        rangesPropertiesDTO.add(range);
    }

    void removeRange(RangeDTO range) {
        rangesPropertiesDTO.remove(range);
    }

        void setListViewItemNotSelectedProperty(boolean selected) {
            this.listViewItemNotSelectedProperty.setValue(selected);
        }

         BooleanProperty getRangesAreEmptyProperty (){
        return this.rangesAreEmptyProperty;
         }
         void setRangesAreEmptyProperty(boolean value){
        this.rangesAreEmptyProperty.setValue(value);
         }
         BooleanProperty getListViewItemNotSelectedProperty(){
            return this.listViewItemNotSelectedProperty;
         }

         void setSelectedRange(RangeDTO range) {
            this.selectedRange = range;
         }

         RangeDTO getSelectedRange() {
        return this.selectedRange;
         }

         int getSelectedRangesSize(){
        return this.rangesPropertiesDTO.size();
         }

         public void initModel(ListView<RangeDTO> ranges, RangesDTO rangesDTO) {
            this.rangesPropertiesDTO.clear();
            this.rangesPropertiesDTO.addAll(rangesDTO.getRanges());
            ranges.setItems(rangesPropertiesDTO);
             this.rangesAreEmptyProperty.setValue(true);
             this.selectedRange = null;
         }
 }
