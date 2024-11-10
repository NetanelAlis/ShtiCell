package user.permission;

public enum PermissionStatus {
    APPROVED("Approved"),
    REJECTED("Rejected"),
    PENDING("Pending");
    
    private final String permissionStatus;
    
    PermissionStatus(String permissionStatus) { this.permissionStatus = permissionStatus; }
    
    public String getPermissionStatus() { return this.permissionStatus; }
}
