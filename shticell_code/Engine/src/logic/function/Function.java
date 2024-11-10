package logic.function;

import component.cell.api.CellType;
import component.sheet.api.ReadonlySheet;
import logic.function.returnable.api.Returnable;

public interface Function {

    String getFunctionName();
    Returnable invoke(ReadonlySheet sheet);
    CellType getReturnType();
}
