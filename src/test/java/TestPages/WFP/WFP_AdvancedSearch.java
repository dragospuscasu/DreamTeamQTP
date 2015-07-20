package TestPages.WFP;

import core.TestVee2.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aracautanu on 5/19/2015.
 */
public class WFP_AdvancedSearch extends WFP_SearchPage {

    public final Element showAdvancedOptBtn = new Element("labelShowDetails");

    public final Element firstNameLabel = new Element("labelFirstName");
    public final Element firstNameInput = new Element("firstName");
    public final Element lastNameLabel = new Element("labelLastName");
    public final Element lastNameInput = new Element("lastName");
    public final Element allocationPercentLabel = new Element("labelAllocationPercent");
    public final Element allocationPercentInput = new Element("allocationPercent");
    public final Element currentProjectLabel = new Element("labelCurrentProject");
    public final Element currentProjectList = new Element("current_project_select");
    public final Element bookedProjectLabel = new Element("labelBookedProject");
    public final Element bookedProjectList = new Element("booked_project_select");
    public final Element releaseDateLabel = new Element("labelReleaseDate");
    public final Element releaseDateInput = new Element("release_date_advanced");
    public final Element gradeListLabel = new Element("labelGrade");
    public final Element gradeList = new Element("grade_select");
    public final Element businessUnitLabel = new Element("labelBusinessUnit");
    public final Element businessUnitList = new Element("business_unit_select");
    public final Element technologyLabel = new Element("labelTechnology");
    public final Element technologyList = new Element("technology_select");
    public final Element coreSkillLabel = new Element("labelCoreSkill");
    public final Element coreSkillList = new Element("coreSkill_select");

    public WFP_AdvancedSearch(String username, String password) {
        super(username, password);
        resultsTable.wait.toBeVisible(20);
    }

    public WFP_AdvancedSearch() {
        open(url);
        resultsTable.wait.toBeVisible(20);
    }

    public ArrayList getGradeOptions() {
        ArrayList<String> grades = new ArrayList<String>();
        Select select = new Select(driver.findElement(By.id("grade_select")));
        List<WebElement> gradesOpt = select.getOptions();
        for (int i = 0; i < gradesOpt.size(); i++) {
            grades.add(gradesOpt.get(i).getText());
        }
        return grades;
    }

    public ArrayList getOptionsList(String id) {
        ArrayList<String> options = new ArrayList<String>();
        Select select = new Select(driver.findElement(By.id(id)));
        List<WebElement> optionsFromList = select.getOptions();
        for (int i = 0; i < optionsFromList.size(); i++) {
            options.add(optionsFromList.get(i).getText());
        }
        return options;
    }
}
