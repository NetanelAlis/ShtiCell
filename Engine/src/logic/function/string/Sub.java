package logic.function.string;

import component.cell.api.CellType;
import logic.function.Function;
import logic.function.TrinaryFunction;
import logic.function.returnable.api.Returnable;
import logic.function.returnable.impl.SpecialValues;
import logic.function.returnable.impl.ReturnableImpl;

public class Sub extends TrinaryFunction {
    private final String name = "CONCAT";

    public Sub(Function argument1, Function argument2, Function argument3) {
        super(argument1, argument2, argument3);
    }

    @Override
    public Returnable calculate(Returnable source, Returnable i_StartIndex, Returnable i_IndIndex) {
        try {
            double startIndex, endIndex;
            String original = source.tryConvertTo(String.class);
            startIndex = i_StartIndex.tryConvertTo(Double.class);
            endIndex = i_IndIndex.tryConvertTo(Double.class);

            return validateIndices(original, startIndex, endIndex) ?
                    new ReturnableImpl(original.substring((int)startIndex, (int)endIndex), CellType.STRING) :
                    SpecialValues.UNDEFINED;

        } catch (ClassCastException | UnsupportedOperationException e) {
            return SpecialValues.UNDEFINED;
        }
    }

    private boolean validateIndices(String source, double start, double end) {
        return isNaturalNumber(start)
                && isNaturalNumber(end)
                && start >= 0
                && end <= source.length()
                && start <= end;
    }

    public boolean isNaturalNumber(double value) {
        return value - (double)((int) value) == 0;
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }

    @Override
    public CellType getReturnType() {
        return CellType.NUMERIC;
    }
}

