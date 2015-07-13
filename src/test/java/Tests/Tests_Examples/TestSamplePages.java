package Tests.Tests_Examples;

import TestPages.TestPages_Examples.*;
import core.TestVee2.Base;
import core.TestVee2.Element;
import core.TestVee2.Page;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class TestSamplePages extends Base {

    @Test
    public void testCareers() {
        EndavaHomePage home = new EndavaHomePage();
        CareersPage careers = home.clickOnCareersButton();
        JobSearchPage search = careers.selectOptionsForSearchJobAndClickOnFindJob_Flow();
        Element jobDescription = new Element("requisitionListInterface.reqTitleLinkAction.row3");
        home.verifyTrue(jobDescription.isVisible());
        jobDescription.click();
    }

    @Test
    public void testSearchFunctionality() {
        EndavaHomePage home = new EndavaHomePage();
        SearchPage search = home.clickOnSearchButton();
        search.CheckSearchEngineFunctionality("test");
    }

    @Test
    @FileParameters("cities.csv")
    public void testPresenceOfacityOnEasternEurope(String city, String emailAddress, String phoneNo){
        Page genericPage = new Page();
        EndavaHomePage home = new EndavaHomePage();
        ContactPage contact = home.clickOnContactButton();
        contact.changeToEasternEurope();
        int cityIndex = contact.findCityIndexInsideLocationList(city);
        Element CustomLocationElement = new Element("maincontent_0_phleftsidenavigation_0_rptLeftSideNavigation_lnkToItem_"+cityIndex, "CustomLocationElement"+cityIndex);
        CustomLocationElement.click();
        genericPage.wait.forText("Public Holidays in " + city, 15);
        genericPage.verifyTrue(contact.LocationTitle.elementContainsText(city));
        genericPage.verifyTrue(genericPage.isTextPresent(emailAddress));
        genericPage.verifyTrue(genericPage.isTextPresent(phoneNo));


    }
}
