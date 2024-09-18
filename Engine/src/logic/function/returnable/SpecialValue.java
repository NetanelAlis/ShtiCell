package logic.function.returnable;

import component.api.CellType;

public enum SpecialValue implements Returnable {

    UNDEFINED(){
        @Override
        public Object getValue() {
            return "UNDEFINED";
        }
    },

    NAN(){
        @Override
        public Object getValue() {
            return Double.NaN;
        }
    },

    EMPTY(){
        @Override
        public Object getValue() {
            return  "";
        }
    },
    UNKNOWN{
        @Override public Object getValue(){
            return "UNKNOWN";
        }
    };

    @Override
    public CellType getCellType() {return CellType.NO_VALUE;}

    @Override
    public <T> T tryConvertTo(Class<T> type) {
        throw new UnsupportedOperationException("Cannot convert to Anything to ERROR_VALUE");
    }

}
