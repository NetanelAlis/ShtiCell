package logic.function.math;

import component.cell.api.CellType;
import logic.function.BinaryFunction;
import logic.function.Function;
import logic.function.returnable.impl.SpecialValues;
import logic.function.returnable.api.Returnable;
import logic.function.returnable.impl.ReturnableImpl;

public class Divide extends BinaryFunction {
    private final String name = "DIVIDE";

    public Divide(Function argument1, Function argument2) {
        super(argument1, argument2);
    }

    @Override
    protected Returnable calculate(Returnable argument1, Returnable argument2) {
        try {
            return argument2.tryConvertTo(Double.class) == 0 ?
                    SpecialValues.NAN :
                    new ReturnableImpl(
                        argument1.tryConvertTo(Double.class) / argument2.tryConvertTo(Double.class),
                        CellType.NUMERIC);
        } catch (ClassCastException | UnsupportedOperationException e) {
            return SpecialValues.NAN;
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
