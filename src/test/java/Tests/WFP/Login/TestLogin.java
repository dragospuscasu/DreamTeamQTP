package Tests.WFP.Login;

import TestPages.WFP.WFP_LoginPage;
import core.TestVee2.Base;
import core.TestVee2.Page;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TestLogin extends Base {
    Page GenericPage = new Page();

    @Test
    public void testLoginEmptyFields(){
        WFP_LoginPage home = new WFP_LoginPage();
        home.login("", "");
        home.verifyTrue(home.isTextPresent(home.badCredentialsMessage));
    }

    @Test
    public void testLoginValidUsernameEmptyPassword(){
        WFP_LoginPage home = new WFP_LoginPage();
        home.login("gcornel","");
        home.verifyTrue(home.isTextPresent(home.badCredentialsMessage));
    }

    @Test
    public void testLoginEmptyUsernameValidPassword(){
        WFP_LoginPage home = new WFP_LoginPage();
        home.login("", "cornel");
        home.verifyTrue(home.isTextPresent(home.badCredentialsMessage));
    }

    @Test
    public void testLoginValidCredentialsCaseSensitive(){
        WFP_LoginPage home = new WFP_LoginPage();
        home.login("gcornel", "CORNEL");
        home.verifyTrue(home.isTextPresent(home.badCredentialsMessage));
    }

    @Test
    public void testLoginValidCredentialsSQLInjection(){
        WFP_LoginPage home = new WFP_LoginPage();
        home.login("' OR 1=1 --", "' OR 1=1 --");
        home.verifyTrue(home.isTextPresent(home.badCredentialsMessage));
        home.verifyFalse(home.isTextPresent("Showing 0 to 0 of 0 entries"));
    }

    @Test
    public void testWFP_Role(){
        WFP_LoginPage home = new WFP_LoginPage();
        home.login("gcornel", "cornel");
        WebElement element = driver.findElement(By.id("menu-user-button"));
        String title = element.getAttribute("title");
        home.verifyTrue(title.equals("WFP"));
    }

    @Test
    public void testLead_Role(){
        WFP_LoginPage home = new WFP_LoginPage();
        home.login("anamaria", "anamaria");
        WebElement element = driver.findElement(By.id("menu-user-button"));
        String title = element.getAttribute("title");
        home.verifyTrue(title.equals("LEAD"));
    }


}
