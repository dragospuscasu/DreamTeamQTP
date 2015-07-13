
package Plugins;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author bclim
 * Before using this method make sure you have modified the Firefox profile to a new one  
 * which needs to be added in automation.properties, after this please install inside the new profile install firebug + netExport addons
 */
public class JavaScriptNetExport extends PageWithNetRecord {

    private String harFileName;
    private int fileCount = 1;
    private final String webSite;
    public String siteTitle = getTitle();
    public String logfile;

    public JavaScriptNetExport(String webSite) throws IOException //  harFileName to be added in constructor
    {
        // when adding a website please avoid the lats / from the end of the URL, preferred URL: http://www.something.co.uk
        this.webSite = webSite.substring(webSite.lastIndexOf("/") + 1);
        sleep(1000);
       // open(webSite);

    }
  
    
    public void renameFile(final String newName) throws IOException {
        System.out.println("start renaming file");
        java.io.File workingDirectory = new java.io.File(harFolder);
        //Use an anonymous inner class
        java.io.File[] listOfFiles = workingDirectory.listFiles(new java.io.FilenameFilter() {
            public boolean accept(File rootDir, String name) {
                return name.contains(webSite);            
            }
        });
        for (File aFile : listOfFiles) {
            //...Do something with the file, including renaming it...
            aFile.renameTo(new File(harFolder + newName + ".har"));
        }
        sleep(1000);

        System.out.println("end renaming file");
        harFileName = newName;
    }

    public void waitforHarFile() {
        logger.debug("Waiting for har to be generated");
        while (new File(harFolder).listFiles().length < fileCount) {
            sleep(200);
        }
        sleep(1000);
        fileCount++;      
    }

                                                                                    // dcs.gif?&dcsdat
                                                                                   // data.de.coremetrics.com
                                                                                   //
    public void searchInsideCoreMetrics(String what) throws ParseException, org.json.simple.parser.ParseException {
        try {
            System.out.println("Reading " + harFolder + "\\" + harFileName + ".har");
            try (FileReader reader = new FileReader(harFolder + harFileName + ".har")) {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
                JSONObject object = (JSONObject) jsonObject.get("log");
                JSONArray entries = (JSONArray) object.get("entries");            // the search is done inside the entries

                // take the elements of the json array
                String myURL = "URL not found";
                for (Object entrie : entries) {
                    JSONObject entry = (JSONObject) entrie;
                    JSONObject request = (JSONObject) entry.get("request");        // inside an entrie is there any request ?
                    String url = (String) request.get("url");                       // inside request should be an url:
                    if (url.contains(what) && url.contains("data.de.coremetrics.com")) {
                        myURL = url;
                        System.out.println("Information searched:  " + what + "  was found in:");
                        System.out.println(myURL);
                        //System.out.println(request);
                        verifyTrue(url.contains(what));
                    }
                }
                if (myURL.equals("URL not found")) {
                    verifyTrue(myURL.contains(what));
                    logger.info(" >>>>>>>>>> The information searched: " + what + " was not found!  <<<<<<<<<<<");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("IO exception during test");
        }
    }
    
    public void searchInsideWebTrends(String what) throws ParseException, org.json.simple.parser.ParseException {
        try {
            System.out.println("Reading " + harFolder + "\\" + harFileName + ".har");
            try (FileReader reader = new FileReader(harFolder + harFileName + ".har")) {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
                JSONObject object = (JSONObject) jsonObject.get("log");
                JSONArray entries = (JSONArray) object.get("entries");            // the search is done inside the entries

                // take the elements of the json array
                String myURL = "URL not found";
                for (Object entrie : entries) {
                    JSONObject entry = (JSONObject) entrie;
                    JSONObject request = (JSONObject) entry.get("request");        // inside an entrie is there any request ?
                    String url = (String) request.get("url");                       // inside request should be an url:
                    if (url.contains(what) && url.contains("dcs.gif?&dcsdat")) {
                        myURL = url;
                        System.out.println("Information searched:  " + what + "  was found in:");
                        System.out.println(myURL);
                        //System.out.println(request);
                        verifyTrue(url.contains(what));
                    }
                }
                if (myURL.equals("URL not found")) {
                    verifyTrue(myURL.contains(what));
                    logger.info(" >>>>>>>>>> The information searched: " + what + " was not found!  <<<<<<<<<<<");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("IO exception during test");
        }
    }
    
    
    public void searchInsideAds(String Resolution, String what) throws ParseException, org.json.simple.parser.ParseException {
        try {
            System.out.println("Reading " + harFolder + "\\" + harFileName + ".har");
            try (FileReader reader = new FileReader(harFolder + harFileName + ".har")) {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
                JSONObject object = (JSONObject) jsonObject.get("log");
                JSONArray entries = (JSONArray) object.get("entries");            // the search is done inside the entries
                // take the elements of the json array
                String myURL = "URL not found";
                for (Object entrie : entries) {
                    JSONObject entry = (JSONObject) entrie;
                    JSONObject request = (JSONObject) entry.get("request");        // inside an entrie is there any request ?
                    String url = (String) request.get("url");                       // inside request should be an url:
                    if (url.contains(what) && url.contains("doubleclick.net") && url.contains(Resolution)) {
                        myURL = url;
                        System.out.println("Information searched:  " + what + "  was found in:");
                        System.out.println(myURL);
                        //System.out.println(request);
                        verifyTrue(url.contains(what));
                    }
                }
                if (myURL.equals("URL not found")) {
                    verifyTrue(myURL.contains(what));
                    logger.info(" >>>>>>>>>> The information searched for resolution"+Resolution+": " + what + " was not found!  <<<<<<<<<<<");
               
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("IO exception during test");
        }
    }
    
    public void searchInsideAdsWithRegex(String Resolution, String what) throws ParseException, org.json.simple.parser.ParseException {
        try {
            System.out.println("Reading " + harFolder + "\\" + harFileName + ".har");
            try (FileReader reader = new FileReader(harFolder + harFileName + ".har")) {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
                JSONObject object = (JSONObject) jsonObject.get("log");
                JSONArray entries = (JSONArray) object.get("entries");            // the search is done inside the entries
                Pattern patern = Pattern.compile(what);
                Matcher match;
                // take the elements of the json array
                String myURL = "URL not found";
                for (Object entrie : entries) {
                    JSONObject entry = (JSONObject) entrie;
                    JSONObject request = (JSONObject) entry.get("request");        // inside an entrie is there any request ?
                    String url = (String) request.get("url");                       // inside request should be an url:
                    match  = patern.matcher(url);
                    if (match.find() && url.contains("doubleclick.net") && url.contains(Resolution)) {
                        myURL = url;
                        System.out.println("Information searched:  " + what + "  was found in:");
                        System.out.println(myURL);
                        //System.out.println(request);
                        verifyTrue(true);
                    }
                }
                match = patern.matcher(myURL);
                if (myURL.equals("URL not found")) {
                    verifyTrue(match.find());
                    logger.info(" >>>>>>>>>> The information searched: " + what + " was not found!  <<<<<<<<<<<");
             
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("IO exception during test");
        }
    }

    public void CleanUp() throws IOException {
        FileUtils.cleanDirectory(new File(harFolder));
        fileCount = 1;
        System.out.println("The har files have been deleted.");
        // File f = new File(harFolder);       
        // f.delete();
    }

    
    // Methods for manipulating diferent request headers:
    
    
    public String generateWTti()
    {
        String titlemodified = siteTitle.replace(" ", "%20");
        return "WT.ti="+titlemodified;
    }
    
    public String generateUL()
    {
        String link = driver.getCurrentUrl();
        String ul = link.replace(":", "%3A").replace("/", "%2F");
        
        return ul;
    }
    public void GeneralLocalSitesMPUCheck(String resolution) throws ParseException, org.json.simple.parser.ParseException
    {
        searchInsideAds(resolution, "N3948");
        searchInsideAdsWithRegex(resolution, "(pgid=)(\\d+)");
        searchInsideAds(resolution, "tile=");
        searchInsideAds(resolution, "sz="+resolution);
        searchInsideAds(resolution, "weathertoday=");
        searchInsideAdsWithRegex(resolution, "weathertoday=\\w+");
        searchInsideAds(resolution, "weathertomorrow=");
        searchInsideAds(resolution, "ord=");
        searchInsideAdsWithRegex(resolution, "ord=\\d+");
        
    }
    public void GeneralMobileSitesMPUCheck(String resolution) throws ParseException, org.json.simple.parser.ParseException
    {
        searchInsideAds(resolution, "N3948");
        searchInsideAdsWithRegex(resolution, "(pgid=)(\\d+)");
        searchInsideAds(resolution, "tile=");
        searchInsideAds(resolution, "sz="+resolution);
        searchInsideAds(resolution, "ord=");
        searchInsideAdsWithRegex(resolution, "ord=\\d+");    
    }
    
    public void GeneralWOW247MPUCheck(String resolution) throws ParseException, org.json.simple.parser.ParseException{
        searchInsideAds(resolution, "N3948");
        searchInsideAds(resolution, "sz="+resolution);
        searchInsideAds(resolution, "rsi=D08734_71868");
        searchInsideAds(resolution, "rsi=D08737_10178");
        searchInsideAds(resolution, "rsi=D08737_0");
        searchInsideAds(resolution, "ord=");
        searchInsideAdsWithRegex(resolution, "(\\d+)(.jp)");
    }
    
    public void GeneralCoremetricsCheck()
    {
      //  searchInsideCoreMetrics(grid);
    }
}
