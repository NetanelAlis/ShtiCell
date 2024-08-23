package component.sheet.api;

public interface WriteOnlySheet {
    Sheet updateCellValueAndCalculate(String cellID, String newValue);;
}
