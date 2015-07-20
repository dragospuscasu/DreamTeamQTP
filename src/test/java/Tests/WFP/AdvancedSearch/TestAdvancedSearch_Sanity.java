package Tests.WFP.AdvancedSearch;

import TestPages.WFP.WFP_AdvancedSearch;
import core.TestVee2.Base;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 * Created by aracautanu on 5/22/2015.
 */
public class TestAdvancedSearch_Sanity extends Base {

    @Test
    public void testSearchByAllFiltersWithCorrectTestData() {

        WFP_AdvancedSearch searchpage = new WFP_AdvancedSearch("gcornel", "cornel");
        searchpage.searchIcon.click();
        searchpage.showAdvancedOptBtn.click();
        searchpage.firstNameInput.type("Jon");
        searchpage.lastNameLabel.click();
        searchpage.lastNameInput.type("Ckusack");
        searchpage.currentProjectLabel.click();
        searchpage.currentProjectList.select("All");
        searchpage.bookedProjectLabel.click();
        searchpage.bookedProjectList.select("All");
        while(!searchpage.businessUnitList.isVisible()) searchpage.businessUnitLabel.click();
        if(searchpage.businessUnitLabel.isVisible()) searchpage.businessUnitList.select("ISD");
        searchpage.technologyLabel.click();
        searchpage.technologyList.select("All");
        searchpage.searchButton.click();
        if (searchpage.getNumberOfResultsWithoutSearching() > 0) {
            System.out.println("MORE THEN 0 RESULTS");
            searchpage.expandButton.click();
            searchpage.verifyTrue(searchpage.getColumnValuesFromResultsTable(2).get(0).getText().toLowerCase().equals("jon"));
            searchpage.verifyTrue(searchpage.getColumnValuesFromResultsTable(3).get(0).getText().toLowerCase().equals("ckusack"));
            searchpage.verifyTrue(driver.findElement(By.id("businessUnit" + searchpage.employeeNumber)).getText().equals("ISD"));
        } else
            System.out.println("NO RESULTS FOR SEARCH");

    }

    @Test
    public void testSearchByAllFiltersWithIncorrectTestData() {
        WFP_AdvancedSearch searchpage = new WFP_AdvancedSearch("gcornel", "cornel");
        searchpage.searchIcon.click();
        searchpage.showAdvancedOptBtn.click();
        searchpage.firstNameInput.type("Jonn");
        searchpage.lastNameLabel.click();
        searchpage.lastNameInput.type("Ckusackk");
        searchpage.currentProjectLabel.click();
        searchpage.currentProjectList.select("All");
        while(!searchpage.businessUnitList.isVisible()) searchpage.businessUnitLabel.click();
        if(searchpage.businessUnitLabel.isVisible()) searchpage.businessUnitList.select("ISD");
        searchpage.searchButton.click();
        searchpage.verifyTrue(searchpage.getNumberOfResultsWithoutSearching() == 0);
    }

    @Test
    public void testSearchByOneFilterCorrectTestData() {
        WFP_AdvancedSearch searchpage = new WFP_AdvancedSearch("gcornel", "cornel");
        searchpage.searchIcon.click();
        searchpage.showAdvancedOptBtn.click();
        searchpage.firstNameInput.type("Jon");
        searchpage.searchButton.click();
        int nrResults = searchpage.getNumberOfResultsWithoutSearching();
        if (nrResults > 0) {
            System.out.println("MORE THEN 0 RESULTS");
            searchpage.verifyTrue(searchpage.getColumnValuesFromResultsTable(2).get(0).getText().toLowerCase().equals("jon"));
        } else
            System.out.println("NO RESULTS FOR SEARCH");
    }
}
