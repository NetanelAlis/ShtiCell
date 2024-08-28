package logic.function.math;

import component.api.CellType;
import logic.function.Function;
import logic.function.returnable.SpecialValue;
import logic.function.returnable.Returnable;
import logic.function.BinaryFunction;
import logic.function.returnable.ReturnableImpl;

public class Mod extends BinaryFunction {

    private final java.lang.String name = "MOD";

    public Mod(Function argument1, Function argument2) {
        super(argument1, argument2);
    }

    @Override
    protected Returnable calculate(Returnable argument1, Returnable argument2) {
        try {
            return new ReturnableImpl(argument1.tryConvertTo(Double.class) % argument2.tryConvertTo(Double.class), CellType.NUMERIC);
        } catch (ClassCastException e) {
            return SpecialValue.NAN;
        }
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }

    @Override
    public CellType getReturnType() {
        return CellType.NUMERIC;
    }

}
