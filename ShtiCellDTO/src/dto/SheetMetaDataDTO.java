package dto;

import logic.permission.Permission;

public class SheetMetaDataDTO {
    private String sheetName;
    private int numberOfCols;
    private int numberOfRows;
    private String userName;
    private Permission permission;

    public SheetMetaDataDTO(String sheetName, int numberOfCols, int numberOfRows, String userName, Permission permission) {
        this.sheetName = sheetName;
        this.numberOfCols = numberOfCols;
        this.numberOfRows = numberOfRows;
        this.permission = permission;
        this.userName = userName;
    }
    public String getSheetName() {
        return this.sheetName;
    }
    public int getNumberOfCols() {
        return this.numberOfCols;
    }
    public int numberOfRows() {
        return this.numberOfRows;
    }
    public String getUserName() {
        return this.userName;
    }
    public Permission getPermission() {
        return this.permission;
    }
}
