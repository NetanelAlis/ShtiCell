package logic.function.returnable;

import component.api.CellType;

public class MyNumber implements Returnable {
    private double value;

    public MyNumber(double value){
        this.value = value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public java.lang.String getFunctionName() {
        return MyNumber.class.getSimpleName();
    }

    @Override
    public Returnable invoke() {
        return this;
    }

    @Override
    public CellType returnType() {
        return CellType.NUMERIC;
    }
}
