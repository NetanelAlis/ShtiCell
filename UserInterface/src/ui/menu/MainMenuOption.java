package ui.menu;

import component.sheet.api.Sheet;
import dto.CellDTO;
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
            String path = ConsolePrinter.getInputFromUser("Please Enter the full path of the file you wish to load:", this::isValidPathFormat);
            try{
                engine.LoadDataFromXML(path);
                System.out.println("Load completed!");
            }
            catch(RuntimeException e){
                System.out.println("Error loading File:\n" + e.getMessage() + "\n");
            }
        }

        public boolean isValidPathFormat(String filePath) {
            Path path = Paths.get(filePath);
            boolean isValid = true;
            int suffixIndex;

            if (!Files.exists(path)) {
                System.out.println("The File " + filePath + " does not exist.");
                isValid = false;
            } else if ((suffixIndex = path.getFileName().toString().lastIndexOf(".")) == -1){
                System.out.println("The file " + filePath + " is not a valid XML file");
                isValid = false;
            } else if (path.getFileName().toString().substring(suffixIndex).equals("xml")){
                System.out.println("The file " + filePath + " is not a valid XML file");
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
                    String cellID = ConsolePrinter.getInputFromUser("Please Enter the cell ID(for example A4):", Sheet::isValidCellID);
                    CellDTO cellDTO = engine.geCellAsDTO(cellID);
                    if(cellDTO.isActive()){
                        ConsolePrinter.printCell(cellDTO);
                    }
                    else{
                        System.out.println("the cell" + cellID + "has no value yet");
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
                try{
                    String cellID = ConsolePrinter.getInputFromUser("Please Enter the cell ID(for example A4):", Sheet::isValidCellID);
                    CellDTO cellDTO = engine.geCellAsDTO(cellID);

                    if(cellDTO.isActive()){
                        ConsolePrinter.printSimplifiedCell(cellDTO);
                    }
                    else{
                        System.out.println("the cell" + cellID + "has no value yet");
                    }

                    String cellNewOriginalValue = ConsolePrinter.getOriginalValueFromUser(cellID);
                    engine.updateSingleCellData(cellID, cellNewOriginalValue);
                    SHOW_SHEET.executeOption(engine);
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