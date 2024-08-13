package logic.function.string;

import logic.function.Function;
import logic.function.TernaryFunction;
import logic.function.returnable.Returnable;
import logic.function.returnable.String;
import logic.function.returnable.Number;

public class Sub extends TernaryFunction {
    private final java.lang.String name = "CONCAT";

    public Sub(Function argument1, Function argument2, Function argument3) {
        super(argument1, argument2, argument3);
    }

    @Override
    public Returnable calculate(Returnable source, Returnable startIndex, Returnable endIndex) {
        String subString = new String("!UNDEFINED!");

        if (this.validateArgumentsTypes(source, startIndex, endIndex)){
            if ((double)startIndex.getValue() >= 0 &&
                    (double)endIndex.getValue() <= ((java.lang.String)source.getValue()).length()){
                subString = new String(((java.lang.String)source.getValue()).substring(
                        (int)endIndex.getValue(), (int)startIndex.getValue()));
            }
        }

        return subString;
    }

    @Override
    protected boolean validateArgumentsTypes(Returnable argument1, Returnable argument2, Returnable argument3) {
        return argument1 instanceof String && argument2 instanceof Number && argument3 instanceof Number;
    }

    @Override
    public java.lang.String getFunctionName() {
        return this.name;
    }
}