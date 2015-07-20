package Tests.WFP;

import TestPages.WFP.WFP_AdvancedSearch;
import TestPages.WFP.WFP_Edit;
import TestPages.WFP.WFP_LoginPage;
import TestPages.WFP.WFP_RequestPage;
import core.TestVee2.Base;
import core.TestVee2.Element;
import core.TestVee2.Page;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 * Created by aracautanu on 5/22/2015.
 */
public class TestForDemo extends Base{
// TEST ADVANCED SEARCH
    @Test
    public void testSearchByAllFiltersWithCorrectTestData() {

        WFP_LoginPage loginPage = new WFP_LoginPage();
        loginPage.loginUsername.type("gcornel");
        loginPage.loginPassword.type("cornel");
        loginPage.wait.forFixedTime(2);
        loginPage.loginButton.click();
        loginPage.wait.forFixedTime(3);

        WFP_AdvancedSearch searchpage = new WFP_AdvancedSearch();
        searchpage.searchIcon.click();
        loginPage.wait.forFixedTime(2);
        searchpage.showAdvancedOptBtn.click();
        loginPage.wait.forFixedTime(2);
        // TEST WITH CORRECT DATA
        searchpage.firstNameInput.type("Jon");
        loginPage.wait.forFixedTime(1);

        searchpage.lastNameLabel.click();
        loginPage.wait.forFixedTime(1);
        searchpage.lastNameInput.type("Ckusack");
        loginPage.wait.forFixedTime(1);
        searchpage.currentProjectLabel.click();
        searchpage.currentProjectList.select("All");
        loginPage.wait.forFixedTime(1);
        searchpage.bookedProjectLabel.click();
        searchpage.bookedProjectList.select("All");
        loginPage.wait.forFixedTime(1);
        while(!searchpage.businessUnitList.isVisible()) searchpage.businessUnitLabel.click();
        if(searchpage.businessUnitLabel.isVisible()) searchpage.businessUnitList.select("ISD");
        loginPage.wait.forFixedTime(1);
        searchpage.technologyLabel.click();
        searchpage.technologyList.select("All");
        loginPage.wait.forFixedTime(1);
        searchpage.searchButton.click();


        if (searchpage.getNumberOfResultsWithoutSearching() > 0) {
            System.out.println("MORE THEN 0 RESULTS");
            searchpage.expandButton.click();
            searchpage.verifyTrue(searchpage.getColumnValuesFromResultsTable(2).get(0).getText().toLowerCase().equals("jon")
                    && searchpage.getColumnValuesFromResultsTable(3).get(0).getText().toLowerCase().equals("ckusack")
                    && driver.findElement(By.id("businessUnit" + searchpage.employeeNumber)).getText().equals("ISD"));
        } else
            System.out.println("NO RESULTS FOR SEARCH");
        loginPage.wait.forFixedTime(10);

        // TEST WITH INCORRECT DATA
        System.out.println();
        System.out.println();
        System.out.println();
        searchpage.searchIcon.click();
        searchpage.showAdvancedOptBtn.click();
        searchpage.firstNameInput.type("Jonn");
        loginPage.wait.forFixedTime(1);
        searchpage.lastNameLabel.click();
        searchpage.lastNameInput.type("Ckusackk");
        loginPage.wait.forFixedTime(1);
        searchpage.currentProjectLabel.click();
        searchpage.currentProjectList.select("All");
        loginPage.wait.forFixedTime(1);
        while(!searchpage.businessUnitList.isVisible()) searchpage.businessUnitLabel.click();
        if(searchpage.businessUnitLabel.isVisible()) searchpage.businessUnitList.select("ISD");
        searchpage.searchButton.click();

        searchpage.verifyTrue(searchpage.getNumberOfResultsWithoutSearching() == 0);
        loginPage.wait.forFixedTime(10);
    }

    //TEST EDIT
    @Test
    public void testAllChangesAreSaved() {
        WFP_Edit edit = new WFP_Edit("gcornel", "cornel");
        int numberOfResults;
        Element editForm = new Element("edit_footer_content");
        String date = edit.buildDate();

        edit.wait.forFixedTime(3);
        numberOfResults = edit.getNumberOfResults("All", "Allocated");
        edit.wait.forFixedTime(3);
        if (numberOfResults != 0) {
            edit.expandButton.click();
            edit.wait.forFixedTime(2);
            edit.editButton.click();
            edit.wait.forFixedTime(2);
            edit.currentProjectField.type("TEST - New Current Project");
            edit.wait.forFixedTime(2);
            edit.allocationPercentField.type("20");
            edit.wait.forFixedTime(2);
            edit.technologyField.type("TEST - New Technology");
            edit.wait.forFixedTime(2);
            edit.releaseDateField.type(date);
            edit.wait.forFixedTime(2);
            edit.saveButton.click();
            editForm.wait.toFade(5, true);
            edit.wait.forFixedTime(10);

            edit.verifyTrue(driver.findElement(By.id("currentProject" + edit.employeeNumber)).getText().equals("TEST - New Current Project")
                            && driver.findElement(By.id("releaseDate" + edit.employeeNumber)).getText().equals(date)
                            && driver.findElement(By.id("allocationPercent" + edit.employeeNumber)).getText().equals("20")
                            && driver.findElement(By.id("technology" + edit.employeeNumber)).getText().equals("TEST - New Technology")
            );
        } else
            System.out.println("No Allocated Employees - No verifications done!");


    }

        // TEST WF REQUEST
        @Test
        public void testWorkforceRequestFunctionality() {
            WFP_RequestPage requestPage = new WFP_RequestPage("anamaria","anamaria");
            requestPage.selectEmail("wrkfrqsttst@mailinator.com");
            requestPage.wait.forFixedTime(5);
            requestPage.wfrSubjectMail.type("Test Subject mail");
            requestPage.wait.forFixedTime(5);
            requestPage.wfrSendButton.click();
            requestPage.wait.forFixedTime(5);
            requestPage.verifyTrue(requestPage.wfrNotificationDialog.isVisible());

            Page verifyEmail = new Page("https://mailinator.com/inbox.jsp?to=wrkfrqsttst");
            requestPage.wait.forFixedTime(5);
            verifyEmail.isTextPresent("Graduates@endava.com");

        }

}
