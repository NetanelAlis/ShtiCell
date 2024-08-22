package component.cell.api;

import logic.function.returnable.Returnable;
import java.util.List;

public interface Cell {

    String getCellId();
    String getOrignalValue();;
    int getVersion();
    List<Cell> getDependsOn();
    List<Cell> getInfluecningOn();
    void setOrignalValue(String value);
    Returnable getEffectiveValue();
}
