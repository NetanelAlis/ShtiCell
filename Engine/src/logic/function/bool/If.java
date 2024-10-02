package logic.function.bool;

import component.cell.api.CellType;
import logic.function.Function;
import logic.function.TrinaryFunction;
import logic.function.returnable.api.Returnable;
import logic.function.returnable.impl.SpecialValues;

public class If extends TrinaryFunction {
    private final String name = "CONCAT";
    
    public If(Function argument1, Function argument2, Function argument3) {
        super(argument1, argument2, argument3);
    }
    
    @Override
    public Returnable calculate(Returnable condition, Returnable i_Then, Returnable i_Else) {
        try {
            if (i_Then.getCellType().equals(i_Else.getCellType())) {
                return condition.tryConvertTo(Boolean.class) ? i_Then : i_Else;
            } else {
                return SpecialValues.UNKNOWN;
            }
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
        return CellType.UNKNOWN;
    }
}
