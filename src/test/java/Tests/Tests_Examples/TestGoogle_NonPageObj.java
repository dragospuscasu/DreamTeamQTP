package Tests.Tests_Examples;

import core.TestVee2.Base;
import core.TestVee2.Element;
import core.TestVee2.Page;
import org.junit.Test;

/**
 * Created by bclim on 2/3/2015.
 */
public class TestGoogle_NonPageObj extends Base {
    public Page GenericPage = new Page();
    @Test
    public void testGoogleAskew() {

        Element SearchField = new Element("lst-ib", "Google Search Element");
        Element SearchBtn = new Element("sblsbb", "Google Seach Button");
        GenericPage.open("http://www.google.ro");
        SearchField.type("askew");
        SearchBtn.click();
        SearchBtn.wait.toBeVisible(10);
        GenericPage.wait.forFixedTime(10);
    }

    @Test
    public void testGoogleRoll() {
        Element SearchField = new Element("lst-ib", "Google Search Element");
        Element SearchBtn = new Element("sblsbb", "Google Seach Button");
        GenericPage.open("http://www.google.ro");
        SearchField.type("Do a barrel roll");
        SearchBtn.click();
        GenericPage.wait.forFixedTime(10);
    }

    @Test
    public void testZergRush() {
        Element SearchField = new Element("lst-ib", "Google Search Element");
        Element SearchBtn = new Element("sblsbb", "Google Seach Button");
        GenericPage.open("http://www.google.ro");
        SearchField.type("zerg rush");
        SearchBtn.click();
        GenericPage.wait.forFixedTime(30);
       // GenericPage x = PageFactory.initElements();
    }

    @Test(timeout = 20)
    public void testGoogleGameOfLife() {
        Element SearchField = new Element("lst-ib", "Google Search Element");
        Element SearchBtn = new Element("sblsbb", "Google Seach Button");
        GenericPage.open("http://www.google.ro");
        SearchField.type("Conway's Game of Life");
        SearchBtn.click();
        GenericPage.wait.forFixedTime(30);
    }
    @Test
    public void testEcad(){
        Page ecadPage = new Page("https://www.e-cad.ro/login");
        ecadPage.wait.forText("Autentificare", 20, true);
        ecadPage.verifyTrue(ecadPage.isTextPresent("Autentificare"));
    }
}
