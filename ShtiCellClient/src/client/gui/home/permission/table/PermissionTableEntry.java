package client.gui.home.permission.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class PermissionTableEntry {
    private final StringProperty username;
    private final StringProperty permissionType;
    private final StringProperty requestStatus;
    
    public PermissionTableEntry(String username, String permissionType, String requestStatus) {
        this.username = new SimpleStringProperty(username);
        this.permissionType = new SimpleStringProperty(permissionType);
        this.requestStatus = new SimpleStringProperty(requestStatus);
    }
    
    public String getUsername() { return this.username.get(); }
    
    public String getPermissionType() { return this.permissionType.get(); }
    
    public String getRequestStatus() { return this.requestStatus.get(); }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionTableEntry that = (PermissionTableEntry) o;
        return Objects.equals(username, that.username) && Objects.equals(permissionType, that.permissionType) && Objects.equals(requestStatus, that.requestStatus);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(username, permissionType, requestStatus);
    }
}
