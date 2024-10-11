package user.permission;

public enum PermissionStatus {
    ACCEPTED("Accepted"),
    DENIED("Denied"),
    PENDING("Pending"),
    OWNER("Owner");

    private final String status;

    PermissionStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
