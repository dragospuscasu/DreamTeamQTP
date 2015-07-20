package Tests.WFP.Search;

import TestPages.WFP.WFP_SearchPage;
import core.TestVee2.Base;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class TestSearch extends Base{

    @Test
    public void testIfDefaultValuesAreAllAll() {
        WFP_SearchPage search = new WFP_SearchPage("anamaria","anamaria");
        search.searchIcon.click();
        Select selectJobTitle = new Select(driver.findElement(By.id("job_title_select")));
        WebElement optionJobTitle = selectJobTitle.getFirstSelectedOption();
        search.allocationStatusLabel.click();
        Select selectAllocationStatus = new Select(driver.findElement(By.id("allocation_status_select")));
        WebElement optionAllocationStatus = selectAllocationStatus.getFirstSelectedOption();
        search.verifyTrue(optionJobTitle.getText().equals("All") && optionAllocationStatus.getText().equals("All"));
    }


}