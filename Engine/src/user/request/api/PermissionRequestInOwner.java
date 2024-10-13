package user.request.api;

import user.permission.PermissionStatus;
import user.permission.PermissionType;
import user.request.impl.PermissionRequest;

public interface PermissionRequestInOwner {

    static PermissionRequest createPermissionRequestInOwner(PermissionType requestedPermissionType, String sheetName, String requesterUserName) {
        return new PermissionRequest(requestedPermissionType, sheetName, requesterUserName);
    }

    PermissionType getRequestedPermissionType();

    PermissionType getCurrentPermission();

    PermissionStatus getRequestedPermissionStatus();

    String getSheetName();

    String getRequesterUserName();
}
