
package com.EndavaJTest.Samples;

import core.TestVee2.Page;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author bclim
 */
@RunWith(JUnitParamsRunner.class)
public class ParameterizedTestSample extends Page {

    @Test
    @FileParameters("data.csv")
    public void testWebsites(String webSite) throws Exception {    // will be run for each website mentioned in site.csv        
       open(webSite);
    }
}
