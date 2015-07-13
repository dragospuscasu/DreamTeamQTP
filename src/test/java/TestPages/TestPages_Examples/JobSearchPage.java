package TestPages.TestPages_Examples;

import core.TestVee2.Element;
import core.TestVee2.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

public class JobSearchPage extends Page {

    public String TextFromTitleSearchResult;

    public final Element EndavaLogo = new Element("//div[@class='wrapper clearfix']/a/img", "EndavaLogo");
    public final Element FirstJob_Title = new Element("requisitionListInterface.reqTitleLinkAction.row1", "FirstJob_Title");
    public final Element SecondJob_Title = new Element("requisitionListInterface.reqTitleLinkAction.row2", "SecondJob_Title");
    public final Element ThirdJob_Title = new Element("requisitionListInterface.reqTitleLinkAction.row3", "ThirdJob_Title");
    public final Element FourthJob_Title = new Element("requisitionListInterface.reqTitleLinkAction.row4", "FourthJob_Title");
    public final Element FifthJob_Title = new Element("requisitionListInterface.reqTitleLinkAction.row5", "FifthJob_Title");
    public final Element SixthJob_Title = new Element("requisitionListInterface.reqTitleLinkAction.row6", "SixthJob_Title");
    public final Element SeventhJob_Title = new Element("requisitionListInterface.reqTitleLinkAction.row7", "SeventhJob_Title");
    public final Element EightJob_Title = new Element("requisitionListInterface.reqTitleLinkAction.row8", "EightJob_Title");
    public final Element NinthJob_Title = new Element("requisitionListInterface.reqTitleLinkAction.row9", "NinthJob_Title");
    public final Element TenthJob_Title = new Element("requisitionListInterface.reqTitleLinkAction.row10", "TenthJob_Title");
    public String SearchPageResult = "requisitionListInterface.ID3268";

    public JobSearchPage() {
        wait.forText("Basic Job Search", 30, true);
    }

    public JobDescription clickOnAnOfferAndOpen() {
        scroll_down();
        TextFromTitleSearchResult = getIntFromString(SearchPageResult);
        System.out.println(TextFromTitleSearchResult);
        clicOnAJobOffer(3).click(Boolean.TRUE);
        return new JobDescription();
    }

    /**
     * ********************************
     * defining functions  
	 ********************************
     */
    public EndavaHomePage clickOnEndavaLogo() {
        EndavaLogo.click(Boolean.TRUE);
        return new EndavaHomePage();
    }

    public void scroll_down() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,900)");
    }

    public String getIntFromString(String input) {
        input = driver.findElement(By.id(SearchPageResult)).getText();
        StringBuilder sb = new StringBuilder(input.length());

        for (int i = 0; i < input.length(); i++) {
            final char c = input.charAt(i);
            if (c > 47 && c < 58) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public Element clicOnAJobOffer(int number) {
        return new Element("requisitionListInterface.reqTitleLinkAction.row" + number);
    }

	//not used anymore
//	public HomePage searchPageClickOnJobAndGoToHome(){
//		scroll_down();
//		TextFromTitleSearchResult = getIntFromString(SearchPageResult);
//		System.out.println(TextFromTitleSearchResult);
//		clickOnAJobOffer("2");
//		sleep(3000);
//		EndavaLogo.click();
//		return new HomePage();
//	}
//	
//	public void clickOnAJobOffer(String option){
//		
//		switch (option) {
//			case "1":
//				FirstJob_Title.click(Boolean.TRUE);
//				waitUntilPageHasThisText("Job Posting", 30, Boolean.TRUE);
//				break;
//			case "2":
//				SecondJob_Title.click(Boolean.TRUE);
//				waitUntilPageHasThisText("Job Posting", 30, Boolean.TRUE);
//				break;
//			case "3":
//				ThirdJob_Title.click(Boolean.TRUE);
//				waitUntilPageHasThisText("Job Posting", 30, Boolean.TRUE);
//				break;
//			case "4":
//				FourthJob_Title.click(Boolean.TRUE);
//				waitUntilPageHasThisText("Job Posting", 30, Boolean.TRUE);
//				break;			
//			case "5":
//				FifthJob_Title.click(Boolean.TRUE);
//				waitUntilPageHasThisText("Job Posting", 30, Boolean.TRUE);
//				break;
//			case "6":
//				SixthJob_Title.click(Boolean.TRUE);
//				waitUntilPageHasThisText("Job Posting", 30, Boolean.TRUE);
//				break;
//			case "7":
//				SeventhJob_Title.click(Boolean.TRUE);
//				waitUntilPageHasThisText("Job Posting", 30, Boolean.TRUE);
//				break;
//			case "8":
//				EightJob_Title.click(Boolean.TRUE);
//				waitUntilPageHasThisText("Job Posting", 30, Boolean.TRUE);
//				break;
//			case "9":
//				NinthJob_Title.click(Boolean.TRUE);
//				waitUntilPageHasThisText("Job Posting", 30, Boolean.TRUE);
//				break;
//			case "10":
//				TenthJob_Title.click(Boolean.TRUE);
//				waitUntilPageHasThisText("Job Posting", 30, Boolean.TRUE);
//				break;
//			default:
//				break;
//		}
//	}
}
