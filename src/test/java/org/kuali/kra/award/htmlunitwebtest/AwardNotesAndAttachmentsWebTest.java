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
import org.junit.Ignore;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 
 * This is the integration test for Award Notes And Attachments Page. 
 */
@SuppressWarnings("unchecked")
public class AwardNotesAndAttachmentsWebTest extends AwardWebTestBase {

    protected static final String NOTES_AND_ATTACHMENTS_LINK_NAME = "notesAndAttachments.x";
    HtmlPage awardNotesAndAttachmentsPage;

    /**
     * The set up method calls the parent super method and gets the 
     * award Time and Money page after that.
     * @see org.kuali.kra.award.htmlunitwebtest.AwardWebTestBase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        awardNotesAndAttachmentsPage = clickOnTab(getAwardHomePage(), NOTES_AND_ATTACHMENTS_LINK_NAME);
    }

    /**
     * This method calls parent tear down method and than sets awardTimeAndMoneyPage to null
     * @see org.kuali.kra.award.htmlunitwebtest.AwardWebTestBase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        awardNotesAndAttachmentsPage = null;
    }
    
    /**
     * 
     * Get the Award Notes & Attachments Web Page. To do this, we first
     * get the Award Home page and fill in the required
     * fields with some default values.  We can then navigate to the
     * Award Time & Money Web Page.
     * @return
     * @throws Exception
     */
    protected HtmlPage getAwardNotesAndAttachmentsPage() throws Exception {
        HtmlPage awardHomePage = this.getAwardHomePage();
        HtmlPage awardNotesAndAttachmentsPage = clickOnTab(awardHomePage, NOTES_AND_ATTACHMENTS_LINK_NAME);
        return awardNotesAndAttachmentsPage;
    }
    
    @Test @Ignore
    public void dummy() {}
}
