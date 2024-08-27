package component.archive.impl;

import component.archive.api.Archive;
import component.sheet.api.Sheet;
import java.util.*;

public class ArchiveImpl implements Archive {
    private final List<Integer> changesPerVersion = new LinkedList<>();
    private final Map <Integer, Sheet> storedSheet = new HashMap<>();

    @Override
    public void storeInArchive(Sheet sheet) {
        this.storedSheet.put(sheet.getVersion(), sheet);
        this.changesPerVersion.add(sheet.getNumberOfCellsThatHaveChanged());
    }

    @Override
    public Sheet retrieveFromArchive(int version) {
            Sheet restoredSheet = this.storedSheet.get(version);

            if(restoredSheet == null) {
                throw new IllegalArgumentException("Version " + version + " does not exist in the current Sheet");
            }

            return restoredSheet;
    }

    @Override
    public List<Integer> getAllVersionsChanges() {
        return this.changesPerVersion;
    }

    public static boolean isValidVersion(String version) {
        try {
            Double.parseDouble(version);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
