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

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * This class is integration/web test for Terms tab on Award Payments and Terms page.
 */
public class AwardSponsorTermWebTest extends AwardPaymentsAndTermsWebTest {
    
    public static final String METHOD_ADD_EQUIPMENT_TERM = 
        "methodToCall.addAwardSponsorTerm.sponsorTermType1.sponsorTermTypeIndex0.anchorAwardTerms:EquipmentApprovalTerms";
    public static final String METHOD_ADD_INVENTION_TERM = 
        "methodToCall.addAwardSponsorTerm.sponsorTermType2.sponsorTermTypeIndex1.anchorAwardTerms:InventionTerms";
    public static final String METHOD_SAVE = "methodToCall.save";
    public static final String METHOD_DELETE = "methodToCall.deleteAwardSponsorTerm.line0.anchor10";
    public static final String EQUIPMENT_DESCRIPTION = "Equipment over $25,000 unit price requires sponsor prior approval";
    public static final String INVENTION_DESCRIPTION = "Joint Title";
    public static final String UNIQUE_LOOKUP_ZERO = "newSponsorTerms[0]";
    public static final String UNIQUE_LOOKUP_ONE = "newSponsorTerms[1]";
    public static final String DESCRIPTION = "description";

    /**
     * The set up method calls the parent super method and gets the 
     * award Payment Reports And Terms page after that.
     * @see org.kuali.kra.award.htmlunitwebtest.AwardWebTestBase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * This method calls parent tear down method and than sets paymentReportsAndTermsPage to null
     * @see org.kuali.kra.award.htmlunitwebtest.AwardWebTestBase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
    
    /**
     * 
     * This method tests the adding of two Award Sponsor Terms from a lookup.  The Sponsor Terms returned from lookup
     * are forced to be unique be passing parameters in lookup. 
     * @throws Exception
     */
    @Test
    public void testAwardSponsorTermAddTwoSponsorTermsFromLookup() throws Exception{
 
        HtmlPage lookupPage = lookup(paymentReportsAndTermsPage, UNIQUE_LOOKUP_ZERO, DESCRIPTION, EQUIPMENT_DESCRIPTION);       
        HtmlPage awardPaymentReportsAndTermsPageAfterAdd = clickOnByName(lookupPage, METHOD_ADD_EQUIPMENT_TERM, true);
              
        assertDoesNotContain(awardPaymentReportsAndTermsPageAfterAdd, ERRORS_FOUND_ON_PAGE);        
        
        HtmlPage lookupPage2 = lookup(awardPaymentReportsAndTermsPageAfterAdd, UNIQUE_LOOKUP_ONE, DESCRIPTION, INVENTION_DESCRIPTION);      
        HtmlPage awardPaymentReportsAndTermsPageAfterSecondAdd = clickOnByName(lookupPage2, METHOD_ADD_INVENTION_TERM, true);
          
        assertDoesNotContain(awardPaymentReportsAndTermsPageAfterSecondAdd, ERRORS_FOUND_ON_PAGE);         
    }
    
    /**
     * 
     * This method tests the adding of two Award Sponsor Terms from a lookup and clicking Save after each Add.  
     * The Sponsor Terms returned from lookup are forced to be unique be passing parameters in lookup. 
     * @throws Exception
     */
    @Test
    public void testAwardSponsorTermAddTwoSponsorTermsFromLookupAndSave() throws Exception{
 
        HtmlPage lookupPage = lookup(paymentReportsAndTermsPage, UNIQUE_LOOKUP_ZERO, DESCRIPTION, EQUIPMENT_DESCRIPTION);
        
        HtmlPage awardPaymentReportsAndTermsPageAfterAdd = clickOnByName(lookupPage, METHOD_ADD_EQUIPMENT_TERM, true);
        
        HtmlPage awardPaymentReportsAndTermsPageAfterSave = clickOnByName(awardPaymentReportsAndTermsPageAfterAdd, METHOD_SAVE, true);
        assertDoesNotContain(awardPaymentReportsAndTermsPageAfterSave, ERROR_TABLE_OR_VIEW_DOES_NOT_EXIST);        
        assertDoesNotContain(awardPaymentReportsAndTermsPageAfterSave, ERRORS_FOUND_ON_PAGE);        
        assertContains(awardPaymentReportsAndTermsPageAfterSave,SAVE_SUCCESS_MESSAGE);
        
        HtmlPage lookupPage2 = lookup(awardPaymentReportsAndTermsPageAfterSave, UNIQUE_LOOKUP_ONE, DESCRIPTION, INVENTION_DESCRIPTION);
        
        HtmlPage awardPaymentReportsAndTermsPageAfterSecondAdd = clickOnByName(lookupPage2, METHOD_ADD_INVENTION_TERM, true);
        
        HtmlPage awardPaymentReportsAndTermsPageAfterSecondSave = clickOnByName(awardPaymentReportsAndTermsPageAfterSecondAdd, METHOD_SAVE, true);
        assertDoesNotContain(awardPaymentReportsAndTermsPageAfterSecondSave, ERROR_TABLE_OR_VIEW_DOES_NOT_EXIST);        
        assertDoesNotContain(awardPaymentReportsAndTermsPageAfterSecondSave, ERRORS_FOUND_ON_PAGE);        
        assertContains(awardPaymentReportsAndTermsPageAfterSecondSave,SAVE_SUCCESS_MESSAGE);  
    }
    
    /**
     * 
     * This method tests deleting a Award Sponsor Term from the Award.
     * @throws Exception
     */
    @Test
    public void testAwardSponsorTermDeleteAwardSponsorTerm() throws Exception{
 
        HtmlPage lookupPage = lookup(paymentReportsAndTermsPage, UNIQUE_LOOKUP_ZERO, DESCRIPTION,EQUIPMENT_DESCRIPTION);
        
        HtmlPage awardPaymentReportsAndTermsPageAfterAdd = clickOnByName(lookupPage, METHOD_ADD_EQUIPMENT_TERM, true);
        
        assertDoesNotContain(awardPaymentReportsAndTermsPageAfterAdd, ERRORS_FOUND_ON_PAGE);  
        
        HtmlPage awardPaymentReportsAndTermsPageAfterDelete = clickOnByName(awardPaymentReportsAndTermsPageAfterAdd, METHOD_DELETE, true);
        
        HtmlPage awardPaymentReportsAndTermsPageAfterSave = clickOn(awardPaymentReportsAndTermsPageAfterDelete, METHOD_SAVE);
        assertDoesNotContain(awardPaymentReportsAndTermsPageAfterSave, ERROR_TABLE_OR_VIEW_DOES_NOT_EXIST);        
        assertDoesNotContain(awardPaymentReportsAndTermsPageAfterSave, ERRORS_FOUND_ON_PAGE);        
        assertContains(awardPaymentReportsAndTermsPageAfterSave,SAVE_SUCCESS_MESSAGE);
        
    }
    
}
