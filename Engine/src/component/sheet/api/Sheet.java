package component.sheet.api;

public interface Sheet extends ReadOnlySheet, WriteOnlySheet{

    static boolean isValidCellID(String cellID) {
        boolean isValid = true;

        if (cellID.isBlank()) {
            System.out.println("Cannot enter an empty cell ID");
            isValid = false;
        }else if (!Character.isLetter(cellID.charAt(0))) {
            System.out.println("Column must be a single letter.");
            isValid = false;
        }else if (!cellID.substring(1).matches("\\d+")) {
            System.out.println("Row must be whole number number bigger than 0.");
            isValid = false;
        }

        return isValid;
    }
}
