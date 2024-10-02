package logic.function.string;

import component.cell.api.CellType;
import logic.function.BinaryFunction;
import logic.function.Function;
import logic.function.returnable.api.Returnable;
import logic.function.returnable.impl.SpecialValues;
import logic.function.returnable.impl.ReturnableImpl;

public class Concat extends BinaryFunction {
    private final String name = "CONCAT";

    public Concat(Function argument1, Function argument2) {
        super(argument1, argument2);
    }

    @Override
    public Returnable calculate(Returnable argument1, Returnable argument2) {
        try {
            return new ReturnableImpl(
                    argument1.tryConvertTo(String.class) + argument2.tryConvertTo(String.class),
                    CellType.STRING);
        } catch (ClassCastException | UnsupportedOperationException e) {
            return SpecialValues.UNDEFINED;
        }
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }

    @Override
    public CellType getReturnType() {
        return CellType.STRING;
    }
}

