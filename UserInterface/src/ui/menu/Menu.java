package ui.menu;

import java.util.List;

public class Menu {

    private List<MenuItem> menuItems;

    public Menu(List<MenuItem> list) {
        this.menuItems = list;
    }

    public void runMenu() {

        printMenu();
    }

    public void printMenu(){

        System.out.println("Welcome to Shticell!\nplease choose on of the following option below:");
        for (int i = 0; i < menuItems.size(); ++i) {
            MenuItem menuItem = menuItems.get(i);
            System.out.println((i + 1) + ")" + menuItem.getMenutemName());
        }
    }
}
