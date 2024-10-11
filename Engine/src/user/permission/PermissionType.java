package user.permission;

public enum PermissionType {
    OWNER("Owner"),
    READER("Reader"),
    WRITER("Writer"),
    NONE("None");

    private final String type;

    PermissionType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
