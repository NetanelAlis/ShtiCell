package user;

import user.permission.PermissionStatus;
import user.permission.PermissionType;
import user.request.api.PermissionRequestInOwner;
import user.request.impl.PermissionRequest;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String userName;
    private List<PermissionRequestInOwner> permissionsRequests;

    public User(String userName) {
        this.userName = userName;
        permissionsRequests = new ArrayList<PermissionRequestInOwner>();
    }

    public void addRequest(PermissionType currentPermission, PermissionType requestedPermission, PermissionStatus currentPermissionStatus,  String sheetName, String requestedUserName) {
        this.permissionsRequests.add(new PermissionRequest(currentPermission, requestedPermission, currentPermissionStatus, sheetName, requestedUserName));
    }
}
