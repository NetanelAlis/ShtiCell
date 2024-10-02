package logic.function.math;

import component.cell.api.CellType;
import component.range.api.Range;
import component.sheet.api.ReadOnlySheet;
import logic.function.Function;
import logic.function.returnable.api.Returnable;
import logic.function.returnable.impl.ReturnableImpl;
import logic.function.returnable.impl.SpecialValues;

public class Sum implements Function {
    private final String name = "SUM";
    private final String rangeName;
    
    public Sum(String rangeName) {
        this.rangeName = rangeName;
    }
    
    @Override
    public String getFunctionName() {
        return this.name;
    }
    
    @Override
    public Returnable invoke(ReadOnlySheet sheet) {
        double sumResult = 0;
        
        if (sheet.isExistingRange(this.rangeName)) {
            Range range = sheet.getRanges().get(this.rangeName);
            
            sumResult += range.getRangeCells().stream()
                    .filter((cell) -> cell.getEffectiveValue().getCellType().equals(CellType.NUMERIC))
                    .mapToDouble((cell) -> cell.getEffectiveValue().tryConvertTo(Double.class))
                    .sum();
            
            return new ReturnableImpl(sumResult, CellType.NUMERIC);
        } else {
            return SpecialValues.NAN;
        }
        
    }
    
    @Override
    public CellType getReturnType() {
        return CellType.NUMERIC;
    }
}
