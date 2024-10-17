package dto.sheet;

import dto.cell.CellDTO;

public class SheetAndCellDTO {
    SheetDTO sheet;
    CellDTO cell;

    public SheetAndCellDTO(SheetDTO sheet, CellDTO cell) {
        this.sheet = sheet;
        this.cell = cell;
    }

    public SheetDTO getSheet() {
        return sheet;
    }

    public CellDTO getCell() {
        return cell;
    }
}
