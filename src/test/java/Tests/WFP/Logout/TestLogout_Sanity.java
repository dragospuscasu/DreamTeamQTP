package Tests.WFP.Logout;

import TestPages.WFP.WFP_GenericPage;
import core.TestVee2.Base;
import org.junit.Test;

public class TestLogout_Sanity extends Base {
    @Test
    public void testLogout() {
        WFP_GenericPage samplePage = new WFP_GenericPage("gcornel","cornel");
        samplePage.menuUser.click();
        samplePage.logOutButton.click();
        samplePage.verifyTrue(samplePage.isTextPresent("Please Sign In"));
    }

    @Test
    public void testIfUserCannotAccesASpecificPageWithoutAuthentication() {
        WFP_GenericPage samplePage = new WFP_GenericPage();
        samplePage.open("http://localhost:8080/WorkForcePlanning/search/searchPage");
        samplePage.verifyTrue(samplePage.isTextPresent("Please Sign In"));
    }

}
