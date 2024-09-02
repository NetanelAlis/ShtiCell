package ui.output;

import dto.CellDTO;
import dto.SheetDTO;
import dto.VersionsChangesDTO;
import logic.function.returnable.Returnable;
import ui.menu.MainMenuOption;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;

public class ConsolePrinter {

    public static void printMainMenu() {
        System.out.println("Please Choose The Option Number out of the Following Options:");
        for (MainMenuOption option : MainMenuOption.values()) {
            if(MainMenuOption.INVALID_CHOICE != option) {
                System.out.println(option.ordinal() + ") " + option);
            }
        }
    }

    public static String getInputFromUser(String messageToUser, String errorMessageToUser ,Predicate<String> inputValidationMethod) {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println(messageToUser + ", Or press Q to go back to the main menu:");
        input = scanner.nextLine();
        String errorMessageWithUserInput = errorMessageToUser.replace("#",input);

        while (!input.equalsIgnoreCase("q") && !inputValidationMethod.test(input)){
            System.out.println(errorMessageWithUserInput + "please try again");
            System.out.println(messageToUser + ", Or press Q to go back to the main menu:");
            input = scanner.nextLine();
        }

        return input;
    }

    public static void printSheet(SheetDTO sheet) {
        // Get details from the sheet
        int numRows = sheet.getLayout().getNumberOfRows();
        int numCols = sheet.getLayout().getNumberOfColumns();
        int colWidth = sheet.getLayout().getColumnWidth();
        String sheetName = sheet.getSheetName();
        int versionNumber = sheet.getSheetVersion();
        Map<String, Returnable> activeCells = sheet.getActiveCells(); // Assume this is the map of active cells

        // Display the sheet name and version
        System.out.println("Sheet Name: " + sheetName);
        System.out.println("Version: " + versionNumber);
        System.out.println();

        // Print the column headers (A, B, C, ...)
        System.out.print("   |"); // For row numbers with a separator
        for (int col = 0; col < numCols; col++) {
            char colHeader = (char) ('A' + col);
            System.out.print(centerText(String.valueOf(colHeader), colWidth) + "|");
        }
        System.out.println();

        // Print each row
        for (int row = 0; row < numRows; row++) {
            System.out.print(padLeft(String.format("%02d ", row + 1), 3) + "|"); // Print row number with leading zero and separator
            for (int col = 0; col < numCols; col++) {
                char colHeader = (char) ('A' + col);
                String cellKey = colHeader + String.valueOf(row + 1);
                String cellValue = "";

                if (activeCells.containsKey(cellKey)) {
                    cellValue = activeCells.get(cellKey).getValue().toString();
                }

                if (cellValue.length() > colWidth) {
                    cellValue = cellValue.substring(0, colWidth);
                }

                cellValue = numberFormatter(cellValue);

                System.out.print(centerText(cellValue, colWidth) + "|");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static String numberFormatter(String input) {
        try{
            double  number = Double.parseDouble(input);
            DecimalFormat formatter = new DecimalFormat("#,###.##");
            formatter.setRoundingMode(RoundingMode.DOWN);
            return formatter.format(number);
        } catch (Exception ignored) {
            return input;
        }
    }

    // Helper method to center text within a given width
    private static String centerText(String s, int width) {
        int padding = (width - s.length()) / 2;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            result.append(" ");
        }
        result.append(s);
        while (result.length() < width) {
            result.append(" ");
        }
        return result.toString();
    }

    // Helper method to pad a string to the right to a given length
    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    // Helper method to pad a string to the left to a given length
    private static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }

    public static void printSimplifiedCell(CellDTO cellDTO){
        System.out.println("Cell ID: " + cellDTO.getCellId());
        System.out.println("Original Value: " + cellDTO.getOriginalValue());
        System.out.println("Effective Value: "
                + numberFormatter(cellDTO.getEffectiveValue().getValue().toString()));
    }

    public static void printCell(CellDTO cellDTO) {
        printSimplifiedCell(cellDTO);
        System.out.println("Last Modified Version: " + cellDTO.getVersion());

        System.out.println("Dependencies: ");
        if (cellDTO.getDependsOn() != null && !cellDTO.getDependsOn().isEmpty()) {
            for (String dep : cellDTO.getDependsOn()) {
                System.out.println(" - " + dep);
            }
        } else {
            System.out.println(" None");
        }

        System.out.println("Influencing: ");
        if (cellDTO.getInfluencingOn() != null && !cellDTO.getInfluencingOn().isEmpty()) {
            for (String dep : cellDTO.getInfluencingOn()) {
                System.out.println(" - " + dep);
            }
        } else {
            System.out.println(" None");
        }
    }

    public static String getOriginalValueFromUser(String cellID) {
        System.out.println("Please Enter the new Value for cell " + cellID + ":");
        System.out.println("The new Value can be of the Following Types:");
        System.out.println(" - Number (positive, negative or floating point)");
        System.out.println(" - Boolean (TRUE/FALSE - Capital Letters Only)");
        System.out.println(" - String (string of any kind)");
        System.out.println(" - Function (Must be in the Following format: {NAME,argument1,argument2}");
        System.out.println("   function name must be capital letters only)");
        System.out.println("   function examples: {PLUS,{REF,A3},4} or {CONCAT, hello, world}");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    protected void printSheetNotLoaded() {
        System.out.println("Sheet is not loaded, please load Sheet before trying any other option");
    }

    public static void printVersions(VersionsChangesDTO versionsChangesDTO) {
        // Print table header
        System.out.println("Version Number | Number of Changed Cells");
        System.out.println("---------------|----------------------");
        int i = 1;
        for (int numOfChanges: versionsChangesDTO.getVersionChanges()) {
            // Print each row in the table
            System.out.printf("%14d | %22d%n", i++, numOfChanges);
        }
    }
}