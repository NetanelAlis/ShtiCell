package component.archive.api;

import component.sheet.api.Sheet;

import java.io.*;
import java.util.List;

public interface Archive extends Serializable {
    static Archive loadFromFile(String path) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
            return (Archive) in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    void storeInArchive(Sheet sheet);
    Sheet retrieveVersion(int version);
    Sheet retrieveLatestVersion();
    List<Integer> getAllVersionsChangesList();
    void saveToFile(String filePath);
}
