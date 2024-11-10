package dto.permission;

public class ReceivedPermissionRequestDTO {
    private final String sender;
    private final String sheetName;
    private final String requestedPermission;
    private final int requestID;
    
    public ReceivedPermissionRequestDTO(String sender, String sheetName, String requestedPermission, int requestID ) {
        this.sender = sender;
        this.sheetName = sheetName;
        this.requestedPermission = requestedPermission;
        this.requestID = requestID;
    }
    
    public String getSender() { return this.sender;}
    
    public String getSheetName() {
        return this.sheetName;
    }
    
    public String getRequestedPermission() { return this.requestedPermission; }
    
    public int getRequestID() { return this.requestID;}
}
