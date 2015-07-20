package TestPages.WFP;

import core.TestVee2.Element;

public class WFP_Book extends WFP_SearchPage{

    private static String url   = "http://192.168.196.13:8888/grads-team3/search/searchPage";
    private String localURL     = "http://localhost:8080/WorkForcePlanning/search/searchPage";

    public int employeeNumber   = 1;
    public Element bookButton   = new Element("bookEmployee" + employeeNumber);
    public Element expandButton = new Element("expand" + employeeNumber);

    public WFP_Book(String user, String pswd) {
        super(user, pswd);
        bookButton.wait.toBeVisible(20);
    }

    public WFP_Book() {
        open(localURL);
        bookButton.wait.toBeVisible(20);
    }





}
