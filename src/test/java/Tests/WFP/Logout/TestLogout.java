package Tests.WFP.Logout;

import TestPages.WFP.WFP_GenericPage;
import core.TestVee2.Base;
import org.junit.Test;

public class TestLogout extends Base {
    @Test
    public void testLogoutFunctionWithBackButton() {
        WFP_GenericPage samplePage = new WFP_GenericPage("gcornel","cornel");
        samplePage.menuUser.click();
        samplePage.logOutButton.click();
        samplePage.goBackInBrowser();
        samplePage.verifyTrue(samplePage.isTextPresent("Please Sign In"));
    }
}
