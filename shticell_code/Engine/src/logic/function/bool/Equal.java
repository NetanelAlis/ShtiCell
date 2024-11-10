package logic.function.bool;

import component.cell.api.CellType;
import logic.function.BinaryFunction;
import logic.function.Function;
import logic.function.returnable.api.Returnable;
import logic.function.returnable.impl.ReturnableImpl;

public class Equal extends BinaryFunction {
    private final String name = "EQUAL";
    
    
    public Equal(Function argument1, Function argument2) {
        super(argument1, argument2);
    }
    
    @Override
    protected Returnable calculate(Returnable argument1, Returnable argument2) {
        return new ReturnableImpl(argument1.equals(argument2), CellType.BOOLEAN);
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