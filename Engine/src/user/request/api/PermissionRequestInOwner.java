package user.request.api;

import user.permission.PermissionStatus;
import user.permission.PermissionType;
import user.request.impl.PermissionRequest;

public interface PermissionRequestInOwner {

    static PermissionRequest createPermissionRequestInOwner(PermissionType currentPermission, PermissionType requestedPermission, PermissionStatus permissionStatus, String sheetName, String requesterUserName) {
        return new PermissionRequest(currentPermission, requestedPermission, permissionStatus, sheetName, requesterUserName);
    }

    PermissionType getRequestedPermissionType();

    PermissionType getCurrentPermission();

    PermissionStatus getRequestedPermissionStatus();

    String getSheetName();

    String getRequesterUserName();
}
