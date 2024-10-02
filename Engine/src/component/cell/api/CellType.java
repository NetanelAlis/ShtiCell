package component.cell.api;

import logic.function.returnable.impl.SpecialValues;

public enum CellType {
    NUMERIC(Double.class),
    STRING(String.class),
    BOOLEAN(Boolean.class),
    UNKNOWN(Void.class),
    NO_VALUE(SpecialValues.class);

    private final Class<?> type;

    CellType(Class<?> type) {
        this.type = type;
    }

    public boolean isAssignableFrom(Class<?> aType) {
        return type.isAssignableFrom(aType);
    }
}
