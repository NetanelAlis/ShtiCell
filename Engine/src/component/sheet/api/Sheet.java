package component.sheet.api;

public interface Sheet extends ReadonlySheet, UpdatableSheet{

    static boolean isValidCellID(String cellID) {
        if (cellID.isBlank()) {
            return false;
        }else if (!Character.isLetter(cellID.charAt(0))) {
            return false;
        }else return cellID.substring(1).matches("\\d+");
    }
}
