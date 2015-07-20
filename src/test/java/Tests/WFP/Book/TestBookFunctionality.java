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
 * Created by rastan on 5/13/2015.
 */
public class TestBookFunctionality extends Base {

    @Test
    public void testIfBookingCanBeMadeOnlyIfAllocationStatusIsAvailable() {
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel", "cornel");
        int number = searchPage.employeeNumber;
        int numberOfResults = searchPage.getNumberOfResults("All","Available");
        String project = "TestProjectGraduation";
        if(numberOfResults > 0) {
            new Element("expand" + number).click();
            new Element("bookEmployee" + number).click();
            new Element("projectName").type(project);
            new Element("save_booked_employee").click();
        }
        Element allocationStatus = new Element("allocationStatus" + number);
        Element bookedProject = new Element("bookedProject" + number);
        searchPage.searchDetailsBox.wait.toBeVisible(3,true);
        searchPage.verifyTrue(allocationStatus.getText().equals("Booked") &&
                bookedProject.getText().toString().equals(project));
    }

    @Test
    public void testIfBookingCannotBeMadeWithAllocationStatusBooked() {
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel", "cornel");
        int number = searchPage.employeeNumber;
        int numberOfResults = searchPage.getNumberOfResults("All","Booked");
        if(numberOfResults > 0) {
            new Element("expand" + number).click();
            searchPage.verifyTrue(!driver.findElement(By.id("bookEmployee" + number)).isEnabled());
        }
    }

    @Test
    public void testIfBookingCannotBeMadeWithAllocationStatusAllocated() {
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel", "cornel");
        int number = searchPage.employeeNumber;
        int numberOfResults = searchPage.getNumberOfResults("All","Allocated");
        if(numberOfResults > 0) {
            new Element("expand" + number).click();
            searchPage.verifyTrue(!driver.findElement(By.id("bookEmployee" + number)).isEnabled());
        }
    }

    @Test
    public void testIfCurrentDateIsUpdated() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String project = "TestProjectGraduation";
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel", "cornel");
        int number = searchPage.employeeNumber;
        int numberOfResults = searchPage.getNumberOfResults("All","Available");
        if(numberOfResults > 0) {
            new Element("expand" + number).click();
            System.out.println("NUMBER :" + number);
            new Element("bookEmployee" + number).click();
            new Element("projectName").type(project);
            new Element("save_booked_employee").click();
        }
        Element bookingDate = new Element("bookingDate" + number, "Booking date");
        bookingDate.wait.toBeVisible(5,true);
        searchPage.verifyTrue(bookingDate.getText().equals(dateFormat.format(date)));
    }
}