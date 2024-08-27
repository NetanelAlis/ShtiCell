package logic.function.string;

import component.api.CellType;
import logic.function.Function;
import logic.function.returnable.SpecialValue;
import logic.function.returnable.Returnable;
import logic.function.BinaryFunction;
import logic.function.returnable.ReturnableImpl;

public class Concat extends BinaryFunction {

    private final java.lang.String name = "CONCAT";

    public Concat(Function argument1, Function argument2) {
        super(argument1, argument2);
    }

    @Override
    protected Returnable calculate(Returnable argument1, Returnable argument2){
        try{
            return new ReturnableImpl(argument1.tryConvertTo(String.class) + argument2.tryConvertTo(String.class), CellType.STRING);
        }
        catch(ClassCastException e){
            return SpecialValue.UNDEFINED;
        }
    }

    @Override
    public java.lang.String getFunctionName() {
        return this.name;
    }

    @Override
    public CellType returnType() {
        return CellType.STRING;
    }

}
