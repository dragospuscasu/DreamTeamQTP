package Tests.WFP.AdvancedSearch;

import TestPages.WFP.WFP_AdvancedSearch;
import core.TestVee2.Base;
import core.TestVee2.Element;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.util.*;

/**
 * Created by aracautanu on 5/22/2015.
 */
public class TestAdvancedSearch_Smoke extends Base {
    @Test
    public void testIfFiltersAppearForAdvanceSearch() {
        WFP_AdvancedSearch advancedSearch = new WFP_AdvancedSearch("gcornel","cornel");
        advancedSearch.searchIcon.click();
        advancedSearch.showAdvancedOptBtn.click();
        advancedSearch.verifyTrue(advancedSearch.isTextPresent("First Name:"));
        advancedSearch.verifyTrue(advancedSearch.isTextPresent("Last Name:"));
        advancedSearch.verifyTrue(advancedSearch.isTextPresent("Allocation percent:"));
        advancedSearch.verifyTrue(advancedSearch.isTextPresent("Current project:"));
        advancedSearch.verifyTrue(advancedSearch.isTextPresent("Booked project:"));
        advancedSearch.verifyTrue(advancedSearch.isTextPresent("Release date:"));
        advancedSearch.verifyTrue(advancedSearch.isTextPresent("Grade:"));
        advancedSearch.verifyTrue(advancedSearch.isTextPresent("Business Unit:"));
        advancedSearch.verifyTrue(advancedSearch.isTextPresent("Technology:"));
        advancedSearch.verifyTrue(advancedSearch.isTextPresent("Core skills:"));
    }

    @Test
    public void testIfGradeFiltersContainsAllOptionFromAcceptanceCriteria() {
        WFP_AdvancedSearch advancedSearch = new WFP_AdvancedSearch("gcornel","cornel");
        advancedSearch.searchIcon.click();
        advancedSearch.showAdvancedOptBtn.click();
        advancedSearch.gradeListLabel.click();
        ArrayList<String> gradesFromList = advancedSearch.getGradeOptions();
        ArrayList<String> gradesRequired = new ArrayList<String>();
        gradesRequired.add("All");
        gradesRequired.add("Junior Technician");
        gradesRequired.add("Technician");
        gradesRequired.add("Senior Technician");
        gradesRequired.add("Engineer");
        gradesRequired.add("Senior Engineer");
        gradesRequired.add("Consultant");
        gradesRequired.add("Senior Consultant");
        gradesRequired.add("Manager");
        for(int i = 0; i < gradesFromList.size(); i++) {
            String grade = gradesFromList.get(i);
            advancedSearch.verifyTrue(gradesRequired.contains(grade));
        }
    }

    @Test
    public void testIfBusinessListContainsAllOptionFromAcceptanceCriteria() {
        WFP_AdvancedSearch advancedSearch = new WFP_AdvancedSearch("gcornel","cornel");
        advancedSearch.searchIcon.click();
        advancedSearch.showAdvancedOptBtn.click();
        advancedSearch.businessUnitLabel.click();
        ArrayList<String> businessOptionsFromList = advancedSearch.getOptionsList("business_unit_select");
        ArrayList<String> businessOptionsRequired = new ArrayList<String>();
        businessOptionsRequired.add("All");
        businessOptionsRequired.add("ISD");
        businessOptionsRequired.add("Bucuresti");
        businessOptionsRequired.add("Cluj");
        for(int i = 0; i < businessOptionsFromList.size(); i++) {
            String business = businessOptionsFromList.get(i);
            advancedSearch.verifyTrue(businessOptionsRequired.contains(business));
        }
    }

    @Test
    public void testIfBookedProjectListContainsAllOptionFromAcceptanceCriteria() {
        WFP_AdvancedSearch advancedSearch = new WFP_AdvancedSearch("gcornel","cornel");
        Set<String> uniqueProj = new HashSet<String>();

        advancedSearch.selectShowEntries("100");

        List<WebElement> columnList = advancedSearch.getColumnValuesFromResultsTable(6);
        for(int i = 0; i < columnList.size(); i++) {
            uniqueProj.add(columnList.get(i).getText());
        }

        advancedSearch.searchIcon.click();
        advancedSearch.showAdvancedOptBtn.click();
        advancedSearch.bookedProjectLabel.click();

        ArrayList<String> currentProjectFromList = advancedSearch.getOptionsList("booked_project_select");

        for(int i = 0; i < currentProjectFromList.size(); i++) {
            String proj = currentProjectFromList.get(i);
            if(proj.equals("All") || proj.equals("None")) {
                continue;
            }
            advancedSearch.verifyTrue(uniqueProj.contains(proj));
        }

    }

    @Test
    public void testIfTechnologiesListContainsAllOptionFromAcceptanceCriteria() {
        WFP_AdvancedSearch advancedSearch = new WFP_AdvancedSearch("gcornel","cornel");
        Set<String> uniqueTech = new HashSet<String>();

        advancedSearch.selectShowEntries("100");

        int len = advancedSearch.getColumnValuesFromResultsTable(1).size();
        for(int i = 0; i < len; i++) {
            new Element("expand" + (i+1)).click();
            String tech = new Element("technology" + (i+1)).getText();
            uniqueTech.add(tech);
            advancedSearch.scroll_down(300);
        }

        advancedSearch.searchIcon.click();
        advancedSearch.showAdvancedOptBtn.click();
        advancedSearch.technologyLabel.click();

        advancedSearch.wait.forText("All",5);
        ArrayList<String> technologyFromList = advancedSearch.getOptionsList("technology_select");
        for(Iterator<String> it = uniqueTech.iterator(); it.hasNext();){
            String tech = it.next();
            if(tech.equals("All")) continue;
            advancedSearch.verifyTrue(technologyFromList.contains(tech));
        }
    }

    @Test
    public void testItCoreSkillsContainsAllOptionFromAcceptanceCriteria() {
        WFP_AdvancedSearch advancedSearch = new WFP_AdvancedSearch("gcornel","cornel");
        Set<String> uniqueTech = new HashSet<String>();
        advancedSearch.selectShowEntries("100");
        int len = advancedSearch.getColumnValuesFromResultsTable(1).size();
        for(int i = 0; i < len; i++) {
            new Element("expand" + (i+1)).click();
            String tech = new Element("coreSkill" + (i+1)).getText();
            uniqueTech.add(tech);
            advancedSearch.scroll_down(300);
        }

        advancedSearch.searchIcon.click();
        advancedSearch.showAdvancedOptBtn.click();
        advancedSearch.coreSkillLabel.click();

        advancedSearch.wait.forText("All",5);
        ArrayList<String> currentProjectFromList = advancedSearch.getOptionsList("coreSkill_select");
        Iterator it = uniqueTech.iterator();
        while(it.hasNext()) {
            String proj = it.next().toString();
            if(proj.equals("All") || proj.equals(" ")) continue;
            advancedSearch.verifyTrue(currentProjectFromList.contains(proj));
        }
    }

    @Test
    public void testIfCurrentProjectListContainsAllOptionFromAcceptanceCriteria() {
        WFP_AdvancedSearch advancedSearch = new WFP_AdvancedSearch("gcornel","cornel");
        Set<String> uniqueProj = new HashSet<String>();

        advancedSearch.selectShowEntries("100");

        List<WebElement> columnList = advancedSearch.getColumnValuesFromResultsTable(5);
        for(int i = 0; i < columnList.size(); i++) {
            uniqueProj.add(columnList.get(i).getText());
        }

        advancedSearch.searchIcon.click();
        advancedSearch.showAdvancedOptBtn.click();
        advancedSearch.currentProjectLabel.click();


        advancedSearch.wait.forText("All",5);
        ArrayList<String> currentProjectFromList = advancedSearch.getOptionsList("current_project_select");

        for(int i = 0; i < currentProjectFromList.size(); i++) {
            String proj = currentProjectFromList.get(i);
            if(proj.equals("All") || proj.equals("None")) {
                continue;
            }
            advancedSearch.verifyTrue(uniqueProj.contains(proj));
        }
    }
}
