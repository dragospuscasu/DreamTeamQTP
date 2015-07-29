package Tests.Users;

import TestPages.GenericPage;
import TestPages.UsersPage;
import org.junit.Test;

/**
 * Created by aracautanu on 7/23/2015.
 */
public class TestUsers extends GenericPage {
    @Test
    public void testAllFieldsAreVisible() {
        UsersPage usersPage = new UsersPage("vladu@dream.com", "123123");
        usersPage.goToUsersPage();
        usersPage.userTable.wait.toBeOnPage(20);
        usersPage.verifyTrue(usersPage.isTextPresent("Username"));
        usersPage.verifyTrue(usersPage.isTextPresent("Email"));
        usersPage.verifyTrue(usersPage.isTextPresent("First Name"));
        usersPage.verifyTrue(usersPage.isTextPresent("Last Name"));
        usersPage.verifyTrue(usersPage.isTextPresent("Groups"));
        usersPage.verifyTrue(usersPage.isTextPresent("Active"));
        usersPage.verifyTrue(usersPage.isTextPresent("Admin"));
    }

    @Test
    public void testAddUserButtonIsVisible() {
        UsersPage usersPage = new UsersPage("vladu@dream.com", "123123");
        usersPage.goToUsersPage();
        usersPage.addUserButton.wait.toBeOnPage(20);
        usersPage.verifyTrue(usersPage.addUserButton.isElementPresent());
    }

    @Test
    public void testEditUserButtonIsVisible() {
        UsersPage usersPage = new UsersPage("vladu@dream.com", "123123");
        usersPage.goToUsersPage();
        usersPage.editUserButton.wait.toBeOnPage(20);
        usersPage.verifyTrue(usersPage.editUserButton.isElementPresent());
    }

    @Test
    public void testDeleteUserButtonIsVisible() {
        UsersPage usersPage = new UsersPage("vladu@dream.com", "123123");
        usersPage.goToUsersPage();
        usersPage.deleteUserButton.wait.toBeOnPage(20);
        usersPage.verifyTrue(usersPage.deleteUserButton.isElementPresent());
    }

    @Test
    public void testUserFieldsAreEditable() {
        UsersPage usersPage = new UsersPage("vladu@dream.com", "123123");
        usersPage.goToUsersPage();
        usersPage.editUserButton.wait.toBeOnPage(20);
        usersPage.editUserButton.click();
        usersPage.editUsernameField.type("TEST");
        usersPage.editEmailField.type("TEST");
        usersPage.editFirstnameField.type("TEST");
        usersPage.editLastNameField.type("TEST");
        usersPage.editPasswordField.type("TEST");
        usersPage.editPassConfirmationField.type("TEST");
        usersPage.verifyTrue(usersPage.editUsernameField.getValue().equals("TEST"));
        usersPage.verifyTrue(usersPage.editEmailField.getValue().equals("TEST"));
        usersPage.verifyTrue(usersPage.editFirstnameField.getValue().equals("TEST"));
        usersPage.verifyTrue(usersPage.editLastNameField.getValue().equals("TEST"));

    }

    @Test
    public void testEditSaveChanges() {
        UsersPage usersPage = new UsersPage("vladu@dream.com", "123123");
        usersPage.goToUsersPage();
        usersPage.userTable.wait.toBeOnPage(5);
        usersPage.editUserButton.wait.toBeOnPage(30);
        usersPage.editUserButton.click();
        usersPage.editUsernameField.wait.toBeOnPage(20);
        usersPage.editUsernameField.type("AnaTEST");
        usersPage.editEmailField.type("anatest@dream.com");
        usersPage.editFirstnameField.type("AnaTest");
        usersPage.editLastNameField.type("EndavaTest");
        usersPage.editPasswordField.type("anatest1");
        usersPage.editPassConfirmationField.type("anatest1");
        usersPage.editSave.click();
        usersPage.editSave.wait.toFade(10);
        usersPage.editUserButton.click();
        usersPage.editUsernameField.wait.toBeOnPage(20);
        usersPage.verifyTrue(usersPage.editUsernameField.getValue().equals("AnaTEST"));
        usersPage.verifyTrue(usersPage.editEmailField.getValue().equals("anatest@dream.com"));
        usersPage.verifyTrue(usersPage.editFirstnameField.getValue().equals("AnaTest"));
        usersPage.verifyTrue(usersPage.editLastNameField.getValue().equals("EndavaTest"));
        usersPage.editCancel.click();
    }

    @Test
    public void testAddAndDeleteButton(){
        UsersPage usersPage = new UsersPage("vladu@dream.com", "123123");
        usersPage.goToUsersPage();
        usersPage.addUserButton.wait.toBeOnPage(60);
        usersPage.addUserButton.click();
        usersPage.editUsernameField.wait.toBeOnPage(20);
        usersPage.editUsernameField.type("Delete");
        usersPage.editEmailField.type("new@delete.com");
        usersPage.editFirstnameField.type("Delete");
        usersPage.editLastNameField.type("Delete");
        usersPage.editPasswordField.type("1111");
        usersPage.editPassConfirmationField.type("1111");
        usersPage.editSave.click();
        usersPage.editSave.wait.toFade(10);
        usersPage.getLastAddedUserDeleteButton().click();
        usersPage.deleteConfirm.wait.toBeOnPage(100);
        usersPage.deleteConfirm.click();
        usersPage.deleteConfirm.wait.toFade(10);
        usersPage.verifyFalse(usersPage.isTextPresent("new@delete.com"));
    }
}
