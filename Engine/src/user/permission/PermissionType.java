package user.permission;

public enum PermissionType {
    OWNER("Owner"),
    READER("Reader"),
    WRITER("Writer"),
    NONE("None");
    
    private final String permission;
    
    PermissionType(String permission) { this.permission = permission; }
    
    public String getPermission() { return this.permission; }
}
