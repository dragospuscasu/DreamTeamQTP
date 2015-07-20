package TestPages.WFP;

import core.TestVee2.Element;
import core.TestVee2.Page;

public class WFP_GenericPage extends Page {

   // public final String url                   = "http://192.168.196.13:8888/grads-team3/search/searchPage";
    public final String url                     = "http://localhost:8080/WorkForcePlanning/search/searchPage";
    public final Element logo                   = new Element("logoHyperlink");
    public final Element main_menu              = new Element("menu_wrapper","Main menu");
    public final Element menuUser               = new Element("menu-user-button","Menu User");
    public final Element logOutButton           = new Element("menu-user-logout-button");
    public final Element searchButtonMainMenu   = new Element("menu-search-button");

    public WFP_GenericPage(){}

    public WFP_GenericPage(String user, String pswd){
        WFP_LoginPage loginPage = new WFP_LoginPage();
        loginPage.login(user, pswd);
    }

    public void scroll_down() { javascript("window.scrollBy(0,250)", true);  }
    public void scroll_down(int nr) {
        javascript("window.scrollBy(0,"+nr+")",true);
    }
    public void scroll_up() { javascript("window.scrollTo(0,0)",true);  }

}
