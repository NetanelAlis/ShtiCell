package logic.function.math;

import component.api.CellType;
import logic.function.Function;
import logic.function.returnable.SpecialValue;
import logic.function.returnable.Returnable;
import logic.function.BinaryFunction;
import logic.function.returnable.ReturnableImpl;


public class Percent extends BinaryFunction {

    private final java.lang.String name = "PERCENT";

    public Percent(Function argument1, Function argument2) {
        super(argument1, argument2);
    }

    @Override
    protected Returnable calculate(Returnable percent, Returnable number) {
        try {
            return new ReturnableImpl((percent.tryConvertTo(Double.class) * number.tryConvertTo(Double.class))/100, CellType.NUMERIC);
        } catch (ClassCastException | UnsupportedOperationException e) {
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
