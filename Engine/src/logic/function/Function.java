package logic.function;
import component.api.CellType;
import component.sheet.api.SheetReadOnly;
import logic.function.returnable.Returnable;

public interface Function{

    String getFunctionName();
    Returnable invoke(SheetReadOnly sheet);
    CellType returnType();
}
