package logic.function.returnable;

import component.api.CellType;

import java.util.Objects;

public class ReturnableImpl implements Returnable {

    private Object value;
    private CellType cellType;

   public ReturnableImpl(Object value, CellType type) {
        this.value = value;
        this.cellType = type;
    }

    public CellType getCellType() {
        return this.cellType;
    }

    @Override
    public Object getValue() {
        return this.value;
    }


    @Override
    public <T> T tryConvertTo(Class<T> type) {
        if (cellType.isAssignableFrom(type)) {
            return type.cast(this.value);
        } else {
            throw new ClassCastException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Returnable that = (Returnable) o;

        if (cellType != that.getCellType()) return false;
        return Objects.equals(value, that.getValue());
    }

    @Override
    public int hashCode() {
        int result = cellType != null ? cellType.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
