package component.sheet.api;

import component.sheet.impl.SheetImpl;

public interface WriteOnlySheet {
    Sheet updateSheet(SheetImpl newSheetVersion, boolean isOriginalValueChanged);
    void deleteRange(String rangeName);
    void createRange(String rangeName, String rang);
}
