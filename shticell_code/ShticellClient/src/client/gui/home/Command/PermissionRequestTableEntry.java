package client.gui.home.Command;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class PermissionRequestTableEntry {
    private final StringProperty sender;
    private final StringProperty sheetName;
    private final StringProperty permission;
    private final int requestID;
    
    public PermissionRequestTableEntry(String sender, String sheetName, String permission, int requestID) {
        this.sender = new SimpleStringProperty(sender);
        this.sheetName = new SimpleStringProperty(sheetName);
        this.permission = new SimpleStringProperty(permission);
        this.requestID = requestID;
    }
    
    public String getSender() { return this.sender.get(); }
    
    public String getSheetName() { return this.sheetName.get(); }
    
    public String getPermission() { return this.permission.get(); }
    
    public int getRequestID() { return this.requestID; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionRequestTableEntry that = (PermissionRequestTableEntry) o;
        return Objects.equals(this.sender.get(), that.sender.get())
                && Objects.equals(this.sheetName.get(), that.sheetName.get())
                && Objects.equals(this.permission.get(), that.permission.get())
                && this.requestID == that.requestID;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.sender, this.sheetName, this.permission, this.requestID);
    }
}
