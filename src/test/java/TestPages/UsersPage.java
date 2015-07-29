package TestPages;

import core.TestVee2.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by aracautanu on 7/23/2015.
 */
public class UsersPage extends GenericPage {

    public String deleteSuccessMessage = "User deleted successfully";
    public int userNumber=2;

    public Element userTable = new Element("user-table");
    public Element editUserButton = new Element("edit-user-" + userNumber);
    public Element addUserButton = new Element("add-user");
    public Element deleteUserButton = new Element("delete-user-" + userNumber);

    public Element editUsernameField = new Element("userName");
    public Element editEmailField = new Element("userEmail");
    public Element editFirstnameField = new Element("userFirstName");
    public Element editLastNameField = new Element("userLastName");
    public Element editPasswordField = new Element("userPassword");
    public Element editPassConfirmationField = new Element("userPasswordConfirmation");

    public Element editSave = new Element("save-button");
    public Element editCancel = new Element("close-button");
    public Element deleteConfirm= new Element("//*[@id=\"ngdialog2\"]/div[2]/div[1]/button");

    public UsersPage(String username, String password) {
        super(username, password);
        menu_buttonLeft.wait.toBeVisible(20);
    }

    public UsersPage() {
        open(URL);
        userTable.wait.toBeVisible(20);
    }

    public void goToUsersPage(){
        this.menu_buttonLeft.click();
        this.goToUsersPage_button.wait.toBeVisible(25);
        this.goToUsersPage_button.click();
    }

    public Element getLastAddedUserDeleteButton() {
        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='user-table']//tr"));
        String btnID = "";
        int rowNr = 0;
        for (WebElement row : rows) {
            if(rowNr==0){
                ++rowNr;
                continue;
            }
            WebElement key = row.findElement(By.xpath("//td[9]/button"));
            btnID = key.getAttribute("id");
        }
        Element deleteBtn = new Element(btnID);

        return  deleteBtn;
    }
}
