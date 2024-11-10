package dto.permission;

public class SentPermissionRequestDTO {
    
    private final String requestedEngineName;
    private final String requestedPermission;
    
    public SentPermissionRequestDTO(String requestedEngineName, String requestedPermission) {
        this.requestedEngineName = requestedEngineName;
        this.requestedPermission = requestedPermission;
    }
    
    public String getRequestedEngineName() {
        return this.requestedEngineName;
    }
    
    public String getRequestedPermission() { return this.requestedPermission; }
}
