package TestPages.TestPages_Examples;

import core.TestVee2.Element;
import core.TestVee2.Page;

public class EndavaHomePage extends Page {

    private String url = "http://www.endava.com";

    public final Element Careers_Button         = new Element("link=Careers", "Careers_Button");
    public final Element Contact_Button         = new Element("link=Contact", "Contact_Button");
    public final Element IasiContact_Link       = new Element("//div[@class='region-cities clearfix']/div/div/ul/li[4]/a", "IasiContact_Link");
    public final Element Home_Button            = new Element("ctl09_lnkHome", "Home_Button");
    public final Element Search_Button          = new Element("ctl09_searchBox_btnSearch", "Search_Button");
    public final Element SearchMessage_Field    = new Element("ctl09_searchBox_txtCriteria", "SearchMessage_Field");

    public EndavaHomePage() {
        open(url);
        wait.forText("Copyright Endava 2014", 15, true);
        wait.forText("All rights reserved.", 15, true);
    }

    public CareersPage clickOnCareersButton() {
        Careers_Button.click();
        return new CareersPage();
    }

    public ContactPage clickOnContactButton() {
        Contact_Button.click();
        return new ContactPage();
    }

    public ContactPage clickOnIasiContactLink() {
        IasiContact_Link.click();
        return new ContactPage();
    }

    public SearchPage clickOnSearchButton() {
        Search_Button.click();
        return new SearchPage();
    }

    public SearchPage clickOnSearchButton(String message) {
        SearchMessage_Field.type(message);
        Search_Button.click();
        return new SearchPage();
    }

}
