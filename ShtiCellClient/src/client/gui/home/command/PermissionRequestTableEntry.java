package client.gui.home.command;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.Objects;

public class PermissionRequestTableEntry {
    private StringProperty sender;
    private StringProperty sheetName;
    private StringProperty permissions;
    private int requestNumber;

    public PermissionRequestTableEntry(String sender, String sheetName, String permissions, int requestNumber) {
        this.sender = new SimpleStringProperty(sender);
        this.sheetName = new SimpleStringProperty(sheetName);
        this.permissions = new SimpleStringProperty(permissions);
        this.requestNumber = requestNumber;
    }

    public String getSender() {
        return sender.get();
    }

    public String getSheetName() {
        return sheetName.get();
    }

    public String getPermissions() {
        return permissions.get();
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionRequestTableEntry that = (PermissionRequestTableEntry) o;
        return requestNumber == that.requestNumber && Objects.equals(sender, that.sender) && Objects.equals(sheetName, that.sheetName) && Objects.equals(permissions, that.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, sheetName, permissions, requestNumber);
    }
}
