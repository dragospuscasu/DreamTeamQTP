package Tests.WFP.Search;

import TestPages.WFP.WFP_SearchPage;
import core.TestVee2.Base;
import core.TestVee2.Element;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class TestSearch_Sanity extends Base {
    @Test
    public void testAllJobTitleOptionsAreVisible() {
        WFP_SearchPage searchPage = new WFP_SearchPage("anamaria","anamaria");
        ArrayList<String> jobTitleSelect = searchPage.getJobTitles();
        ArrayList<String> jobTitleOptions = new ArrayList<String>();

        jobTitleOptions.add("All");
        jobTitleOptions.add("Software Tester");
        jobTitleOptions.add("Software Developer");
        jobTitleOptions.add("Business Analyst");
        jobTitleOptions.add("Project Manager");
        jobTitleOptions.add("Software Architect");

        for(int i = 0; i < jobTitleOptions.size(); i++){
            String job = jobTitleOptions.get(i);
            searchPage.verifyTrue(jobTitleSelect.contains(job));
        }
    }

    @Test
    public void testAllAllocationStatusOptionsAreVisible() {
        WFP_SearchPage searchPage = new WFP_SearchPage("anamaria","anamaria");
        searchPage.searchIcon.click();
        searchPage.allocationStatusLabel.click();
        searchPage.wait.forText("All",5);
        ArrayList<String> allocationStatusSelect = searchPage.getAllocationStatus();
        ArrayList<String> allocationStatusOptions = new ArrayList<>();
        allocationStatusOptions.add("All");
        allocationStatusOptions.add("Allocated");
        allocationStatusOptions.add("Booked");
        allocationStatusOptions.add("Available");

        for(int i = 0; i < allocationStatusOptions.size(); i++){
            String status = allocationStatusOptions.get(i);
            searchPage.verifyTrue(allocationStatusSelect.contains(status));
        }
    }

    @Test
    public void testSelection_AllJob_OneStatus() {
        WFP_SearchPage search = new WFP_SearchPage("anamaria","anamaria");
        search.searchIcon.click();
        new Element("headingTwo").click();
        search.allocationStatusSelect.select("Available");
        search.clickOnSearchButton();
        boolean validator = true;
        List<WebElement> resultsAllocationStatus = search.getColumnValuesFromResultsTable(4);
        for(int i = 0; i < resultsAllocationStatus.size(); i++) {
            if(!resultsAllocationStatus.get(i).getText().equals("Available")) {
                validator = false;
                break;
            }
        }
        search.verifyTrue(validator);
    }
}
