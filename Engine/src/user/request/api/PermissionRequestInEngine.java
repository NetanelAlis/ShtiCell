package user.request.api;

import user.permission.PermissionStatus;
import user.permission.PermissionType;
import user.request.impl.PermissionRequest;

public interface PermissionRequestInEngine {

    static PermissionRequest createPermissionRequestInEngine(PermissionType currentPermission, PermissionType requestedPermission, PermissionStatus permissionStatus){
        return new PermissionRequest(currentPermission, requestedPermission, permissionStatus);
    }
    PermissionType getRequestedPermissionType();

    PermissionType getCurrentPermission();

    PermissionStatus getRequestedPermissionStatus();

    void setCurrentPermission(PermissionType permissionType);
}
