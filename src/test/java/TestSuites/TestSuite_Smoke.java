package TestSuites;

import Tests.Login.TestLogin_Smoke;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestLogin_Smoke.class})

public class TestSuite_Smoke {
    public static TestSuite suite() {
        return new TestSuite("TestSuiteSmoke");
    }
}
