package TestSuites;


import Tests.Login.TestLogin;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestLogin.class})

public class TestSuite_Regression {
    public static TestSuite suite() {
        return new TestSuite("TestSuiteRegression");
    }
}

