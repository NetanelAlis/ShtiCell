package dto.sheet;

import dto.cell.CellDTO;

public class SheetAndCellDTO {
    ColoredSheetDTO sheet;
    CellDTO cell;

    public SheetAndCellDTO(ColoredSheetDTO sheet, CellDTO cell) {
        this.sheet = sheet;
        this.cell = cell;
    }

    public ColoredSheetDTO getSheet() {
        return sheet;
    }

    public CellDTO getCell() {
        return cell;
    }
}
