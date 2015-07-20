package Tests.WFP.Search;

import TestPages.WFP.WFP_SearchPage;
import core.TestVee2.Base;
import core.TestVee2.Element;
import org.junit.Test;

public class TestSearch_Smoke extends Base {
    @Test
    public void testAllFiltersAreVisibleInSearchForm(){
        WFP_SearchPage searchPage = new WFP_SearchPage("anamaria","anamaria");
        searchPage.verifyTrue(searchPage.jobTitleSelect.isElementPresent());
        searchPage.verifyTrue(searchPage.allocationStatusSelect.isElementPresent());
    }

    @Test
    public void testHeadTableIsVisible() {
        WFP_SearchPage search = new WFP_SearchPage("anamaria","anamaria");
        search.verifyTrue(search.isTextPresent("Id"));
        search.verifyTrue(search.isTextPresent("First name"));
        search.verifyTrue(search.isTextPresent("Last name"));
        search.verifyTrue(search.isTextPresent("Allocation status"));
        search.verifyTrue(search.isTextPresent("Current project"));
        search.verifyTrue(search.isTextPresent("Booked project"));
        search.verifyTrue(search.isTextPresent("Job title"));
        search.verifyTrue(search.isTextPresent("Grade"));
        search.verifyTrue(search.isTextPresent("Details"));
    }
    @Test
    public void testIfAllExportButtonAreVisible() {
        WFP_SearchPage search = new WFP_SearchPage("anamaria","anamaria");
        for(int i = 0; i < 5; i++) {
            Element e = new Element("ToolTables_searchResults_" + i);
            search.verifyTrue(e.isVisible());
        }
    }

    @Test
    public void testIfDetailsButtonIsFunctionalAndAllOptionsAreVisible() {
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel","cornel");
        searchPage.expandButton.click();
        searchPage.verifyTrue(searchPage.isTextPresent("Allocation Percent"));
        searchPage.verifyTrue(searchPage.isTextPresent("Discipline"));
        searchPage.verifyTrue(searchPage.isTextPresent("Release date"));
        searchPage.verifyTrue(searchPage.isTextPresent("Booking date"));
        searchPage.verifyTrue(searchPage.isTextPresent("Business unit"));
        searchPage.verifyTrue(searchPage.isTextPresent("Technology"));
        searchPage.verifyTrue(searchPage.isTextPresent("Core skill"));
    }
}
