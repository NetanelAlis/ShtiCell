package logic.function.returnable;

import component.api.CellType;

public enum ErrorValue implements Returnable {

    UNDEFINED(){
        @Override
        public Object getValue() {
            return "!UNDEFINED!";
        }
    },

    NAN(){
        @Override
        public Object getValue() {
            return Double.NaN;
        }
    };

    @Override
    public CellType getCellType() {return CellType.NO_VALUE;}

    @Override
    public <T> T tryConvertTo(Class<T> type) {
        throw new UnsupportedOperationException("Cannot convert to Anything to ERROR_VALUE");
    }

}
