package component.cell.api;

import logic.function.returnable.Returnable;
import java.io.Serializable;
import java.util.List;

public interface Cell extends Serializable{
    String getCellId();
    String getOriginalValue();;
    int getVersion();
    List<Cell> getDependsOn();
    List<Cell> getInfluecningOn();
    void setOriginalValue(String value, int newSheetVersion);
    Returnable getEffectiveValue();
    boolean calculateEffectiveValue();
    void updateVersion(int sheetUpdatedVersion);
}
