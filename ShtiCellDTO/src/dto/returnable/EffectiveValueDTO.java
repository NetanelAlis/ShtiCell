package dto.returnable;

import logic.function.returnable.api.Returnable;

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
}
