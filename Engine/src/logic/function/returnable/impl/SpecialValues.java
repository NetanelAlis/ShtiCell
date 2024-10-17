package logic.function.returnable.impl;

import com.google.gson.Gson;
import component.cell.api.CellType;
import logic.function.returnable.api.Returnable;

public enum SpecialValues implements Returnable {
    NAN{
        @Override
        public Object getValue() {
            return Double.NaN;
        }
    },
    UNDEFINED{
        @Override
        public Object getValue() {
            return "!UNDEFINED!";
        }
    },
    EMPTY{
        @Override
        public Object getValue() {
            return "";
        }
    },
    UNKNOWN{
        @Override
        public Object getValue() {
            return "UNKNOWN";
        }
    };

    @Override
    public CellType getCellType() {
        return CellType.NO_VALUE;
    }



    @Override
    public <T> T tryConvertTo(Class<T> type) {
        throw new UnsupportedOperationException("Cannot convert Anything to ERROR_VALUE");
    }
}
