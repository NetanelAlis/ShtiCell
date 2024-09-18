package logic.function.bool;

import component.api.CellType;
import logic.function.Function;
import logic.function.UnaryFunction;
import logic.function.returnable.Returnable;
import logic.function.returnable.ReturnableImpl;
import logic.function.returnable.SpecialValue;

public class Not extends UnaryFunction {
    private String name = "NOT";

    public Not(Function arg) {
        super(arg);
    }

    @Override
    protected Returnable calculate(Returnable arg) {
        try {
            return new ReturnableImpl(!arg.tryConvertTo(Boolean.class), CellType.BOOLEAN);
        } catch (ClassCastException | UnsupportedOperationException e)  {
            return SpecialValue.UNKNOWN;
        }
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }

    @Override
    public CellType getReturnType() {
        return CellType.BOOLEAN;
    }
}
