package logic.function.math;

import component.cell.api.CellType;
import logic.function.BinaryFunction;
import logic.function.Function;
import logic.function.returnable.api.Returnable;
import logic.function.returnable.impl.ReturnableImpl;
import logic.function.returnable.impl.SpecialValues;

public class Percent extends BinaryFunction {
    private final String name = "PERCENT";
    
    public Percent(Function argument1, Function argument2) {
        super(argument1, argument2);
    }
    
    @Override
    protected Returnable calculate(Returnable part, Returnable whole) {
        try {
            return new ReturnableImpl(
                    part.tryConvertTo(Double.class) * whole.tryConvertTo(Double.class) / 100,
                    CellType.NUMERIC);
        } catch (ClassCastException | UnsupportedOperationException e) {
            return SpecialValues.NAN;
        }
    }
    
    @Override
    public CellType getReturnType() {
        return CellType.NUMERIC;
    }
    
    @Override
    public String getFunctionName() {
        return this.name;
    }
}
