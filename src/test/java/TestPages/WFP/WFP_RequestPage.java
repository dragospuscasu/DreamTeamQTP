package TestPages.WFP;

import core.TestVee2.Element;

/**
 * Created by rastan on 5/20/2015.
 */
public class WFP_RequestPage extends WFP_GenericPage {

    public final Element emailAddressTest = new Element("wrkfrqsttst@mailinator.com");
    public final Element wfrPanel                           = new Element("workforce_request_panel","Workforce Request panel");
    public final Element wfrRecipientEmails                 = new Element("workforce_request_recipient","WFP Emails");
    public String emailInformation                          = "\tHello,\n" +
            "We have a new request for personnel with the following description:\n" +
            "Client name: <client name>\n" +
            "Project name: <project name>\n" +
            "Project description: <project description: commercial model, methodology, other information relevant to a staffing decision>\n" +
            "Required personnel:\n" +
            "<no> <job title> with strong <skill>, grade: <grade>, available on date: mm/dd/yy\n" +
            "...\n" +
            "Please let me know if there are any resources available that meet the requirements above.\n" +
            "Thank you,\n" +
            "Ana Maria\n";
    public final Element wfrMenuBtn                         = new Element("menu-workforce-request-button","Workforce Request menu button");
    public final Element wfrSubjectMail                     = new Element("workforce_request_subject","Workforce request subject mail");
    public final Element wfrSendButton                      = new Element("request_workforce_button","Send button");
    public final Element wfrNotificationDialog              = new Element("notificationDialog","Notification dialog");
    public final Element wfrBody                            = new Element("workforce_request_body","Body for Workforce Request");

    public WFP_RequestPage (String username, String password) {
        super(username, password);
        wfrMenuBtn.click();
        wfrPanel.wait.toBeVisible(5,true);
    }

    public void selectEmail(String email) {
        wfrRecipientEmails.select(email);
    }
}
