package Tests.Tests_Examples;

import TestPages.TestPages_Examples.GooglePage;
import core.TestVee2.Base;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by bclim on 2/3/2015.
 */
@RunWith(JUnitParamsRunner.class)
public class TestGoogle_PageObj extends Base {
    @Test
    @FileParameters("GoogleEasterEggs.csv")
    public void testGoogleEasterEgg(String EasterEggsText) {
        GooglePage newGooglePage = new GooglePage();
        newGooglePage.CheckOutTheEasterEggs(EasterEggsText, 15);
    }
}
