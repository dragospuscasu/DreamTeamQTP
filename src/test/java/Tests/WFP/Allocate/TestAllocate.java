package Tests.WFP.Allocate;

import TestPages.WFP.WFP_SearchPage;
import core.TestVee2.Base;
import core.TestVee2.Element;
import org.junit.Test;

public class TestAllocate extends Base {
    @Test
    public void testIfReleaseDateCanBeModifiedFromEdit() {
        WFP_SearchPage searchPage = new WFP_SearchPage("gcornel","cornel");
        int numberOfResults = searchPage.getNumberOfResults("All","Allocated");
        String releaseDate = searchPage.buildDate();
        if(numberOfResults != 0) {
            searchPage.expandButton.click();
            searchPage.editButton.click();
            new Element("release_date_edit", "Release date field from edit form").type(releaseDate);
            new Element("edit_release_date_div", "Edit release date field").click();
            new Element("save_editted_employee", "Save change button from edit form").click();
            searchPage.searchDetailsBox.wait.toBeVisible(3, true);
            Element relDate = new Element("releaseDate" + searchPage.employeeNumber, "Release date for an employee");
            searchPage.verifyTrue(relDate.getText().toString().equals(releaseDate));
        }else {
            System.out.println("No Allocated Employees - No verifications done!");
            searchPage.verifyTrue(false);
        }
    }
}
