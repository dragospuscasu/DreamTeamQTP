package Tests.DreamTeam.Login;

import TestPages.DreamTeam.DT_LoginPage;
import core.TestVee2.Base;
import core.TestVee2.Page;
import org.junit.Test;

/**
 * Created by aracautanu on 7/13/2015.
 */
public class TestLogin extends Base {
    Page GenericPage = new Page();

    @Test
    public void testLoginFieldsAreVisible() throws InterruptedException {
        DT_LoginPage home = new DT_LoginPage();
        home.verifyTrue(home.loginUsername.isVisible());
        home.verifyTrue(home.loginPassword.isVisible());
    }

    @Test
    public void testLoginValidUsername_ValidPassword() throws InterruptedException {
        DT_LoginPage home = new DT_LoginPage();
        home.login("vladu@dream.com", "123123");
        home.verifyFalse(home.isTextPresent(home.badCredentialsMessage));
        home.verifyTrue(home.menuButton.isElementPresent());
    }

    @Test
    public void testLoginValidUsername_UpperCasePassword() throws InterruptedException {
        DT_LoginPage home = new DT_LoginPage();
        home.login("ana@dream.com", "ANA");
        home.verifyFalse(home.isTextPresent(home.badCredentialsMessage));
    }

    @Test
    public void testLoginSQLInjection() throws InterruptedException {
        DT_LoginPage home = new DT_LoginPage();
        home.login("' OR 1=1 --", "' OR 1=1 --");
        home.verifyTrue(home.isTextPresent(home.invalidEmailMessage));
    }

    @Test
    public void testLoginButtonIsVisible() throws InterruptedException {
        DT_LoginPage home = new DT_LoginPage();
        home.verifyTrue(home.loginButton.isElementPresent());
    }

    @Test
    public void testLogoutButton() throws InterruptedException {
        DT_LoginPage home = new DT_LoginPage();
        home.login("vladu@dream.com", "123123");
        home.menuButton.click();
        home.logoutButton.click();
        home.wait.forFixedTime(5);
        home.verifyTrue(home.loginForm.isVisible());
    }



}
