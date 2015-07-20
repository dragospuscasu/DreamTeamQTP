package Tests.WFP.Allocate;

import TestPages.WFP.WFP_SearchPage;
import core.TestVee2.Base;
import core.TestVee2.Element;
import org.junit.Test;
import org.openqa.selenium.By;

public class TestAllocate_Sanity extends Base {
    @Test
    public void testIfModificationsAreVisibleAfterChangingAllocationStatus() {
        WFP_SearchPage allocate = new WFP_SearchPage("gcornel", "cornel");
        String releaseDate = allocate.buildDate();
        int numberOfResults = allocate.getNumberOfResults("All", "Available");
        if (numberOfResults != 0) {
            allocate.expandButton.click();
            allocate.allocateButton.click();
            allocate.allocationInpAlloc.type("80");
            allocate.projectInputAlloc.type("TestProjectGraduation");
            allocate.releaseDateAlloc.type(releaseDate);
            allocate.saveChangesAlloc.click();
            allocate.searchDetailsBox.wait.toBeVisible(3, true);
            allocate.verifyTrue(driver.findElement(By.id("allocationStatus" + allocate.employeeNumber)).getText().equals("Allocated"));
            allocate.verifyTrue(driver.findElement(By.id("currentProject" + allocate.employeeNumber)).getText().equals("TestProjectGraduation"));
            allocate.verifyTrue(driver.findElement(By.id("allocationPercent" + allocate.employeeNumber)).getText().equals("80"));
            allocate.verifyTrue(driver.findElement(By.id("releaseDate" + allocate.employeeNumber)).getText().equals(releaseDate));
        } else {
            System.out.println("No Available Employees - No verifications done!");
            allocate.verifyTrue(false);
        }
    }

    @Test
    public void testInvalidReleaseDateForAllocation() {
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel", "cornel");
        int numberOfResults = searchPage.getNumberOfResults("All","Available");
        if(numberOfResults != 0) {
            searchPage.expandButton.click();
            searchPage.allocateButton.click();
            searchPage.releaseDateAlloc.type("2012-05-13");
            searchPage.saveChangesAlloc.click();
            searchPage.verifyTrue(searchPage.isTextPresent("Release date must be in the future"));
        }
        else {
            System.out.println("No Available Employees - No verifications done!");
            searchPage.verifyTrue(false);
        }
    }

    @Test
    public void testIfErrorMessageAppearsIfNotCurrentProjectIsSelected() {
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel", "cornel");
        int numberOfResults = searchPage.getNumberOfResults("All","Available");
        if(numberOfResults != 0) {
            searchPage.expandButton.click();
            searchPage.allocateButton.click();
            searchPage.releaseDateAlloc.type(searchPage.buildDate());
            searchPage.saveChangesAlloc.click();
            searchPage.verifyTrue(searchPage.isTextPresent("No current project selected"));
        } else {
            System.out.println("No Available Employees - No verifications done!");
            searchPage.verifyTrue(false);
        }
    }
    @Test
    public void testThatAfterAnBookedEmployeeIsAllocatedThenBookedProjectIsChangedToNone() {
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel","cornel");
        int number = searchPage.employeeNumber;
        int numberOfResults = searchPage.getNumberOfResults("All","Booked");
        String project = "TestProjectGraduation", allocPercent = "35", releaseDate = searchPage.buildDate();;
        if(numberOfResults != 0) {
            searchPage.expandButton.click();
            searchPage.allocateButton.click();
            searchPage.allocationInpAlloc.type(allocPercent);
            searchPage.projectInputAlloc.type(project);
            searchPage.releaseDateAlloc.type(releaseDate);
            searchPage.saveChangesAlloc.click();
            searchPage.searchDetailsBox.wait.toBeVisible(3, true);
            Element projectStatus = new Element("bookedProject" + number, "Booked project");
            searchPage.verifyTrue(projectStatus.getText().length() == 0);
        }else {
            System.out.println("No Booked Employees - No verifications done!");
            searchPage.verifyTrue(false);
        }
    }

}
