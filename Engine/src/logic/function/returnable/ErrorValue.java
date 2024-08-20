package logic.function.returnable;

import component.api.CellType;

public enum ErrorValue implements Returnable {

    UNDEFINED(){
    },

    NAN(){
    },
    ;

    @Override
    public CellType getCellType() {return CellType.NO_VALUE;}

    @Override
    public <T> T tryConvertTo(Class<T> type) {
        throw new UnsupportedOperationException("Cannot convert to Anything to ERROR_VALUE");
    }
}
