package TestSuites.TestSuites_Examples;

import Tests.Tests_Examples.TestSamplePages;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by bclim on 2/3/2015.
 */


@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestSamplePages.class

})
public class TestSuiteEndavaSite {
    public static TestSuite suite()
    {
        return new TestSuite("TestEndavaSite");
    }
}
