package TestSuites.DreamTeam;

import Tests.WFP.Login.TestLogin;
import Tests.WFP.Logout.TestLogout;
import Tests.WFP.Search.TestSearch;
import Tests.WFP.TestGeneric;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestGeneric.class,
        TestLogin.class})

public class TestSuite_Regression {
    public static TestSuite suite() {
        return new TestSuite("TestSuiteRegression");
    }
}

