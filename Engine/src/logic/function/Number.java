package logic.function;

import java.util.List;

public class Number implements MathFunction{
    private java.lang.Number value;

    public Number(java.lang.Number number){
        this.value = number;
    }
    @Override
    public java.lang.Number invoke() {
        return this.value;
    }

    @Override
    public String getFunctionName() {
        return "";
    }
}
