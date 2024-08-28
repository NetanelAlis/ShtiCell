package logic.function.math;
import component.api.CellType;
import logic.function.Function;
import logic.function.returnable.SpecialValue;
import logic.function.returnable.Returnable;
import logic.function.UnaryFunction;
import logic.function.returnable.ReturnableImpl;

public class Abs extends UnaryFunction {

    private final java.lang.String name = "ABS";

    public Abs(Function argument) {
        super(argument);
    }

    @Override
    protected Returnable calculate(Returnable argument) {
        try {
            return new ReturnableImpl(Math.abs(argument.tryConvertTo(Double.class)), CellType.NUMERIC);
        } catch (ClassCastException e) {
            return SpecialValue.NAN;
        }
    }

    @Override
    public java.lang.String getFunctionName() {
        return this.name;
    }

    @Override
    public CellType getReturnType() {
        return CellType.NUMERIC;
    }

}
