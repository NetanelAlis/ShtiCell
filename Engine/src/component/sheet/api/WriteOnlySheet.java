package component.sheet.api;

import component.sheet.impl.SheetImpl;

public interface WriteOnlySheet {
    Sheet updateSheet(SheetImpl newSheetVersion);
}
