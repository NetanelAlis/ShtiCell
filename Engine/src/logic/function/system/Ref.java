package logic.function.system;

import component.api.CellType;
import component.sheet.api.ReadOnlySheet;
import logic.function.Function;
import logic.function.returnable.Returnable;

public class Ref implements Function {

    private final java.lang.String name = "REF";
    private String cellID;

    public Ref(String cellID) {
        this.cellID = cellID;
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }

    @Override
    public Returnable invoke(ReadOnlySheet sheet) {
        return sheet.getCell(this.cellID).getEffectiveValue();
    }

    @Override
    public CellType returnType() {
    return null;
    }

}
