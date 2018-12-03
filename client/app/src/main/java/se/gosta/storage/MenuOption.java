package se.gosta.storage;


public class MenuOption {

    public String menuOption;
    public int menuNavigation;


    public MenuOption (String menuOption, int menuNavigation){

        this.menuOption = menuOption;
        this.menuNavigation = menuNavigation;
    }

    @Override
    public String toString() {
        return menuOption;
    }
}
