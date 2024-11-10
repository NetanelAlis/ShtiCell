package dto.version;

import java.util.LinkedList;
import java.util.List;

public class VersionChangesDTO {
    private final List<Integer> versionChanges = new LinkedList<>();

    public VersionChangesDTO(List<Integer> versionChanges) {
        this.versionChanges.addAll(versionChanges);
    }

    public List<Integer> getVersionChanges() {
        return this.versionChanges;
    }
}
