package component.api;

import logic.function.returnable.SpecialValue;

public enum CellType {
        NUMERIC(Double.class),
        STRING(String.class) ,
        BOOLEAN(Boolean.class),
        NO_VALUE(SpecialValue.class),
        UNKNOWN(Void.class);

        private Class<?> type;

        CellType(Class<?> type) {
            this.type = type;
        }

        public boolean isAssignableFrom(Class<?> aType) {
            return type.isAssignableFrom(aType);
        }

}
