/*
 * Copyright 2005-2010 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.award.htmlunitwebtest;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;





/**
 * 
 * This is the integration test for Award Time and Money Page. 
 */
public class AwardTimeAndMoneyWebTest extends AwardWebTestBase{
    
    protected static final String TIME_AND_MONEY_LINK_NAME = "timeAndMoney.x";
    protected static final String TIME_AND_MONEY_BUTTON_NAME = "methodToCall.timeAndMoney";
    protected static final String RETURN_TO_AWARD_BUTTON_NAME = "methodToCall.returnToAward";
    HtmlPage awardTimeAndMoneyPage;
    HtmlPage tempAwardPage;
    
    /**
     * The set up method calls the parent super method and gets the 
     * award Time and Money page after that.
     * @see org.kuali.kra.award.htmlunitwebtest.AwardWebTestBase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        awardTimeAndMoneyPage = clickOn(getAwardHomePage(), TIME_AND_MONEY_BUTTON_NAME);
    }

    /**
     * This method calls parent tear down method and than sets awardTimeAndMoneyPage to null
     * @see org.kuali.kra.award.htmlunitwebtest.AwardWebTestBase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        awardTimeAndMoneyPage = null;
        tempAwardPage = null;
    }
    
    /**
     * 
     * Get the Award Time & Money Web Page. To do this, we first
     * get the Award Home page and fill in the required
     * fields with some default values.  We can then navigate to the
     * Award Time & Money Web Page.
     * @return
     * @throws Exception
     */
    protected HtmlPage getAwardTimeAndMoneyPage() throws Exception {
        HtmlPage awardHomePage = this.getAwardHomePage();
        HtmlPage awardTimeAndMoneyPage = clickOn(awardHomePage, TIME_AND_MONEY_BUTTON_NAME);
        return awardTimeAndMoneyPage;
    }
    
    @Test
    public void testReturnToAward() throws Exception{
        tempAwardPage = clickOn(awardTimeAndMoneyPage,RETURN_TO_AWARD_BUTTON_NAME);
        assertDoesNotContain(tempAwardPage, ERROR_TABLE_OR_VIEW_DOES_NOT_EXIST);
        assertDoesNotContain(tempAwardPage, ERRORS_FOUND_ON_PAGE);
        assertContains(tempAwardPage, DEFAULT_DOCUMENT_DESCRIPTION);
    }
    
    @Test
    public void testTimeAndMoneyHomePage() throws Exception{
        assertDoesNotContain(awardTimeAndMoneyPage, ERROR_TABLE_OR_VIEW_DOES_NOT_EXIST);
        assertDoesNotContain(awardTimeAndMoneyPage, ERRORS_FOUND_ON_PAGE);
        awardTimeAndMoneyPage = clickOn(awardTimeAndMoneyPage, SAVE_PAGE);
        assertDoesNotContain(awardTimeAndMoneyPage, ERROR_TABLE_OR_VIEW_DOES_NOT_EXIST);
        assertDoesNotContain(awardTimeAndMoneyPage, ERRORS_FOUND_ON_PAGE);
    }
    
    
}
