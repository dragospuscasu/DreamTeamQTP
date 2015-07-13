
package core.TestVee2;

import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Property of Endava
 * @author dambroze + others
 */
public class ParentElement extends Element {

    public ParentElement(String locator) {
        super(locator);
    }

    public List<WebElement> getChildElements(String locator) {
        Page.logger.debug("Search for childs with locator: " + locator);
        List<WebElement> result;
        try {
            result = getWebElement().findElements(findLocator(locator));
        } catch (StaleElementReferenceException e) {

            waitForStaledElements(locator, 10);
            result = getWebElement().findElements(findLocator(locator));
        }

        return result;
    }

    public Element getFirstChild(String locator, boolean throwException) {
        Element result;
        List<WebElement> allchilds = getChildElements(locator);
        if (allchilds.isEmpty()) {
            if (throwException) {
                throw new NotFoundException("No child Elements matching:  "
                        + locator);
            } else {
                return null;
            }
        } else {
            result = new Element(allchilds.get(0));
        }
        return result;
    }

    public boolean hasChilds(String locator) {
        return (!getChildElements(locator).isEmpty());
    }

    private void waitForStaledElements(String locator, int seconds) {
        boolean staleElements = false;
        float remaining = seconds;
        do {
            if (remaining == 0) {
                return;
            }
            // todo:fa ceva aici
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            try {
                getWebElement().findElement(findLocator(locator));
            } catch (StaleElementReferenceException e) {
                staleElements = true;
            }
            remaining -= 0.5;
        } while (staleElements);
    }

}
