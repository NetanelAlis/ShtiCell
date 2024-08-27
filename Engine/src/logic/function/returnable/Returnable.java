package logic.function.returnable;

import component.api.CellType;
import java.io.Serializable;

public interface Returnable extends Serializable {
    CellType getCellType();
    Object getValue();
    <T> T tryConvertTo(Class<T> clazz);
}
