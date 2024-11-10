package logic.function.bool;

import component.cell.api.CellType;
import logic.function.BinaryFunction;
import logic.function.Function;
import logic.function.returnable.api.Returnable;
import logic.function.returnable.impl.ReturnableImpl;
import logic.function.returnable.impl.SpecialValues;

public class Bigger extends BinaryFunction {
    private final String name = "BIGGER";
    
    public Bigger(Function argument1, Function argument2) {
        super(argument1, argument2);
    }
    
    @Override
    protected Returnable calculate(Returnable argument1, Returnable argument2) {
        try {
            return new ReturnableImpl(
                    argument1.tryConvertTo(Double.class) >= argument2.tryConvertTo(Double.class),
                    CellType.BOOLEAN);
        } catch (ClassCastException | UnsupportedOperationException e) {
            return SpecialValues.UNKNOWN;
        }
    }
    
    @Override
    public CellType getReturnType() {
        return CellType.BOOLEAN;
    }
    
    @Override
    public String getFunctionName() {
        return this.name;
    }
}
