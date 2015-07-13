package BDD;

import core.TestVee2.Page;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Created by bclim on 1/28/2015.
 */
public class SampleStepDefinitions extends Page {

    @Given(value = "^sample feature file is ready$")
    public void givenStatment(){
        System.out.println("Given statement executed successfully");
    }
    @When(value = "^I run the feature file$")
    public void whenStatement(){
        System.out.println("When statement executed successfully");
    }
    @Then(value = "^run should be successful$")
    public void thenStatment(){
        System.out.println("Then statement executed successfully");
    }



}
