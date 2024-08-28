package component.archive.api;

import component.sheet.api.Sheet;

import java.io.*;
import java.util.List;

public interface Archive extends Serializable {
    void storeInArchive(Sheet sheet);
    Sheet retrieveFromArchive(int version);
    List<Integer> getAllVersionsChanges();
    void saveToFile(String path);
    public static Archive loadFromFile(String path) {
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(path))) {
            // Deserialize the object from the file and cast it to Archive
            return (Archive) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    Sheet retrieveLastSheetVersionFromArchive();
}

