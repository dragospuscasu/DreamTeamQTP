/*
 * All rights reserved. This program and the accompanying materials 
 * can be made available only with Endava agreement.
 */

package com.EndavaJTest.Samples;

import core.TestVee2.Element;
import core.TestVee2.Page;
import org.junit.Test;

/**
 *
 * @author bclim
 */

public class LocatorsTestCase extends Page
{
    @Test
    public void testLocators() throws InterruptedException
    {
        Element locatorById                 = new Element("input_search");
        Element locatorByName               = new Element("s");
        Element locatorByXpath              = new Element("//*[@class='input-text2' and @name='s' and @value='caută în...']");
        Element locatorByCSS                = new Element("input#input_search.input-text2");
        Element locatorByClassName          = new Element("classname=input-text2");
        Element locatorByTagName            = new Element("tagname=iframe");
        Element locatorByLinkText           = new Element("link=Resigilate");
        Element locatorByPartialLinkText    = new Element("partiallink=Card");      
    }
}
