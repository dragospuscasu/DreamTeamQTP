package BDD;

import TestPages.TestPages_Examples.GooglePage;
import core.TestVee2.Page;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Created by bclim on 2/4/2015.
 */
public class GoogleSptesDefinitions extends Page {

    public GooglePage page1;
    @Given("^I open Google Page$")
    public void I_open_Google_Page() {
        page1 = new GooglePage();
    }

    @When("^I search for (.*)$")
    public void I_search_for(String text){
        page1.CheckOutTheEasterEggs(text, 1);
    }

    @Then("^I get the easter Egg result$")
    public void I_get_the_easter_Egg_result() {
        page1.wait.forFixedTime(50);

}
}
