package core.TestVee2.RequestTesting;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils
{
    /**
     * Loads the provided file into Properties structure
     * @param pathToProps
     * @return Properties
     */
    public static Properties loadProperties(String pathToProps)
    {
        Properties tmpProps = new Properties();
        try
        {
            String fileContent = Files.toString(new File(pathToProps), Charsets.UTF_8);
            InputStream is = IOUtils.toInputStream(fileContent, "UTF-8");

            tmpProps.load(is);

            return tmpProps;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

    }
}
