package dto.returnable;

import logic.function.returnable.api.Returnable;

public class EffectiveValueDTO {
    private String effectiveValue;
    private String cellType;

    public EffectiveValueDTO(Returnable returnable) {
        this.effectiveValue = returnable.getValue().toString();
        this.cellType = returnable.getCellType().toString();
    }

    public String getEffectiveValue() {
        return effectiveValue;
    }

    public String getCellType() {
        return cellType;
    }
}
