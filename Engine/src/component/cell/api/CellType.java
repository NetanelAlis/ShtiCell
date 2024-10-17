package component.cell.api;

import logic.function.returnable.impl.SpecialValues;

public enum CellType {
    NUMERIC(Double.class){
        @Override
        public String toString(){
            return "Numeric";
        }
    },
    STRING(String.class) {
        @Override
        public String toString() {
            return "String";
        }
    },
    BOOLEAN(Boolean.class) {
        @Override
        public String toString() {
            return "Boolean";
        }
    },
    UNKNOWN(Void.class) {
        @Override
        public String toString() {
            return "Unknown";
        }
    },
    NO_VALUE(SpecialValues.class) {
        @Override
        public String toString() {
            return "No Value";
        }
    };

    private final Class<?> type;

    CellType(Class<?> type) {
        this.type = type;
    }

    public boolean isAssignableFrom(Class<?> aType) {
        return type.isAssignableFrom(aType);
    }
}
