/*
 * Copyright 2005-2010 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.committee.web;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.kuali.kra.irb.web.ProtocolWebTestBase;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

/**
 * Base class for all htmlunit tests involving the Committee Page.
 * 
 * @author $Author: gmcgrego $
 * @version $Revision: 1.18 $
 */

//@PerSuiteUnitTestData(
//        @UnitTestData(
//            sqlFiles = {
//                @UnitTestFile(filename = "classpath:sql/dml/load_committee_type.sql", delimiter = ";")
//               ,@UnitTestFile(filename = "classpath:sql/dml/load_COMM_MEMBERSHIP_TYPE.sql", delimiter = ";")
//               ,@UnitTestFile(filename = "classpath:sql/dml/load_MEMBERSHIP_ROLE.sql", delimiter = ";")
//            }
//        )
//    )
public abstract class CommitteeWebTestBase extends ProtocolWebTestBase {
//public abstract class CommitteeWebTestBase extends IrbWebTestBase {
    
    protected static final String CENTRAL_ADMIN_TAB = "Central Admin";
    
    protected static final String COMMITTEE_LINK_NAME = "committee";
    protected static final String SCHEDULE_LINK_NAME = "committeeSchedule";
    protected static final String MEMBERS_LINK_NAME = "committeeMembership";
    protected static final String ACTIONS_LINK_NAME = "committeeActions";
    
    protected static final String SUBMIT_BUTTON_NAME = "methodToCall.route";
    protected static final String SEARCH_BUTTON_NAME = "methodToCall.search";
    
    protected static final String DOCUMENT_HEADER_PREFIX = "document.documentHeader.";
    protected static final String COMMITTEE_LIST_PREFIX = "document.committeeList[0].";
    
    protected static final String DOCUMENT_DESCRIPTION = "documentDescription";
    protected static final String COMMITTEE_TYPE_CODE = "committeeTypeCode";
    protected static final String COMMITTEE_MAX_PROTOCOLS = "maxProtocols";
    protected static final String COMMITTEE_HOME_UNIT_NUMBER = "homeUnitNumber";
    protected static final String COMMITTEE_MIN_MEMBERS_REQUIRED = "minimumMembersRequired";
    protected static final String COMMITTEE_NAME = "committeeName";
    protected static final String COMMITTEE_ADV_SUBMISSION_DAYS_REQUIRED = "advancedSubmissionDaysRequired";
    protected static final String COMMITTEE_REVIEW_TYPE_CODE = "reviewTypeCode";
    protected static final String COMMITTEE = "committeeFIELD";
    protected static final String COMMITTEE_DESCRIPTION = "committeeDescription";
    protected static final String COMMITTEE_SCHEDULE_DESCRIPTION = "scheduleDescription";
    
    protected static final String DOCUMENT_DESCRIPTION_ID = DOCUMENT_HEADER_PREFIX + "documentDescription";
    protected static final String COMMITTEE_TYPE_CODE_ID = COMMITTEE_LIST_PREFIX + "committeeTypeCode";
    protected static final String COMMITTEE_MAX_PROTOCOLS_ID = COMMITTEE_LIST_PREFIX + "maxProtocols";
    protected static final String COMMITTEE_HOME_UNIT_NUMBER_ID = COMMITTEE_LIST_PREFIX + "homeUnitNumber";
    protected static final String COMMITTEE_MIN_MEMBERS_REQUIRED_ID = COMMITTEE_LIST_PREFIX + "minimumMembersRequired";
    protected static final String COMMITTEE_NAME_ID = COMMITTEE_LIST_PREFIX + "committeeName";
    protected static final String COMMITTEE_ADV_SUBMISSION_DAYS_REQUIRED_ID = COMMITTEE_LIST_PREFIX + "advancedSubmissionDaysRequired";
    protected static final String COMMITTEE_REVIEW_TYPE_CODE_ID = COMMITTEE_LIST_PREFIX + "reviewTypeCode";
    protected static final String COMMITTEE_ID_ID = COMMITTEE_LIST_PREFIX + "committeeId";
    protected static final String COMMITTEE_DESCRIPTION_ID = COMMITTEE_LIST_PREFIX + "committeeDescription";
    protected static final String COMMITTEE_SCHEDULE_DESCRIPTION_ID = COMMITTEE_LIST_PREFIX + "scheduleDescription";
    
    protected static final String DEFAULT_DOCUMENT_DESCRIPTION = "Committee Web Test";
    protected static final String DEFAULT_TYPE_CODE = "1"; // IRB
    protected static final String DEFAULT_MAX_PROTOCOLS = "10";
    protected static final String DEFAULT_HOME_UNIT_NUMBER = "000001";
    protected static final String DEFAULT_MIN_MEMBERS_REQUIRED = "3";
    protected static final String DEFAULT_NAME = "Committee Test ";
    protected static final String DEFAULT_ADV_SUBMISSION_DAYS_REQUIRED = "1";
    protected static final String DEFAULT_REVIEW_TYPE_CODE = "1"; // FULL
    protected static final String DEFAULT_DESCRIPTION = "xxx";
    protected static final String DEFAULT_SCHEDULE_DESCRIPTION = "foo";
    
    protected static final String SAVE_PAGE = "methodToCall.save";
    /* check for save success - any errors found in the page */
    protected static final String ERRORS_FOUND_ON_PAGE = "error(s) found on page";
    protected static final String SAVE_SUCCESS_MESSAGE = "Document was successfully saved";
    protected static final String LOADED_SUCCESS_MESSAGE = "Document was successfully reloaded.";

    
    private static Integer nextCommitteeId = 0;

    private HtmlPage committeePage;
    
    /**
     * Web test setup overloading. Sets up Portal Page and Committee page access.
     * 
     * @see org.kuali.kra.KraWebTestBase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        webClient.setAlertHandler(new AlertHandler() {
            public void handleAlert(Page page, String message) {
                assertThat(message, startsWith("Committee "));
                assertThat(message, endsWith(" submitted successfully."));
            }
        });
        setCommitteePage(buildCommitteePage());
    }
    
    /**
     * @see org.kuali.kra.KraWebTestBase#getLoginUserName()
     */
    protected String getLoginUserName() {
        return "chew";
    }
        
    /**
     * Create a new instance of the committee page by clicking on the link to the portal page. 
     * The resulting page of the click through is a frame, so it is important to get the inner page.
     * 
     * @return <code>{@link HtmlPage}</code> instance of the committee page
     * @throws IOException
     */
    protected final HtmlPage buildCommitteePage() throws Exception {
        HtmlPage centralAdminPage = clickOn(getPortalPage(), CENTRAL_ADMIN_TAB);
        HtmlPage retval = clickOn(centralAdminPage, "Create Committee", "Kuali Portal Index");
        retval = getInnerPages(retval).get(0);
        assertTrue("Kuali :: Committee Document".equals(retval.getTitleText()));
        return retval;
    }

    /**
     * Web test tear down overloading.
     * 
     * @see org.kuali.kra.KraWebTestBase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Gets the Committee web page for creating a new Committee document.
     * We don't want to test within the Portal.  This means that we will extract the
     * committee web page from within the Portal's Inline Frame (iframe).
     * 
     * @return the Committee web page.
     */
    protected final HtmlPage getCommitteePage() {
        return this.committeePage;
    }
    
    /**
     * Sets the committee page for tests. Typically, run out of <code>{@link #setUp()}</code>
     * 
     * @param committeePage <code>{@link HtmlPage}</code> instance for the test
     */
    protected final void setCommitteePage(HtmlPage committeePage) {
        this.committeePage = committeePage;
    }
    
    /**
     * Sets the Committee's required fields to legal default values.
     * @param page the Committee web page.
     * @return committeeId
     */
    protected String setDefaultRequiredFields(HtmlPage page) {
        String committeeId = getNextCommitteeID();
        setRequiredFields(page, DEFAULT_DOCUMENT_DESCRIPTION,
                                DEFAULT_TYPE_CODE,
                                DEFAULT_MAX_PROTOCOLS,
                                DEFAULT_HOME_UNIT_NUMBER,
                                DEFAULT_REVIEW_TYPE_CODE,
                                DEFAULT_NAME + committeeId,
                                DEFAULT_MIN_MEMBERS_REQUIRED,
                                DEFAULT_ADV_SUBMISSION_DAYS_REQUIRED,
                                committeeId,
                                DEFAULT_DESCRIPTION,
                                DEFAULT_SCHEDULE_DESCRIPTION);
        return committeeId;
    }
    
    /**
     * Sets the required (and more) fields for a Committee document.
     */
    protected void setRequiredFields(HtmlPage page, String docDescription, String committeeTypeCode, 
                                     String maxProtocols, String homeUnitNumber, String reviewType, 
                                     String name, String minMembersRequired, String advSubmissionDaysRequired,
                                     String committeeId, String description, String scheduleDescription) {
        setFieldValue(page, DOCUMENT_DESCRIPTION_ID, docDescription);
        setFieldValue(page, COMMITTEE_TYPE_CODE_ID, committeeTypeCode);
        setFieldValue(page, COMMITTEE_MAX_PROTOCOLS_ID, maxProtocols);
        setFieldValue(page, COMMITTEE_HOME_UNIT_NUMBER_ID, homeUnitNumber);
        setFieldValue(page, COMMITTEE_REVIEW_TYPE_CODE_ID, reviewType);
        setFieldValue(page, COMMITTEE_NAME_ID, name);
        setFieldValue(page, COMMITTEE_MIN_MEMBERS_REQUIRED_ID, minMembersRequired);
        setFieldValue(page, COMMITTEE_ADV_SUBMISSION_DAYS_REQUIRED_ID, advSubmissionDaysRequired);
        setFieldValue(page, COMMITTEE_ID_ID, committeeId);
        setFieldValue(page, COMMITTEE_DESCRIPTION_ID, description);
        setFieldValue(page, COMMITTEE_SCHEDULE_DESCRIPTION_ID, scheduleDescription);
    }
    
    /**
     * 
     * This method checks the values mentioned in the map against the values in from the page.
     * It uses getFieldValue(HtmlPage,string) method to get the value from page by using key.
     * @param page
     * @param keyValues
     */
    protected void validatePage(HtmlPage page, Map<String, String> keyValues) {
        Iterator<String> it = keyValues.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            assertEquals(getFieldValue(page, key), keyValues.get(key));
        }
    }
    
    /**
     * This method is to save a given page
     * @param page
     * @return saved page
     * @throws Exception
     */
    protected HtmlPage savePage(HtmlPage page) throws Exception {
        HtmlPage savedPage = clickOn(page, SAVE_PAGE);
        return savedPage;
    }

    /**
     * This method is to validate a saved page. Check to see if there are no errors in the page
     * and save success message is displayed
     * @param page
     * @return
     * @throws Exception
     */
    protected void validateSavedPage(HtmlPage page) throws Exception {
        assertDoesNotContain(page, ERRORS_FOUND_ON_PAGE);
        assertContains(page,SAVE_SUCCESS_MESSAGE);        
    }

    /**
     * Get the Members Web Page. To do this, we first
     * get the Committee Web Page and fill in the required
     * fields with some default values.  We can then navigate to the
     * Members Web Page.
     * 
     * @return the Members Web Page.
     * @throws Exception
     */
    protected HtmlPage getMembersPage() throws Exception {
        HtmlPage committeePage = this.getCommitteePage();
        this.setDefaultRequiredFields(committeePage);
        committeePage = savePage(committeePage);
        validateSavedPage(committeePage);
        HtmlPage membersPage = clickOnTab(committeePage, MEMBERS_LINK_NAME);
        return membersPage;
    }
    
    /**
     * Get the Schedule Web Page. To do this, we first
     * get the Committee Web Page and fill in the required
     * fields with some default values.  We can then navigate to the
     * Schedule Web Page.
     * 
     * @return the Schedule Web Page.
     * @throws Exception
     */
    protected HtmlPage getSchedulePage() throws Exception {
        HtmlPage committeePage = this.getCommitteePage();
        this.setDefaultRequiredFields(committeePage);
        HtmlPage schedulePage = clickOnTab(committeePage, SCHEDULE_LINK_NAME);
        return schedulePage;
    }
    
    /**
     * Get the Actions Web Page. To do this, we first
     * get the Committee Web Page, fill in the required
     * fields with some default values, submit, and go back.  
     * We can then navigate to the Actions Web Page.
     * 
     * @return the Actions Web Page.
     * @throws Exception
     */
    protected HtmlPage getActionsPage() throws Exception {
        HtmlPage committeePage = this.getCommitteePage();
        this.setDefaultRequiredFields(committeePage);
        HtmlPage submittedCommitteePage = submit(committeePage);
        HtmlPage actionsPage = clickOnTab(submittedCommitteePage, ACTIONS_LINK_NAME);
        assertContains(actionsPage, LOADED_SUCCESS_MESSAGE);
        return actionsPage;
    }
    
    /**
     * Submit the committee, and since submission redirects to the
     * portal page, searches for the submitted committee and returns
     * the submitted committee page.
     * 
     * @param committeePage
     * @return the Submitted Committee Web Page.
     * @throws Exception
     */
    protected HtmlPage submit(HtmlPage committeePage) throws Exception {
        HtmlPage portalPage = clickOn(committeePage, SUBMIT_BUTTON_NAME);
        HtmlPage centralAdminPage = clickOn(portalPage, CENTRAL_ADMIN_TAB);
        
        HtmlPage lookupPage = clickOn(centralAdminPage, "Committee Lookup", "Kuali Portal Index");
        setFieldValue(lookupPage, COMMITTEE_NAME, DEFAULT_NAME);

        // click on the search button
        HtmlImageInput searchBtn = (HtmlImageInput) getElement(lookupPage, "methodToCall.search", "search", "search");
        HtmlPage resultsPage = (HtmlPage) searchBtn.click();

        HtmlTable table = (HtmlTable) getElement(resultsPage, "row");
        HtmlTableBody body = table.getBodies().get(0);
        List<HtmlTableRow> rows = body.getRows();

        HtmlTableRow row = rows.get(0);
        List<HtmlTableCell> cells = row.getCells();
        HtmlTableCell cell = cells.get(0);
        HtmlAnchor editAnchor = (HtmlAnchor) getFirstChild(cell);
        HtmlAnchor viewAnchor = (HtmlAnchor) getNextSibling(editAnchor);
        return (HtmlPage) viewAnchor.click();
    }
    
    /**
     * Use for switching between tabs.
     * @param page
     * @param tabName
     * @return
     * @throws Exception
     */
    protected HtmlPage clickOnTab(HtmlPage page, String tabName) throws Exception {
        HtmlElement element = getElementByNameEndsWith(page, tabName);
        return clickOn(element);
    }
    
    /**
     * Click on the Members tab.
     *
     * @return the Members tab web page
     * @throws Exception
     */
    protected HtmlPage clickMembersHyperlink(HtmlPage page) throws Exception {
        return clickOnTab(page, MEMBERS_LINK_NAME);
    }
    
    /**
     * Click on the Committee tab.
     *
     * @return the Committee tab web page
     * @throws Exception
     */
    protected HtmlPage clickCommitteeHyperlink(HtmlPage page) throws Exception {
        return clickOnTab(page, COMMITTEE_LINK_NAME);
    }
    
    /**
     * Click on the Schedule tab.
     *
     * @return the Schedule tab web page
     * @throws Exception
     */
    protected HtmlPage clickScheduleHyperlink(HtmlPage page) throws Exception {
        return clickOnTab(page, SCHEDULE_LINK_NAME);
    }
    
    /**
     * This method returns the committee ID for the next committee that is created.
     *  
     * @return String - containing the next available committee ID
     */
    protected String getNextCommitteeID() {
    // TODO : this will cause duplicate committeeID if more than 2 test classes call this.
    // use timestamp for now.  Can use oracle sequence number too ?
        //nextCommitteeId++;
        return (new Long(new java.util.Date().getTime())).toString();
        //return nextCommitteeId.toString();
    }
}
