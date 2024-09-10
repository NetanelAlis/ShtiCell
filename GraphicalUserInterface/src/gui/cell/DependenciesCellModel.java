package gui.cell;

import java.util.List;

public interface DependenciesCellModel {
     List<String> getDependsOnPropertyList(List<String> dependencies);
     List<String> getInfluencingOnPropertyList(List<String> influencing);
}
