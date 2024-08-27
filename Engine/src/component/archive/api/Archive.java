package component.archive.api;

import component.sheet.api.Sheet;

import java.util.List;

public interface Archive {
    void storeInArchive(Sheet sheet);
    Sheet retrieveFromArchive(int version);
    List<Integer> getAllVersionsChanges();
}
