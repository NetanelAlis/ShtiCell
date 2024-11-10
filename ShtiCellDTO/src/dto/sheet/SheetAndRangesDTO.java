package dto.sheet;

import dto.range.RangesDTO;

public class SheetAndRangesDTO {
    
    private final ColoredSheetDTO coloredSheetDTO;
    private final RangesDTO rangesDTO;
    private final boolean isInReaderMode;
    
    public SheetAndRangesDTO(ColoredSheetDTO coloredSheetDTO, RangesDTO rangesDTO, boolean isInReaderMode) {
        this.coloredSheetDTO = coloredSheetDTO;
        this.rangesDTO = rangesDTO;
        this.isInReaderMode = isInReaderMode;
    }
    
    public ColoredSheetDTO getSheetDTO() { return this.coloredSheetDTO; }
    
    public RangesDTO getRangesDTO() { return this.rangesDTO; }
    
    public boolean isInReaderMode() { return this.isInReaderMode; }
}
