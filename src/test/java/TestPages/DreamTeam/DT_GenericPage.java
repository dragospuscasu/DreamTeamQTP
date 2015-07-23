package TestPages.DreamTeam;

import core.TestVee2.Element;
import core.TestVee2.Page;

/**
 * Created by aracautanu on 7/13/2015.
 */
public class DT_GenericPage extends Page {

        public String URL = "";
        public Element header = new Element("classname=header-container");
        public Element footer = new Element("classname=footer");
        public Element menu_buttonLeft = new Element("menu_button_left");
        public Element menu_buttonRight = new Element("menu_button_right");

        public DT_GenericPage(){}

        public DT_GenericPage(String username, String password){
                DT_LoginPage loginPage = new DT_LoginPage();
                loginPage.login(username, password);
        }


}
