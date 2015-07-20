package TestSuites.WFP;

import Tests.WFP.TestGeneric;
import Tests.WFP.Login.TestLogin;
import Tests.WFP.Logout.TestLogout;
import Tests.WFP.Search.TestSearch;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestGeneric.class,
        TestLogin.class,
        TestLogout.class,
        TestSearch.class})

public class TestSuite_Regression {
    public static TestSuite suite() {
        return new TestSuite("TestSuiteRegression");
    }
}

