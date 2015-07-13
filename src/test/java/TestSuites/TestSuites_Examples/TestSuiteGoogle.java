package TestSuites.TestSuites_Examples;

import Tests.Tests_Examples.TestGoogle_NonPageObj;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by bclim on 2/3/2015.
 */


@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestGoogle_NonPageObj.class

})
public class TestSuiteGoogle {
    public static TestSuite suite()
    {
        return new TestSuite("TestGoogle");
    }
}
