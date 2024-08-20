package logic.function;
import component.api.CellType;
import logic.function.returnable.Returnable;

public interface Function{

    String getFunctionName();
    Returnable invoke();
    CellType returnType();
}
