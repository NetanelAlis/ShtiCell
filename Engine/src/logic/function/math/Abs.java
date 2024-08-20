package logic.function.math;
import component.api.CellType;
import logic.function.Function;
import logic.function.returnable.MyNumber;
import logic.function.returnable.Returnable;
import logic.function.UnaryFunction;

public class Abs extends UnaryFunction {

    private final java.lang.String name = "ABS";

    public Abs(Function argument) {
        super(argument);
    }

    @Override
    protected Returnable calculate(Returnable argument) {
        return argument instanceof MyNumber ?  new MyNumber(Math.abs((double) argument.getValue())) :
                new MyNumber(Double.NaN);
        }

    @Override
    public java.lang.String getFunctionName() {
        return this.name;
    }

    @Override
    public CellType returnType() {
        return CellType.NUMERIC;
    }

}
