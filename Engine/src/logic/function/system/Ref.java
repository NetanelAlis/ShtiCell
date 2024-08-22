package logic.function.system;

import component.api.CellType;
import logic.function.Function;
import logic.function.UnaryFunction;
import logic.function.returnable.Returnable;

public class Ref extends UnaryFunction {

    private final java.lang.String name = "REF";

    Ref(Function argument) {
        super(argument);}

    @Override
    protected Returnable calculate(Returnable result1) {
        return null;
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }

    @Override
    public CellType returnType() {
    return null;
    }

}
