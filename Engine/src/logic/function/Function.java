package logic.function;

import component.cell.api.CellType;
import component.sheet.api.ReadOnlySheet;
import logic.function.returnable.api.Returnable;

public interface Function {

    String getFunctionName();
    Returnable invoke(ReadOnlySheet sheet);
    CellType getReturnType();
}
