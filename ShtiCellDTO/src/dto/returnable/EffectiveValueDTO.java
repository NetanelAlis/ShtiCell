package dto.returnable;

import logic.function.returnable.api.Returnable;

import java.util.Objects;

public class EffectiveValueDTO {
    private String effectiveValue;
    private String cellType;

    public EffectiveValueDTO(Returnable returnable) {
        this.effectiveValue = returnable.getValue().toString();
        this.cellType = returnable.getCellType().toString();
    }

    public EffectiveValueDTO(String part, String part1) {
        this.effectiveValue = part;
        this.cellType = part1;
    }

    public String getEffectiveValue() {
        return effectiveValue;
    }

    public String getCellType() {
        return cellType;
    }

    @Override
    public String toString() {
        return effectiveValue + "-" + cellType; // Or some other unique string representation
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EffectiveValueDTO that = (EffectiveValueDTO) o;
        return Objects.equals(effectiveValue, that.effectiveValue) && Objects.equals(cellType, that.cellType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(effectiveValue, cellType);
    }
}
