package TestSuites;

import Tests.Login.TestLogin_Sanity;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestLogin_Sanity.class})

public class TestSuite_Sanity {
    public static TestSuite suite() {
        return new TestSuite("TestSuiteSanity");
    }
}



