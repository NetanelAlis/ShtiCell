package dto;

import user.permission.PermissionType;

public class ReceivedRequestDTO {
    private PermissionType requestedPermission;
    private String sheetName;
    private String requesterUserName;

    public ReceivedRequestDTO(PermissionType requestedPermission, String requesterUserName, String sheetName) {
        this.requestedPermission = requestedPermission;
        this.requesterUserName = requesterUserName;
        this.sheetName = sheetName;
    }

    public PermissionType getRequestedPermission() {
        return requestedPermission;
    }

    public String getRequesterUserName() {
        return requesterUserName;
    }

    public String getSheetName() {
        return this.sheetName;
    }
}
