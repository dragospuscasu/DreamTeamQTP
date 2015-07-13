/*
 * All rights reserved. This program and the accompanying materials 
 * can be made available only with Endava agreement.
 */

package BDD;

import core.TestVee2.Page;
import cucumber.api.CucumberOptions;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 *
 * @author bclim
 */
@RunWith(Cucumber.class)
@CucumberOptions(format = {"pretty","html:reports/test-report"})
public class BDDRunner extends Page {

    @Before
    public void setUp() throws Exception { super.setUp(); }
    @After
    public void tearDown() throws Exception { super.tearDown(); }
}
