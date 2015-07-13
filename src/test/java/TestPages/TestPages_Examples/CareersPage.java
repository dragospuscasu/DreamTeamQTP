package TestPages.TestPages_Examples;

import core.TestVee2.Element;
import core.TestVee2.Page;


public class CareersPage extends Page {

    public final Element SearchBy_Location  = new Element("maincontent_0_ctl02_rptWidgets_ctl00_0_ddlLocation_0", "SearchBy_Location");
    public final Element SearchBy_JobField  = new Element("maincontent_0_ctl02_rptWidgets_ctl00_0_ddlDiscipline_0", "SearchBy_JobField");
    public final Element SearchBy_Role      = new Element("maincontent_0_ctl02_rptWidgets_ctl00_0_ddlSubDiscipline_0", "SearchBy_Role");
    public final Element FindJob_Button     = new Element("maincontent_0_ctl02_rptWidgets_ctl00_0_lnkSearchJob_0", "FindJob_Button");

    public CareersPage() {
        wait.forText("Endava offers you a lot of opportunities to grow in your career", 30, true);
    }

    public JobSearchPage selectOptionsForSearchJobAndClickOnFindJob_Flow() {

        scroll_down();
        selectLocation(Endava_Constants.DEFAULT_LOCATION);
        selectJobField(Endava_Constants.DEFAULT_JOB_FIELD);
        selectRole(Endava_Constants.DEFAULT_ROLE);
        clickOnFindJob();
        return new JobSearchPage();
    }

    /**
     * ********************************
     * defining functions  
	 ********************************
     */
    public void selectLocation(String location) {
        SearchBy_Location.select(location);
    }

    public void selectJobField(String field) {
        SearchBy_JobField.select(field);
    }

    public void selectRole(String role) {
        SearchBy_Role.select(role);
    }

    public void clickOnFindJob() {
        FindJob_Button.click(Boolean.TRUE);
    }

    // we advise to avoid executing javascript as it is used in the following example
    public void scroll_down() {
        javascript("window.scrollBy(0,250)", true);
    }
}
