package dto;

import java.util.LinkedList;
import java.util.List;

public class VersionsChangesDTO {
    private List<Integer> versionsChanges =  new LinkedList<>();

    public VersionsChangesDTO(List<Integer> versionsChanges) {
        this.versionsChanges.addAll(versionsChanges);
    }

    public List<Integer>getVersionChanges(){
        return versionsChanges;
    }
}
