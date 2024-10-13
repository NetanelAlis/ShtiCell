package dto;

import user.permission.PermissionType;

public class SendRequestDTO {
    private PermissionType requestedPermission;
    private String sheetName;

    public SendRequestDTO(PermissionType requestedPermission, String sheetName) {
        this.requestedPermission = requestedPermission;
        this.sheetName = sheetName;
    }

    public PermissionType getRequestedPermission() {
        return requestedPermission;
    }

    public String getSheetName() {
        return this.sheetName;
    }
}
