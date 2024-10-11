package user.request.impl;

import user.permission.PermissionStatus;
import user.permission.PermissionType;
import user.request.api.PermissionRequestInEngine;
import user.request.api.PermissionRequestInOwner;

public class PermissionRequest implements PermissionRequestInEngine, PermissionRequestInOwner {
    private PermissionType requestedPermissionType;
    private PermissionStatus requestedStatus;
    private PermissionType currentPermissionType;
    private String sheetName;
    private String requesterUserName;

    public PermissionRequest(PermissionType currentPermissionType, PermissionType requestedPermissionType, PermissionStatus requestedPermissionStatus) {
        this.requestedPermissionType = requestedPermissionType;
        this.requestedStatus = requestedPermissionStatus;
        this.currentPermissionType = currentPermissionType;
        this.sheetName = null;
        this.requesterUserName = null;
    }

    public PermissionRequest(PermissionType currentPermissionType, PermissionType requestedPermissionType, PermissionStatus requestedPermissionStatus, String sheetName, String requesterUserName) {
        this.requestedPermissionType = requestedPermissionType;
        this.requestedStatus = requestedPermissionStatus;
        this.currentPermissionType = currentPermissionType;
        this.sheetName = sheetName;
        this.requesterUserName = requesterUserName;
    }

    @Override
    public PermissionType getRequestedPermissionType() {
        return this.currentPermissionType;
    }

    @Override
    public PermissionType getCurrentPermission() {
        return null;
    }

    @Override
    public PermissionStatus getRequestedPermissionStatus() {
        return this.requestedStatus;
    }

    @Override
    public void setCurrentPermission(PermissionType permissionType) {
        this.currentPermissionType = permissionType;
    }

    @Override
    public String getSheetName() {
        return this.sheetName;
    }

    @Override
    public String getRequesterUserName() {
        return this.requesterUserName;
    }

}

