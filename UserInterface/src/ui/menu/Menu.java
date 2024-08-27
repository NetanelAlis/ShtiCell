package ui.menu;

import logic.Engine;
import logic.EngineImpl;
import ui.output.ConsolePrinter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    private MainMenuOption chosenItem;
    Engine engine = new EngineImpl();

    public void runMenu() {
        do {
            ConsolePrinter.printMainMenu();
            this.chosenItem = this.getMenuOptionFromUser();
            this.chosenItem.executeOption(engine);
        }
        while (true);
    }

    private MainMenuOption getMenuOptionFromUser() {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        try {

            userInput = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Error: input entered is not a whole number");
        }

          return userInput > 0 && userInput < MainMenuOption.values().length ?
                    MainMenuOption.values()[userInput] :
                    MainMenuOption.INVALID_CHOICE;
    }
}

