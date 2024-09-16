package logic.function.string;

import component.api.CellType;
import logic.function.Function;
import logic.function.TernaryFunction;
import logic.function.returnable.SpecialValue;
import logic.function.returnable.Returnable;
import logic.function.returnable.ReturnableImpl;

public class Sub extends TernaryFunction {
    private final java.lang.String name = "SUB";

    public Sub(Function argument1, Function argument2, Function argument3) {
        super(argument1, argument2, argument3);
    }

    @Override
    public Returnable calculate(Returnable source, Returnable i_StartIndex, Returnable i_EndIndex) {
        try {
            String sourceString = source.tryConvertTo(String.class);
            double startIndex = i_StartIndex.tryConvertTo(Double.class);
            double endIndex = i_EndIndex.tryConvertTo(Double.class);

            return this.isIndexesValid(startIndex, endIndex, sourceString.length()) ?
             new ReturnableImpl(sourceString.substring((int) startIndex, (int) endIndex), CellType.STRING):
             SpecialValue.UNDEFINED;
        }  catch (ClassCastException | UnsupportedOperationException e)  {
            return SpecialValue.UNDEFINED;
        }
    }

    @Override
    public java.lang.String getFunctionName() {
        return this.name;
    }

    @Override
    public CellType getReturnType() {
        return CellType.STRING;
    }

    boolean isIndexesValid(double startIndex, double endIndex, int stringLength) {
          return startIndex >= 0 &&
          endIndex <= stringLength &&
          isNaturalNumber(startIndex) &&
          isNaturalNumber(endIndex);
    }

    public boolean isNaturalNumber(double value){
        return value - (double)(int)value == 0;
    }

}