package dto;

public class SheetNameAndSizeDTO {
    private String sheetName;
    private int numberOfCols;
    private int numberOfRows;

    public SheetNameAndSizeDTO(String sheetName, int numberOfCols, int numberOfRows) {
        this.sheetName = sheetName;
        this.numberOfCols = numberOfCols;
        this.numberOfRows = numberOfRows;
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
}
