package logic.function.math;

import component.api.CellType;
import logic.function.Function;
import logic.function.returnable.MyNumber;
import logic.function.returnable.NoValue;
import logic.function.returnable.Returnable;
import logic.function.BinaryFunction;



public class Plus extends BinaryFunction {

    private final java.lang.String name = "PLUS";

    public Plus(Function argument1, Function argument2) {
        super(argument1, argument2);
    }

    @Override
    protected Returnable calculate(Returnable firstNumber, Returnable secondNumber) {
        return ValidateArgumentsTypes(firstNumber, secondNumber) ?
                new MyNumber((double) firstNumber.getValue() + (double) secondNumber.getValue()) :
                 NoValue.NAN;
        }

    @Override
    public String getFunctionName() {
        return this.name;
    }

    @Override
    public CellType returnType() {
            return CellType.NUMERIC;
    }

    @Override
    protected boolean validateArgumentsTypes(Returnable firstNumber, Returnable secondNumber){
        return firstNumber instanceof MyNumber && secondNumber instanceof MyNumber;
    }


        protected boolean ValidateArgumentsTypes(Returnable firstNumber, Returnable secondNumber){
        return firstNumber.returnType().equals(CellType.NOVALUE) || secondNumber.returnType().equals(CellType.NOVALUE);

        }
    }
}
