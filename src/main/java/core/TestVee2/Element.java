
package core.TestVee2;

import core.TestVee2.utils.Messages;
import core.TestVee2.utils.Model;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.rules.TestName;
import org.openqa.selenium.*;
import org.openqa.selenium.By.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.FlickAction;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.PrintWriter;
import java.io.StringWriter;

import static core.TestVee2.Base.logger;
import static core.TestVee2.Page.*;
import static junit.framework.TestCase.fail;

/**
 * Property of Endava
 * @author bclim
 */
public class Element {

    protected String locator;
    protected String logName;
    protected WebElement boundElement = null;
    public TestName name = new TestName();
    public String getTestName(){
        return name.getMethodName();
    }
    public waitClass wait;

    public Element(String locator) {
        this.locator = locator;
        logName = locator;
        wait = new waitClass();
    }

    public Element(String locator, String logName) {
        this.locator = locator;
        this.logName = logName;
        wait = new waitClass();
    }

    public Element(WebElement webElement) {
        boundElement = webElement;
        locator = boundElement.getTagName() + "." + boundElement.getAttribute("class").replace(" ", ".");
        logName = locator;
        wait = new waitClass();
    }

    public WebElement getWebElement() {
        if (boundElement != null) {
            return boundElement;
        } else {
            return driver.findElement(findLocator(locator));
        }
    }

    private boolean isElement(LocatorType selection, String s) {
        switch (selection) {
            case byId:
                if (!driver.findElements(By.id(s)).isEmpty()) { // check if the string is an id locator
                    System.out.println("Found element [" + logName + "] by id");
                    return true;
                }
                break;
            case byName:
                if (!driver.findElements(By.name(s)).isEmpty()) { // check if the string is a name locator
                    System.out.println("Found element [" + logName + "] by name");
                    return true;
                }
                break;
            case byCss:
                if (!driver.findElements(By.cssSelector(s)).isEmpty()) { // check if the string is a Css locator
                    System.out.println("Found element [" + logName + "] by css");
                    return true;
                }
                break;
        }
        return false;
    }

    protected By findLocator(String s) {
//      locator strategy hierarchy (findLocator optimisation)
//      accepted values: byId, byName, byCss (byXPath is verified first in all cases)
        LocatorType strg1choise = LocatorType.byId;
        LocatorType strg2choise = LocatorType.byName;
        LocatorType strg3choise = LocatorType.byCss;

        s = getString(s);

        Boolean path = s.startsWith("/");
        Boolean path1 = s.startsWith("(/");
        Boolean link = s.startsWith("link=");
        Boolean partiallink = s.startsWith("partiallink=");
        Boolean classname = s.startsWith("classname=");
        Boolean tagname = s.startsWith("tagname=");

        if (path || path1) {
            System.out.println("Found element [" + logName + "] by xpath");
            return new ByXPath(s);
        } else if (link) {
            System.out.println("Found element [" + logName + "] by link");
            return new ByLinkText(s.substring(5, s.length()));
        } else if (partiallink) {
            System.out.println("Found element [" + logName + "] by partiallink");
            return new ByPartialLinkText(s.substring(12, s.length()));
        } else if (classname) {
            System.out.println("Found element [" + logName + "] by classname");
            return new ByClassName(s.substring(10, s.length()));
        } else if (tagname) {
            System.out.println("Found element [" + logName + "] by tagname");
            return new ByTagName(s.substring(8, s.length()));
        } else if (isElement(strg1choise, s)) {
            return getLocator(strg1choise, s);
        } else if (isElement(strg2choise, s)) {
            return getLocator(strg2choise, s);
        } else if (isElement(strg3choise, s)) {
            return getLocator(strg3choise, s);
        }
        {
            System.out.println("Could not find element [" + logName + "]");
            throw new IllegalArgumentException("Element identified by: " + logName + ", not found");
        }

    }

    private enum LocatorType {

        byId, byName, byCss
    }

    private static By getLocator(LocatorType selection, String s) {
        switch (selection) {
            case byId:
                return new ById(s);
            case byName:
                return new ByName(s);
            case byCss:
                return new ByCssSelector(s);
        }
        return null;
    }

    public String getString(String key) {
        String value;
        if (key == null) {
            return "";
        }
        value = Messages.getString(key);
        if (value.equals("!" + key + "!")) {
            value = Model.getString(key);
        }
        if (value.equals("!" + key + "!")) {
            value = key;
        }

        return value;
    }

    public String getValue() {

        locator = getString(locator);
        logger.debug("Executing:  getValue(" + logName + ")");

        try {
            return getWebElement().getAttribute("value");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command:  getValue(" + logName + "): " + "\n" + commaSeparator + e);
            return "";
        }
    }
    
    public void setByValueIndexorVisibleText(String option, String value) {

        logger.debug("Executing:  setByValueIndexorVisibleText(" + logName + ")");
        try {
            Select dropdown = new Select(getWebElement());
            switch (option) {
                case "value":
                    dropdown.selectByValue(value);
                    logger.debug("Executing:  setByValue(" + logName + ", "+value+")");
                    break;
                case "index":
                    dropdown.selectByIndex(Integer.parseInt(value));
                    logger.debug("Executing:  setByIndex(" + logName + ", "+value+")");
                    break;
                case "text":
                    dropdown.selectByVisibleText(value);
                    logger.debug("Executing:  setByVisibleText(" + logName + ", "+value+")");
                    break;
            }
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command:  setByValueIndexorVisibleText(" + logName + "): " + "\n" + commaSeparator + e);
            
        }
    }

    private String throwableToString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        Page x = new Page();
        x.captureScreen();
        t.printStackTrace(pw);
        return sw.toString();
    }

    public boolean isElementPresent() {

        locator = getString(locator);
        logger.debug("Checking:  isElementPresent(" + logName + ")");
        try {
            if (driver.findElements(findLocator(locator)).size() != 0) {
                return true;
            } else {
                // Practically dead branch resulted from findLocator
                // implementation
                return false;
            }
        } catch (IllegalArgumentException e) {
            return false;
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command:  isElementPresent(" + logName + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            return false;
        }

    }

    public boolean isEditable() {
        locator = getString(locator);
        logger.debug("Checking:  isEditable(" + logName + ")");
        try {

            WebElement element = getWebElement();
            String isReadOnly = element.getAttribute("readonly");

            if (isReadOnly == null) {
                return element.isEnabled();
            } else {
                return false;
            }

        } catch (Exception e) {
            logger.debug("Exception when trying to execute command:  isEditable(" + logName + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            return false;
        }

    }

    public boolean isChecked() {
        logger.debug("Checking:  isChecked(" + getString(logName) + ")");

        try {
            return getWebElement().isSelected();
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command:  isChecked(" + getString(logName) + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            return false;
        }

    }

    public boolean elementContainsText(String text, Boolean... ignoreCase) {

        assert ignoreCase.length <= 1;
        boolean ignoreParam = ignoreCase.length > 0 ? ignoreCase[0].booleanValue() : false;

        String elementLocator = StringEscapeUtils.unescapeHtml(getText());
        String searchedText = StringEscapeUtils.unescapeHtml(getString(text));

        logger.debug("Checking:  elementContainsText(" + getString(logName) + "," + searchedText + "). Actual text: " + elementLocator);

        try {
            if (ignoreParam) {
                return elementLocator.toLowerCase().contains(searchedText.toLowerCase());
            } else {
                return elementLocator.contains(searchedText);
            }
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command:  elementContainsText(" + logName + "," + text + "," + Boolean.toString(ignoreParam) + "): " + "\n" + commaSeparator
                    + e);
            Page.PassFailMonitor.addError(null, e);
            return false;
        }

    }

    public boolean elementContainsValue(String value) {

        String elementLocator = StringEscapeUtils.unescapeHtml(getValue());
        String searchedValue = StringEscapeUtils.unescapeHtml(getString(value));

        logger.debug("Checking:  elementContainsValue(" + getString(logName) + "," + searchedValue + "). Actual value: " + elementLocator);
        try {
            return elementLocator.contains(searchedValue);
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " elementContainsValue(" + logName + "," + value + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            return false;
        }

    }

    /**
     * Verifies that a select list has the specified value selected
     *
     * @param value
     */
    public boolean isValueFromListSelected(String value) {
        value = getString(value);
        logger.debug("Checking " + commaSeparator + " isValueFromListSelected(" + value + "," + getString(logName) + ")");
        try {
            return StringEscapeUtils.unescapeHtml(value).equals(getSelectedLabel());
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " isValueFromListSelected(" + value + "," + logName + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            return false;
        }
    }

    public boolean isVisible() {
        locator = getString(locator);
        logger.debug("Checking " + commaSeparator + " isVisible(" + logName + ")");

        try {
            return getWebElement().isDisplayed();
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " isVisible(" + logName + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            return false;
        }

    }

    public void handleConfirmationMessage(boolean chooseOk) {
        Page x = new Page();
        if (x.isAlertPresent()) {

            Alert alert = driver.switchTo().alert();

            if (chooseOk) {
                try {
                    alert.accept();
                    logger.debug("Executed command " + commaSeparator + " handleConfirmationMessage - accept ");
                } catch (Exception e) {
                    logger.debug("Exception when trying to execute command " + commaSeparator + " handleConfirmationMessage - accept: " + "\n" + commaSeparator + e);
                    Page.PassFailMonitor.addError(null, e);
                    fail(throwableToString(e));
                }

            } else {
                try {
                    alert.dismiss();
                    logger.debug("Executed command " + commaSeparator + " handleConfirmationMessage - dismiss");
                } catch (Exception e) {
                    logger.debug("Exception when trying to execute command " + commaSeparator + " handleConfirmationMessage - dismiss: " + "\n" + commaSeparator + e);
                    Page.PassFailMonitor.addError(null, e);
                    fail(throwableToString(e));
                }

            }
        } else {
            logger.debug("Confirmation window not present " + commaSeparator + " handleConfirmationMessage");
        }
    }

    public void type(String value) {
        WebElement element = getWebElement();
        locator = getString(locator);
        value = getString(value);

        try {
            wait.toBeVisible(10);
            element.clear();
            element.sendKeys(value);
            logger.debug("Executed command:  type(" + logName + "," + value + ")");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command:  type(" + logName + "," + value + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            fail(throwableToString(e));
        }
    }

    public void click(Boolean... assertOnFail) {
        locator = getString(locator);

        assert assertOnFail.length <= 1;
        boolean assertParam = assertOnFail.length <= 0 || assertOnFail[0].booleanValue();

        try {
            wait.toBeVisible(10);
            getWebElement().click();
            logger.debug("Command executed:  click(" + logName + ")");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command:  click(" + logName + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            if (assertParam) {
                fail(throwableToString(e));

            }
        }

    }

    public void clickAndHandleMessage(boolean pressOk) {
//        if (typeOfBrowser.equals(safari))
//        {
//            // Modal dialogs not supported:
//            // Safari - http://code.google.com/p/selenium/issues/detail?id=3862
//            click(key, false);
//            return;
//        }
//        else if (typeOfBrowser.equals(opera) || typeOfBrowser.equals(android))
//        {
//            // Opera - http://code.google.com/p/selenium/wiki/OperaDriver
//            return;
//        }
        click();
        handleConfirmationMessage(pressOk);
    }

    public void mouseOver(Boolean... assertOnFail) {
        locator = getString(locator);

        assert assertOnFail.length <= 1;
        boolean assertParam = assertOnFail.length > 0 ? assertOnFail[0].booleanValue() : true;

        try {
            logger.debug("Executing command " + commaSeparator + " mouseOver(" + logName + ")");
            Actions action = new Actions(driver);
            action.moveToElement(getWebElement()).build().perform();
            logger.debug("Executed command " + commaSeparator + " mouseOver(" + logName + ")");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " mouseOver(" + logName + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            if (assertParam) {
                fail(throwableToString(e));
            }
        }

    }

    public void check() {
        locator = getString(locator);
        try {
            if (!isChecked()) {
                click();
            } else {
                logger.debug("Element already checked:  " + logName);
            }
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " check(" + logName + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            fail(throwableToString(e));
        }

    }

    public void uncheck() {
        locator = getString(locator);
        try {
            if (isChecked()) {
                click();
            } else {
                logger.debug("Element already unchecked:  " + logName);
            }
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " uncheck(" + logName + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            fail(throwableToString(e));
        }
    }

    public void setCheckboxState(boolean state) {
        try {
            if (state) {
                check();
            } else {
                uncheck();
            }
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " setCheckboxState(" + getString(logName) + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            fail(throwableToString(e));
        }

    }



    public void singleTap(Boolean... assertOnFail) {
        locator = getString(locator);

        assert assertOnFail.length <= 1;
        boolean assertParam = assertOnFail.length > 0 ? assertOnFail[0].booleanValue() : true;

        try {
            TouchActions myActions = new TouchActions(driver);

            myActions.singleTap(getWebElement());

            myActions.perform();

            logger.debug("Executed command " + commaSeparator + " singleTap(" + logName + ")");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " singleTap(" + logName + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            if (assertParam) {
                fail(throwableToString(e));
            }
        }
    }

    public void flick(int xOffset, int yOffset, int... speed) {
        locator = getString(locator);

        assert speed.length <= 1;
        int speedParam = speed.length > 0 ? speed[0] : FlickAction.SPEED_FAST;

        try {
            WebElement onElement = getWebElement();

            TouchActions myActions = new TouchActions(driver);

            myActions.flick(onElement, xOffset, yOffset, speedParam);

            myActions.perform();

            logger.debug("Executed command " + commaSeparator + " flick(" + logName + Integer.toString(xOffset) + "," + Integer.toString(yOffset) + "," + Integer.toString(speedParam) + ")");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " flick(" + logName + "," + Integer.toString(xOffset) + "," + Integer.toString(yOffset) + ","
                    + Integer.toString(speedParam) + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            fail(throwableToString(e));
        }

    }

    public void scroll(int xOffset, int yOffset) {
        locator = getString(locator);

        try {
            WebElement onElement = getWebElement();

            TouchActions myActions = new TouchActions(driver);

            myActions.scroll(onElement, xOffset, yOffset);

            myActions.perform();

            logger.debug("Executed command " + commaSeparator + " scroll(" + logName + Integer.toString(xOffset) + "," + Integer.toString(yOffset) + ")");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " scroll(" + logName + "," + Integer.toString(xOffset) + "," + Integer.toString(yOffset) + "): " + "\n"
                    + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            fail(throwableToString(e));
        }

    }

    public void select(String option) {
        locator = getString(locator);
        option = getString(option);
        try {
            Select select = new Select(getWebElement());
            // investigate case when deselectAll is needed
//            if (!select.getAllSelectedOptions().isEmpty())
//            {
//                select.deselectAll();
//            }
            select.selectByVisibleText(option);

            logger.debug("Executed command " + commaSeparator + " select(" + logName + "," + option + ")");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " select(" + logName + "," + option + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            fail(throwableToString(e));
        }
    }

    public int getElementHeight() {

        locator = getString(locator);
        logger.debug("Executing " + commaSeparator + " getElementHeight(" + logName + ")");

        try {
            int height = getWebElement().getSize().getHeight();
            logger.debug("Height of element " + locator + " = " + height);
            return height;
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " getElementHeight(" + logName + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            return 0;
        }
    }

    /**
     * Get the Width value of this element.
     *
     * @return - the Width of the element.
     */
    public int getElementWidth() {

        locator = getString(locator);
        logger.debug("Executing " + commaSeparator + " getElementWidth(" + logName + ")");

        try {
            int width = getWebElement().getSize().getWidth();
            logger.debug("Width of element " + locator + " = " + width);
            return width;
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " getElementWidth(" + logName + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            return 0;
        }
    }

    /**
     * Get the visible (i.e. not hidden by CSS) innerText of this element,
     * including sub-elements, without any leading or trailing whitespace.
     *
     * @return - The innerText of key element.
     */
    public String getText() {

        locator = getString(locator);
        logger.debug("Executing " + commaSeparator + " getText(" + logName + ")");

        try {
            return getWebElement().getText();
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " getText(" + logName + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            return "";
        }

    }

    /**
     * Gets option label (visible text) for selected option in the specified
     * select element.
     *
     * @return the selected option label in the specified select drop-down
     */
    public String getSelectedLabel() {
        try {

            Select select = new Select(getWebElement());

            WebElement option = select.getFirstSelectedOption();

            return option.getText();

        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " getSelectedLabel(" + logName + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            return "";
        }

    }

    protected void pressTabKey() {
        try {
            WebElement element = getWebElement();
            element.sendKeys(Keys.TAB);
            logger.debug("Executed command " + commaSeparator + " pressTabKey");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " pressTabKey: " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            fail(throwableToString(e));
        }
    }

    public void pressKey(Keys x) {
        try {
            WebElement element = getWebElement();
            element.sendKeys(x);
            logger.debug("Executed command " + commaSeparator + "custom Key");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + "custom Key: " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            fail(throwableToString(e));
        }
    }

    /**
     * Drag and drop an element
     *
     * @param targetElementKey the target element key
     */
    protected void dragAndDrop(String targetElementKey) {

        locator = getString(locator);
        targetElementKey = getString(targetElementKey);
        try {
            WebElement sourceElement = getWebElement();
            WebElement targetElement = driver.findElement(findLocator(targetElementKey));

            (new Actions(driver)).dragAndDrop(sourceElement, targetElement).perform();
            logger.debug("Executed command " + commaSeparator + " dragAndDrop(" + logName + "," + targetElementKey + ")");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " dragAndDrop(" + logName + "," + targetElementKey + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            fail(throwableToString(e));
        }

    }

    /**
     * ELEMENT SPECIFIC WAIT FUNCTIONS
     */


    public class waitClass {


        public void toBeEditable(int timeout, Boolean... assertOnFail) {
            assert assertOnFail.length <= 1;
            boolean logFail = assertOnFail.length > 0 ? assertOnFail[0] : true;

            for (int second = 0; second < timeout + 1; second++) {
                if (second >= timeout) {
                    logger.debug("Timed out waiting for '" + getString(logName) + "' to become editable");
                    Exception e = new Exception("Timeout after waiting for"+ getString(logName) + " to become editable");
                    Page.PassFailMonitor.addError(null, e);
                    if (logFail) {
                        fail("Timeout after '" + timeout + "' seconds in test "+getTestName());
                    }
                } else if (isEditable()) {
                    break;
                }
                sleep(1000);
            }
        }

        public void toBeVisible(int timeout, Boolean... assertOnFail) {
            assert assertOnFail.length <= 1;
            boolean logFail = assertOnFail.length > 0 ? assertOnFail[0] : true;

            try {
                new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(findLocator(locator)));
            } catch (TimeoutException e) {
                logger.debug("Timed out waiting for '" + getString(logName) + "' to become visible");
                if (logFail) {
                    fail("Timeout after '" + timeout + "' seconds in test "+getTestName());
                }
            } catch (Exception e) {
                Page.PassFailMonitor.addError(null, e);
                logger.debug("Exception when trying to execute command " + commaSeparator + " waitForElementToBecomeVisible(" + logName + "," + timeout + "): " + "\n" + commaSeparator + e);
                fail(throwableToString(e));
            }
        }

        public void toContainText(String value, int timeout, Boolean... assertOnFail) {
            assert assertOnFail.length <= 1;
            boolean logFail = assertOnFail.length > 0 ? assertOnFail[0] : true;

            try {
                new WebDriverWait(driver, timeout).until(ExpectedConditions.textToBePresentInElement(findLocator(locator), value));
            } catch (TimeoutException e) {
                logger.debug("Timed out waiting for '" + logName + "' to contain text " + value);
                if (logFail) {
                    fail("Timeout after '" + timeout + "' seconds in test "+getTestName());
                }
            } catch (Exception e) {
                Page.PassFailMonitor.addError(null, e);
                logger.debug("Exception when trying to execute command " + commaSeparator + " waitForElementToBecomeVisible(" + logName + "," + timeout + "): " + "\n" + commaSeparator + e);
                fail(throwableToString(e));
            }
        }

        public void toBeOnPage(int timeout, Boolean... assertOnFail) {
            Element x = new Element(locator);
            assert assertOnFail.length <= 1;
            boolean logFail = assertOnFail.length > 0 ? assertOnFail[0] : true;

            for (int second = 0; second < timeout + 1; second++) {
                if (second >= timeout) {
                    logger.debug("Timed out waiting for '" + getString(logName) + "'");
                    Exception e = new Exception("Timeout after waiting for"+ getString(logName) + " to be contained by the page");
                    Page.PassFailMonitor.addError(null, e);
                    if (logFail) {
                        fail("Timeout after '" + timeout + "' seconds in test "+getTestName());
                    }
                } else if (x.isElementPresent()) {
                    break;
                }
                sleep(1000);
            }
        }

        public void toFade(int timeout, Boolean... assertOnFail) {
            assert assertOnFail.length <= 1;
            boolean logFail = assertOnFail.length > 0 ? assertOnFail[0] : true;

            try {
                new WebDriverWait(driver, timeout).until(ExpectedConditions.invisibilityOfElementLocated(findLocator(locator)));
            } catch (IllegalArgumentException e) {
            } catch (TimeoutException e) {
                logger.debug("Element still visible/present even after timeout '" + getString(logName) + "'");
                if (logFail) {
                    fail("Timeout after '" + timeout + "' seconds in test "+getTestName());
                }
            } catch (Exception e) {
                Page.PassFailMonitor.addError(null, e);
                logger.debug("Exception when trying to execute command " + commaSeparator + " waitForPageNotToContainElement(" + logName + "," + timeout + "): " + "\n" + commaSeparator + e);
                fail(throwableToString(e));
            }
        }
    }

}
