package ui.menu;

import java.util.List;

public interface MenuItem {

    List<MenuItem> menuItems();

    String getMenutemName();

     void executeOption();

}
