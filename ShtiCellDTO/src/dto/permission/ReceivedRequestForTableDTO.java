package dto.permission;

import user.permission.PermissionType;

public class ReceivedRequestForTableDTO {
    private PermissionType requestedPermission;
    private String sheetName;
    private String requesterUserName;
    private int requestNumber;

    public ReceivedRequestForTableDTO(PermissionType requestedPermission, String requesterUserName, String sheetName, int requestNumber) {
        this.requestedPermission = requestedPermission;
        this.requesterUserName = requesterUserName;
        this.sheetName = sheetName;
        this.requestNumber = requestNumber;
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

    public int getRequestNumber() {
        return this.requestNumber;
    }
}
