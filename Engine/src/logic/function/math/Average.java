package logic.function.math;

import component.api.CellType;
import component.range.api.Range;
import component.sheet.api.ReadOnlySheet;
import logic.function.Function;
import logic.function.returnable.Returnable;
import logic.function.returnable.ReturnableImpl;
import logic.function.returnable.SpecialValue;


public class Average implements Function {
    private final String name = "AVERAGE";
    private final String rangeName;

    public Average(final String rangeName) {
    this.rangeName = rangeName;
    }
    @Override
    public String getFunctionName() {
    return this.name;
    }

    @Override
    public Returnable invoke(ReadOnlySheet sheet) {
    double sumResult = 0;
    long numberOfNumericCellsInRange = 0;
    Range range = sheet.getRanges().get(rangeName);

    if(sheet.isExistingRange(range.getName())) {
        numberOfNumericCellsInRange =
                range.getRangeCells().stream()
                .filter(cell -> cell.getEffectiveValue().getCellType().equals(CellType.NUMERIC))
                .count();

        if(numberOfNumericCellsInRange > 0) {
            sumResult = range.getRangeCells().stream()
                    .filter(cell -> cell.getEffectiveValue().getCellType().equals(CellType.NUMERIC))
                    .mapToDouble(cell -> cell.getEffectiveValue().tryConvertTo(Double.class))
                    .sum();
            return new ReturnableImpl(sumResult / numberOfNumericCellsInRange, CellType.NUMERIC);
        }else{
            return SpecialValue.NAN;
        }
    }else{
        return SpecialValue.NAN;
    }


}

    @Override
    public CellType getReturnType() {
    return CellType.NUMERIC;
    }
}
