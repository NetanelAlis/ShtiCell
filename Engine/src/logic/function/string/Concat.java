package logic.function.string;

import logic.function.Function;
import logic.function.returnable.Returnable;
import logic.function.BinaryFunction;
import logic.function.returnable.MyString;

public class Concat extends BinaryFunction {

    private final java.lang.String name = "CONCAT";

    public Concat(Function argument1, Function argument2) {
        super(argument1, argument2);
    }

    @Override
    protected Returnable calculate(Returnable firstNumber, Returnable secondNumber) {
            return validateArgumentsTypes(firstNumber, secondNumber) ?
                new MyString((java.lang.String)firstNumber.getValue() + secondNumber.getValue()) :
                new MyString("!UNDEFINED!");
    }

    @Override
    public java.lang.String getFunctionName() {
        return this.name;
    }

    @Override
    protected boolean validateArgumentsTypes(Returnable firstNumber, Returnable secondNumber){
        return firstNumber instanceof MyString && secondNumber instanceof MyString;
    }

}
