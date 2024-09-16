package component.sheet.api;

public interface Sheet extends ReadOnlySheet, WriteOnlySheet{

    static boolean isValidCellID(String cellID) {
        boolean isValid = true;

        if (cellID.isBlank()) {
            isValid = false;
        }else if (!Character.isLetter(cellID.charAt(0))) {
            isValid = false;
        }else if (!cellID.substring(1).matches("\\d+")) {
            isValid = false;
        }

        return isValid;
    }

     boolean isExistingRange(String rangeName);

}
