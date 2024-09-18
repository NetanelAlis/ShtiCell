package logic.function.bool;

import component.api.CellType;
import logic.function.Function;
import logic.function.returnable.Returnable;
import logic.function.BinaryFunction;
import logic.function.returnable.ReturnableImpl;
import logic.function.returnable.SpecialValue;


public class Bigger extends BinaryFunction {

    private final java.lang.String name = "BIGGER";

    public Bigger(Function argument1, Function argument2) {
        super(argument1, argument2);
    }

    @Override
    protected Returnable calculate(Returnable argument1, Returnable argument2) {
        try {
            return new ReturnableImpl(argument1.tryConvertTo(Double.class) >= argument2.tryConvertTo(Double.class), CellType.BOOLEAN);
        } catch (ClassCastException | UnsupportedOperationException e) {
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