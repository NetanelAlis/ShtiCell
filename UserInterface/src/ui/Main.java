package ui;

import ui.menu.MainMenuOptions;
import ui.menu.Menu;
import ui.menu.MenuItem;

public class Main {
    public static void main(String[] args) {

        MenuItem menuItem = MainMenuOptions.INITIALIZER;
        Menu menu = new Menu(menuItem.menuItems());

        menu.printMenu();
    }
}
