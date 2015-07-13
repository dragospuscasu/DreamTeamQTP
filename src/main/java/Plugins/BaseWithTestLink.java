package Plugins;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;
import br.eti.kinoshita.testlinkjavaapi.constants.TestLinkMethods;
import br.eti.kinoshita.testlinkjavaapi.constants.TestLinkParams;
import br.eti.kinoshita.testlinkjavaapi.model.ReportTCResultResponse;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;
import br.eti.kinoshita.testlinkjavaapi.util.Util;
import core.TestVee2.Base;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.*;
import org.junit.runners.model.FrameworkMethod;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.logging.Logger;


@SuppressWarnings("unused")
public class BaseWithTestLink extends Base {

    public static TestLinkAPI api;
    private static XmlRpcClient client;
    private static String devKey;
    private static Integer testPlanId;
    private Integer tPId = 0;

    private static Properties props = new Properties();
    private static String errorMessage; 
    private static Boolean isConnected = false;
    
    protected static final Logger log = Logger.getLogger("src\\testLinkReport.txt");

    private static class DefaultTrustManager implements X509TrustManager {
        
        //@Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        //@Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        //@Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
    
    @Rule
    public TestName testName = new TestName();
    
    public Integer getTestPlanId()
    {
        return tPId;
    }
    
    @Before
    public void setUp() throws Exception{
        super.setUp();
        log.info(" ____ Testink connection created ____");
        String profilePath = getValueFromFile("profilePath", "testlink.properties");
        URL url = null;

        try {
            String strUrl = getValueFromFile("Url", "testlink.properties");
            url = new URL(strUrl);
        } catch ( MalformedURLException mue ) {
            errorMessage = mue.getMessage();
            logger.info(errorMessage);
            return;
        } catch ( Exception e ) {
            errorMessage = "The 'Url' value is empty / null or missing in the 'testlink.properties' file.";
            logger.info(errorMessage);
            return;
        }

        try {
            devKey = getValueFromFile("DevKey", "testlink.properties");
        }
        catch(Exception e) {
            errorMessage = "The 'DevKey' value is empty / null or missing in the 'testlink.properties' file.";
            logger.info(errorMessage);
            return;
        }
        try {

            testPlanId = null;
        }
        catch (Throwable t) {
            errorMessage = "The 'TestPlanId' value is empty / null or missing in the 'testlink.properties' file."; 
            logger.info(errorMessage);
            return;
        }
                  
        try {

            // Accept all certified
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
            SSLContext.setDefault(ctx);
            
            // Create new TestLinkAPI object
            api = new TestLinkAPI(url, devKey);

            // Try to test method e.g. about()
            api.about();
            
            // Get XMLRpcClient object
            client = api.getXmlRpcClient();
             
            // Get latest build for test plan
            //lastBuild = api.getLatestBuildForTestPlan(testPlanId);
             
            isConnected = true;
        } catch (Exception e) {
            isConnected = false;
            errorMessage = e.getMessage();
            logger.info(errorMessage);
        }
    }
     
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        logger.info("$$$ Close TestLink connection");
        isConnected = false;
        api = null;
        errorMessage = null;
    }

    protected String setBuildName(){
        return "";
    }
    /**
     * Set result in test case
     * @param status Passed or Failed status
     * @param notes
     */
    protected void SetResult(ExecutionStatus status, String notes) {

        if (!isConnected) {
            logger.info(errorMessage);
            return;
        }
        
        try {

            Class<?> clazz = Class.forName(this.getClass().getName());
            Method method = clazz.getMethod(testName.getMethodName());
            
            // Check if TestLink annotation is not present or not 
            if (!method.isAnnotationPresent(TestLink.class)) {
                logger.info("Class: [" + clazz.getName() + "] Test Method: [" + method.getName() + "] Status: " + status.name() + " - @TestLink(testId=xyz) annotation is not present.");
                return;
            }
            
            // Get annotation from this method
            TestLink testCase = method.getAnnotation(TestLink.class);
                
            if (testCase.testId() > 0) {
                
                // Set result in test case
                try {
                    logger.info("Test Plan ID is: " + getTestPlanId());

                    reportTCResult(
                            testCase.testId(),      // testCaseId
                            null,                   // testCaseExternalId
getTestPlanId(),             // testPlanId
                            status,                 // status
                            null, //lastBuild.getId(),      // buildId
                            setBuildName(),    // buildName
                            notes,                  // notes
                            false,                  // guess
                            null,                   // bugId
                            0,                      // platformId
                            null,                   // platformName
                            null,                   // customFields
                            false                   // overwrite
                            );
                    
                    /*
                    String generateScreenshotPath = SeleniumUtils.generateScreenshotPath();
                    SeleniumUtils.createFolder(generateScreenshotPath);

                    DateFormat dateFormat = new SimpleDateFormat("HH_mm_ss");
                    String currentDate = dateFormat.format(new Date());

                    String browserTypeName = typeOfBrowser.replace("*", "");
                    //generateScreenshotPath = generateScreenshotPath + File.separator + browserTypeName + "_" + getName() + "_" + System.currentTimeMillis() + ".png";
                    generateScreenshotPath = generateScreenshotPath + File.separator + browserTypeName + "_" + getName() + "_" + currentDate + ".png";

                    try
                    {
                        if (!typeOfBrowser.equals("*firefox"))
                        {
                            selenium.captureScreenshot(generateScreenshotPath);
                        }
                        else
                        {
                            selenium.captureEntirePageScreenshot(generateScreenshotPath, "background=#CCC");
                        }

                        String screenShotUrl = generateScreenshotPath.replace("C:", serverHost);
                        screenShotUrl = screenShotUrl.replace("\\", "/");
                        logger.debug("Screenshot captured " + commaSeparator + " " + screenShotUrl);
                    }
                    catch (Exception e)
                    {
                        System.err.println(e);
                        logger.debug("Screenshot capture failed " + commaSeparator + " " + e);
                    }
                    
                    Attachment attachment =  api.uploadExecutionAttachment(
                            1, //executionId 
                            "Setting customer plan", //title 
                            "In this screen the attendant is defining the customer plan", //description  
                            "screenshot_customer_plan_"+System.currentTimeMillis()+".jpg", //fileName 
                            "image/jpeg", //fileType
                            fileContent); //content
                    */
                    
                    logger.info("Class: [" + clazz.getName() + "] Test Method: [" + method.getName() + "] TestId: [" + testCase.testId() + "] Status: " + status.name() + " - Test case result is updated successfully.");
                }
                catch (Throwable t) {
                    //t.printStackTrace(System.err);
                    logger.info("Class: [" + clazz.getName() + "] Test Method: [" + method.getName() + "] TestId: [" + testCase.testId() + "] Status: " + status.name() + " - Test case result isn't updated successfully. " + String.format(t.getMessage(), testCase.testId()));
                }
            } 
            else {
                logger.info("Class: [" + clazz.getName() + "] Test Method: [" + method.getName() + "] - test case Id can't be negative or null, please provide a valid test case Id.");
            }
        } 
        catch (Throwable t) {
            //t.printStackTrace(System.err);
            logger.info(t.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private ReportTCResultResponse reportTCResult(Integer testCaseId, Integer testCaseExternalId, Integer testPlanId, ExecutionStatus status, Integer buildId, String buildName,
        String notes, Boolean guess, String bugId, Integer platformId, String platformName, Map<String, String> customFields, Boolean overwrite) throws TestLinkAPIException {
        
        ReportTCResultResponse reportTCResultResponse = null;

        try {
            Map<String, Object> executionData = new HashMap<String, Object>();
            executionData.put(TestLinkParams.TEST_CASE_ID.toString(), testCaseId);
            executionData.put(TestLinkParams.TEST_CASE_EXTERNAL_ID.toString(), testCaseExternalId);
            executionData.put(TestLinkParams.TEST_PLAN_ID.toString(), testPlanId);
            executionData.put(TestLinkParams.STATUS.toString(), status.toString());
            executionData.put(TestLinkParams.BUILD_ID.toString(), buildId);
            executionData.put(TestLinkParams.BUILD_NAME.toString(), buildName);
            executionData.put(TestLinkParams.NOTES.toString(), notes);
            executionData.put(TestLinkParams.GUESS.toString(), guess);
            executionData.put(TestLinkParams.BUG_ID.toString(), bugId);
            executionData.put(TestLinkParams.PLATFORM_ID.toString(), platformId);
            executionData.put(TestLinkParams.PLATFORM_NAME.toString(), platformName);
            executionData.put(TestLinkParams.CUSTOM_FIELDS.toString(), customFields);
            //executionData.put(TestLinkParams.OVERWRITE.toString(), overwrite);
            
            Object response = executeXmlRpcCall(TestLinkMethods.REPORT_TC_RESULT.toString(), executionData);
            
            // the error verification routine is called inside 
            // super.executeXml...
            if (response instanceof Object[]) {
                Object[] responseArray = Util.castToArray(response);
                Map<String, Object> responseMap = (Map<String, Object>) responseArray[0];
            
                reportTCResultResponse = Util.getReportTCResultResponse(responseMap);
            }
        } catch (XmlRpcException xmlrpcex) {
            throw new TestLinkAPIException("Error reporting TC result: " + xmlrpcex.getMessage(), xmlrpcex);
        }

        return reportTCResultResponse;
    }
    
    private Object executeXmlRpcCall(String methodName, Map<String, Object> executionData) throws XmlRpcException, TestLinkAPIException {
        
        List<Object> params = new ArrayList<Object>();

        if (executionData != null) {
            if (executionData.get(TestLinkParams.DEV_KEY.toString()) == null) {
                executionData.put(TestLinkParams.DEV_KEY.toString(), devKey);
            }
            
            params.add(executionData);
        }

        final Object o = client.execute(methodName, params);
        checkResponseError(o);
        return o;
    }
     
    @SuppressWarnings("unchecked")
    private void checkResponseError(Object response) throws TestLinkAPIException { // may be an array of errors (IXError)
    
        if (response instanceof Object[]) {
            
            final Object[] responseArray = Util.castToArray(response);
            for (int i = 0; i < responseArray.length; i++) {
                Object maybeAMap = responseArray[i];
                // may be a map with error code and message
                if (maybeAMap instanceof Map<?, ?>) {
                    Map<String, Object> errorMap = (Map<String, Object>) maybeAMap;
                    Integer code = Util.getInteger(errorMap, "code");
                    String message = Util.getString(errorMap, "message");

                    if (code != null) {
                        throw new TestLinkAPIException(code, message);
                    }
                } // endif
            } // endfor
        } 
        else if (response instanceof Map<?, ?>) {
            final Map<String, Object> errorMap = (Map<String, Object>) response;
            final Integer statusOk = Util.getInteger(errorMap, "status_ok");
            final String message = Util.getString(errorMap, "msg");
            
            if (statusOk != null && statusOk.equals(FALSE_IN_PHP)) {
                throw new TestLinkAPIException(statusOk, message);
            }
        }
    }

    private static final Integer FALSE_IN_PHP = 0;

    @Rule
    public TestRule watchman = new TestWatcher() {


        public void failed(Throwable e, FrameworkMethod method) {
            SetResult(ExecutionStatus.FAILED, System.getenv("TESTENVIRONMENT") + System.getProperty("line.separator") +  e.getMessage());
        }


        public void succeeded(FrameworkMethod method) {
            SetResult(ExecutionStatus.PASSED, System.getenv("TESTENVIRONMENT") + System.getProperty("line.separator") + "Passed.");
        }
    };
}