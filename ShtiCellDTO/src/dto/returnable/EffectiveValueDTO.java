package dto.returnable;

import logic.function.returnable.api.Returnable;

import java.util.Objects;

public class EffectiveValueDTO {
    private final String effectiveValue;
    private final String type;
    
    public EffectiveValueDTO(Returnable returnable) {
        this.effectiveValue = returnable.getValue().toString();
        this.type = returnable.getCellType().toString();
    }
    
    public EffectiveValueDTO(String effectiveValue, String type) {
        this.effectiveValue = effectiveValue;
        this.type = type;
    }
    
    public String getEffectiveValue() {return this.effectiveValue;}
    
    public String getType() {return this.type;}
    
    @Override
    public String toString() {
        return effectiveValue + "-" + type; // Or some other unique string representation
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EffectiveValueDTO that = (EffectiveValueDTO) o;
        return Objects.equals(effectiveValue, that.effectiveValue) && Objects.equals(type, that.type);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(effectiveValue, type);
    }
}
