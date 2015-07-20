package Tests.WFP.Edit;

import TestPages.WFP.WFP_Edit;
import core.TestVee2.Base;
import core.TestVee2.Element;
import org.junit.Test;
import org.openqa.selenium.By;

import java.util.List;

/**
 * Created by aracautanu on 5/15/2015.
 */
public class TestEdit_Sanity extends Base {

    @Test
    public void testFieldsAreEditable_ForBookedEmployee() {
        WFP_Edit edit = new WFP_Edit("gcornel", "cornel");
        int numberOfResults = edit.getNumberOfResults("All", "Booked");

        if (numberOfResults != 0) {
            edit.expandButton.click();
            edit.editButton.click();
            edit.verifyTrue(edit.bookedProjectField.isEditable());
            edit.verifyTrue(edit.technologyField.isEditable());
            edit.verifyFalse(edit.currentProjectField.isEditable());
            edit.verifyFalse(edit.allocationPercentField.isEditable());
            edit.verifyFalse(edit.releaseDateField.isEditable());
        } else
            System.out.println("No Booked Employees - No verifications done!");
    }

    @Test
    public void testFieldsAreEditable_ForAvailableEmployee() {
        WFP_Edit edit = new WFP_Edit("gcornel", "cornel");
        int numberOfResults = edit.getNumberOfResults("All", "Available");

        if (numberOfResults != 0) {
            edit.expandButton.click();
            edit.editButton.click();
            edit.verifyTrue(edit.technologyField.isEditable());
            edit.verifyFalse(edit.bookedProjectField.isEditable());
            edit.verifyFalse(edit.currentProjectField.isEditable());
            edit.verifyFalse(edit.allocationPercentField.isEditable());
            edit.verifyFalse(edit.releaseDateField.isEditable());
        } else
            System.out.println("No Available Employees - No verifications done!");
    }

    @Test
    public void testFieldsAreEditable_ForAllocatedEmployee() {
        WFP_Edit edit = new WFP_Edit("gcornel", "cornel");
        int numberOfResults = edit.getNumberOfResults("All", "Allocated");

        if (numberOfResults != 0) {
            edit.expandButton.click();
            edit.editButton.click();
            edit.verifyTrue(edit.allocationPercentField.isEditable());
            edit.verifyTrue(edit.currentProjectField.isEditable());
            edit.verifyTrue(edit.releaseDateField.isEditable());
            edit.verifyTrue(edit.technologyField.isEditable());
            edit.verifyFalse(edit.bookedProjectField.isEditable());
        } else
            System.out.println("No Allocated Employees - No verifications done!");
    }

    @Test
    public void testListBoxesAcceptNewEntries_CurrentProjectAndTechnologyList() {
        WFP_Edit edit = new WFP_Edit("gcornel", "cornel");
        int numberOfResults;
        List<String> datalistContent;
        Element editForm = new Element("edit_footer_content");

        numberOfResults = edit.getNumberOfResults("All", "Allocated");
        if (numberOfResults != 0) {
            edit.expandButton.click();
            edit.editButton.click();
            edit.currentProjectField.type("TEST - New Current Project");
            edit.technologyField.type("TEST - New Technology");
            edit.releaseDateField.type(edit.buildDate());
            edit.saveButton.click();
            editForm.wait.toFade(5, true);
            edit.editButton.click();
            datalistContent = edit.getAllDatalistOptions("current_project_list_edit");
            edit.verifyTrue(datalistContent.contains("TEST - New Current Project"));
            datalistContent = edit.getAllDatalistOptions("technology_list_edit");
            edit.verifyTrue(datalistContent.contains("TEST - New Technology"));
        } else
            System.out.println("No Allocated Employees - No verifications done!");
    }

    @Test
    public void testListBoxesAcceptNewEntries_BookedProjectList() {
        WFP_Edit edit = new WFP_Edit("gcornel", "cornel");
        int numberOfResults;
        Element editForm = new Element("edit_footer_content");

        numberOfResults = edit.getNumberOfResults("All", "Booked");
        if (numberOfResults != 0) {
            edit.expandButton.click();
            edit.editButton.click();
            edit.bookedProjectField.type("TEST - New booked project");
            edit.saveButton.click();
            editForm.wait.toFade(5, true);
            edit.editButton.click();
            List<String> datalistContent = edit.getAllDatalistOptions("booked_project_list_edit");
            edit.verifyTrue(datalistContent.contains("TEST - New booked project"));
        } else
            System.out.println("No Booked Employees - No verifications done!");
    }
    @Test
    public void testAllChangesAreSaved() {
        WFP_Edit edit = new WFP_Edit("gcornel", "cornel");
        int numberOfResults;
        Element editForm = new Element("edit_footer_content");
        String date = edit.buildDate();
        numberOfResults = edit.getNumberOfResults("All", "Allocated");
        if (numberOfResults != 0) {
            edit.expandButton.click();
            edit.editButton.click();
            edit.currentProjectField.type("TEST - New Current Project");
            edit.releaseDateField.type(date);
            edit.allocationPercentField.type("20");
            edit.technologyField.type("TEST - New Technology");
            edit.saveButton.click();
            editForm.wait.toFade(5, true);

            edit.verifyTrue(driver.findElement(By.id("currentProject" + edit.employeeNumber)).getText().equals("TEST - New Current Project")
                            && driver.findElement(By.id("releaseDate" + edit.employeeNumber)).getText().equals(date)
                            && driver.findElement(By.id("allocationPercent" + edit.employeeNumber)).getText().equals("20")
                            && driver.findElement(By.id("technology" + edit.employeeNumber)).getText().equals("TEST - New Technology")
            );
        } else
            System.out.println("No Allocated Employees - No verifications done!");

        numberOfResults = edit.getNumberOfResults("All", "Booked");
        if (numberOfResults != 0) {
            edit.expandButton.click();
            edit.editButton.click();
            edit.bookedProjectField.type("TEST - New booked project");
            edit.saveButton.click();
            editForm.wait.toFade(5, true);
            edit.verifyTrue(driver.findElement(By.id("bookedProject" + edit.employeeNumber)).getText().equals("TEST - New booked project"));
        } else
            System.out.println("No Booked Employees - No verifications done!");
    }
}