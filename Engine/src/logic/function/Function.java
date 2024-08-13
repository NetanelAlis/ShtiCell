package logic.function;
import logic.function.returnable.Returnable;

public interface Function{

    String getFunctionName();

    Returnable invoke();
}
