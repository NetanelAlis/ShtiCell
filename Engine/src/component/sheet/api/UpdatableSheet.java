package component.sheet.api;

import component.sheet.impl.SheetImpl;

import java.io.Serializable;

public interface UpdatableSheet extends Serializable {
    Sheet updateSheet(SheetImpl newSheetVersion, boolean isOriginalValueChanged);
    void createRange(String rangeName, String range);
    void deleteRange(String rangeName);
}
