package logic.function;

import component.sheet.api.ReadOnlySheet;
import logic.function.returnable.Returnable;

public abstract class UnaryFunction implements Function {

    private Function argument;

    public UnaryFunction(Function firstArgument) {
        this.argument = firstArgument;
    }

    @Override
    public Returnable invoke(ReadOnlySheet sheet) {
        return calculate(argument.invoke(sheet));
    }

    abstract protected Returnable calculate(Returnable result1);

}