package dto.permission;

import user.permission.PermissionType;

public class SendRequestDTO {
    private PermissionType requestedPermission;
    private String sheetName;

    public SendRequestDTO(String requestedPermissionAsString, String sheetName) {
        this.requestedPermission = PermissionType.valueOf(requestedPermissionAsString.trim().toUpperCase());
        this.sheetName = sheetName;
    }

    public PermissionType getRequestedPermission() {
        return requestedPermission;
    }

    public String getSheetName() {
        return this.sheetName;
    }
}
