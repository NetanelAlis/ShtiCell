package logic.function.returnable;

public class Number implements Returnable {
    private double value;

    public Number(double value){
        this.value = value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public java.lang.String getFunctionName() {
        return "";
    }

    @Override
    public Returnable invoke() {
        return this;
    }
}
