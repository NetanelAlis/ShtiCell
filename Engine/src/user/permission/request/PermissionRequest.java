package user.permission.request;

import user.permission.PermissionStatus;
import user.permission.PermissionType;
import java.util.Objects;

public class PermissionRequest {
    private PermissionType requestedPermissionType;
    private PermissionStatus requestedStatus;
    private String senderName;
    private int requestNumber;

    public PermissionRequest(String senderName, PermissionType requestedPermissionType, PermissionStatus requestedPermissionStatus, int requestNumber) {
        this.requestedPermissionType = requestedPermissionType;
        this.requestedStatus = requestedPermissionStatus;
        this.senderName = senderName;
        this.requestNumber = requestNumber;
    }

    public PermissionType getRequestedPermissionType() {
        return this.requestedPermissionType;
    }

    public PermissionStatus getRequestedPermissionStatus() {
        return this.requestedStatus;
    }

    public void setRequestedPermissionStatus(PermissionStatus permissionStatus) {
        this.requestedStatus = permissionStatus;
    }

    public void setRequestedPermission(PermissionType permissionType) {
        this.requestedPermissionType = permissionType;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public int getRequestNumber() {
        return this.requestNumber;
    }


    public PermissionRequest deepCopy() {
        return new PermissionRequest(this.senderName, this.requestedPermissionType, this.requestedStatus, this.requestNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionRequest that = (PermissionRequest) o;
        return requestedPermissionType == that.requestedPermissionType && requestedStatus == that.requestedStatus && Objects.equals(senderName, that.senderName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestedPermissionType, requestedStatus, senderName);
    }

}

