package ui.menu;

import component.archive.impl.ArchiveImpl;
import component.sheet.api.Sheet;
import dto.CellDTO;
import dto.VersionChangesDTO;
import logic.engine.Engine;
import ui.io.ConsoleUtils;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.exit;

public enum MainMenuOption {
    INVALID_CHOICE{
        @Override
        public void executeOption(Engine engine) {
            System.out.println("No such menu option, Please Try Again:");
        }
    },
    LOAD_XML_FILE{
        @Override
        public void executeOption(Engine engine) {
            try {
                String messageToUser = "Please Enter the full path of the file you wish to load";
                String errorMessage = "The File # does not exist or not a valid xml file.";
                String path = ConsoleUtils.getInputFromUser(messageToUser, errorMessage, this::isValidXMLPathFormat);
                if (!path.equalsIgnoreCase("Q")) {
                    engine.LoadData(path.trim());
                    System.out.println("File Loaded Successfully");
                }
            } catch (RuntimeException e) {
                System.out.println("Error Loading File:\n" + e.getMessage() + "\n");
            }
        }

        public boolean isValidXMLPathFormat(String filePath) {
            Path path = Paths.get(filePath);
            int suffixIndex;

            if (!Files.exists(path)) {
               return false;
            } else if ((suffixIndex = path.getFileName().toString().lastIndexOf(".")) == -1){
                return false;
            } else return !path.getFileName().toString().substring(suffixIndex).equals("xml");
        }

        @Override
        public String toString() {
            return "Load Sheet From XML File";

        }
    },
    SHOW_SHEET{
        @Override
        public void executeOption(Engine engine) {
            if (engine.isSheetLoaded()){
                ConsoleUtils.printSheet(engine.getSheetAsDTO());
            } else {
                ConsoleUtils.printSheetNotLoaded();
            }
        }

        @Override
        public String toString() {
            return "Show Sheet Details";
        }
    },
    SHOW_SINGLE_CELL{
        @Override
        public void executeOption(Engine engine) {
            if (engine.isSheetLoaded()){
                try {
                    String messageToUser = "Please Enter the cell ID(for example A4)";
                    String errorMessage = "The Cell ID # is Invalid, must be in format: Column as letter, and Row as number. examples: A4, d17.";
                    String cellID = ConsoleUtils.getInputFromUser(messageToUser, errorMessage, Sheet::isValidCellID);
                    if (!cellID.equalsIgnoreCase("Q")){
                        CellDTO cellDTO = engine.getSingleCellData(cellID);
                        if (cellDTO.isActive()) {
                            ConsoleUtils.printCell(cellDTO);
                        } else {
                            System.out.println("The cell " + cellID + " has no value yet.");
                        }
                    }
                } catch (RuntimeException e) {
                    System.out.println("Error Finding Cell:\n" + e.getMessage() + "\n");
                }
            } else {
                ConsoleUtils.printSheetNotLoaded();
            }
        }

        @Override
        public String toString() {
            return "Show Single Cell Details";
        }
    },
    UPDATE_SINGLE_CELL{
        @Override
        public void executeOption(Engine engine) {
            if (engine.isSheetLoaded()){
                try {
                    String messageToUser = "Please Enter the cell ID(for example A4)";
                    String errorMessage = "The Cell ID # is Invalid, must be in format: Column as letter, and Row as number. examples: A4, d17.";
                    String cellID = ConsoleUtils.getInputFromUser(messageToUser, errorMessage, Sheet::isValidCellID);                    if (!cellID.equalsIgnoreCase("q")) {
                        CellDTO cellDTO = engine.getSingleCellData(cellID);

                        if (cellDTO.isActive()) {
                            ConsoleUtils.printSimplifiedCell(cellDTO);
                        } else {
                            System.out.println("The cell " + cellID + " has no value yet.");
                        }

                        String newOriginalValue = ConsoleUtils.getOriginalValueFromUser(cellID);
                        engine.updateSingleCellData(cellID, newOriginalValue);
                        SHOW_SHEET.executeOption(engine);
                    }
                } catch (RuntimeException e) {
                    System.out.println("Error Updating Cell:\n" + e.getMessage() + "\n");
                }
            } else {
                ConsoleUtils.printSheetNotLoaded();
            }
        }

        @Override
        public String toString() {
            return "Update Single Cell Value";
        }
    },
    SHOW_VERSIONS{
        @Override
        public void executeOption(Engine engine) {
            if (engine.isSheetLoaded()){
                try {
                    VersionChangesDTO versionChangesDTO = engine.showVersions();
                    ConsoleUtils.printVersionsTable(versionChangesDTO);
                    String messageToUser = "Please enter the version number you wish to see";
                    String errorMessage = "The Version number # is not a valid Version Number." +
                            " version number must be a natural number  between 1 and " +
                            versionChangesDTO.getVersionChanges().size();
                    String version = ConsoleUtils.getInputFromUser(messageToUser, errorMessage, ArchiveImpl::isValidVersion);
                    if (!version.equalsIgnoreCase("Q")){
                        ConsoleUtils.printSheet(engine.getSheetVersionAsDTO(Integer.parseInt(version)));
                    }
                } catch (RuntimeException e) {
                    System.out.println("Error Accessing Archive:\n" + e.getMessage() + "\n");
                }
            } else {
                ConsoleUtils.printSheetNotLoaded();
            }
        }

        @Override
        public String toString() {
            return "Show Sheet Versions";
        }
    },
    SAVE_TO_FILE{
        @Override
        public void executeOption(Engine engine) {
            if (engine.isSheetLoaded()){
                try {
                    String messageToUser = "Please Enter the full path for the file you wish to save";
                    String errorMessage = "The File Path # is not a valid File Path.";
                    String path = ConsoleUtils.getInputFromUser(messageToUser, errorMessage, this::isValidPathFormat);
                    if (!path.equalsIgnoreCase("Q")) {
                        engine.SaveToFile(path.trim());
                        System.out.println("File saved Successfully");
                    }
                } catch (RuntimeException e) {
                    System.out.println("Error saving File:\n" + e.getMessage() + "\n");
                }
            } else {
                ConsoleUtils.printSheetNotLoaded();
            }
        }

        public boolean isValidPathFormat(String filePath) {
            try{
                Paths.get(filePath);
                return true;
            } catch (InvalidPathException e) {
                return false;
            }
        }

        @Override
        public String toString() {
            return "Save Current Sheet State To a File";
        }
    },
    LOAD_FROM_FILE{
        @Override
        public void executeOption(Engine engine) {
            try {
                    String messageToUser = "Please Enter the full path for the file you wish to Load";
                    String errorMessage = "The File Path # is not valid File Path or the File doesnt exists.";
                    String path = ConsoleUtils.getInputFromUser(messageToUser, errorMessage, this::isValidPathFormat);
                    if (!path.equalsIgnoreCase("Q")) {
                        engine.LoadFromFile(path.trim());
                        System.out.println("File loaded Successfully");
                    }
            } catch (RuntimeException e) {
                System.out.println("Error loading File:\n" + e.getMessage() + "\n");
            }
        }

        public boolean isValidPathFormat(String filePath) {
            try{
                Path path = Paths.get(filePath);
                return Files.exists(path);
            } catch (InvalidPathException e) {
                return false;
            }
        }

        @Override
        public String toString() {
            return "Load Existing Sheet From File";
        }
    },
    EXIT{
        @Override
        public void executeOption(Engine engine) {
            System.out.println("THANK YOU FOR USING SHTICELL!");
            exit(0);
        }

        @Override
        public String toString() {
            return "Exit Shticell";
        }
    };

    public abstract void executeOption(Engine engine);
}
