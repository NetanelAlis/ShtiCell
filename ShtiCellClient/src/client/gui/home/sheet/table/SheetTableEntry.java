package client.gui.home.sheet.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SheetTableEntry {
    private StringProperty userName;
    private StringProperty sheetName;
    private StringProperty sheetSize;
    private StringProperty permission;
    
    public SheetTableEntry(String userName, String sheetName, String sheetSize, String permission) {
        this.userName = new SimpleStringProperty(userName);
        this.sheetName = new SimpleStringProperty(sheetName);
        this.sheetSize = new SimpleStringProperty(sheetSize);
        this.permission = new SimpleStringProperty(permission);
    }
    
    public SheetTableEntry deepCopy() {
        return new SheetTableEntry(
                this.getUserName(),  // Create a new SimpleStringProperty for userName
                this.getSheetName(),  // Create a new SimpleStringProperty for sheetName
                this.getSheetSize(),  // Create a new SimpleStringProperty for sheetSize
                this.getPermission() // Create a new SimpleStringProperty for permissions
        );
    }
    
    public String getUserName() { return this.userName.get(); }
    
    public String getSheetName() { return this.sheetName.get(); }
    
    public String getSheetSize() { return this.sheetSize.get(); }
    
    public String getPermission() { return this.permission.get(); }
}
