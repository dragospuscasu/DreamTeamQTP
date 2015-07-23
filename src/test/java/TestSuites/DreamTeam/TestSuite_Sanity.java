package TestSuites.DreamTeam;

import Tests.WFP.AdvancedSearch.TestAdvancedSearch_Sanity;
import Tests.WFP.Allocate.TestAllocate_Sanity;
import Tests.WFP.Book.TestBook_Sanity;
import Tests.WFP.Edit.TestEdit_Sanity;
import Tests.WFP.Login.TestLogin_Sanity;
import Tests.WFP.Logout.TestLogout_Sanity;
import Tests.WFP.Search.TestSearch_Sanity;
import Tests.WFP.TestWorkforceRequest;
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



