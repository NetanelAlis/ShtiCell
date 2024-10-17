package dto.permission;

import user.permission.PermissionStatus;
import user.permission.PermissionType;

public class RequestedRequestForTableDTO {
    private String requesterUserName;
    private PermissionType requestedPermission;
    private PermissionStatus requestPermissionStatus;

    public RequestedRequestForTableDTO(String requesterUserName, PermissionType requestedPermission, PermissionStatus requestPermissionStatus) {
        this.requesterUserName = requesterUserName;
        this.requestedPermission = requestedPermission;
        this.requestPermissionStatus = requestPermissionStatus;
    }

    public String getRequesterUserName() {
        return this.requesterUserName;
    }

    public String getRequestedPermission() {
        return this.requestedPermission.getType();
    }

    public String getRequestPermissionStatus() {
        return this.requestPermissionStatus.getStatus();
    }
}
