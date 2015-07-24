package TestPages;

import core.TestVee2.Element;
import core.TestVee2.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by aracautanu on 7/13/2015.
 */
public class LoginPage extends Page {

    public final String url             = "http://192.168.210.14:8080/dream_cms_app/";
    public final String badCredentialsMessage = "Failed to login.";
    public final String invalidEmailMessage = "Enter a valid email.";

    public final Element loginForm      = new Element("//*[@id=\"light\"]/div/form");
    public final Element loginUsername  = new Element("j_username");
    public final Element loginPassword  = new Element("j_password");
    public final Element loginButton    = new Element("classname=login-button");
    public final Element logoutButton   = new Element("logout-button");
    public final Element menuButton     = new Element("menu-button");


    public LoginPage(){
        open(url);
        loginForm.wait.toBeVisible(5);
    }

    public void login(String user, String pswd) {
        if (!user.equals("")) this.loginUsername.type(user);
        if (!pswd.equals("")) this.loginPassword.type(pswd);
        loginButton.click();
    }
}
