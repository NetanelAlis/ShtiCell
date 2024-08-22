package ui.menu;

import logic.Engine;
import logic.EngineImpl;
import ui.output.Printer;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    private MainMenuOption chosenItem;
    Engine engine = new EngineImpl();

    public void runMenu() {
        do {
            Printer.printMenu();
            this.chosenItem = this.getMenuOptionFromUser();
            this.chosenItem.executeOption(engine);
        }
        while (true);
    }

    private MainMenuOption getMenuOptionFromUser() {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;
        MainMenuOption optionToReturn;

        try {

            userInput = scanner.nextInt();
        } catch (InputMismatchException e) {
//            MainMenuOptions.INITIALIZER.invoke()
            return MainMenuOption.INVALID_CHOICE;
        }

        return
                userInput > 0 && userInput < MainMenuOption.values().length ? MainMenuOption.values()[userInput] :
                        MainMenuOption.INVALID_CHOICE;
    }

    String getFilePathFromUser(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter The Full Path To your XML file:");
        return scanner.nextLine();
    }
}

