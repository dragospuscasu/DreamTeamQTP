package TestPages;

import core.TestVee2.Element;
import core.TestVee2.Page;

/**
 * Created by aracautanu on 7/13/2015.
 */
public class GenericPage extends Page {

        public String URL = "";
        public Element header = new Element("classname=header-container");
        public Element footer = new Element("classname=footer");
        public Element menu_buttonLeft = new Element("menu-button-left");
        public Element menu_buttonRight = new Element("menu-button-right");
        public Element goToUsersPage_button = new Element("link=Users");

        public GenericPage(){}

        public GenericPage(String username, String password){
                LoginPage loginPage = new LoginPage();
                loginPage.login(username, password);
        }


}
