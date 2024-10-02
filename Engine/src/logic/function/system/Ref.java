package logic.function.system;

import component.cell.api.CellType;
import component.sheet.api.ReadonlySheet;
import logic.function.Function;
import logic.function.returnable.api.Returnable;
import logic.function.returnable.impl.SpecialValues;

public class Ref implements Function {
    private final String name = "REF";
    private String CellID;

    public Ref(String cellID) {
        this.CellID = cellID;
    }

    @Override
    public String getFunctionName() {
        return this.name;
    }

    @Override
    public Returnable invoke(ReadonlySheet sheet) {
        return sheet.getCell(this.CellID).getEffectiveValue().getCellType().equals(CellType.NO_VALUE) ?
                SpecialValues.EMPTY :
                sheet.getCell(this.CellID).getEffectiveValue();
    }

    @Override
    public CellType getReturnType() {
        return CellType.UNKNOWN;
    }
}
