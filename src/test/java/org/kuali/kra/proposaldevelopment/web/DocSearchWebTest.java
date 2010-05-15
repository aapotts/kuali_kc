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
package org.kuali.kra.proposaldevelopment.web;

import java.io.IOException;

import org.junit.Test;
import org.kuali.rice.kew.exception.WorkflowException;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * This class tests a document search through the web ui via HtmlUnit.
 */
@org.junit.Ignore("This test is not meant to be run against the 2.0 release")
public class DocSearchWebTest extends ProposalDevelopmentWebTestBase {

    @Test
    public void testDocSearch() throws Exception {
        // save a new document and get the doc #
        String documentNumber = saveDocument();

        //1. Got the Portal Page.
        final HtmlPage portalPage = getPortalPage();

        //2. Click on doc search and get doc search page.
        final HtmlPage docSearchPage = clickOn(portalPage, "Document Search");

        //3. Fill in docNbr and click search.
        setFieldValue(docSearchPage, "routeHeaderId", documentNumber);
        final HtmlPage docSearchResultsPage = clickOn(docSearchPage, "methodToCall.search");

        //4. Click on docNbr hyperlink to get the document page.
        final HtmlAnchor docNumberHyperlink = docSearchResultsPage.getAnchorByHref("../kew/DocHandler.do?command=displayDocSearchView&docId=" + documentNumber);
        final HtmlPage proposalPage = clickOn(docNumberHyperlink, "Kuali :: Proposal Development Document");
        assertEquals(documentNumber, getFieldValue(proposalPage, "document.documentHeader.documentNumber"));
    }

    /**
     * This method uses HtmlUnit to navigate the web UI, save a new document and return the
     * document number.
     * @return document number of the newly saved document.
     * @throws WorkflowException
     * @throws IOException
     */
    private String saveDocument() throws WorkflowException, IOException {
        HtmlPage proposalPage = getProposalDevelopmentPage();

        setRequiredFields(proposalPage, DEFAULT_DOCUMENT_DESCRIPTION, "005891", DEFAULT_PROPOSAL_TITLE, "08/14/2007", "08/21/2007", DEFAULT_PROPOSAL_ACTIVITY_TYPE, DEFAULT_PROPOSAL_TYPE_CODE, DEFAULT_PROPOSAL_OWNED_BY_UNIT);

        String documentNumber = getFieldValue(proposalPage, "document.documentHeader.documentNumber");

        HtmlPage savedProposalPage = clickOn(proposalPage, "methodToCall.save", "Kuali :: Proposal Development Document");

        assertContains(savedProposalPage, "Document was successfully saved.");

        return documentNumber;
    }

}
