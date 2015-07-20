package Tests.WFP.Book;

import TestPages.WFP.WFP_SearchPage;
import core.TestVee2.Base;
import core.TestVee2.Element;
import org.junit.Test;
import org.openqa.selenium.By;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aracautanu on 5/15/2015.
 */
public class TestBook_Sanity extends Base {
    @Test
    public void testIfBookingCanBeMadeOnlyIfAllocationStatusIsAvailable() {
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel", "cornel");
        int numberOfResults = searchPage.getNumberOfResults("All","Available");
        String project = "TestProjectGraduation";
        if(numberOfResults > 0) {
            searchPage.expandButton.click();
            searchPage.bookButton.click();
            new Element("projectName").type(project);
            new Element("save_booked_employee").click();
            Element allocationStatus = new Element("allocationStatus" + searchPage.employeeNumber);
            Element bookedProject = new Element("bookedProject" + searchPage.employeeNumber);
            searchPage.searchDetailsBox.wait.toBeVisible(3, true);
            searchPage.verifyTrue(allocationStatus.getText().equals("Booked") &&
                    bookedProject.getText().toString().equals(project));
        }else {
            System.out.println("No Available Employees - No verifications done!");
            searchPage.verifyTrue(false);
        }
    }

    @Test
    public void testIfBookingCannotBeMadeWithAllocationStatusBooked() {
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel", "cornel");
        int numberOfResults = searchPage.getNumberOfResults("All","Booked");
        if(numberOfResults > 0) {
            searchPage.expandButton.click();
            searchPage.verifyTrue(!driver.findElement(By.id("bookEmployee" + searchPage.employeeNumber)).isEnabled());
        } else {
            System.out.println("No Booked Employees - No verifications done!");
            searchPage.verifyTrue(false);
        }
    }

    @Test
    public void testIfBookingCannotBeMadeWithAllocationStatusAllocated() {
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel", "cornel");
        int numberOfResults = searchPage.getNumberOfResults("All","Allocated");
        if(numberOfResults > 0) {
            searchPage.expandButton.click();
            searchPage.verifyTrue(!driver.findElement(By.id("bookEmployee" + searchPage.employeeNumber)).isEnabled());
        }else {
            System.out.println("No Allocated Employees - No verifications done!");
            searchPage.verifyTrue(false);
        }
    }

    @Test
    public void testIfCurrentDateIsUpdated() {
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel", "cornel");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String project = "TestProjectGraduation";

        int numberOfResults = searchPage.getNumberOfResults("All","Available");
        if(numberOfResults > 0) {
            searchPage.expandButton.click();
            System.out.println("NUMBER :" + searchPage.employeeNumber);
            searchPage.bookButton.click();
            new Element("projectName").type(project);
            new Element("save_booked_employee").click();
            searchPage.wait.forFixedTime(1);
            Element bookingDate = new Element("bookingDate" + searchPage.employeeNumber, "Booking date");
            bookingDate.wait.toBeVisible(5, true);
            searchPage.verifyTrue(bookingDate.getText().equals(dateFormat.format(date)));
        }else {
            System.out.println("No Available Employees - No verifications done!");
            searchPage.verifyTrue(false);
        }
    }
}
