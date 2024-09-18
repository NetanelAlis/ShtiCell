package logic.function.bool;

import component.api.CellType;
import logic.function.Function;
import logic.function.TernaryFunction;
import logic.function.returnable.SpecialValue;
import logic.function.returnable.Returnable;
import logic.function.returnable.ReturnableImpl;

public class If extends TernaryFunction {
    private final java.lang.String name = "IF";

    public If(Function argument1, Function argument2, Function argument3) {
        super(argument1, argument2, argument3);
    }

    @Override
    public Returnable calculate(Returnable i_Condition, Returnable i_Than, Returnable i_Else) {
        try {
             if(this.typeEqual(i_Than, i_Else)){
                 return i_Condition.tryConvertTo(Boolean.class) ? i_Than : i_Else;
             } else{
                 return SpecialValue.UNKNOWN;
             }

        }  catch (ClassCastException | UnsupportedOperationException e)  {
            return SpecialValue.UNKNOWN;
        }
    }

    @Override
    public java.lang.String getFunctionName() {
        return this.name;
    }

    @Override
    public CellType getReturnType() {
        return CellType.UNKNOWN;
    }

    boolean typeEqual(Returnable i_Then, Returnable i_Else) {
        return i_Then.getCellType().equals(i_Else.getCellType());
    }
}