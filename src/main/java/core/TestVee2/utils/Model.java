package core.TestVee2.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author oion Class to handle model values from file
 */
public final class Model
{
    private static final String         BUNDLE_NAME_TEST = "com.endava.model";
    private static final ResourceBundle RESOURCE_BUNDLE  = ResourceBundle.getBundle(BUNDLE_NAME_TEST);

    private Model(){    }
    public static String getString(String key){
        try{
            return RESOURCE_BUNDLE.getString(key);
        }
        catch (MissingResourceException e){
            return '!' + key + '!';
        }
    }
}
