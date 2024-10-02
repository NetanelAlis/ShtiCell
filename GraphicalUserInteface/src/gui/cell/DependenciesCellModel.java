package gui.cell;

import javafx.beans.property.StringProperty;

import java.util.List;

public interface DependenciesCellModel {
    StringProperty getSelectedCellProperty();
    List<String> getDependingOn() ;
    List<String> getInfluencingOn() ;
    void setDependingOn(List<String> dependingOn);
    void setInfluencingOn(List<String> influencingOn);
    void setSelectedCell(String cellId);
    boolean isSelectedCell(String cellId);
}

