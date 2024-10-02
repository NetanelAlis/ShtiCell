package logic.function;

import component.sheet.api.ReadOnlySheet;
import logic.function.returnable.api.Returnable;

abstract public class TrinaryFunction implements Function {
    private Function argument1;
    private Function argument2;
    private Function argument3;

    public TrinaryFunction(Function argument1, Function argument2, Function argument3) {
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.argument3 = argument3;
    }

    @Override
    public Returnable invoke(ReadOnlySheet sheet) {
        return calculate(this.argument1.invoke(sheet), this.argument2.invoke(sheet), this.argument3.invoke(sheet));
    }

    @Override
    public String toString() {
        return "{" + this.getFunctionName() + "," + this.argument1 + "," + this.argument2 + "," + this.argument3 + "}";
    }

    abstract protected Returnable calculate(Returnable argument1, Returnable argument2, Returnable argument3);
}
