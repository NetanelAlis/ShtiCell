package logic.permission;

public enum Permission {
    OWNER("Owner"),
    READER("Reader"),
    WRITER("Writer"),
    NONE("None");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }
}
