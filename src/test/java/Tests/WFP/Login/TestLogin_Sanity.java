package Tests.WFP.Login;

import TestPages.WFP.WFP_LoginPage;
import core.TestVee2.Base;
import core.TestVee2.Page;
import org.junit.Test;

public class TestLogin_Sanity extends Base {
    Page GenericPage = new Page();

    @Test
    public void testLoginValidCredentials(){
        WFP_LoginPage home = new WFP_LoginPage();
        home.login("gcornel", "cornel");
        home.verifyTrue(home.isTextPresent("Showing 0 to 0 of 0 entries"));
    }

    @Test
    public void testLoginWrongCredentials(){
        WFP_LoginPage home = new WFP_LoginPage();
        home.login("ggcornel","ccornel");
        home.verifyTrue(home.isTextPresent(home.badCredentialsMessage));
    }

}
