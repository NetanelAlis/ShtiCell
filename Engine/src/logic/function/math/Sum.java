package logic.function.math;

import component.api.CellType;
import component.range.api.Range;
import component.sheet.api.ReadOnlySheet;
import logic.function.Function;
import logic.function.returnable.Returnable;
import logic.function.returnable.ReturnableImpl;

public class Sum implements Function {
    private final String name = "SUM";
    private final String rangeName;

    public Sum(final String rangeName) {
        this.rangeName = rangeName;
    }
    @Override
    public String getFunctionName() {
        return this.name;
    }

    @Override
    public Returnable invoke(ReadOnlySheet sheet) {

        double sum = 0;
        if (sheet.isExistingRange(rangeName)) {
            Range range = sheet.getRanges().get(rangeName);
            sum = range.getRangeCells().stream()
                    .filter(cell -> cell.getEffectiveValue().getCellType().equals(CellType.NUMERIC))
                    .mapToDouble(cell -> cell.getEffectiveValue().tryConvertTo(Double.class))
                    .sum();

            return new ReturnableImpl(sum, CellType.NUMERIC);
        } else {
            throw new IllegalArgumentException("Cannot calculate sum of not existing range, " + this.rangeName);
        }
    }

    @Override
    public CellType getReturnType() {
        return CellType.NUMERIC;
    }
}
