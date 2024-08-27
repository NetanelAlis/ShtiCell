package logic.function.system;

import component.api.CellType;
import component.sheet.api.ReadOnlySheet;
import component.sheet.api.Sheet;
import logic.function.Function;
import logic.function.returnable.Returnable;
import logic.function.returnable.ReturnableImpl;
import logic.function.returnable.SpecialValue;

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
        return sheet.getCell(this.cellID).getEffectiveValue().getCellType().equals(CellType.NO_VALUE) ?
                SpecialValue.UNDEFINED :
                sheet.getCell(this.cellID).getEffectiveValue();
    }

    @Override
    public CellType returnType() {
    return CellType.UNKOWN;
    }

}
