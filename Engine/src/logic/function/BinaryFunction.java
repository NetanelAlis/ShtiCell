package logic.function;

import component.sheet.api.ReadOnlySheet;
import logic.function.returnable.Returnable;

public abstract class BinaryFunction implements Function {
    private Function firstArgument;
    private Function secondArgument;

    public BinaryFunction(Function firstArgument, Function secondArgument) {
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
    }

    public boolean areInstancesOf(Returnable obj1, Returnable obj2, Class<?> clazz) {
        return clazz.isInstance(obj1) && clazz.isInstance(obj2);
    }

    @Override
    public Returnable invoke(ReadOnlySheet sheet) {
        return calculate(firstArgument.invoke(sheet), secondArgument.invoke(sheet));
    }

    // Abstract method to be implemented by subclasses
    abstract protected Returnable calculate(Returnable result1, Returnable result2);

}