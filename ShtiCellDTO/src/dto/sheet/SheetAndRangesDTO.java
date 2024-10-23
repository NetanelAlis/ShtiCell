package dto.sheet;

import dto.range.RangesDTO;

public class SheetAndRangesDTO {
    private final ColoredSheetDTO sheetDTO;
    private final RangesDTO rangesDTO;
    private final boolean userCantEditTheSheet;

    public SheetAndRangesDTO(ColoredSheetDTO sheetDTO, RangesDTO rangesDTO, boolean isInReaderMode) {
        this.sheetDTO = sheetDTO;
        this.rangesDTO = rangesDTO;
        this.userCantEditTheSheet = isInReaderMode;
    }

    public ColoredSheetDTO getSheetDTO() {
        return sheetDTO;
    }

    public RangesDTO getRangesDTO() {
        return rangesDTO;
    }

    public boolean userCantEditTheSheet() {
        return userCantEditTheSheet;
    }

}
