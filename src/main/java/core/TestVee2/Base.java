package core.TestVee2;

import autoitx4java.AutoItX;
import com.opera.core.systems.OperaDriver;
import com.saucelabs.saucerest.SauceREST;
import core.TestVee2.utils.Messages;
import core.TestVee2.utils.Model;
import core.TestVee2.utils.SeleniumUtils;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.*;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Property of Endava
 * @author bclim
 */

public class Base extends TestCase
{
    // Driver variable
    public static WebDriver driver;

    // Timeout variables
    protected static int pageLoadingTimeout;
    protected static int WebElementLoadingTimeout;

    // Browser specific variables
    public static String StartURL;
    public static String typeOfBrowser;
    public static String nameOfBrowser;
    public static String versionOfBrowser;

    public static String firefox            = "*firefox";
    public static String chrome             = "*googlechrome";
    public static String internetExplorer   = "*iexplore";
    public static String opera              = "*opera";
    public static String safari             = "*safari";
    public static String android            = "*android";
    public static String grid               = "*grid";
    public static String verifyOnSauce      = "*verifyOnSauce";
    public static String SauceLabs          = "*SauceLabs";

    // ZAP app variables
    protected static String zapPID;
    protected static boolean zapStop;

    // Results variable
    public static TestResult PassFailMonitor    = null;
    public static int passed = 0;
    public static int failed = 0;

    public static final String properties               = "automation.properties";
    public static final String DBproperties             = "db.properties";
    public static final String testDescription          = "resources//testdata//tcDescription//TestsDescription.properties";
    public static final String testSuiteName            = "MyTestSuite";

    // Other variables for: logging, handling windows,
    public static org.apache.log4j.Logger logger;
    protected static AutoItX              autoIT;
    protected static String commaSeparator  = ";";
    public static boolean isJsOFF;
    protected StringBuffer verificationErrors = new StringBuffer();

    public static boolean netExportOption = false;


    // SauceLabs User and Password
    public static String sauceUser;
    public static String sauceKey;

    protected static String getMachineName() {
        return System.getenv("COMPUTERNAME");
    }

    @Rule
    public TestName name = new TestName();
    public String getTestName(){
        return  name.getMethodName();
    }

    public void maximize() { // Maximises browser window
        if (typeOfBrowser.equals(opera)) {
            AutoItInitialization();
            // maximises browser page/tab based on caption
            autoIT.winSetState("Connect to Debugger", "", 3);
        } else if (typeOfBrowser.equals(android)) {
            // No need for window maximisation for Android
        } else {
            driver.manage().window().maximize();
        }
    }

    public void checkForErrors() /**
     * Checks if there are any verification errors
     */
    {
        String verificationErrorString = verificationErrors.toString();
        clearErrors();
        if (!"".equals(verificationErrorString)) {
            //logger.info("Test failed");
            AssertionFailedError err = new AssertionFailedError("fail");
            PassFailMonitor.addFailure(null, err);
            fail(verificationErrorString);
        }
    }

    public void clearErrors() /**
     * Clears out the list of verification errors
     */
    {
        verificationErrors = new StringBuffer();
    }

    public WebDriver driverSetup()/**
     * Instantiates desired driver type if no system browser.type property is
     * provided, automation.properties value is used
     */

    {
        String driverType = typeOfBrowser;

        boolean useSecurity = (getValueFromFile("useSecurity", properties).equalsIgnoreCase("true"));
        boolean zapStart = (getValueFromFile("startZAP", properties).equalsIgnoreCase("true"));
        zapStop = (getValueFromFile("stopZAP", properties).equalsIgnoreCase("true"));
        String profilePath = getValueFromFile("profilePath", properties);
        String ZAPpath = getValueFromFile("ZAPpath", properties);


        WebDriver driver = null;

        if (driverType.equals(firefox)) {
            if (useSecurity) {
                if (zapStart) {
                    AutoItInitialization();
                    logger.info("Launching Zed Attack Proxy");
                    autoIT.run("ZAP.exe", ZAPpath);
                    autoIT.winWait("Untitled Session - OWASP ZAP");
                    zapPID = autoIT.winGetProcess("Untitled Session - OWASP ZAP");
                }
                // set Profile for ZAP security tool
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("network.proxy.driverType", 1);
                profile.setPreference("network.proxy.http", "localhost");
                profile.setPreference("network.proxy.http_port", 8090);
                profile.setPreference("network.proxy.no_proxies_on", "");

                driver = new FirefoxDriver(profile);
            } else if (isJsOFF) {
                // set profile for running with Java Scripts disabled
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("javascript.enabled", false);
                driver = new FirefoxDriver(profile);
            } else if (!profilePath.equals("") && netExportOption == true) {
                File profileDir = new File(profilePath);
                FirefoxProfile profile = new FirefoxProfile(profileDir);
                profile.setPreference("extensions.firebug.defaultPanelName", "net");
                profile.setPreference("extensions.firebug.currentVersion", "2.0");
                profile.setPreference("extensions.firebug.delayLoad", "true");
                profile.setPreference("extensions.firebug.netexport.alwaysEnableAutoExport", true);
                profile.setPreference("extensions.firebug.netexport.defaultLogDir", System.getProperty("user.dir") +"\\Har\\");
                profile.setPreference("extensions.firebug.netexport.showPreview", false);
                profile.setPreference("extensions.firebug.netexport.pageLoadedTimeout", 100000);
                profile.setPreference("extensions.firebug.netexport.timeout", 60000);
                profile.setPreference("extensions.firebug.netexport.Automation", true);
                profile.setPreference("extensions.firebug.allPagesActivation", "on");
                profile.setPreference("extensions.firebug.net.enableSites", true);

                driver = new FirefoxDriver(profile);
            } else {
                driver = new FirefoxDriver();
            }
        } else if (driverType.equals(chrome)) {
            // jsOFF not supported
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--test-type");
            capabilities.setCapability("chrome.binary","src\\resources\\drivers\\chromedriver.exe");
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);

            System.setProperty("webdriver.chrome.driver", "src\\resources\\drivers\\chromedriver.exe");
            driver = new ChromeDriver(capabilities);
        } else if (driverType.equals(internetExplorer)) {
            System.setProperty("webdriver.ie.driver", "src\\resources\\drivers\\IEDriverServer x86.exe");
            driver = new InternetExplorerDriver();
        } else if (driverType.equals(opera)) {
            driver = new OperaDriver();
        } else if (driverType.equals(safari)) {
            System.setProperty("SafariDefaultPath", "C:\\PROGRA~2\\Safari\\safari.exe");
            driver = new SafariDriver();
        } else if (driverType.equals(android)) {
            driver = new AndroidDriver();
        } else if (driverType.equals(grid)) {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setBrowserName(nameOfBrowser);
      //      capabilities.setVersion(versionOfBrowser);
            capabilities.setPlatform(Platform.WIN8);
            capabilities = DesiredCapabilities.firefox();
            try {
                driver = new RemoteWebDriver(new URL("http://192.168.206.157:5566/wd/hub"), capabilities);
                       // "http://localhost:4444/wd/hub"), capabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else if (driverType.equals(verifyOnSauce)){

            DesiredCapabilities capabilities = new DesiredCapabilities();
            switch(nameOfBrowser){
                case "iexplore":
                    capabilities = DesiredCapabilities.internetExplorer();
                    capabilities.setCapability("platform", "Windows 8");
                    capabilities.setCapability("version", "10");
                    break;
                case "firefox":
                    capabilities = DesiredCapabilities.firefox();
                    capabilities.setCapability("platform", "Windows 8");
                    capabilities.setCapability("version", "31");
                    break;
                case "googlechrome":
                    capabilities = DesiredCapabilities.chrome();
                    capabilities.setCapability("platform", "Windows 8");
                    capabilities.setCapability("version", "33");
                    break;
                case "safari":
                    capabilities = DesiredCapabilities.safari();
                    capabilities.setCapability("platform", "OS X 10.8");
                    capabilities.setCapability("version", "6");
                    break;
                case "android":
                    capabilities = DesiredCapabilities.android();
                    capabilities.setCapability("platform", "Linux");
                    capabilities.setCapability("version", "4.0");
                    capabilities.setCapability("device-orientation", "portrait");
                    break;
            }
            capabilities.setCapability("name", getTestName() + capabilities.getBrowserName() + capabilities.getVersion() + capabilities.getCapability("platform") );
            try{
                driver = new RemoteWebDriver(new URL("http://"+ sauceUser +":"+ sauceKey +"@ondemand.saucelabs.com:80/wd/hub"), capabilities);
            }
            catch(MalformedURLException e)
            {
                logger.debug("There was a problem accessing the sauceLabs URL", e);
            }
        }
        else if(driverType.equals(SauceLabs)){

            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setBrowserName(System.getenv("SELENIUM_BROWSER"));
            desiredCapabilities.setVersion(System.getenv("SELENIUM_VERSION"));
            desiredCapabilities.setCapability(CapabilityType.PLATFORM, System.getenv("SELENIUM_PLATFORM"));
            desiredCapabilities.setCapability("name", getTestName() + desiredCapabilities.getBrowserName() + desiredCapabilities.getVersion() + desiredCapabilities.getCapability("platform") );
            try{
                driver = new RemoteWebDriver(new URL("http://"+ sauceUser +":"+ sauceKey +"@ondemand.saucelabs.com:80/wd/hub"), desiredCapabilities);
            }
            catch(MalformedURLException e)
            {
                logger.debug("There was a problem accessing the sauceLabs URL", e);
            }
        }
        else {
            throw new IllegalArgumentException("Invalid driver parameter");
        }

        return driver;

    }

    @Before
    public void setUp() throws Exception
    {
        PassFailMonitor = new TestResult();
        System.setProperty("log4j.defaultInitOverride", "true");
        logger = org.apache.log4j.Logger.getLogger("endava"); //$NON-NLS-1$

        Properties logConfiProps = new Properties();
        InputStream inputStream = Base.class.getClassLoader().getResourceAsStream("log4j.properties");
        try
        {
            logConfiProps.load(inputStream);
            Enumeration<?> enumProps = logConfiProps.propertyNames();
            String key;
            String relativePath = logConfiProps.getProperty("log4j.appender.R.File");
            while (enumProps.hasMoreElements())
            {
                key = (String) enumProps.nextElement();

                if (key.equals("log4j.appender.R.File"))
                {
                    logConfiProps.setProperty(key, System.getProperty("user.dir") + relativePath + SeleniumUtils.getCurrentDateInSpecifiedFormat("_yyyy_MMM_dd_") + testSuiteName
                            + ".csv");
                    break;
                }
            }
            PropertyConfigurator.configure(logConfiProps); //$NON-NLS-1$
            logger.info("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");
            System.out.println("Log saved in: " + logConfiProps.getProperty("log4j.appender.R.File"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        // load the values from automation.properties if browser.url and
        // browser.type parameters are not passed as system parameters
        StartURL = System.getProperty("browser.url");
        if (StartURL == null) {
            StartURL = getValueFromFile("startURL", properties);
        }

        typeOfBrowser = System.getProperty("browser.type");
        if(typeOfBrowser == null)
            typeOfBrowser = System.getProperty("browserTypeMaven");
        if (typeOfBrowser == null) {
            typeOfBrowser = getValueFromFile("typeOfBrowser", properties);
        }

        isJsOFF = (getValueFromFile("isJSOFF", properties).equalsIgnoreCase("true"));

        nameOfBrowser = getValueFromFile("browserName", properties);

        versionOfBrowser = getValueFromFile("browserVersion", properties);
        pageLoadingTimeout = new Integer(getValueFromFile("pageLoadTimeout", properties));
        WebElementLoadingTimeout = new Integer(getValueFromFile("elementWaitTimeout", properties));
        sauceUser = System.getenv("sauceUser");
        sauceKey = System.getenv("sauceApiKey");
        if(sauceUser == null){
            sauceUser = getValueFromFile("SauceLabsUser", properties);
            sauceKey = getValueFromFile("SauceLabsKey", properties);
        }

        logger.info("Running Tests on " + getMachineName() + "");
        logger.info("Browser Type: " + typeOfBrowser);

        driver = driverSetup();

        // setting default timeouts
        try {
            driver.manage().timeouts().implicitlyWait(WebElementLoadingTimeout, TimeUnit.MILLISECONDS);
            driver.manage().timeouts().pageLoadTimeout(pageLoadingTimeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            if (typeOfBrowser.equals(safari) || typeOfBrowser.equals(chrome) || typeOfBrowser.equals(android)) {
                System.out.println("Safari, Chrome and Android drivers do not support timeouts interface");
            } else {
                throw new Exception(e);
            }
        }

        maximize();
        logger.info("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");
        logger.info("-> New test started:  " + TestSuite.getTestConstructor(getClass()).getName() + commaSeparator + getTestName() + commaSeparator + getTestDescription(getName()));
        // Verifications should be per test not for a general run and need a reset at every setup
        failed = 0;
        passed = 0;
        //open(StartURL);
    }

    @After
    public void tearDown() throws Exception {

        String tempBrowser = typeOfBrowser;
        logger.info("$$$ Test ended: " + commaSeparator + getTestName());
        logger.debug("Verifications done:    Passed: " + passed + "    Failed: " + failed);

        try {
            checkForErrors();
        }
        catch(Exception e){
            logger.debug("Exception occurred when trying to set the PassFailMonitor into SauceLabs after the test was finished: "+e);

        } finally {
            if(tempBrowser.equals(verifyOnSauce) || tempBrowser.equals(SauceLabs)){
                String testID = ((RemoteWebDriver)driver).getSessionId().toString();
                SauceREST Sauceclient = new SauceREST(sauceUser, sauceKey);
                if(PassFailMonitor.wasSuccessful() && PassFailMonitor.errorCount() == 0){
                    logger.info("Test  "+ getTestName() + "  PASSED");
                    Sauceclient.jobPassed(testID);
                }
                else{
                    logger.info("Test  "+ getTestName() + "  FAILED");
                    Sauceclient.jobFailed(testID);
                }
            }
            else{
                if(PassFailMonitor.wasSuccessful() && PassFailMonitor.errorCount() == 0)
                    logger.info("Test  "+ getTestName() + "  PASSED");
                else
                    logger.info("Test  "+ getTestName() + "  FAILED");
            }
            driver.quit();
        }
        if (zapStop) {
            if (autoIT.processExists(zapPID) != 0) {
                logger.info("Closing Zed Attack Proxy");
                autoIT.processClose(zapPID);
            }
        }

    }


    public static String searchForKey(String key)  /**searches for the given key in the messages and model files*/
    {
        String value = key;
        if (key == null)
        {
            return "";
        }
        value = Messages.getString(key);
        if (value.equals("!" + key + "!"))
        {
            value = Model.getString(key);
        }
        if (value.equals("!" + key + "!"))
        {
            value = key;
        }

        return value;
    }

    public static String replaceCharAtSpecifiedPosition(String s, int pos, char c) /**Replaces character c inside String s at the position pos*/
    {
        return s.substring(0, pos) + c + s.substring(pos + 1);
    }

   
    public static String getValueFromFile(String key, String file)
    {
        return buildFileMap(file).getProperty(key);

    }
    
    public static void setValueToFile(String key, String value, String File)
    {
        try{
            buildFileMap(File).setProperty(key, value);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
   
    public static Properties buildFileMap(String file)
    {
        Properties confiProps = new Properties();
        InputStream inputStream = Base.class.getClassLoader().getResourceAsStream(file);

        try
        {
            confiProps.load(inputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return confiProps;

    }
  
    protected String getTestDescription(String name) /** Returns the test description based on a test name*/
    {
        String filePath = "";
        String description = null;
        filePath = testDescription;
        try
        {
            description = getValueFromFile(name, filePath);
        }
        catch (Exception e)
        {
            description = "Description file not found!";
        }
        if (description == null)
        {
            description = "Test was not found in test description file!";
        }
        else
        {
            if (description.equals(""))
            {
                description = "Description for test is missing!";
            }
        }
        return description;

    }

    public static Connection getDBConnection(String hostName, String userName, String password) /** Returns the connection to database.*/
    {
        if (hostName == null)
        {
            hostName = getValueFromFile("db.oracle.host", DBproperties);
        }
        if (userName == null)
        {
            userName = getValueFromFile("db.oracle.username", DBproperties);
        }
        if (password == null)
        {
            password = getValueFromFile("db.oracle.password", DBproperties);
        }

        // The following parameters are loaded from db.properties file
        String url = "jdbc:oracle:thin:@" + hostName + ":" + getValueFromFile("db.oracle.port", DBproperties) + ":" + getValueFromFile("db.oracle.sid", DBproperties);
        logger.info(" Will try connecting to " + url);
        Connection con = null;
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(url, userName, password);
            logger.debug("Successfully connected to " + hostName + "'s  database");
        }
        catch (SQLException se)
        {
            // Report the problem to the standard error stream:
            System.err.println("Exception creating the database connection: " + se);
            logger.debug("Got Exception when trying to connect to " + hostName + "'s  database");
        }
        catch (ClassNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return con;
    }

    public static Connection getDBConnection()
    {
        return getDBConnection(null, null, null);
    }

    
    public static ResultSet getQueryResult(String query, Connection conn)   /**Utility method for executing a query against specified connection*/
    {
        Statement stmt = null;
        ResultSet result = null;
        try
        {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        }
        catch (SQLException e)
        {
            System.err.println(e);
        }

        return result;
    }

    public static void closeConnection(Connection conn)   /**Utility method for closing a database connection*/

    {
        if (conn != null)
        {
            try
            {
                conn.close();
                logger.debug("Successfuly closed connection to db");
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                logger.debug("Got exception when trying to close connection to db");
            }
        }
    }
    
    public static void AutoItInitialization() /**Loads AutoIT drivers*/
    {
    	autoIT = new AutoItX();
        String osArch = System.getProperty("os.arch");       
        System.out.println("AutoIT running on: " + osArch);

    }

    public void captureScreen(String... additionalName) {
        assert additionalName.length <= 1;
        String addName = additionalName.length > 0 ? additionalName[0] : "_";
        if (addName != "_") {
            addName = "_" + addName + "_";
        }
        String file = SeleniumUtils.generateScreenshotPath() + "/" + getName() + addName + System.currentTimeMillis() + ".jpg";
        SeleniumUtils.createFolder(SeleniumUtils.generateScreenshotPath());
        try {
            if (!typeOfBrowser.equals(safari)) {
                File capture = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(capture, new File(file));
            } else {
                // workaround to avoid safari bugs
                BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                ImageIO.write(image, "jpg", new File(file));
            }

            logger.debug("Screenshot captured: '" + file + "'");
        } catch (AWTException | HeadlessException | IOException | WebDriverException e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " captureScreen(): " + "\n" + commaSeparator + e);
        }
    }

    protected String throwableToString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        captureScreen();
        t.printStackTrace(pw);
        return sw.toString();
    }
    public void selectWindow(String windowID) /**
     * Moves to the provided window (all the future commands will be sent to the
     * provided window)
     */
    {
        try {
            driver.switchTo().window(windowID);

            logger.debug("Executed command " + commaSeparator + " selectWindow(" + windowID + ")");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " selectWindow(" + windowID + "): " + "\n" + commaSeparator + e);
            fail(throwableToString(e));
        }
    }

    public void switchToOtherWindow(String windowTitle){
        Set<String> windows = driver.getWindowHandles();
        for(String window: windows){
            driver.switchTo().window(window);
            System.out.println("Windows found: "+ window);
            if(driver.getTitle().contains(windowTitle)){
                System.out.println("The request window was found:" + window);
                return;
            }
        }
    }

    public void goBackInBrowser() {      // Navigates back - equivalent with Back button
        try {
            driver.navigate().back();
            logger.debug("Executed command " + commaSeparator + " goBackInBrowser()");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " goBackInBrowser(): " + "\n" + commaSeparator + e);
            fail(throwableToString(e));
        }
    }

    public static void quit() /**
     * Closes browser instance
     */
    {
        try {
            driver.quit();
            logger.debug("Executed command " + commaSeparator + " quit");
        } catch (Exception e) {
            logger.debug("Exception when trying to execute command " + commaSeparator + " quit: " + "\n" + commaSeparator + e);
        }
    }
}
