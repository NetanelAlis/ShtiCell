package logic.function.math;
import logic.function.Function;
import logic.function.returnable.Number;
import logic.function.returnable.Returnable;
import logic.function.UnaryFunction;

public class Abs extends UnaryFunction {

    private final java.lang.String name = "ABS";

    public Abs(Function argument) {
        super(argument);
    }

    @Override
    protected Returnable calculate(Returnable argument) {
        return argument instanceof Number ?  new Number(Math.abs((double) argument.getValue())) :
                new Number(Double.NaN);
        }

    @Override
    public java.lang.String getFunctionName() {
        return this.name;
    }

}
