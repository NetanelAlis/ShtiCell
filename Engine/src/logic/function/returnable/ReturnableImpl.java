package logic.function.returnable;

import component.api.CellType;

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

}
