package logic.function.returnable.impl;

import com.google.gson.Gson;
import component.cell.api.CellType;
import logic.function.returnable.api.Returnable;

import java.util.Objects;

public class ReturnableImpl implements Returnable {
    private final Object value;
    private final CellType cellType;

    public ReturnableImpl(Object value, CellType type) {
        this.value = value;
        this.cellType = type;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public CellType getCellType() {
        return this.cellType;
    }

    @Override
    public <T> T tryConvertTo(Class<T> type) {
        if (cellType.isAssignableFrom(type)) {
            return type.cast(this.value);
        } else {
            throw new ClassCastException("Cannot convert to " + type);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReturnableImpl that = (ReturnableImpl) o;
        return Objects.equals(value, that.value) && cellType == that.cellType;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value, cellType);
    }

}
