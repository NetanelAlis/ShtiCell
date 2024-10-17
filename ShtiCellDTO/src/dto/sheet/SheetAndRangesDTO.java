package dto.sheet;

import dto.range.RangesDTO;

public class SheetAndRangesDTO {
    private SheetDTO sheetDTO;
    private RangesDTO rangesDTO;

    public SheetAndRangesDTO(SheetDTO sheetDTO, RangesDTO rangesDTO) {
        this.sheetDTO = sheetDTO;
        this.rangesDTO = rangesDTO;
    }

    public SheetDTO getSheetDTO() {
        return sheetDTO;
    }

    public RangesDTO getRangesDTO() {
        return rangesDTO;
    }


}
