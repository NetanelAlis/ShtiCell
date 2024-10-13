package client.gui.home.command;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PermissionRequestTableEntry {
    StringProperty sender;
    StringProperty sheetName;
    StringProperty permissions;

    public PermissionRequestTableEntry(String sender, String sheetName, String permissions) {
        this.sender = new SimpleStringProperty(sender);
        this.sheetName = new SimpleStringProperty(sheetName);
        this.permissions = new SimpleStringProperty(permissions);
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
}
