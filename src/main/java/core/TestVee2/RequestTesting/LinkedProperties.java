package core.TestVee2.RequestTesting;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;

/**
 * Extension of Properties class, keeps the key/value pairs in the order they
 * were added
 * @author icernopolc
 */
@SuppressWarnings("serial")
public class LinkedProperties extends Properties
{

    private final LinkedHashSet<Object> keys = new LinkedHashSet<>();

    public Enumeration<Object> keys()
    {
        return Collections.<Object> enumeration(keys);
    }

    public Object put(Object key, Object value)
    {
        keys.add(key);
        return super.put(key, value);
    }
}
