package logic.function;
import component.sheet.api.SheetReadOnly;
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
    public Returnable invoke(SheetReadOnly sheet) {
        return calculate(firstArgument.invoke(sheet), secondArgument.invoke(sheet), thirdArgument.invoke(sheet));
    }

    abstract protected Returnable calculate(Returnable result1, Returnable result2, Returnable result3);

}