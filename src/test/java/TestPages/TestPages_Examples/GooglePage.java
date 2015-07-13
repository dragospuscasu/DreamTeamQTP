package TestPages.TestPages_Examples;

import core.TestVee2.Element;
import core.TestVee2.Page;

/**
 * Created by bclim on 2/3/2015.
 */
public class GooglePage extends Page {
    private Element GoogleLogo                  = new Element("hplogo", "Google Logo");


    public final Element SearchField            = new Element("lst-ib", "Google Search Element");
    public final Element SearchBtn              = new Element("sblsbb", "Google Seach Button");
    public final Element FeelingLuckyBtn        = new Element("gbqfbb", "Google Feel Lucky Btn");


    public GooglePage(){
        open("http://www.google.ro");
        GoogleLogo.wait.toBeVisible(10, true);
    }
    public void CheckOutTheEasterEggs(String EasterEggText, int waitingTime){
        SearchField.type(EasterEggText);
        SearchBtn.click();
        wait.forFixedTime(waitingTime); // for the easter egg show you should have a static wait here
    }

}
