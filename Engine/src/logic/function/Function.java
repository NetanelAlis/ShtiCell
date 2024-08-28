package logic.function;
import component.api.CellType;
import component.sheet.api.ReadOnlySheet;
import logic.function.returnable.Returnable;

public interface Function{

    String getFunctionName();
    Returnable invoke(ReadOnlySheet sheet);
    CellType getReturnType();
}
