package Tests.WFP.Allocate;

import TestPages.WFP.WFP_SearchPage;
import core.TestVee2.Base;
import org.junit.Test;

public class TestAllocate_Smoke extends Base {
    @Test
    public void testIfAllocateBookEditButtonsAreVisible() {
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel", "cornel");
        searchPage.scroll_down();
        searchPage.expandButton.click();
        searchPage.verifyTrue(searchPage.bookButton.isVisible());
        searchPage.verifyTrue(searchPage.allocateButton.isVisible());
        searchPage.verifyTrue(searchPage.editButton.isVisible());
    }

    @Test
    public void testIfFormIsVisibleAfterClickOnAllocationButtonAndAllocationStatusIsAvailable() {
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel", "cornel");
        int numberOfResults = searchPage.getNumberOfResults("All", "Available");
        if (numberOfResults > 0 ) {
            searchPage.expandButton.click();
            searchPage.allocateButton.click();
            searchPage.allocationBarModalLabel.wait.toBeVisible(3,true);
            searchPage.verifyTrue(searchPage.isTextPresent("Allocate"));
        } else {
            System.out.println("No Available Employees - No verifications done!");
            searchPage.verifyTrue(false);
        }
    }

    @Test
    public void testIfFormIsVisibleAfterClickOnAllocationButtonAndAllocationsStatusIsBooked() {
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel", "cornel");
        int numberOfResults = searchPage.getNumberOfResults("All", "Booked");
        if (numberOfResults != 0) {
            searchPage.expandButton.click();
            searchPage.allocateButton.click();
            searchPage.allocationBarModalLabel.wait.toBeVisible(3,true);
            searchPage.verifyTrue(searchPage.isTextPresent("Booked"));
        } else {
            System.out.println("No Booked Employees - No verifications done!");
            searchPage.verifyTrue(false);
        }
    }

    @Test
    public void testIfFormIsVisibleAfterClickOnBookedButton() {
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel", "cornel");
        int numberOfResults = searchPage.getNumberOfResults("All", "Available");
        if (numberOfResults != 0) {
            searchPage.expandButton.click();
            searchPage.bookButton.click();
            searchPage.verifyTrue(searchPage.isTextPresent("Book"));
        } else {
            System.out.println("No Available Employees - No verifications done!");
            searchPage.verifyTrue(false);
        }
    }
}
