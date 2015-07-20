package Tests.WFP.Login;

import TestPages.WFP.WFP_LoginPage;
import core.TestVee2.Base;
import org.junit.Test;

public class TestLogin_Smoke extends Base {

    @Test
    public void testLoginFormAndComponentsAreVisible(){
        WFP_LoginPage home = new WFP_LoginPage();
        home.verifyTrue(home.logo.isVisible());
        home.verifyTrue(home.loginForm.isElementPresent());
        home.verifyTrue(home.loginUsername.isElementPresent());
        home.verifyTrue(home.loginPassword.isElementPresent());
        home.verifyTrue(home.loginButton.isElementPresent());
    }
}