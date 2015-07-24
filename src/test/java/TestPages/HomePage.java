package TestPages;

import core.TestVee2.Element;

/**
 * Created by aracautanu on 7/23/2015.
 */
public class HomePage extends GenericPage {

    public Element goToUsers = new Element("link=users");
    public Element goToCourses = new Element("link=courses");

    public HomePage(String username, String password) {
        super(username, password);
        menu_buttonLeft.wait.toBeVisible(10);
    }

    public HomePage(){
        open(URL);
        menu_buttonLeft.wait.toBeVisible(10);
    }

}
