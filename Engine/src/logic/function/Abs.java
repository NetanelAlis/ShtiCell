package logic.function;

public class Abs implements MathFunction {
    private final String name = "Abs";
    private MathFunction argument1;

    public Abs(MathFunction argument1, MathFunction argument2){
        this.argument1 = argument1;
    }
    @Override
    public java.lang.Number invoke() {
      return Math.abs(argument1.invoke().doubleValue());
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }
}
