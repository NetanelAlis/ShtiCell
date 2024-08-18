package component;

import logic.function.returnable.Returnable;
import java.util.List;

public interface Cell {

    String getCellId();
    String getOrignalValue();
    int getRow();
    int getCol();
    int getVersion();
    List<Cell> getDependsOn();
    List<Cell> getInfluecningOn();
    void setOrignalValue(String value);
    Returnable getEffectiveValue();

    static String createCellId(int row, int col){
        return row + ":" + col;
    }

}
