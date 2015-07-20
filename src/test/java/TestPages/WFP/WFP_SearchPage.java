    package TestPages.WFP;

    import core.TestVee2.Element;
    import org.openqa.selenium.By;
    import org.openqa.selenium.Keys;
    import org.openqa.selenium.WebElement;
    import org.openqa.selenium.support.ui.Select;

    import java.text.DateFormat;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;

    public class WFP_SearchPage extends WFP_GenericPage {

        public final Element searchIcon             = new Element("searchIcon");
        public final Element searchButton            = new Element("search_employees_button");
        public final Element jobTitleSelect         = new Element("job_title_select", "Job Title");
        public final Element allocationStatusSelect = new Element("allocation_status_select", "Allocation Status");
        public final Element showNrOfEntries        = new Element("//*[@id=\"searchResults_length\"]/label/select","Show number of entries filter");
        public final Element resultsTable           = new Element("searchResults", "Results Table");

        public       int employeeNumber             = 1;
        public final Element saveButton             = new Element("save_editted_employee");
        public final Element editButton             = new Element("editEmployee" + employeeNumber);
        public final Element expandButton           = new Element("expand" + employeeNumber);
        public final Element allocateButton         = new Element("allocateEmployee" + employeeNumber);
        public final Element bookButton             = new Element("bookEmployee" + employeeNumber);


        public final Element allocationInpAlloc     = new Element("modalAllocationPercent","Allocation input from allocation form");
        public final Element projectInputAlloc      = new Element("modalAllocationCurrentProject","For project input from allocation form");
        public final Element releaseDateAlloc       = new Element("release_date_allocate","Release date from allocation form");
        public final Element saveChangesAlloc       = new Element("save_allocated_employee","Save changes button from allocation form");

        //public final Element jobTitleBar            = new Element("#headingOne > h4","Job title bar");
        public final Element allocationBarSearch    = new Element("#headingTwo > h4 > a","");
        public final Element allocationBarModalLabel= new Element("allocateModalLabel","Allocate Modal Label");
        public final Element allocationStatusLabel  = new Element("labelAllocationStatus","Allocation status label");

        public final Element searchDetailsBox       = new Element("searchDetailsBox","Search details box");


        public WFP_SearchPage(String username, String password) {
            super(username, password);
            resultsTable.wait.toBeVisible(20);
        }

        public WFP_SearchPage() {
            open(url);
            resultsTable.wait.toBeVisible(20);
        }

        public void selectShowEntries(String nr) {
            showNrOfEntries.select(nr);
        }

        public ArrayList<String> getJobTitles(){
            ArrayList<String> jobs = new ArrayList<String>();
            Select select = new Select(driver.findElement(By.id("job_title_select")));
            List<WebElement> jobOpt = select.getOptions();
            for(int i = 0; i < jobOpt.size(); i++) {
                jobs.add(jobOpt.get(i).getText());
            }
            return jobs;
        }

        public ArrayList<String> getAllocationStatus() {
            ArrayList<String> status = new ArrayList<String>();
            Select select = new Select(driver.findElement(By.id("allocation_status_select")));
            List<WebElement> statusOpt = select.getOptions();
            for(int i = 0; i < statusOpt.size(); i++){
                status.add(statusOpt.get(i).getText());
            }
            return status;
        }

        public void clickOnSearchButton(){
            this.searchIcon.click();
            driver.findElement(By.id("search_employees_button")).submit();
        }

        public List<WebElement> getColumnValuesFromResultsTable(int colIndex) {
            List<WebElement> colValues  = driver.findElements(By.cssSelector("#searchResults>tbody>tr>td:nth-child(" + colIndex + ")"));
            return colValues;
        }

        public void deselectAll(String selectId) {
            WebElement select = driver.findElement(By.id(selectId));
            Select selectData = new Select(select);
            List<WebElement> lists = select.findElements(By.tagName("option"));
            for(WebElement element: lists){
                selectData.deselectByValue(element.getText());
            }
        }

        public int getNumberOfResults(String job, String allocationStatus){
            this.searchIcon.click();
            this.jobTitleSelect.select(job);
            this.allocationBarSearch.click();
            this.allocationStatusSelect.select(allocationStatus);
            driver.findElement(By.id("search_employees_button")).submit();
            if(this.isTextPresent("No records were found that match the specified search criteria")) {
                return 0;
            }
            return this.getNumberOfResultsWithoutSearching();
        }

        public int getNumberOfResultsWithoutSearching() {
            if(this.isTextPresent("No records were found that match the specified search criteria")) {
                return 0;
            }
            return this.getColumnValuesFromResultsTable(1).size();
        }

        public String buildDate() {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String s = dateFormat.format(date).toString();
            System.out.println(s);
            String[] items = s.split("-");
            int luna = Integer.parseInt(items[1]);
            int zi = Integer.parseInt(items[2]);
            int an = Integer.parseInt(items[0]);
            if(zi == 30) {
                zi = 1;
                if(luna == 12) {
                    an++;
                } else {
                    luna ++;
                }
            } else {
                zi++;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(an);
            sb.append("-");
            if(luna < 10) {
                sb.append("0" + luna);
            } else {
                sb.append(luna);
            }
            sb.append("-");
            sb.append(zi);
            System.out.println(sb.toString());
            return sb.toString();
        }
        public void pressEnterKey(){
            searchButton.pressKey(Keys.ENTER);
        }
    }
