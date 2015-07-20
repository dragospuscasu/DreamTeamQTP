package TestSuites.WFP;

import Tests.WFP.AdvancedSearch.TestAdvancedSearch_Smoke;
import Tests.WFP.Allocate.TestAllocate_Smoke;
import Tests.WFP.Edit.TestEdit_Smoke;
import Tests.WFP.Login.TestLogin_Smoke;
import Tests.WFP.Search.TestSearch_Smoke;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestLogin_Smoke.class,
        TestSearch_Smoke.class,
        TestEdit_Smoke.class,
        TestAllocate_Smoke.class,
        TestAdvancedSearch_Smoke.class})

public class TestSuite_Smoke {
    public static TestSuite suite() {
        return new TestSuite("TestSuiteSmoke");
    }
}
