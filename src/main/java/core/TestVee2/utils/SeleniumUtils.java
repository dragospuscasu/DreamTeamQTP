package core.TestVee2.utils;

import core.TestVee2.Base;
import core.TestVee2.Page;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author oion Utility class for Selenium Contains static methods
 */
public final class SeleniumUtils extends Page
{

    /**
     */
    private SeleniumUtils()
    {
    }

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("selenium.utils"); //$NON-NLS-1$

    /**
     * @param dateF the date format. Could be: dd/MM/yyyy, d/M/yyyy, dd, MM,
     * yyyy
     * @return the date in requested format
     */
    public static String getCurrentDateInSpecifiedFormat(String dateF)
    {
        DateFormat dateFormat = new SimpleDateFormat(dateF);
        java.util.Date date = new java.util.Date();
        String dateStr = dateFormat.format(date);
        return dateStr;
    }

    /**
     * @param timeF the time format. (eg. "HH_mm_ss" )
     * @return the time in requested format
     */
    public static String getCurrentTimeInSpecifiedFormat(String timeF)
    {
        DateFormat timeFormat = new SimpleDateFormat(timeF);

        long ts = (new Date()).getTime();
        String timeStr = timeFormat.format(ts);
        return timeStr;
    }

    /**
     * @return generates the path for screenshot utility exception
     */
    public static String generateScreenshotPath()
    {
        return System.getProperty("user.dir") + "/SeleniumResults/Screenshot/" + getCurrentDateInSpecifiedFormat("yyyy_MM_dd");
    }

    /**
     * @return generates the path for screenshot utility
     */
    public static String generateHtmlSourcePath()
    {
        return System.getProperty("user.dir") + "/SeleniumResults/HtmlSource/" + getCurrentDateInSpecifiedFormat("yyyy_MM_dd");
    }

    /**
     * @return generates the path for screenshot utility exception
     */
    public static String generateRequestContentPath()
    {
        return System.getProperty("user.dir") + "/SeleniumResults/Requests/" + getCurrentDateInSpecifiedFormat("yyyy_MM_dd");
    }

    /**
     * @return generates the path for screenshot utility exception
     */
    public static String generateResultHtmlFolderPath()
    {
        return System.getProperty("user.dir") + "/SeleniumResults/Reports/" + getCurrentDateInSpecifiedFormat("yyyy_MM_dd");
    }

    /**
     * @return generates the path for screenshot utility exception
     */
    public static String generateResultHtmlFilePath()
    {
        return "/" + System.currentTimeMillis() + ".html";// + testName
    }

    /**
     * Copies a file from a location to another, using a 1KB buffer
     * @param srFile source file
     * @param dtFile destination file
     * @throws Exception if something goes wrong
     */
    public static void copyFile(String srFile, String dtFile) throws Exception
    {
        // try
        // {
        File f1 = new File(srFile);
        File f2 = new File(dtFile);
        OutputStream out;
        // For Append the file.
        // OutputStream out = new FileOutputStream(f2,true);
        // For Overwrite the file.
        try (InputStream in = new FileInputStream(f1)) {
            // For Append the file.
            // OutputStream out = new FileOutputStream(f2,true);
            // For Overwrite the file.
            out = new FileOutputStream(f2);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0)
            {
                out.write(buf, 0, len);
            }
        }
        out.close();
        logger.debug("Copied from " + srFile + " to " + dtFile);
    }

    /**
     * @param path the path if something goes wrong
     */
    public static void createFile(String path)
    {
        File file = new File(path);

        System.out.println(file.getPath());

        try
        {
            File dir = file.getParentFile();
            if (!file.exists())
            {
                dir.mkdirs();
            }
            file.createNewFile();
            // logger.debug("Created file " + path);

        }
        catch (IOException e)
        {
            e.printStackTrace();
            logger.debug("Got exception when trying to create file " + path);
        }
    }

    /**
     * @param path the path if something goes wrong
     */
    public static void createFolder(String path)
    {
        File file = new File(path);

        try
        {
            File dir = file.getParentFile();
            if (!file.exists())
            {
                dir.mkdirs();
            }
            file.mkdirs();
            // logger.debug("Created folder " + path);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.debug("Got exception when trying to create file " + path);
        }
    }

    /**
     * deletes specified file
     * @return the true is successfull
     * @param file the file to be deleted
     */
    public static boolean deleteFile(String file)
    {
        File f1 = new File(file);
        boolean success = false;
        if (f1.isDirectory())
        {
            String[] children = f1.list();
            for (String children1 : children) {
                File f2 = new File(f1, children1);
                success = f2.delete();
                if (!success)
                {
                    logger.debug("Unable to delete file " + f2);
                }
            }
            success = f1.delete();
            if (!success)
            {
                logger.debug("Unable to delete file " + f1);
            }
        }

        if (!f1.isDirectory())
        {
            success = f1.delete();
            logger.debug("File successfully deleted");
            if (!success)
            {
                logger.debug("Unable to delete file " + f1);
            }
        }
        return success;
    }

    /**
     * Delete all the contend from a folder
     * @param path the path to Folder
     */
    public static void deleteFolderContent(String path)
    {
        File f1 = new File(path);
        if (f1.exists())
        {
            Stack<File> dirStack = new Stack<>();
            dirStack.push(f1);

            boolean containsSubFolder;
            while (!dirStack.isEmpty())
            {
                File currDir = dirStack.peek();
                containsSubFolder = false;

                String[] fileArray = currDir.list();
                for (String fileArray1 : fileArray) {
                    String fileName = currDir.getAbsolutePath() + File.separator + fileArray1;
                    File file = new File(fileName);
                    if (file.isDirectory())
                    {
                        dirStack.push(file);
                        containsSubFolder = true;
                    }
                    else
                    {
                        file.delete();
                    }
                }

                if (!containsSubFolder)
                {
                    dirStack.pop();
                    currDir.delete();
                }
            }
        }
    }

    /**
     * Utility method that writes the source of a request to specified file
     * @throws Exception exception
     * @param requestContent the request content
     */
    protected void writeRequestContentToFile(String requestContent) throws Exception
    {
        String folder = SeleniumUtils.generateRequestContentPath();
        SeleniumUtils.createFolder(folder);
        String filePath = folder + "/" + getName() + "_" + SeleniumUtils.getXpathRoot(requestContent) + "_" + System.currentTimeMillis() + ".xml";
        // SeleniumUtils.createFile(filePath);
        File requestFile = new File(filePath);
        try
        {
            FileUtils.writeStringToFile(requestFile, requestContent);
            logger.debug("Saved request to file");
        }
        catch (IOException e)
        {
            logger.debug("Exception when trying to execute command " + commaSeparator + " writeRequestContentToFile: " + "\n" + commaSeparator + e);
        }

    }

    /**
     * @param inputDate inputDate
     * @param dateF dateF Letter Date or Time Component Presentation Examples G
     * Era designator Text AD y Year Year 1996; 96 M Month in year Month July;
     * Jul; 07 w Week in year Number 27 W Week in month Number 2 D Day in year
     * Number 189 d Day in month Number 10 F Day of week in month Number 2 E Day
     * in week Text Tuesday; Tue a Am/pm marker Text PM H Hour in day (0-23)
     * Number 0 k Hour in day (1-24) Number 24 K Hour in am/pm (0-11) Number 0 h
     * Hour in am/pm (1-12) Number 12 m Minute in hour Number 30 s Second in
     * minute Number 55 S Millisecond Number 978 z Time zone General time zone
     * Pacific Standard Time; PST; GMT-08:00 Z Time zone RFC 822 time zone -0800
     * @return the date in requested format
     */
    public static String resolveDateinSpecifiedFormat(String inputDate, String dateF)
    {
        DateFormat dateFormat = new SimpleDateFormat(dateF, Locale.ENGLISH);
        @SuppressWarnings("deprecation")
        java.util.Date date = new java.util.Date(inputDate);
        return dateFormat.format(date);
    }

    /**
     * @param dateDay dateDay
     * @return the extension for day
     */
    public static String resolveExtensionForDay(String dateDay)
    {
        String extension = "th";
        if (dateDay.equals("01") || dateDay.equals("21") || dateDay.equals("31"))
        {
            extension = "st";
        }
        if (dateDay.equals("02") || dateDay.equals("22"))
        {
            extension = "nd";
        }
        if (dateDay.equals("03") || dateDay.equals("23"))
        {
            extension = "rd";
        }
        return extension;
    }

    /**
     * @param date the date with extension. eg: 28th March 2010
     * @return the date without extension
     */
    public static String removeExtensionFromDate(String date)
    {
        int temp = 0;
        try
        {
            temp = new Integer(date.substring(0, 2));
        }
        catch (NumberFormatException e)
        {
            // e.printStackTrace();
            logger.debug("Date is less than 10th of month.. getting only one char");
            temp = new Integer(date.substring(0, 1));
        }

        if (temp >= 10)
        {
            date = date.substring(0, 2) + date.substring(4, date.length());
        }
        else
        {
            date = date.substring(0, 1) + date.substring(3, date.length());
        }
        return date;

    }

    /**
     * @param requestContent requestContent
     * @return the XpathRoot - the service
     * @throws ParserConfigurationException ParserConfigurationException
     * @throws SAXException SAXException
     * @throws IOException IOException
     * @throws XPathExpressionException XPathExpressionException
     */
    public static String getXpathRoot(String requestContent) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException
    {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(requestContent));

        Document doc = db.parse(is);
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr = xpath.compile("local-name(/*)");
        return expr.evaluate(doc, XPathConstants.STRING).toString();

    }

    /**
     * Compares the request values from file with the one got from browser
     * @param xmlString xmlString
     * @param pathToRequestMatch pathToRequestMatch
     */
    public void compareTheSpecifiedRequest(String xmlString, String pathToRequestMatch)
    {

        HashMap<String, Object> file = new HashMap<>();

        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(new File(Base.class.getClassLoader().getResource(pathToRequestMatch).toString().substring("file:/".length()))));

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                file.put(line.split("=")[0], line.split("=")[1]);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }

        parseXMLAndMatch(xmlString, file);

    }

    /**
     * Parses the xml and compares the values from morethan with the values from
     * xmlString
     * @param xmlString xmlString
     * @param valuesToMatch valuesToMatch
     */
    public void parseXMLAndMatch(String xmlString, Map<String, Object> valuesToMatch)
    {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlString));

            Document doc = db.parse(is);

            Set<String> keySet = valuesToMatch.keySet();

            for (String s : keySet)
            {
                logger.debug("Getting: " + s);
                NodeList nodes = doc.getElementsByTagName(s);
                int items = nodes.getLength();
                logger.debug("Nodes length = " + items);
                boolean condition = false;
                for (int i = 0; i < items; i++)
                {
                    Element element = (Element) nodes.item(i);
                    if (s.equalsIgnoreCase("ns36:PolicyNumber"))
                    {
                        logger.debug("Expected: " + valuesToMatch.get(s) + ";Actual: " + getDataFromElement(element));
                    }
                    logger.debug("Expected: " + valuesToMatch.get(s) + ";Actual: " + getDataFromElement(element));
                    condition = condition || valuesToMatch.get(s).equals(getDataFromElement(element));
                }
                verifyTrue(condition);
            }

        }
        catch (IOException | ParserConfigurationException | SAXException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Parses the xml and returns the values for specified node
     * @param xmlString xmlString
     * @param node the path to xml node
     * @return the data for specified xml node
     */
    public ArrayList<String> returnXmlValuesForNode(String xmlString, String node)
    {
        ArrayList<String> values = new ArrayList<>();
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlString));

            Document doc = db.parse(is);

            logger.debug("Getting: " + node);
            NodeList nodes = doc.getElementsByTagName(node);
            int items = nodes.getLength();
            for (int i = 0; i < items; i++)
            {
                Element element = (Element) nodes.item(i);
                values.add(getDataFromElement(element));
            }
        }
        catch (IOException | ParserConfigurationException | SAXException e)
        {
            System.err.println(e);
        }
        return values;

    }

    /**
     * Parses the xml and returns the value for specified node
     * @param xmlString xmlString
     * @param node the path to xml node
     * @param value the value to be compared with
     */
    public void verifyXmlValueForNode(String xmlString, String node, String value)
    {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlString));

            Document doc = db.parse(is);

            logger.debug("Getting: " + node);
            NodeList nodes = doc.getElementsByTagName(node);
            int items = nodes.getLength();
            logger.debug("Nodes length = " + items);
            boolean condition = false;
            for (int i = 0; i < items; i++)
            {
                Element element = (Element) nodes.item(i);
                logger.debug("Expected: " + value + "=Actual: " + getDataFromElement(element));
                condition = condition || value.equals(getDataFromElement(element));
            }
            verifyTrue(condition);
        }
        catch (IOException | ParserConfigurationException | SAXException e)
        {
            System.err.println(e);
        }

    }

    /**
     * Parses the xml and returns the value for specified node
     * @param requestContent xmlString
     * @return the data for specified xml node
     */
    public ArrayList<String> returnAllNodesForXmlString(String requestContent)
    {
        ArrayList<String> nodeNames = new ArrayList<>();
        try
        {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(requestContent));
            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName("*");
            for (int i = 0; i < nodes.getLength(); i++)
            {
                try
                {
                    // Get element
                    Element element = (Element) nodes.item(i);
                    nodeNames.add(element.getNodeName());
                    logger.debug("Node: ," + element.getNodeName() + ",Value: ," + getDataFromElement(element));

                }
                catch (Exception e)
                {
                    System.err.println(e);
                }
            }

        }
        catch (IOException | ParserConfigurationException | SAXException e)
        {
            e.printStackTrace();
        }
        return nodeNames;

    }

    /**
     * Parses the xml and returns the value for specified node
     * @param requestContent xmlString
     * @return the data for specified xml node
     */
    public ArrayList<String> returnAllXpathNodesForXmlString(String requestContent)
    {
        ArrayList<String> absolutPaths = new ArrayList<>();
        try
        {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(requestContent));
            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName("*");
            for (int i = 0; i < nodes.getLength(); i++)
            {
                try
                {
                    Element element = (Element) nodes.item(i);
                    String absolutePath = element.getNodeName();
                    Node e = element;
                    for (int j = 0; j < 100; j++)
                    {

                        if (e.getParentNode() != null)
                        {
                            Node p = e.getParentNode();

                            if (!e.getNodeName().equalsIgnoreCase((p.getNodeName())) && !p.getNodeName().equalsIgnoreCase("#document"))
                            {
                                e = p;
                                absolutePath = e.getNodeName() + "/" + absolutePath;
                            }
                            else
                            {
                                break;
                            }
                        }
                        else
                        {
                            break;
                        }

                    }
                    absolutePath = absolutePath + "/text()";
                    absolutPaths.add(absolutePath);
                    logger.debug(absolutePath);
                    try
                    {
                        logger.debug(returnValueForSpecifiedXpath(requestContent, absolutePath));
                    }
                    catch (Exception e2)
                    {
                        System.err.println(e2);
                    }
                }
                catch (Exception e)
                {
                    System.err.println(e);
                }
            }

        }
        catch (IOException | ParserConfigurationException | SAXException e)
        {
            e.printStackTrace();
        }
        return absolutPaths;

    }

    /**
     * @param e the element
     * @return the data for specified element
     */
    public static String getDataFromElement(Element e)
    {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData)
        {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "?";
    }

    /**
     * @param requestContent requestContent
     * @param specifiedXpath specifiedXpath
     * @return the value for specified xpath
     * @throws Exception e
     */
    public static String returnValueForSpecifiedXpath(String requestContent, String specifiedXpath) throws Exception
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(requestContent));
        Document doc = db.parse(is);
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr = xpath.compile(specifiedXpath);
        return expr.evaluate(doc, XPathConstants.STRING).toString();
    }

    /**
     * Checks for HTTP authentication dialog and handles it
     * @param user
     * @param password
     */
    public static boolean handleAuthenticationDialog(String user, String password)
    {
        
        String caption = SeleniumUtils.getAuthCaption();

        if (autoIT.winExists(caption))
        {
            autoIT.winActivate(caption);
            autoIT.send(user);
            autoIT.send("{TAB}", false);
            autoIT.send(password);
            autoIT.send("{ENTER}", false);

            return true;
        }
        else
        {
            return false;
        }

    }

    public static String getAuthCaption()
    {
        String caption = "";
        if (typeOfBrowser.equals("*firefox"))
        {
            caption = "Authentication Required";
        }
        if (typeOfBrowser.equals("*iexplore"))
        {
            caption = "Windows Security";
            if (!autoIT.winExists(caption))
            {
                // on ie6 the security window has a different name
                autoIT.autoItSetOption("WinTitleMatchMode", "2");
                caption = "Connect to ";
            }
        }
        if (typeOfBrowser.equals("*googlechrome") || typeOfBrowser.equals("*opera"))
        {
            // not identifiable dialogs, blind field completion to be performed
            // watch focus loose cases
            sleep(3000); // safe time needed for authentication dialog to appear
        }
        if (typeOfBrowser.equals("*safari"))
        {
            sleep(3000); // safe time needed for authentication dialog to appear
            // a safer approach using winapi and windows identification by owner
            // can be done in future
            // watch focus loose cases
            caption = autoIT.winGetHandle("ACTIVE");
        }
        return caption;
    }

    /**
     * Utility method for file upload, triggers and handles Upload dialog
     * @param path - full path of the file, extension included
     */
    public static void handleUploadDialog(String path)
    {
        AutoItInitialization();

        // generates dialog to appear
        // simple click is not enough
        // fireevent is not supported in webdriver
        autoIT.send("{SPACE}", false);

        String caption = "";

        if (typeOfBrowser.equals("*firefox"))
        {
            caption = "File Upload";
        }
        if (typeOfBrowser.equals("*iexplore"))
        {
            caption = "Choose File to Upload";
        }
        if (typeOfBrowser.equals("*googlechrome") || typeOfBrowser.equals("*opera"))
        {
            caption = "Open";
        }
        if (typeOfBrowser.equals("*safari"))
        {
            caption = "Upload file";
        }

        autoIT.winActivate(caption);
        autoIT.winWaitActive(caption);
        autoIT.ControlSetText(caption, "", "[CLASS:Edit; INSTANCE:1]", path);
        autoIT.controlClick(caption, "", "&Open");
    }

    /**
     * Returns weather the string is a number or not
     * @param text string to be tested
     * @return result
     */
    public static boolean isNumeric(String text)
    {
        try
        {
            Double.parseDouble(text);
        }
        catch (NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    /**
     * Trim a string
     * @param original untrimmed string
     * @return trimmed string
     */
    public static String trimString(String original)
    {
        return original.replaceAll("^\\s+|\\s+$", "");
    }
}
