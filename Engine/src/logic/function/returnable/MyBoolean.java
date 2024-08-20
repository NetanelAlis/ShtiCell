package logic.function.returnable;

import component.api.CellType;

public class MyBoolean implements Returnable {

    private boolean value;

    public MyBoolean(boolean value){
        this.value = value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public java.lang.String getFunctionName() {
        return MyBoolean.class.getSimpleName();
    }

    @Override
    public Returnable invoke() {
        return this;
    }

    @Override
    public CellType returnType() {
        return null;
    }

}
