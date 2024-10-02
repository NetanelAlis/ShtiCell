package ui;

import ui.menu.Menu;

public class Main {

    public static void main(String[] args) {
        System.out.println("WELCOME TO SHTICELL!\n");

        Menu mainMenu = new Menu();
        mainMenu.runMenu();
    }
}
