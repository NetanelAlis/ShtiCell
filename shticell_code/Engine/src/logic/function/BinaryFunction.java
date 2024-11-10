package logic.function;

import component.sheet.api.ReadonlySheet;
import logic.function.returnable.api.Returnable;

public abstract class BinaryFunction implements Function {
    private Function argument1;
    private Function argument2;

    public BinaryFunction(Function argument1, Function argument2) {
        this.argument1 = argument1;
        this.argument2 = argument2;
    }

    @Override
    public Returnable invoke(ReadonlySheet sheet) {
        return calculate(this.argument1.invoke(sheet), this.argument2.invoke(sheet));
    }

    @Override
    public String toString() {
        return "{" + this.getFunctionName() + "," + this.argument1 + "," + this.argument2 + "}";
    }

    abstract protected Returnable calculate(Returnable argument1, Returnable argument2);
}
