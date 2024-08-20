package logic.function.returnable;

import component.api.CellType;

public class MyString implements Returnable {

    private java.lang.String value;

    public MyString(java.lang.String str){
        this.value = str;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public Returnable invoke() {
        return this;
    }

    @Override
    public CellType returnType() {
        return CellType.STRING;
    }

    @Override
    public java.lang.String getFunctionName() {
        return MyString.class.getSimpleName();
    }

}
