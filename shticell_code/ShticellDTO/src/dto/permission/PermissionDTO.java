package dto.permission;

public class PermissionDTO {
    private String username;
    private String permissionType;
    private String requestStatus;
    
    public PermissionDTO(String username, String permissionType, String requestStatus) {
        this.username = username;
        this.permissionType = permissionType;
        this.requestStatus = requestStatus;
    }
    
    public String getUsername() {return this.username;}
    
    public String getPermissionType() {return this.permissionType;}
    
    public String getRequestStatus() {return this.requestStatus;}
}
