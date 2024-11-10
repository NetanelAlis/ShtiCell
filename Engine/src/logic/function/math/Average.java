package logic.function.math;

import component.cell.api.CellType;
import component.range.api.Range;
import component.sheet.api.ReadonlySheet;
import logic.function.Function;
import logic.function.returnable.api.Returnable;
import logic.function.returnable.impl.ReturnableImpl;
import logic.function.returnable.impl.SpecialValues;

public class Average implements Function {
    private final String name = "AVERAGE";
    private final String rangeName;
    
    public Average(String rangeName) {
        this.rangeName = rangeName;
    }
    
    @Override
    public String getFunctionName() {
        return this.name;
    }
    
    @Override
    public Returnable invoke(ReadonlySheet sheet) {
        double sumResult = 0;
        long numOfElements = 0;
        
        if (sheet.isExistingRange(this.rangeName)) {
            Range range = sheet.getRanges().get(this.rangeName);
            
            numOfElements += range.getRangeCells().stream()
                    .filter(cell -> cell.getEffectiveValue().getCellType().equals(CellType.NUMERIC))
                    .count();
            
            if (numOfElements > 0) {
                sumResult += range.getRangeCells().stream()
                        .filter((cell) -> cell.getEffectiveValue().getCellType().equals(CellType.NUMERIC))
                        .mapToDouble((cell) -> cell.getEffectiveValue().tryConvertTo(Double.class))
                        .sum();
                return new ReturnableImpl(sumResult / numOfElements, CellType.NUMERIC);
            }
            else {
                return SpecialValues.NAN;
            }
        } else {
            return SpecialValues.NAN;
        }
    }
    
    @Override
    public CellType getReturnType() {
        return CellType.NUMERIC;
    }
}
