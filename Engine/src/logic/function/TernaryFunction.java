package logic.function;
import logic.function.returnable.Returnable;

public abstract class TernaryFunction implements Function {
    private Function firstArgument;
    private Function secondArgument;
    private Function thirdArgument;

    public TernaryFunction(Function firstArgument, Function secondArgument, Function thirdArgument) {
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
        this.thirdArgument = thirdArgument;
    }

    @Override
    public Returnable invoke() {
        return calculate(firstArgument.invoke(), secondArgument.invoke(), thirdArgument.invoke());
    }

    abstract protected Returnable calculate(Returnable result1, Returnable result2, Returnable result3);
    abstract protected boolean validateArgumentsTypes(Returnable result1, Returnable result2, Returnable result3);
}