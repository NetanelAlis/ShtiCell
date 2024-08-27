package ui.menu;

import component.archive.api.Archive;
import component.archive.impl.ArchiveImpl;
import component.sheet.api.Sheet;
import dto.CellDTO;
import dto.VersionsChangesDTO;
import logic.Engine;
import ui.output.ConsolePrinter;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.exit;

public enum MainMenuOption {
    INVALID_CHOICE {
        @Override
        public void executeOption(Engine engine) {
            System.out.println("No such menu option, Please Try Again:");
        }
    },
    LOAD_XML_FILE {
        @Override
        public void executeOption(Engine engine) {
            try{
                String userMessage = "Please Enter the full path of the file you wish to load:";
                String errorMessage = "The file # is not a valid XML file";
                String input = ConsolePrinter.getInputFromUser(userMessage, errorMessage, this::isValidPathFormat);
                if(!input.equalsIgnoreCase("q")) {
                    engine.LoadDataFromXML(input);
                    System.out.println("Load completed!");
                }
                } catch (RuntimeException e) {
                    System.out.println("Error loading File:\n" + e.getMessage() + "\n");
                }
        }

        public boolean isValidPathFormat(String filePath) {
            Path path = Paths.get(filePath);
            boolean isValid = true;
            int suffixIndex;

            if (!Files.exists(path)) {
                isValid = false;
                } else if ((suffixIndex = path.getFileName().toString().lastIndexOf(".")) == -1){
                    isValid = false;
                } else if (path.getFileName().toString().substring(suffixIndex).equals("xml")){
                    isValid = false;
                }

            return isValid;
        }

        @Override
        public String toString() {
            return "Load Sheet From XML File";

        }
    },
    SHOW_SHEET {
        @Override
        public void executeOption(Engine engine) {
            if(engine.isSheetLoaded()){
                ConsolePrinter.printSheet(engine.getSheetAsDTO());
            }
            else{
                ConsolePrinter.printMainMenu();
            }
        }

        @Override
        public String toString() {
            return "Show Sheet Details";
        }
    },
    SHOW_SINGLE_CELL {
        @Override
        public void executeOption(Engine engine) {
            if(engine.isSheetLoaded()){
                try{
                    String userMessage = "Please Enter the cell ID(for example A4)";
                    String errorMessage = "The cell ID is not valid. It must be in the format: a letter for the column followed by a number for the row (e.g., A4)";
                    String cellID = ConsolePrinter.getInputFromUser(userMessage,errorMessage, Sheet::isValidCellID);
                    if(!cellID.equalsIgnoreCase("q")) {
                        CellDTO cellDTO = engine.geCellAsDTO(cellID);
                        if (cellDTO.isActive()) {
                            ConsolePrinter.printCell(cellDTO);
                        } else {
                            System.out.println("the cell" + cellID + "has no value yet");
                        }
                    }
                }
                catch(RuntimeException e){
                    System.out.println(e.getMessage());
                }
            }
            else{
                ConsolePrinter.printMainMenu();
            }
        }

        @Override
        public String toString() {
            return "Show Single Cell Details";
        }
    },
    UPDATE_SINGLE_CELL {
        @Override
        public void executeOption(Engine engine) {
            if(engine.isSheetLoaded()){
                try {
                    String userMessage = "Please Enter the cell ID(for example A4)";
                    String errorMessage = "The cell ID is not valid. It must be in the format: a letter for the column followed by a number for the row (e.g., A4)";
                    String cellID = ConsolePrinter.getInputFromUser(userMessage, errorMessage, Sheet::isValidCellID);
                    if (!cellID.equalsIgnoreCase("q")) {
                        CellDTO cellDTO = engine.geCellAsDTO(cellID);

                        if (cellDTO.isActive()) {
                            ConsolePrinter.printSimplifiedCell(cellDTO);
                        } else {
                            System.out.println("the cell" + cellID + "has no value yet");
                        }

                        String cellNewOriginalValue = ConsolePrinter.getOriginalValueFromUser(cellID);
                        engine.updateSingleCellData(cellID, cellNewOriginalValue);
                        SHOW_SHEET.executeOption(engine);
                    }
                }
                catch(RuntimeException e){
                    System.out.println(e.getMessage());
                }
            }
            else{
                ConsolePrinter.printMainMenu();
            }

        }

        @Override
        public String toString() {
            return "Update Single Cell Value";
        }
    },

    SHOW_VERSIONS {
        @Override
        public void executeOption(Engine engine) {
            if(engine.isSheetLoaded()){
                try {
                    String userMessage = "Please enter the version number you wish to see";
                    String errorMessage = "The version number # is not a valid version number.";
                    ConsolePrinter.printVersions(engine.getVersionsChangesAsDTO());
                    String version = ConsolePrinter.getInputFromUser(userMessage,errorMessage, ArchiveImpl::isValidVersion);
                    if (!version.equalsIgnoreCase("q")) {
                        ConsolePrinter.printSheet(engine.getSheetVersionsAsDTO(Integer.parseInt(version)));
                    }
                }
                catch (RuntimeException e){
                    System.out.println("Error Accessing Archive " + e.getMessage() + "\n");
                }

            }
        }

        @Override
        public String toString() {
            return "Show Sheet Versions";
        }
    },
    EXIT {
        @Override
        public void executeOption(Engine engine) {
            exit(0);
        }

        @Override
        public String toString() {
            return "Exit Shticell";
        }
    };

    public abstract void executeOption(Engine engine);

}