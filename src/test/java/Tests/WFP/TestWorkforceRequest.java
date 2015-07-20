package Tests.WFP;

import TestPages.WFP.WFP_RequestPage;
import core.TestVee2.Base;
import core.TestVee2.Page;
import org.junit.Test;

/**
 * Created by rastan on 5/20/2015.
 */
public class TestWorkforceRequest extends Base {

    @Test
    public void testIfAllComponentsAreVisible() {
        WFP_RequestPage requestPage = new WFP_RequestPage("anamaria","anamaria");
        requestPage.verifyTrue(requestPage.wfrSubjectMail.isVisible());
        requestPage.verifyTrue(requestPage.wfrSubjectMail.isVisible());
        requestPage.verifyTrue(requestPage.wfrBody.isVisible());
        requestPage.verifyTrue(requestPage.wfrSendButton.isVisible());
    }

    @Test
    public void testWorkforceRequestFunctionality() {
        WFP_RequestPage requestPage = new WFP_RequestPage("anamaria","anamaria");
        requestPage.selectEmail("wrkfrqsttst@mailinator.com");
        requestPage.wfrSubjectMail.type("Test Subject mail");
        requestPage.wfrSendButton.click();
        requestPage.verifyTrue(requestPage.wfrNotificationDialog.isVisible());

        Page verifyEmail = new Page("https://mailinator.com/inbox.jsp?to=wrkfrqsttst");
        verifyEmail.isTextPresent("Graduates@endava.com");

    }
}
