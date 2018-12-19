package se.gosta.storage;


public class MenuOption {

    public String menuOption;
    public int menuNavigation;

    /**
     * Constructor for Event
     * @param menuOption Name of the menu option
     * @param menuNavigation The ID of the destination to where the menuoption leads
     */
    public MenuOption (String menuOption, int menuNavigation){

        this.menuOption = menuOption;
        this.menuNavigation = menuNavigation;
    }

    /**
     * Returns a String representation of the MenuOption
     * @return a String representation of the MenuOption
     */
    @Override
    public String toString() {
        return menuOption;
    }
}
