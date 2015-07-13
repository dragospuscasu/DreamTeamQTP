package TestPages.TestPages_Examples;

import core.TestVee2.Element;
import core.TestVee2.Page;

public class SearchPage extends Page {
    
    public final Element EndavaLogo             = new Element("//div[@class='wrapper clearfix']/a/img", "EndavaLogo");
    public final Element Search_Button          = new Element("ctl09_searchBox_btnSearch", "Search_Button");
    public final Element SearchMessage_Field    = new Element("ctl09_searchBox_txtCriteria", "SearchMessage_Field");
    
    public SearchPage() {
        wait.forText("Search for:", 30, true);
    }
    
    public void CheckSearchEngineFunctionality(String message) {
        SearchFor(message);
        verifyTrue(textCounterValidator(message, 2, true));
    }

    /**
     * ********************************
     * defining functions *******************************
     */    
    public EndavaHomePage clickOnEndavaLogo() {
        EndavaLogo.click();
        return new EndavaHomePage();
    }
    
    public void SearchFor(String message) {
        SearchMessage_Field.type(message);
        Search_Button.click();
        wait.forText("Search for:", 30, true);
        logger.debug("Searched for: " + message);
    }
}
