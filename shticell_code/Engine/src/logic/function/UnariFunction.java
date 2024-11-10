package logic.function;

import component.sheet.api.ReadonlySheet;
import logic.function.returnable.api.Returnable;

abstract public class UnariFunction implements Function{
    private Function argument;

    public UnariFunction(Function argument) {
        this.argument = argument;
    }

    protected Function getArgument() {
        return argument;
    }

    @Override
    public Returnable invoke(ReadonlySheet sheet) {
        return calculate(this.argument.invoke(sheet));
    }

    @Override
    public String toString() {
        return "{" + this.getFunctionName() + "," + this.argument.toString() + "}";
    }

    abstract protected Returnable calculate(Returnable argument);
}
