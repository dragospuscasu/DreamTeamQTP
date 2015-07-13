package TestPages.TestPages_Examples;

import core.TestVee2.Element;
import core.TestVee2.Page;

public class ContactPage extends Page {

    public final Element EndavaLogo                 = new Element("//div[@class='wrapper clearfix']/a/img", "EndavaLogo");
    public final Element EasternEurope_Button       = new Element("//div[@class='contactus-regions']/div/div/a/span", "EasternEurope_Button");
    public final Element Iasi_Button                = new Element("maincontent_0_phleftsidenavigation_0_rptLeftSideNavigation_lnkToItem_3", "Iasi_Button");
    public final Element LocationTitle              = new Element("#mainform > div.wrapper.clearfix > h2", "LocationTitle");


    public ContactPage() {
        wait.forText("Contact us", 15, true);
    }

    public int findCityIndexInsideLocationList(String city){
        int lineNumber =-1;
        for (int i = 0; i <= 4; i++){
             Element x = new Element("maincontent_0_phleftsidenavigation_0_rptLeftSideNavigation_lnkToItem_"+i,"locationElement"+ i);
            logger.debug("the value found is " + x.getText() + " the value searched is " + city);
             if(x.getText().contains(city))
                lineNumber = i;
        }
        return lineNumber;
    }



    public EndavaHomePage NavigateToIasiLocationAndGoHome() {
        changeToEasternEurope();
        changeToIasi();
        EndavaLogo.click();
        return new EndavaHomePage();
    }

    /**
     * ********************************
     * defining functions  
	 ********************************
     */
    public EndavaHomePage clickOnEndavaLogo() {
        EndavaLogo.click();
        return new EndavaHomePage();
    }

    public void changeToEasternEurope() {
        EasternEurope_Button.click();
        wait.forText("Bucharest", 30, true);
    }

    public void changeToIasi() {
        Iasi_Button.click();
        wait.forText("Public Holidays in Iasi", 30, true);
    }

}
