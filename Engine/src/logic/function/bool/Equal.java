package logic.function.bool;

import component.api.CellType;
import logic.function.Function;
import logic.function.returnable.Returnable;
import logic.function.BinaryFunction;
import logic.function.returnable.ReturnableImpl;


public class Equal extends BinaryFunction {

    private final java.lang.String name = "EQUAL";

    public Equal(Function argument1, Function argument2) {
        super(argument1, argument2);
    }

    @Override
    protected Returnable calculate(Returnable argument1, Returnable argument2) {
        return new ReturnableImpl(argument1.equals(argument2), CellType.BOOLEAN);
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
