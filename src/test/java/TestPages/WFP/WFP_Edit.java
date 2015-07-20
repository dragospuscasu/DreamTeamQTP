package TestPages.WFP;

import core.TestVee2.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aracautanu on 5/5/2015.
 */
public class WFP_Edit extends WFP_SearchPage {

    private final static String url             = "http://192.168.196.13:8888/grads-team3/search/searchPage";
    private final String localURL               = "http://localhost:8080/WorkForcePlanning/search/searchPage";

    public       int employeeNumber             = 1;
    public final Element saveButton             = new Element("save_editted_employee");
    public final Element editButton             = new Element("editEmployee" + employeeNumber);
    public final Element expandButton           = new Element("expand" + employeeNumber);
    public final Element allocateButton         = new Element("allocateEmployee" + employeeNumber);
    public final Element allocationPercentField = new Element("allocation_percent_edit");
    public final Element currentProjectField    = new Element("current_project_input_edit");
    public final Element bookedProjectField     = new Element("booked_project_input_edit");
    public final Element releaseDateField       = new Element("release_date_edit");
    public final Element technologyField        = new Element("technology_input_edit");


    public WFP_Edit(String user, String pswd) {
        super(user, pswd);
    }

    public WFP_Edit() {
        open(localURL);
        expandButton.wait.toBeVisible(5);
    }

    public List<String> getAllDatalistOptions(String id){
        WebElement datalist = driver.findElement(By.id(id));
        List<WebElement> list = datalist.findElements(By.tagName("option"));
        List<String> result = new ArrayList<>();
        for(int i = 0; i<list.size(); i++){
            result.add(list.get(i).getAttribute("value"));
        }
        return result;
    }
}
