package Tests.WFP.Edit;

import TestPages.WFP.WFP_Edit;
import core.TestVee2.Base;
import org.junit.Test;

public class TestEdit extends Base {

    @Test
    public void testAllocationPercentBounderies_lowerBound() {
        WFP_Edit edit = new WFP_Edit("gcornel", "cornel");
        int numberOfResults = edit.getNumberOfResults("All", "Allocated");
        String errorMessage = "Please enter a number between 1 and 100";
        if (numberOfResults != 0) {
            edit.expandButton.click();
            edit.editButton.click();
            edit.allocationPercentField.type("-1");
            edit.saveButton.click();
            edit.verifyTrue(edit.isTextPresent(errorMessage));
        } else
            System.out.println("No Allocated Employees - No verifications done!");
    }

    @Test
    public void testAllocationPercentBounderies_upperBound() {
        WFP_Edit edit = new WFP_Edit("gcornel", "cornel");
        int numberOfResults = edit.getNumberOfResults("All", "Allocated");
        String errorMessage = "Please enter a number between 1 and 100";
        if (numberOfResults != 0) {
            edit.expandButton.click();
            edit.editButton.click();
            edit.allocationPercentField.type("101");
            edit.saveButton.click();
            edit.verifyTrue(edit.isTextPresent(errorMessage));
        } else
            System.out.println("No Allocated Employees - No verifications done!");
    }

    @Test
    public void testAllocationPercentBounderies_betweenBounderies() {
        WFP_Edit edit = new WFP_Edit("gcornel", "cornel");
        int numberOfResults = edit.getNumberOfResults("All", "Allocated");
        String errorMessage = "Please enter a number between 1 and 100";

        if (numberOfResults != 0) {
            edit.expandButton.click();
            edit.editButton.click();
            edit.allocationPercentField.type("100");
            edit.saveButton.click();
            edit.verifyFalse(edit.isTextPresent(errorMessage));
        } else
            System.out.println("No Allocated Employees - No verifications done!");
    }
}


