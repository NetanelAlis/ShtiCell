package client.gui.home.sheet.table;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SheetTableEntry {
    StringProperty userName;
    StringProperty sheetName;
    StringProperty sheetSize;
    StringProperty permissions;

    public SheetTableEntry(String userName, String sheetName, String sheetSize, String permissions) {
        this.userName = new SimpleStringProperty(userName);
        this.sheetName = new SimpleStringProperty(sheetName);
        this.sheetSize = new SimpleStringProperty(sheetSize);
        this.permissions = new SimpleStringProperty(permissions);
    }

    public String getUserName() {
        return userName.get();
    }

    public String getSheetName() {
        return sheetName.get();
    }

    public String getSheetSize() {
        return sheetSize.get();
    }

    public String getPermissions() {
        return permissions.get();
    }
}
