package logic.function;

import logic.function.returnable.Returnable;

public abstract class UnaryFunction implements Function {

    private Function argument;

    public UnaryFunction(Function firstArgument) {
        this.argument = firstArgument;
    }

    @Override
    public Returnable invoke() {
        return (argument.invoke());
    }

    abstract protected Returnable calculate(Returnable result1);

}