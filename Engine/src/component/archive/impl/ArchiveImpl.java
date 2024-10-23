package component.archive.impl;

import component.archive.api.Archive;
import component.sheet.api.Sheet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class ArchiveImpl implements Archive {
    private final Map<Integer, Sheet> storedSheets = new HashMap<>();
    private final List<Integer> changesPerVersion = new LinkedList<>();

    private Object changesPerVersionLock = new Object();
    private Object storedSheetsLock = new Object();

    @Override
    public void storeInArchive(Sheet sheet) {
        synchronized (storedSheetsLock) {
        this.storedSheets.put(sheet.getVersion(), sheet);
        }

        synchronized (changesPerVersionLock) {
        this.changesPerVersion.add(sheet.getNumOfCellsUpdated());
        }
    }

    @Override
    public Sheet retrieveVersion(int version) {
        synchronized (storedSheetsLock) {
            Sheet restoredSheet = this.storedSheets.get(version);

            if (restoredSheet == null) {
                throw new IllegalArgumentException("Version " + version + " does not exist.");
            }
            return restoredSheet;
        }
    }

    @Override
    public Sheet retrieveLatestVersion() {
        synchronized (changesPerVersionLock) {
        return this.retrieveVersion(this.changesPerVersion.size());
        }
    }

    @Override
    public List<Integer> getAllVersionsChangesList() {
        synchronized (changesPerVersionLock) {
        return this.changesPerVersion;
        }
    }

    @Override
    public void saveToFile(String filePath) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(this);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isValidVersion(String version) {
        try {
            Integer.parseInt(version);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
