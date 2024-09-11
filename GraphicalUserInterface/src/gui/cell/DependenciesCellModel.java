package gui.cell;

import java.util.List;

public interface DependenciesCellModel {
     List<String> getDependsOnPropertyList();
     List<String> getInfluencingOnPropertyList();
     String getSelectedCellID();
     void setDependingOn(List<String> dependingOn);
     void setInfluencingOn(List<String> influencingOn);
     void setSelectedCell(String cellId);
}
