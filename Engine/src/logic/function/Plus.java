package logic.function;

public class Plus implements MathFunction {
    private final String name = "Plus";
    private MathFunction argument1;
    private MathFunction argument2;

    public Plus(MathFunction argument1, MathFunction argument2){
        this.argument1 = argument1;
        this.argument2 = argument2;
    }
    @Override
    public java.lang.Number invoke() {
        return this.argument1.invoke().doubleValue() + this.argument2.invoke().doubleValue();
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }
}
