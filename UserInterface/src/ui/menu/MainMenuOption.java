package ui.menu;

import dto.SheetDTO;
import logic.Engine;
import java.util.Scanner;
import static java.lang.System.exit;

public enum MainMenuOption {
    INVALID_CHOICE {
        @Override
        public void executeOption() {
            System.out.println("No such menu option, Please Try Again:");
        }
    },
    LOAD_XML_FILE {
        @Override
        public void executeOption(Engine engine) {
            String path = getFilePathFromUser();
            if (engine.loadXmlFile(path)) {
                System.out.println("file load succesfully");
            } else {
                System.out.println("file dosnt exist");
            }
        }

        @Override
        public String toString() {
            return "Load Sheet From XML File";

        }
    },
    SHOW_SHEET {
        @Override
        public void executeOption(Engine engine) {
            SheetDTO sheetdto = engine.showSheet();
        }

        @Override
        public String toString() {
            return "Show Sheet Details";
        }
    },
    SHOW_SINGLE_CELL {
        @Override
        public void executeOption(Engine engine) {
            String cellId = getCellIDFromUser();
            engine.showCellData(cellId);
        }

        @Override
        public String toString() {
            return "Show Single Cell Details";
        }
    },
    UPDATE_SINGLE_CELL {
        @Override
        public void executeOption(Engine engine) {
            String cellId = getCellIDFromUser();
            engine.updateCellData(cellId);
        }

        @Override
        public String toString() {
            return "Update Single Cell Value";
        }
    },
    SHOW_VERSIONS {
        @Override
        public void executeOption() {

        }

        @Override
        public String toString() {
            return "Show Sheet Versions";
        }
    },
    EXIT {
        @Override
        public void executeOption() {
            exit(0);
        }

        @Override
        public String toString() {
            return "Exit Shticell";
        }
    };

    public abstract void executeOption(Engine engine);

    String getFilePathFromUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the full path to your XML file:");
        return scanner.nextLine();
    }

    private static String getCellIDFromUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please Enter the cell ID(for example A4):");
        String cellID = scanner.nextLine();

        while (!isValidCellID(cellID)){
            System.out.println("Please Enter the valid cell ID(for example A4):");
            cellID = scanner.nextLine();
        }

        return cellID;
    }

    private static boolean isValidCellID(String cellID) {
        boolean isValid = true;

        if (cellID.isBlank()) {
            System.out.println("Cannot enter an empty cell ID");
            isValid = false;
        }else if (!Character.isUpperCase(cellID.charAt(0))) {
            System.out.println("Please enter column as upper case letter.");
            isValid = false;
        }else if (!cellID.substring(1).matches("\\d+")) {
            System.out.println("Please enter row as number");
            isValid = false;
        }

        return isValid;
    }

}