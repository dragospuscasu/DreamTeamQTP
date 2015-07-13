/*
 * All rights reserved. This program and the accompanying materials 
 * can be made available only with Endava agreement.
 */

package BDD;

import TestPages.TestPages_Examples.ContactPage;
import TestPages.TestPages_Examples.EndavaHomePage;
import core.TestVee2.Base;
import core.TestVee2.Element;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 *
 * @author bclim
 */

public class EndavaStepsDefinitions extends Base {

    private EndavaHomePage home;
    private ContactPage contact;


    @Given(value = "^I have the Endava Home Page$")
    public void I_Have_Endava_HomePage()
    {
           home = new EndavaHomePage();
    }
    
    @When(value = "^I load the page$")
    public void I_load_the_page() {
        home.verifyTrue(home.getTitle().contains("Endava: In Your Zone"));
    }
    
    @Then("^the careers link should be displayed$")
    public void the_careers_link_displayed() {
        home.verifyTrue(home.Careers_Button.isVisible());
    }

    @When("^I click Contact link$")
    public void I_click_Contact_link(){
        contact = home.clickOnContactButton();

    }

    @When("^I click Eastern Europe buton on Contact page$")
    public void I_click_Eastern_Europe_button_on_Contact_page(){
        contact.changeToEasternEurope();
    }
    @Then("I should see the (.*) and (.*)")
    public void Then_I_Should_See_X_and_Y(String location, String details){

        int cityIndex = contact.findCityIndexInsideLocationList(location);
        Element CustomLocationElement = new Element("maincontent_0_phleftsidenavigation_0_rptLeftSideNavigation_lnkToItem_"+cityIndex, "CustomLocationElement"+cityIndex);
        CustomLocationElement.click();
        home.wait.forText("Public Holidays in " + location, 15);
        home.verifyTrue(contact.LocationTitle.elementContainsText(location));
        home.verifyTrue(home.isTextPresent(details));
    }
}
