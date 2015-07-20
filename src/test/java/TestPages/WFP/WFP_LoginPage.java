package TestPages.WFP;

import core.TestVee2.Element;
import core.TestVee2.Page;

public class WFP_LoginPage extends Page {
  
    public final String url             = "http://192.168.210.14:8080/dream_cms_app/";
    private final String localURL       = "http://localhost:8080/WorkForcePlanning/loginPage";
    public final String badCredentialsMessage = "Bad credentials! Please fill in the correct username and password!";
    public final Element logo           = new Element("logoHyperlink");
    public final Element loginForm      = new Element("loginform");
    public final Element loginUsername  = new Element("j_username");
    public final Element loginPassword  = new Element("j_password");
    public final Element loginButton    = new Element("btn-login");

    public WFP_LoginPage() {
        open(localURL);
        loginForm.wait.toBeVisible(20);
    }

    public void login(String user, String pswd) {
        if (!user.equals("")) this.loginUsername.type(user);
        if (!pswd.equals("")) this.loginPassword.type(pswd);
        loginButton.click();
    }

}

