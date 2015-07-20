package Tests.WFP;

import TestPages.WFP.WFP_GenericPage;
import core.TestVee2.Base;
import core.TestVee2.Page;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import org.junit.Test;

import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class TestGeneric extends Base {
    public Page GenericPage = new Page();

    @Test
    @FileParameters("URLs.csv")
    public void testLayout(String url, String user, String pswd) {
        WFP_GenericPage samplePage = new WFP_GenericPage(user, pswd);
        samplePage.open(url);
        samplePage.verifyTrue(samplePage.main_menu.isVisible());
        samplePage.verifyTrue(samplePage.logo.isVisible());
        samplePage.verifyTrue(samplePage.searchButtonMainMenu.isVisible());
        samplePage.verifyTrue(samplePage.menuUser.isVisible());
        samplePage.menuUser.click();
        samplePage.verifyTrue(samplePage.logOutButton.isVisible());
    }


}