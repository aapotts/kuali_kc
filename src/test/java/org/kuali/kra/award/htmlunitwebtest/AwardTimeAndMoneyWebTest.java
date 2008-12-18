/*
 * Copyright 2006-2008 The Kuali Foundation
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

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 
 * This is the integration test for Award Time and Money Page. 
 */
@SuppressWarnings("unchecked")
public class AwardTimeAndMoneyWebTest extends AwardWebTestBase{
    
    HtmlPage awardTimeAndMoneyPage;

    /**
     * The set up method calls the parent super method and gets the 
     * award Time and Money page after that.
     * @see org.kuali.kra.award.htmlunitwebtest.AwardWebTestBase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        awardTimeAndMoneyPage = getAwardTimeAndMoneyPage();
    }

    /**
     * This method calls parent tear down method and than sets awardTimeAndMoneyPage to null
     * @see org.kuali.kra.award.htmlunitwebtest.AwardWebTestBase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        awardTimeAndMoneyPage = null;
    }
    
    /**
     * 
     * This method tests the adding of 2 F & A Rates (on and off campus) 
     * and saving them.  
     * @throws Exception
     */
    @Test
    public void testAwardFandaRatePanelAddFandaRate() throws Exception{
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.applicableFandaRate", "5");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.fandaRateTypeCode", "1");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.fiscalYear", "2008");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.startDate", "07/01/2007");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.endDate", "06/30/2008");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.onCampusFlag", "N");
        
        final HtmlForm form1 = (HtmlForm) awardTimeAndMoneyPage.getForms().get(0);        
        String completeButtonName1=getImageTagName(awardTimeAndMoneyPage, "methodToCall.addFandaRate");        
        final HtmlImageInput button1 = (HtmlImageInput) form1.getInputByName(completeButtonName1);
        HtmlPage awardTimeAndMoneyPageAfterAddingOnCampus = (HtmlPage) button1.click();
        
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.applicableFandaRate", "5");
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.fandaRateTypeCode", "1");
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.fiscalYear", "2008");
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.startDate", "07/01/2007");
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.endDate", "06/30/2008");
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.onCampusFlag", "F");
        
        final HtmlForm form2 = (HtmlForm) awardTimeAndMoneyPageAfterAddingOnCampus.getForms().get(0);        
        String completeButtonName2=getImageTagName(awardTimeAndMoneyPageAfterAddingOnCampus, "methodToCall.addFandaRate");        
        final HtmlImageInput button2 = (HtmlImageInput) form2.getInputByName(completeButtonName2);
        HtmlPage awardTimeAndMoneyPageAfterAddingOnCampusAndOffCampus = (HtmlPage) button2.click();
        
        HtmlPage awardTimeAndMoneyPageAfterSave = clickOn(awardTimeAndMoneyPageAfterAddingOnCampusAndOffCampus, "methodToCall.save");
        assertDoesNotContain(awardTimeAndMoneyPageAfterSave, ERRORS_FOUND_ON_PAGE);
        assertContains(awardTimeAndMoneyPageAfterSave,SAVE_SUCCESS_MESSAGE);        
    }
    
    /**
     * 
     * This method tests the business rule that says
     * F&A rates must be input in pairs(both on and off campus rates).
     * 
     * @throws Exception
     */
    @Test
    public void testAwardFandaRatePanelAddFandaRateOnlyOnCampus() throws Exception{
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.applicableFandaRate", "5");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.fandaRateTypeCode", "1");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.fiscalYear", "2008");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.startDate", "07/01/2007");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.endDate", "06/30/2008");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.onCampusFlag", "N");
        
        final HtmlForm form1 = (HtmlForm) awardTimeAndMoneyPage.getForms().get(0);        
        String completeButtonName1=getImageTagName(awardTimeAndMoneyPage, "methodToCall.addFandaRate");        
        final HtmlImageInput button1 = (HtmlImageInput) form1.getInputByName(completeButtonName1);
        HtmlPage awardTimeAndMoneyPageAfterAddingOnCampus = (HtmlPage) button1.click();
        
        HtmlPage awardTimeAndMoneyPageAfterSave = clickOn(awardTimeAndMoneyPageAfterAddingOnCampus, "methodToCall.save");
        assertDoesNotContain(awardTimeAndMoneyPageAfterSave, SAVE_SUCCESS_MESSAGE);
        assertContains(awardTimeAndMoneyPageAfterSave,ERRORS_FOUND_ON_PAGE);
        assertContains(awardTimeAndMoneyPageAfterSave,"F&A rates must be input in pairs(both on and off campus rates).");
        
    }
    
    /**
     * 
     * This method...
     * @param page
     * @param uniqueNamePrefix
     * @return
     */
    protected String getImageTagName(HtmlPage page, String uniqueNamePrefix) {
        int idx1 = page.asXml().indexOf(uniqueNamePrefix);        
        int idx2 = page.asXml().indexOf("\"", idx1);
        return page.asXml().substring(idx1, idx2).replace("&amp;", "&").replace("((&lt;&gt;))", "((<>))");
    }
    
    /**
     * 
     * This method tests deletion of F & A Rate from the Award Time & Money page.
     * @throws Exception
     */
    @Test
    public void testAwardFandaRatePanelDeleteFandaRate() throws Exception{
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.applicableFandaRate", "5");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.fandaRateTypeCode", "1");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.fiscalYear", "2008");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.startDate", "07/01/2007");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.endDate", "06/30/2008");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.onCampusFlag", "N");
        
        final HtmlForm form1 = (HtmlForm) awardTimeAndMoneyPage.getForms().get(0);        
        String completeButtonName1=getImageTagName(awardTimeAndMoneyPage, "methodToCall.addFandaRate");        
        final HtmlImageInput button1 = (HtmlImageInput) form1.getInputByName(completeButtonName1);
        HtmlPage awardTimeAndMoneyPageAfterAddingOnCampus = (HtmlPage) button1.click();
        
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.applicableFandaRate", "5");
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.fandaRateTypeCode", "1");
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.fiscalYear", "2008");
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.startDate", "07/01/2007");
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.endDate", "06/30/2008");
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.onCampusFlag", "F");
        
        final HtmlForm form2 = (HtmlForm) awardTimeAndMoneyPageAfterAddingOnCampus.getForms().get(0);        
        String completeButtonName2=getImageTagName(awardTimeAndMoneyPageAfterAddingOnCampus, "methodToCall.addFandaRate");        
        final HtmlImageInput button2 = (HtmlImageInput) form2.getInputByName(completeButtonName2);
        HtmlPage awardTimeAndMoneyPageAfterAddingOnCampusAndOffCampus = (HtmlPage) button2.click();
        
        HtmlPage awardTimeAndMoneyPageAfterSave = clickOn(awardTimeAndMoneyPageAfterAddingOnCampusAndOffCampus, "methodToCall.save");
        assertDoesNotContain(awardTimeAndMoneyPageAfterSave, ERRORS_FOUND_ON_PAGE);
        assertContains(awardTimeAndMoneyPageAfterSave,SAVE_SUCCESS_MESSAGE);
        
        HtmlPage awardTimeAndMoneyPageAfterDelete1 = clickOn(awardTimeAndMoneyPageAfterSave, "methodToCall.deleteFandaRate.line1.anchor4");
        HtmlPage awardTimeAndMoneyPageAfterDelete2 = clickOn(awardTimeAndMoneyPageAfterDelete1, "methodToCall.deleteFandaRate.line0.anchor4");
        
        HtmlPage awardTimeAndMoneyPageAfterSaveAgain = clickOn(awardTimeAndMoneyPageAfterDelete2, "methodToCall.save");
        
        assertDoesNotContain(awardTimeAndMoneyPageAfterSaveAgain, ERRORS_FOUND_ON_PAGE);
        assertContains(awardTimeAndMoneyPageAfterSaveAgain,SAVE_SUCCESS_MESSAGE);
                
    }
    
    /**
     * 
     * This method tests the recalculate on Award F & A Rate on Time & Money page.
     * @throws Exception
     */
    @Test
    public void testAwardFandaRateRecalculate() throws Exception{
        
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.applicableFandaRate", "5");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.fandaRateTypeCode", "1");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.fiscalYear", "2008");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.startDate", "07/01/2007");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.endDate", "06/30/2008");
        setFieldValue(awardTimeAndMoneyPage, "newAwardFandaRate.onCampusFlag", "N");
        
        final HtmlForm form1 = (HtmlForm) awardTimeAndMoneyPage.getForms().get(0);        
        String completeButtonName1=getImageTagName(awardTimeAndMoneyPage, "methodToCall.addFandaRate");        
        final HtmlImageInput button1 = (HtmlImageInput) form1.getInputByName(completeButtonName1);
        HtmlPage awardTimeAndMoneyPageAfterAddingOnCampus = (HtmlPage) button1.click();
        
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.applicableFandaRate", "5");
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.fandaRateTypeCode", "1");
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.fiscalYear", "2008");
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.startDate", "07/01/2007");
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.endDate", "06/30/2008");
        setFieldValue(awardTimeAndMoneyPageAfterAddingOnCampus, "newAwardFandaRate.onCampusFlag", "F");
        
        final HtmlForm form2 = (HtmlForm) awardTimeAndMoneyPageAfterAddingOnCampus.getForms().get(0);        
        String completeButtonName2=getImageTagName(awardTimeAndMoneyPageAfterAddingOnCampus, "methodToCall.addFandaRate");        
        final HtmlImageInput button2 = (HtmlImageInput) form2.getInputByName(completeButtonName2);
        HtmlPage awardTimeAndMoneyPageAfterAddingOnCampusAndOffCampus = (HtmlPage) button2.click();
        
        HtmlPage awardTimeAndMoneyPageAfterSave = clickOn(awardTimeAndMoneyPageAfterAddingOnCampusAndOffCampus, "methodToCall.save");
        assertDoesNotContain(awardTimeAndMoneyPageAfterSave, ERRORS_FOUND_ON_PAGE);
        assertContains(awardTimeAndMoneyPageAfterSave,SAVE_SUCCESS_MESSAGE);
        
        setFieldValue(awardTimeAndMoneyPageAfterSave,"document.awardList[0].awardFandaRate[0].underrecoveryOfIndirectCost","2000.00");
        setFieldValue(awardTimeAndMoneyPageAfterSave,"document.awardList[0].awardFandaRate[1].underrecoveryOfIndirectCost","1945.00");
        
        HtmlPage awardTimeAndMoneyPageAfterRecalculate = clickOn(awardTimeAndMoneyPageAfterSave, "methodToCall.recalculateFandARate");
        System.out.println(awardTimeAndMoneyPageAfterRecalculate.asText());
        assertContains(awardTimeAndMoneyPageAfterRecalculate,"3945");
    }

}
