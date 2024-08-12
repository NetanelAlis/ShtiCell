package logic.function;

public class Divide implements MathFunction {
    private final String name = "Divide";
    private MathFunction argument1;
    private MathFunction argument2;

    public Divide(MathFunction argument1, MathFunction argument2){
        this.argument1 = argument1;
        this.argument2 = argument2;
    }
    @Override
    public java.lang.Number invoke() {
        return this.argument2.invoke().doubleValue() != 0 ?
                this.argument1.invoke().doubleValue() / this.argument2.invoke().doubleValue():
               Double.NaN;
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }
}
