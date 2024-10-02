package logic.function.bool;

import component.cell.api.CellType;
import logic.function.Function;
import logic.function.UnariFunction;
import logic.function.returnable.api.Returnable;
import logic.function.returnable.impl.ReturnableImpl;
import logic.function.returnable.impl.SpecialValues;

public class Not extends UnariFunction {
    private final String name = "NOT";
    
    public Not(Function argument) {
        super(argument);
    }
    
    @Override
    protected Returnable calculate(Returnable argument) {
        try {
            return new ReturnableImpl(!argument.tryConvertTo(Boolean.class), CellType.BOOLEAN);
        } catch (ClassCastException | UnsupportedOperationException e) {
            return SpecialValues.UNKNOWN;
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
