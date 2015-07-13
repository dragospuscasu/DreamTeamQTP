package Plugins;

import core.TestVee2.Page;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author bclim
 * Please have a look on the information on top of JSRecordObject class
 */
public class PageWithNetRecord extends Page {

    protected final String harFolder = System.getProperty("user.dir") +"\\Har\\";
    
    @Override
    public void setUp() throws Exception{
        netExportOption = true;
        super.setUp();
    }
    
    @Override
    public void tearDown() throws Exception{
        super.tearDown();
        netExportOption = false;
 
    }
    
    public void setResults(String propname, String propvalue) {
        Properties prop = new Properties();
        OutputStream output = null;

        try {        
            output = new FileOutputStream("httparchive.properties");

            // set the properties value
            prop.setProperty(propname, propvalue);

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
