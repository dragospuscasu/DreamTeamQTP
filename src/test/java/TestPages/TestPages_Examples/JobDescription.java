package TestPages.TestPages_Examples;

import core.TestVee2.Element;
import core.TestVee2.Page;
import org.openqa.selenium.By;

public class JobDescription extends Page {

    public final Element EndavaLogo = new Element("//div[@class='wrapper clearfix']/a/img", "EndavaLogo");
    String PrimaryLocation = driver.findElement(By.id("requisitionDescriptionInterface.ID1856.row1")).getText();

    public JobDescription() {
        wait.forText("Job Description", 30, true);
    }

    public JobDescription(By number) {
        wait.forText("Job Description", 30, true);
    }

    public EndavaHomePage clickOnEndavaLogo() {
        EndavaLogo.click();
        return new EndavaHomePage();
    }

    public EndavaHomePage JobdescriptionFlowAndGoToHomePage() {
        String message = "Romania-Bucharest-Bucharest";
        logger.debug("Search for location: " + message);
        verifyTrue(true, message);
        EndavaLogo.click();
        return new EndavaHomePage();
    }

    /**
     * ********************************
     * defining functions  
	 ********************************
     */
}
