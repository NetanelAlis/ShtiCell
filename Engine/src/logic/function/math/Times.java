package logic.function.math;

import logic.function.Function;
import logic.function.returnable.MyNumber;
import logic.function.returnable.Returnable;
import logic.function.BinaryFunction;

public class Times extends BinaryFunction {

    private final java.lang.String name = "TIMES";

    public Times(Function argument1, Function argument2) {
        super(argument1, argument2);
    }

    @Override
    protected Returnable calculate(Returnable firstNumber, Returnable secondNumber) {
        return validateArgumentsTypes(firstNumber, secondNumber) ?
                new MyNumber((double) firstNumber.getValue() * (double) secondNumber.getValue()) :
                new MyNumber(Double.NaN);
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }

    @Override
    protected boolean validateArgumentsTypes(Returnable firstNumber, Returnable secondNumber){
        return firstNumber instanceof MyNumber && secondNumber instanceof MyNumber;
    }


}