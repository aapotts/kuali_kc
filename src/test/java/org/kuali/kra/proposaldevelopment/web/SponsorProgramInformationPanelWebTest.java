/*
 * Copyright 2007 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
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
package org.kuali.kra.proposaldevelopment.web;

import org.junit.Test;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import edu.iu.uis.eden.exception.WorkflowException;

/**
 * This class tests tests the Sponsor & Program Information panel of the Proposal Development
 * Proposal web page.
 */
public class SponsorProgramInformationPanelWebTest extends ProposalDevelopmentWebTestBase {

    private static final String ERRORS_FOUND_ON_PAGE = "error(s) found on page";

    @Test
    public void testSponsorProgramInformationPanel() throws Exception {

        HtmlPage proposalPage = getProposalDevelopmentPage();

        setRequiredFields(proposalPage, DEFAULT_DOCUMENT_DESCRIPTION, "005891", DEFAULT_PROPOSAL_TITLE, "08/14/2007", "08/21/2007", DEFAULT_PROPOSAL_ACTIVITY_TYPE, DEFAULT_PROPOSAL_TYPE_CODE, "IN-PERS");

        // sponsor program info fields
        setFieldValue(proposalPage, "document.deadlineDate", "2007-08-14");
        setFieldValue(proposalPage, "document.deadlineType", "P"); //3
        setFieldValue(proposalPage, "document.primeSponsorCode", "005984");
        setFieldValue(proposalPage, "document.nsfCode", "J.02"); //39
        setFieldValue(proposalPage, "document.agencyDivisionCode", "123");
        setFieldValue(proposalPage, "document.programAnnouncementTitle", "we want to give you money");
        setFieldValue(proposalPage, "document.noticeOfOpportunityCode", "2"); //8
        setFieldValue(proposalPage, "document.cfdaNumber", "123456");
        setFieldValue(proposalPage, "document.programAnnouncementNumber", "123478");
        setFieldValue(proposalPage, "document.sponsorProposalNumber", "234567");
        setFieldValue(proposalPage, "document.subcontracts", "on");
        setFieldValue(proposalPage, "document.agencyProgramCode", "456");

        String documentNumber = getFieldValue(proposalPage, "document.documentHeader.documentNumber");

        HtmlPage savedProposalPage = clickOn(proposalPage, "methodToCall.save", "Kuali :: Proposal Development Document");

        assertDoesNotContain(savedProposalPage, ERRORS_FOUND_ON_PAGE);

        // make sure the document saved correctly
        ProposalDevelopmentDocument doc = (ProposalDevelopmentDocument) documentService.getByDocumentHeaderId(documentNumber);
        assertNotNull(doc);

        verifySavedRequiredFields(doc, DEFAULT_PROPOSAL_ACTIVITY_TYPE, "IN-PERS", DEFAULT_DOCUMENT_DESCRIPTION, "005891", DEFAULT_PROPOSAL_TITLE, "2007-08-14", "2007-08-21", DEFAULT_PROPOSAL_TYPE_CODE);

        // check sponsor program info fields
        assertEquals("P", doc.getDeadlineType());
        assertEquals("005984", doc.getPrimeSponsorCode());
        assertEquals("J.02", doc.getNsfCode());
        assertEquals("123", doc.getAgencyDivisionCode());
        assertEquals("we want to give you money", doc.getProgramAnnouncementTitle());
        assertEquals("2", doc.getNoticeOfOpportunityCode());
        assertEquals("123456", doc.getCfdaNumber());
        assertEquals("123478", doc.getProgramAnnouncementNumber());
        assertEquals("234567", doc.getSponsorProposalNumber());
        assertTrue("Subcontracts should be true", doc.getSubcontracts());
        assertEquals("456", doc.getAgencyProgramCode());

        // make sure the fields we set are displayed on the form after saving
        assertContains(savedProposalPage, "Document was successfully saved.");

        // sponsor program info fields
        assertEquals("08/14/2007", getFieldValue(savedProposalPage, "document.deadlineDate"));
        assertEquals("P", getFieldValue(savedProposalPage, "document.deadlineType"));
        assertEquals("005984", getFieldValue(savedProposalPage, "document.primeSponsorCode"));
        assertEquals("J.02", getFieldValue(savedProposalPage, "document.nsfCode"));
        assertEquals("123", getFieldValue(savedProposalPage, "document.agencyDivisionCode"));
        assertEquals("we want to give you money", getFieldValue(savedProposalPage, "document.programAnnouncementTitle"));
        assertEquals("2", getFieldValue(savedProposalPage, "document.noticeOfOpportunityCode"));
        assertEquals("123456", getFieldValue(savedProposalPage, "document.cfdaNumber"));
        assertEquals("123478", getFieldValue(savedProposalPage, "document.programAnnouncementNumber"));
        assertEquals("234567", getFieldValue(savedProposalPage, "document.sponsorProposalNumber"));
        assertEquals("on", getFieldValue(savedProposalPage, "document.subcontracts"));
        assertEquals("456", getFieldValue(savedProposalPage, "document.agencyProgramCode"));

        // test label
        final HtmlDivision sponsorNameDiv = (HtmlDivision) savedProposalPage.getHtmlElementById("sponsorName.div");
        assertEquals("Baystate Medical Center", sponsorNameDiv.asText());

        // test label
        final HtmlDivision primeSponsorNameDiv = (HtmlDivision) savedProposalPage.getHtmlElementById("primeSponsorName.div");
        assertEquals("Kuwait Petroleum Corporation", primeSponsorNameDiv.asText());
    }

    /**
     * This method checks document fields against the passed in values
     * @param doc the document to check values against
     * @param activityType to check
     * @param ownedByUnit to check
     * @param description to check
     * @param sponsorCode to check
     * @param title toi check
     * @param requestedStartDateInitial to check
     * @param requestedEndDateInitial to check
     * @param proposalTypeCode to check
     * @throws WorkflowException
     */
    private void verifySavedRequiredFields(ProposalDevelopmentDocument doc, String activityType, String ownedByUnit, String description, String sponsorCode, String title, String requestedStartDateInitial, String requestedEndDateInitial, String proposalTypeCode) throws WorkflowException {
        assertEquals(activityType, doc.getActivityTypeCode());
        assertEquals(ownedByUnit, doc.getOwnedByUnit());
        assertEquals(description, doc.getDocumentHeader().getFinancialDocumentDescription());
        assertEquals(sponsorCode, doc.getSponsorCode());
        assertEquals(title, doc.getTitle());
        assertEquals(requestedStartDateInitial, doc.getRequestedStartDateInitial().toString());
        assertEquals(requestedEndDateInitial, doc.getRequestedEndDateInitial().toString());
        assertEquals(proposalTypeCode, doc.getProposalTypeCode());
    }
}
