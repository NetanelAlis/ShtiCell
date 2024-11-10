package dto.sheet;

public class SheetMetaDataDTO {
    private String ownerName;
    private String sheetName;
    private int numberOfRows;
    private int numberOfColumns;
    private String permission;
    
    public SheetMetaDataDTO(String ownerName,
                            String sheetName,
                            int numberOfRows,
                            int numberOfColumns,
                            String permission) {
        this.ownerName = ownerName;
        this.sheetName = sheetName;
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.permission = permission;
    }
    
    public String getOwnerName() { return this.ownerName; }
    
    public String getSheetName() {
        return this.sheetName;
    }
    
    public int getNumberOfRows() { return this.numberOfRows; }
    
    public int getNumberOfColumns() { return this.numberOfColumns; }
    
    public String getPermission() { return this.permission; }
}
