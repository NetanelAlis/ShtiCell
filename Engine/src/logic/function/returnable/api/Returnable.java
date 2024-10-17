package logic.function.returnable.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import component.cell.api.CellType;

import java.io.Serializable;

public interface Returnable extends Serializable {
    CellType getCellType();
    Object getValue();
    <T> T tryConvertTo(Class<T> type);
    }
