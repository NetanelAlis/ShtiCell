package user.request;

import user.permission.PermissionStatus;
import user.permission.PermissionType;

import java.util.Objects;

public class PermissionRequest {
    private String senderName;
    private PermissionType requestedPermission;
    private PermissionStatus requestStatus;
    private int requestID;
    
    public PermissionRequest(String senderName, PermissionType requestedPermission, PermissionStatus requestStatus, int requestID) {
        this.senderName = senderName;
        this.requestedPermission = requestedPermission;
        this.requestStatus = requestStatus;
        this.requestID = requestID;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionRequest that = (PermissionRequest) o;
        return Objects.equals(senderName, that.senderName) && requestedPermission == that.requestedPermission && requestStatus == that.requestStatus;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(senderName, requestedPermission, requestStatus);
    }
    
    public PermissionRequest deepCopy() {
        return new PermissionRequest(this.senderName, this.requestedPermission, this.requestStatus, this.requestID);
    }
    
    public String getSenderName() {
        return this.senderName;
    }
    
    public PermissionType getRequestedPermission() { return this.requestedPermission; }
    
    public PermissionStatus getRequestStatus() { return this.requestStatus; }
    
    public int getRequestID() { return this.requestID; }
    
    public void updateRequestStatus(boolean answer) {
        if (answer) {
            this.requestStatus = PermissionStatus.APPROVED;
        } else {
            this.requestStatus = PermissionStatus.REJECTED;
        }
    }
}
