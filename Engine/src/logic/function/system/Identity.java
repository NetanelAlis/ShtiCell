package logic.function.system;

import component.api.CellType;
import component.sheet.api.ReadOnlySheet;
import logic.function.Function;
import logic.function.returnable.Returnable;
import logic.function.returnable.ReturnableImpl;

public class Identity implements Function {

    private Object value;
    private CellType type;

    public Identity(Object value, CellType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public String getFunctionName() {
        return "Identity";
    }

    @Override
    public Returnable invoke(ReadOnlySheet sheet) {
        return new ReturnableImpl(this.value, this.type);
    }

    @Override
    public CellType returnType() {
        return null;
    }
}
