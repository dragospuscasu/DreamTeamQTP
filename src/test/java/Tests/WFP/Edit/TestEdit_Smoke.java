package Tests.WFP.Edit;

import TestPages.WFP.WFP_Edit;
import core.TestVee2.Base;
import org.junit.Test;

public class TestEdit_Smoke extends Base {

    @Test
    public void testEditButtonIsPresent() {
        WFP_Edit edit = new WFP_Edit("gcornel", "cornel");
        edit.expandButton.click();
        edit.verifyTrue(edit.editButton.isElementPresent());
    }

    @Test
    public void testSaveButtonIsPresent() {
        WFP_Edit edit = new WFP_Edit("gcornel", "cornel");
        edit.expandButton.click();
        edit.editButton.click();
        edit.verifyTrue(edit.saveButton.isElementPresent());
    }


}
