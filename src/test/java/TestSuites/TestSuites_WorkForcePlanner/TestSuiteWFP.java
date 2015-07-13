package TestSuites.TestSuites_WorkForcePlanner;

import Tests.Tests_Examples.TestSamplePages;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by aracautanu on 4/22/2015.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestSamplePages.class

})
public class TestSuiteWFP {
    public static TestSuite suite() {
        return new TestSuite("TestWFPSearch");
    }
}
