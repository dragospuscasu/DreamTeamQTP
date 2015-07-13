package core.TestVee2;

import core.TestVee2.utils.SeleniumUtils;
import core.TestVee2.utils.ThreadUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Property of Endava
 * @author bclim
 */
public class Page extends Base{
    public waitClass wait;

    public Page(){
         wait = new waitClass();
    }
    public Page(String url)
    {
        open(url);
        wait = new waitClass();
    }

    public void open(String url)
    {
        try {
            long loadTime;
            long start = System.currentTimeMillis();
            driver.get(url);
            long end = System.currentTimeMillis();
            loadTime = end - start;
            logger.debug("Command executed:  open(" + url + ") in " + loadTime + "ms / " + (double) loadTime / 1000 + "s ");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command:  open(" + url + "): " + "\n" + commaSeparator + e);
            fail(throwableToString(e));
            PassFailMonitor.addError(null, e);
        }
    }

    public void openWithAuthenticationDialog(String url, String user, String password) /**
     * Opens the provided page, handles the authentication
     */
    {
        int elapsed = 0;
        AutoItInitialization();

        // HTTP authentication
        ThreadUtils monThread = null;
        try {
            // monitoring thread
            monThread = new ThreadUtils(user, password, pageLoadingTimeout);
            // main thread
            driver.get(url);
            // extra verification for IE cross authentication, in case extra
            // error handling support is needed, more verification and filtering
            // can be added
            while (!monThread.dialogHandled && elapsed < pageLoadingTimeout) {
                sleep(1000);
                elapsed++;
            }
            monThread.stop();
            logger.debug("Command executed:  openWithAuthenticationDialog(" + url + ")");
        } catch (Exception e) {
            monThread.stop();
            logger.debug("Exception when trying to execute command:  openWithAuthenticationDialog(" + url + "): " + "\n" + commaSeparator + e);
            fail(throwableToString(e));
            PassFailMonitor.addError(null, e);
        }
    }

    public void verifyTrue(boolean b)
    {
        try {
            assertTrue(b);
            logger.debug("Verification passed");
            passed++;
        } catch (Error e) {
            verificationErrors.append(throwableToString(e));
            logger.debug("Verification failed");
            failed++;
        }
    }

    public void verifyFalse(boolean b) /**
     * Like assertFalse, fails at the end of the test (during tearDown)
     */
    {
        try {
            assertFalse(b);
            logger.debug("Verification passed");
            passed++;
        } catch (Error e) {
            verificationErrors.append(throwableToString(e));
            logger.debug("Verification failed");
            failed++;
        }
    }

    public void verifyFalse(boolean b, String s) /**
     * Like assertFalse, fails at the end of the test (during tearDown)
     */
    {
        try {
            assertFalse(s, b);
            logger.debug("Verification passed");
            passed++;
        } catch (Error e) {
            verificationErrors.append(throwableToString(e));
            logger.info("Verification failed with message" + commaSeparator + " " + s);
            failed++;
        }
    }

    public void verifyTrue(boolean b, String s) /**
     * Like assertTrue, fails at the end of the test (during tearDown). It will
     * show the string as error.
     */
    {
        try {
            assertTrue(s, b);
            logger.debug("Verification passed");
            passed++;
        } catch (Error e) {
            verificationErrors.append(throwableToString(e));
            logger.info("Verification failed with message" + commaSeparator + " " + s);
            failed++;
        }
    }

    public boolean isTextPresent(String text, Boolean... ignoreCase) /**
     * Verifies that the specified text pattern appears somewhere on the
     * rendered page shown to the user.
     */
    {
        text = searchForKey(text);

        assert ignoreCase.length <= 1;
        boolean ignoreParam = ignoreCase.length > 0 ? ignoreCase[0].booleanValue() : false;

        logger.debug("Checking:  isTextPresent(" + text + "," + Boolean.toString(ignoreParam) + ")");

        try {
            if (ignoreParam) {
                return StringEscapeUtils.unescapeHtml(driver.findElement(By.tagName("body")).getText()).toLowerCase().contains(StringEscapeUtils.unescapeHtml(text.toLowerCase()));
            } else {
                return StringEscapeUtils.unescapeHtml(driver.findElement(By.tagName("body")).getText()).contains(StringEscapeUtils.unescapeHtml(text));
            }
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " isTextPresent(" + text + "," + Boolean.toString(ignoreParam) + "): " + "\n" + commaSeparator + e);
            return false;
        }
    }
    
    /**
     *
     * @param text Text to search for 
     * @param count The number of supposed findings of the text
     * @param ignoreCase Case Sensitive or not 
     * @return
     */
    public boolean textCounterValidator(String text, int count, Boolean... ignoreCase)
    {
        text = searchForKey(text);
        int counter = 0;
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assert ignoreCase.length <= 1;
        boolean ignoreParam = ignoreCase.length > 0 ? ignoreCase[0].booleanValue() : false;
        logger.debug("Checking:  textCounterValidator(" + text + "," + Boolean.toString(ignoreParam) + ")");
        
        try {
            if (ignoreParam) {
                for(int i = 0; i<count; i++){
                    if(StringEscapeUtils.unescapeHtml(bodyText).toLowerCase().contains(StringEscapeUtils.unescapeHtml(text.toLowerCase()))){
                        counter++;
                        bodyText = bodyText.toLowerCase().substring(bodyText.indexOf(text.toLowerCase()));
                    } 
                }
                logger.debug("Text found "+ counter + " times from total requested of "+count+ " times");
                return counter == count;
            } else {
               for(int i = 0; i<count; i++){
                    if(StringEscapeUtils.unescapeHtml(bodyText).contains(StringEscapeUtils.unescapeHtml(text))){
                        counter++;
                        bodyText = bodyText.substring(bodyText.indexOf(text));
                    } 
                }
                logger.debug("Text found "+ counter + " times from total requested of "+count+ " times");
                return counter == count;
            }
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " textCounterValidator(" + text + "," + Boolean.toString(ignoreParam) + "): " + "\n" + commaSeparator + e);
            return false;
        }
    }





    protected void close() /**
     * Closes current page, if it is the last one closes browser instance
     */
    {
        try {
            driver.close();
            logger.debug("Executed command " + commaSeparator + " close");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " close: " + "\n" + commaSeparator + e);
        }
    }


    public boolean isAlertPresent(int... timeout) /**
     * Verifies if an alert/confirmation is present
     */
    {
        // setting the default value
        assert timeout.length <= 1;
        int tmout = timeout.length > 0 ? timeout[0] : 5;
        try {
            Alert newAlert = (new WebDriverWait(driver, tmout)).until(ExpectedConditions.alertIsPresent());
            if (newAlert != null) {
                logger.debug("New alert found when running" + commaSeparator + " isAlertPresent(" + Integer.toString(tmout) + ")");
                return true;
            } else {
                logger.debug("No alert found when running" + commaSeparator + " isAlertPresent(" + Integer.toString(tmout) + ")");
                return false;
            }
        } catch (TimeoutException e) {
            logger.debug("No alert found when running" + commaSeparator + " isAlertPresent(" + Integer.toString(tmout) + ")");
            return false;
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " isAlertPresent(" + Integer.toString(tmout) + "): " + "\n" + commaSeparator + e);
            return false;
        }
    }



    public String getTitle() /**
     * Gets the title of the current page.
     */
    {
        try {
            return driver.getTitle();
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " getTitle(): " + "\n" + commaSeparator + e);
            return "";
        }
    }

    public String getLocation() {   //Gets the URL of the current page.
        try {
            return driver.getCurrentUrl();
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " getLocation(): " + "\n" + commaSeparator + e);
            return "";
        }
    }

    protected int getXpathCount(String xPath) /**
     * Returns the number of nodes that match the specified xPath
     */
    {
        xPath = searchForKey(xPath);
        try {
            int count = driver.findElements(By.xpath(xPath)).size();
            logger.debug("Executed command " + commaSeparator + " getXpathCount(" + xPath + ")");
            return count;
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " getXpathCount(" + xPath + "): " + "\n" + commaSeparator + e);
            return 0;
        }
    }

    protected static String getMachineName() {    //Returns Machine name
        return System.getenv("COMPUTERNAME");
    }



    protected static void sleep(int milisecs) {   //Static wait function
        try {
            Thread.sleep(milisecs);
        } catch (InterruptedException e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " sleep(" + Integer.toString(milisecs) + "): " + "\n" + commaSeparator + e);
        }
    }



    protected void writeHtmlSourceToFile() throws Exception {   //Utility method that writes the html source to specified file
        SeleniumUtils.createFolder(SeleniumUtils.generateHtmlSourcePath());
        String filePath = SeleniumUtils.generateHtmlSourcePath() + "/" + getName() + System.currentTimeMillis() + ".html";
        // SeleniumUtils.createFile(filePath);
        File htmlFile = new File(filePath);
        try {
            FileUtils.writeStringToFile(htmlFile, driver.getPageSource());
            logger.debug("Saved htmlSource to file");
        } catch (IOException e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " writeHtmlSourceToFile: " + "\n" + commaSeparator + e);
        }

    }

    public void pressTabKey(WebElement element) {   // Presses Tab key
        try {
            element.sendKeys(Keys.TAB);
            logger.debug("Executed command " + commaSeparator + " pressTabKey");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " pressTabKey: " + "\n" + commaSeparator + e);
            fail(throwableToString(e));
        }
    }

    public void pressEscKey() {  // Presses Esc key
        try {
            // looks for a dummy locator to get an element object
            WebElement element = driver.findElement(By.id("DummyID"));
            element.sendKeys(Keys.ESCAPE);
            logger.debug("Executed command " + commaSeparator + " pressEscKey");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " pressEscKey: " + "\n" + commaSeparator + e);
            fail(throwableToString(e));
        }
    }

    public void setJavaScriptState(boolean enableJS, String... url) {  // Loads a new browser instance with Java Script enabled or disabled
        assert url.length <= 1;
        String urlToLoad = url.length > 0 ? url[0] : StartURL;

        if (typeOfBrowser.equals(firefox)) {
            isJsOFF = !enableJS;
            quit();
            driver = driverSetup();
            maximize();
            open(urlToLoad);
        }
    }

    protected void javascript(String command,Boolean... assertOnFail) {

        assert assertOnFail.length <= 1;
        boolean assertParam = assertOnFail.length > 0 ? assertOnFail[0].booleanValue() : true;

        try {
            ((JavascriptExecutor) driver).executeScript(command);
            logger.debug("Executed command " + commaSeparator + " javascript(" + command + ")");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " javascript(" + command + "): " + "\n" + commaSeparator + e);
            Page.PassFailMonitor.addError(null, e);
            if (assertParam) {
                fail(throwableToString(e));
            }
        }
    }



    public void uploadFile(String key, String path) { // Uploads the desired file
        Element x = new Element(key);
        x.click(true);

        SeleniumUtils.handleUploadDialog(path);
    }

    protected Object PendingAjaxCallsCount(JavascriptExecutor jsExecutor, Object result) {
        try {
            result = jsExecutor.executeScript(
                    "if (typeof jQuery != 'undefined') {  \n"
                    + "					  // jQuery is loaded  \n"
                    + "					  return jQuery.active;\n"
                    + "					  } else {\n"
                    + "					  // jQuery is not loaded\n"
                    + "					  return 0;\n"
                    + "					  }");
        } catch (Exception e) {
            assertTrue("Error when counting Ajax Calls: " + e.getMessage(), true);
        }
        return result;
    }

    /**
     * PAGE SPECIFIC WAIT CLASS
     */

    public class waitClass{
        @Deprecated
        public void forFixedTime(int seconds){
            sleep(seconds*1000);
        }
        public void forText(String text, int timeout, Boolean... assertOnFail) { //Dynamic wait function

            assert assertOnFail.length <= 1;
            boolean logFail = assertOnFail.length > 0 ? assertOnFail[0] : true;
            for (int second = 0; second < timeout + 1; second++) {
                if (second >= timeout) {
                    logger.debug("Timed out waiting for '" + searchForKey(text) + "' to appear in the page");
                    TimeoutException e = new TimeoutException("Timeout waiting for " + searchForKey(text) + " to appear in the page.");
                    PassFailMonitor.addError(null, e);
                    if (logFail) {
                        fail("Timeout after '" + timeout + "' seconds in test "+ getTestName());
                    }
                } else if (isTextPresent(text)) {
                    break;
                }
                sleep(1000);
            }
        }

        public void forTextFade(String text, int timeout, Boolean... assertOnFail) {  // Dymanic wait function
            assert assertOnFail.length <= 1;
            boolean logFail = assertOnFail.length > 0 ? assertOnFail[0] : true;

            for (int second = 0; second < timeout + 1; second++) {
                if (second >= timeout) {
                    logger.debug("Timed out waiting for '" + searchForKey(text) + "' to dissappear from the page");
                    TimeoutException e = new TimeoutException("Timeout waiting for a text to be visible.");
                    PassFailMonitor.addError(null, e);
                    if (logFail) {
                        fail("Timeout after '" + timeout + "' seconds in test "+ getTestName());
                    }
                } else if (!isTextPresent(text)) {
                    break;
                }
                sleep(1000);
            }
        }

        public void forPopUp(final String windowID, int... timeout) /**
         * Method waits and connects to an specific (windowID) popup
         */
        {
            // setting the default value
            assert timeout.length <= 1;
            int tmout = timeout.length > 0 ? timeout[0] : 10;
            String strtmout = Integer.toString(tmout);
            try {
                @SuppressWarnings("unused")
                String newWindow = (new WebDriverWait(driver, tmout)).until(new ExpectedCondition<String>() {
                    public String apply(WebDriver input) {
                        try {
                            driver.switchTo().window(windowID);
                            return "popup found"; // dummy string
                        } catch (Exception e) {
                            return null;
                        }
                    }
                });
                logger.debug("Executed command " + commaSeparator + " waitForPopUp(" + windowID + "," + strtmout + ")");
            } catch (TimeoutException e) {
                logger.debug("No popup found " + commaSeparator + " waitForPopUp(" + windowID + "," + strtmout + ")");
            } catch (Exception e) {
                logger.debug("Exception when trying to execute command " + commaSeparator + " waitForPopUp(" + windowID + "," + strtmout + "): " + "\n" + commaSeparator + e);
            }
        }

        public void forJSRequests() {

            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            if (jsExecutor == null) {
                logger.info("JavaScrip Executor error!");
                return;
            }
            boolean ajaxCallsPending = true;
            while (ajaxCallsPending) {
                Object result = null;
                result = PendingAjaxCallsCount(jsExecutor, result);
                if (result == null) {
                    break;
                }
                int numberOfPendingAjaxCalls = Integer.parseInt(result.toString());
                //  logger.info("Waiting for pending Ajax Calls. Active = " + numberOfPendingAjaxCalls);
                ajaxCallsPending = numberOfPendingAjaxCalls > 0;
                // logger.info("AjaxCallsPening = " + ajaxCallsPending);
            }
            logger.info("Ajax calls wait completed !");
        }

        public void forPageToLoad(String title) {
            if (title == null) {
                logger.info("Skipping title wait, Title is null");

                return;
            }
            WebDriverWait wait = new WebDriverWait(driver, 20);
            while (!driver.getTitle().equals(title)) {
                driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.MILLISECONDS);
                //  logger.info("10 milisec passed");
            }
            forJSRequests();
        }

        public void forStatementToBeTrue(Boolean statement, int timeout, Boolean... assertOnFail) {
            assert assertOnFail.length <= 1;
            boolean logFail = assertOnFail.length > 0 ? assertOnFail[0] : true;
            for (int second = 0; second < timeout + 1; second++) {
                if (second >= timeout) {
                    logger.debug("Timeout after waiting for a particular statement to become true");
                    Exception e = new Exception("Timeout after waiting for a particular statement to become true");
                    Page.PassFailMonitor.addError(null, e);
                    if (logFail) {
                        fail("Timeout after '" + timeout + "' seconds in test " + getTestName());
                    }
                } else if (statement) {

                    break;
                }
                sleep(1000);
            }
        }
    }
}
