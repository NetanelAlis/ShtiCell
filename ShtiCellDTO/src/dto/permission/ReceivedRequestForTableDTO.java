package dto.permission;

import user.permission.PermissionType;

public class ReceivedRequestForTableDTO {
    private PermissionType requestedPermission;
    private String sheetName;
    private String requesterUserName;
    private int requestNumber;

    public ReceivedRequestForTableDTO(String requestedPermissionAsString, String requesterUserName, String sheetName, int requestNumber) {
        this.requestedPermission = PermissionType.valueOf(requestedPermissionAsString.trim().toUpperCase());
        this.requesterUserName = requesterUserName;
        this.sheetName = sheetName;
        this.requestNumber = requestNumber;
    }

    public String getRequestedPermission() {
        return this.requestedPermission.getType();
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
