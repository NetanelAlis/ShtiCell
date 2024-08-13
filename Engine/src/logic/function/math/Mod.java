package logic.function.math;

import logic.function.Function;
import logic.function.returnable.Number;
import logic.function.returnable.Returnable;
import logic.function.BinaryFunction;

public class Mod extends BinaryFunction {

    private final java.lang.String name = "MOD";

    public Mod(Function argument1, Function argument2) {
        super(argument1, argument2);
    }

    @Override
    protected Returnable calculate(Returnable firstNumber, Returnable secondNumber) {
        return (validateArgumentsTypes(firstNumber, secondNumber) ?
                new Number((double) firstNumber.getValue() % (double) secondNumber.getValue()) :
                new Number(Double.NaN));
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }

    @Override
    protected boolean validateArgumentsTypes(Returnable firstNumber, Returnable secondNumber){
        return firstNumber instanceof Number && secondNumber instanceof Number;
    }


}
