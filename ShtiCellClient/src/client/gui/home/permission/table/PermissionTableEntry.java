package client.gui.home.permission.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PermissionTableEntry {
    StringProperty userName;
    StringProperty requestType;
    StringProperty requestStatus;

    public PermissionTableEntry(String userName, String requestType, String requestStatus) {
        this.userName = new SimpleStringProperty(userName);
        this.requestType = new SimpleStringProperty(requestType);
        this.requestStatus = new SimpleStringProperty(requestStatus);
    }

    public String getUserName() {
        return userName.get();
    }

    public String getRequestType() {
        return requestType.get();
    }

    public String getRequestStatus() {
        return requestStatus.get();
    }

}
