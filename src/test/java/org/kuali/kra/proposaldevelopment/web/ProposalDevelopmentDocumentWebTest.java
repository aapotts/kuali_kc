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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.core.UserSession;
import org.kuali.core.service.DocumentService;
import org.kuali.core.util.GlobalVariables;
import org.kuali.kra.KraTestBase;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.rice.KNSServiceLocator;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * This class tests the KraServiceLocator
 */
public class ProposalDevelopmentDocumentWebTest extends KraTestBase {

    private static final Logger LOG = Logger.getLogger(ProposalDevelopmentDocumentWebTest.class);
    private static final int TEXT_INPUT = 0;
    private static final int TEXT_AREA = 1;
    private static final int SELECTED_INPUT = 2;
    private static final int HIDDEN_INPUT = 3;
    private static final int IMAGE_INPUT = 4;
    private static final int SUBMIT_INPUT_BY_NAME = 5;
    private static final int SUBMIT_INPUT_BY_VALUE = 6;
    private static final String ERRORS_FOUND_ON_PAGE = "error(s) found on page";
    private DocumentService documentService = null;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        GlobalVariables.setUserSession(new UserSession("quickstart"));
        documentService = KNSServiceLocator.getDocumentService();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        GlobalVariables.setUserSession(null);
        documentService = null;
    }

    @Test
    public void testProposalTypeLink() throws Exception {
        final WebClient webClient = new WebClient();
        final URL url = new URL("http://localhost:" + getPort() + "/kra-dev/");

        // Administration Tab - LOGIN
        final HtmlPage page3 = login(webClient, url, "portal.do?selectedTab=portalAdministrationBody");

        assertEquals("Kuali Portal Index", page3.getTitleText());

        // test proposalType link
        final HtmlPage page4 = (HtmlPage) webClient
                .getPage(url
                        + "kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kra.proposaldevelopment.bo.ProposalType&returnLocation=kra-dev/portal.do&hideReturnLink=true&docFormKey=88888888");
        assertEquals("Kuali :: Lookup", page4.getTitleText());

        // test proposalType link - based on anchor
        final HtmlAnchor proposalTypeLink = (HtmlAnchor) page3.getAnchorByName("lookupProposalType");
        final HtmlPage page5 = (HtmlPage) webClient.getPage(url + proposalTypeLink.getHrefAttribute());
        assertEquals("Kuali :: Lookup", page5.getTitleText());

    }

    @Test
    public void testHelpLink() throws Exception {
        final WebClient webClient = new WebClient();
        final URL url = new URL("http://localhost:" + getPort() + "/kra-dev/");
        final HtmlPage page3 = login(webClient, url, "proposalDevelopmentProposal.do?methodToCall=docHandler&command=initiate&docTypeName=ProposalDevelopmentDocument");
        assertEquals("Kuali :: Proposal Development Document", page3.getTitleText());

        // test document overview help link
        LOG.info("getting page4");
        final HtmlPage page4 = (HtmlPage) webClient
                .getPage(url
                        + "kr/help.do?methodToCall=getAttributeHelpText&businessObjectClassName=org.kuali.core.bo.DocumentHeader&attributeName=financialDocumentDescription");
        assertEquals("Kuali :: Kuali Help", page4.getTitleText());

        // test proposal development document attribute help link
        LOG.info("getting page5");
        final HtmlPage page5 = (HtmlPage) webClient
                .getPage(url
                        + "kr/help.do?methodToCall=getAttributeHelpText&businessObjectClassName=org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument&attributeName=sponsorCode");
        assertEquals("Kuali :: Kuali Help", page5.getTitleText());
    }

    @Test
    public void testSaveProposalDevelopmentDocumentWeb() throws Exception {
        final WebClient webClient = new WebClient();
        final URL url = new URL("http://localhost:" + getPort() + "/kra-dev/");
        final HtmlPage page3 = login(webClient, url, "proposalDevelopmentProposal.do?methodToCall=docHandler&command=initiate&docTypeName=ProposalDevelopmentDocument");
        assertEquals("Kuali :: Proposal Development Document", page3.getTitleText());

        final HtmlForm kualiForm = (HtmlForm) page3.getForms().get(0);
        setupProposalDevelopmentDocumentRequiredFields(kualiForm);


        final HtmlHiddenInput documentNumber = (HtmlHiddenInput) kualiForm.getInputByName("document.documentHeader.documentNumber");

        final HtmlPage page4 = clickButton(kualiForm,"methodToCall.save",IMAGE_INPUT);
        assertEquals("Kuali :: Proposal Development Document", page4.getTitleText());

        String page4AsText = page4.asText();
        String errorMessage = "Errors were found: ";
        int index1 = page4AsText.indexOf("error");
        if (index1 > -1) {
            int index2 = page4AsText.indexOf("Document Overview");
            if (index2 > -1) {
                errorMessage += page4AsText.substring(index1, index2);
            }
            else {
                errorMessage += page4AsText.substring(index1);
            }
        }

        assertFalse(errorMessage, page4.asText().contains(ERRORS_FOUND_ON_PAGE));

        ProposalDevelopmentDocument doc = (ProposalDevelopmentDocument) documentService.getByDocumentHeaderId(documentNumber
                .getDefaultValue());
        assertNotNull(doc);

        verifySavedRequiredFields(doc);

    }

    @Test
    public void testSaveProposalDevelopmentDocumentNotNewWeb() throws Exception {
        final WebClient webClient = new WebClient();
        final URL url = new URL("http://localhost:" + getPort() + "/kra-dev/");
        final HtmlPage page1 = (HtmlPage) webClient.getPage(url);

        assertEquals("Kuali Portal Index", page1.getTitleText());

        // LOGIN
        final HtmlPage page2 = (HtmlPage) webClient
                .getPage(url
                        + "proposalDevelopmentProposal.do?methodToCall=docHandler&command=initiate&docTypeName=ProposalDevelopmentDocument");

        // Get the form that we are dealing with and within that form,
        // find the submit button and the field that we want to change.
        final HtmlForm form = (HtmlForm) page2.getForms().get(0);
        final HtmlSubmitInput button = (HtmlSubmitInput) form.getInputByValue("Login");

        // Now submit the form by clicking the button and get back the
        // second page.
        final HtmlPage page3 = (HtmlPage) button.click();
        assertEquals("Kuali :: Proposal Development Document", page3.getTitleText());

        final HtmlForm kualiForm = (HtmlForm) page3.getForms().get(0);
        final HtmlImageInput saveButton = (HtmlImageInput) kualiForm.getInputByName("methodToCall.save");

        final HtmlTextInput description = (HtmlTextInput) kualiForm
                .getInputByName("document.documentHeader.financialDocumentDescription");
        description.setValueAttribute("ProposalDevelopmentDocumentWebTest test");

        final HtmlTextInput sponsorCode = (HtmlTextInput) kualiForm.getInputByName("document.sponsorCode");
        sponsorCode.setValueAttribute("123456");

        final HtmlTextArea title = (HtmlTextArea) kualiForm.getTextAreasByName("document.title").get(0);
        title.setText("project title");

        final HtmlTextInput startDate = (HtmlTextInput) kualiForm.getInputByName("document.requestedStartDateInitial");
        startDate.setValueAttribute("08/14/2007");

        final HtmlTextInput endDate = (HtmlTextInput) kualiForm.getInputByName("document.requestedEndDateInitial");
        endDate.setValueAttribute("08/21/2007");

        final HtmlSelect activityType = (HtmlSelect) kualiForm.getSelectByName("document.activityTypeCode");
        assertEquals(10, activityType.getOptionSize());
        activityType.setSelectedAttribute("1", true);

        final HtmlSelect proposalType = (HtmlSelect) kualiForm.getSelectByName("document.proposalTypeCode");
        assertEquals(10, proposalType.getOptionSize());
        proposalType.setSelectedAttribute("2", true);

        final HtmlTextInput sponsorProposalNumber = (HtmlTextInput) kualiForm.getInputByName("document.sponsorProposalNumber");
        sponsorProposalNumber.setValueAttribute("123456");

        final HtmlSelect ownedByUnit = (HtmlSelect) kualiForm.getSelectByName("document.ownedByUnit");
        assertEquals(3, ownedByUnit.getOptionSize());
        ownedByUnit.setSelectedAttribute("000002", true);

        final HtmlHiddenInput documentNumber = (HtmlHiddenInput) kualiForm.getInputByName("document.documentHeader.documentNumber");

        final HtmlPage page4 = (HtmlPage) saveButton.click();
        assertEquals("Kuali :: Proposal Development Document", page4.getTitleText());

        String page4AsText = page4.asText();
        String errorMessage = "Errors were found: ";
        int index1 = page4AsText.indexOf("error");
        if (index1 > -1) {
            int index2 = page4AsText.indexOf("Document Overview");
            if (index2 > -1) {
                errorMessage += page4AsText.substring(index1, index2);
            }
            else {
                errorMessage += page4AsText.substring(index1);
            }
        }

        assertFalse(errorMessage, page4.asText().contains(ERRORS_FOUND_ON_PAGE));

        ProposalDevelopmentDocument doc = (ProposalDevelopmentDocument) documentService.getByDocumentHeaderId(documentNumber
                .getDefaultValue());
        assertNotNull(doc);

        assertEquals("1", doc.getActivityTypeCode());
        assertEquals("000002", doc.getOwnedByUnit());
        assertEquals("ProposalDevelopmentDocumentWebTest test", doc.getDocumentHeader().getFinancialDocumentDescription());
        assertEquals("123456", doc.getSponsorCode());
        assertEquals("project title", doc.getTitle());
        assertEquals("2007-08-14", doc.getRequestedStartDateInitial().toString());
        assertEquals("2007-08-21", doc.getRequestedEndDateInitial().toString());
        assertEquals("2", doc.getProposalTypeCode());
        assertEquals("123456", doc.getSponsorProposalNumber());

    }

    @Test
    public void testSaveProposalDevelopmentDocumentWithErrorsWeb() throws Exception {
        final WebClient webClient = new WebClient();
        final URL url = new URL("http://localhost:" + getPort() + "/kra-dev/");
        final HtmlPage page1 = (HtmlPage) webClient.getPage(url);

        assertEquals("Kuali Portal Index", page1.getTitleText());

        // LOGIN
        final HtmlPage page2 = (HtmlPage) webClient
                .getPage(url
                        + "proposalDevelopmentProposal.do?methodToCall=docHandler&command=initiate&docTypeName=ProposalDevelopmentDocument");

        // Get the form that we are dealing with and within that form,
        // find the submit button and the field that we want to change.
        final HtmlForm form = (HtmlForm) page2.getForms().get(0);
        final HtmlSubmitInput button = (HtmlSubmitInput) form.getInputByValue("Login");

        // Now submit the form by clicking the button and get back the
        // second page.
        final HtmlPage page3 = (HtmlPage) button.click();
        assertEquals("Kuali :: Proposal Development Document", page3.getTitleText());

        final HtmlForm kualiForm = (HtmlForm) page3.getForms().get(0);
        final HtmlImageInput saveButton = (HtmlImageInput) kualiForm.getInputByName("methodToCall.save");

        final HtmlTextInput description = (HtmlTextInput) kualiForm
                .getInputByName("document.documentHeader.financialDocumentDescription");
        description.setValueAttribute("ProposalDevelopmentDocumentWebTest test");

        final HtmlTextInput sponsorCode = (HtmlTextInput) kualiForm.getInputByName("document.sponsorCode");
        sponsorCode.setValueAttribute("123456");

        final HtmlTextArea title = (HtmlTextArea) kualiForm.getTextAreasByName("document.title").get(0);
        title.setText("project title");

        final HtmlTextInput startDate = (HtmlTextInput) kualiForm.getInputByName("document.requestedStartDateInitial");
        startDate.setValueAttribute("08/14/2007");

        final HtmlTextInput endDate = (HtmlTextInput) kualiForm.getInputByName("document.requestedEndDateInitial");
        endDate.setValueAttribute("08/21/2007");

        final HtmlSelect activityType = (HtmlSelect) kualiForm.getSelectByName("document.activityTypeCode");
        assertEquals(10, activityType.getOptionSize());
        activityType.setSelectedAttribute("1", true);

        final HtmlSelect proposalType = (HtmlSelect) kualiForm.getSelectByName("document.proposalTypeCode");
        assertEquals(10, proposalType.getOptionSize());
        proposalType.setSelectedAttribute("2", true);

        final HtmlSelect ownedByUnit = (HtmlSelect) kualiForm.getSelectByName("document.ownedByUnit");
        assertEquals(3, ownedByUnit.getOptionSize());
        ownedByUnit.setSelectedAttribute("000002", true);

        final HtmlHiddenInput documentNumber = (HtmlHiddenInput) kualiForm.getInputByName("document.documentHeader.documentNumber");

        final HtmlPage page4 = (HtmlPage) saveButton.click();
        assertEquals("Kuali :: Proposal Development Document", page4.getTitleText());

        String page4AsText = page4.asText();
        String errorMessage = "Errors were found: ";
        int index1 = page4AsText.indexOf("error");
        if (index1 > -1) {
            int index2 = page4AsText.indexOf("Document Overview");
            if (index2 > -1) {
                errorMessage += page4AsText.substring(index1, index2);
            }
            else {
                errorMessage += page4AsText.substring(index1);
            }
        }

        assertTrue(errorMessage, page4.asText().contains(ERRORS_FOUND_ON_PAGE));

    }

    @Test
    public void testOrganizationLocationPanel() throws Exception {
        final WebClient webClient = new WebClient();
        final URL url = new URL("http://localhost:" + getPort() + "/kra-dev/");

        final HtmlPage page3 = login(webClient, url,
                "proposalDevelopmentProposal.do?methodToCall=docHandler&command=initiate&docTypeName=ProposalDevelopmentDocument");
        assertEquals("Kuali :: Proposal Development Document", page3.getTitleText());

        final HtmlForm kualiForm = (HtmlForm) page3.getForms().get(0);
        setupProposalDevelopmentDocumentRequiredFields(kualiForm);

        // start to set up organization/location panel

        // organization
        StringBuffer orgLookupTagName = new StringBuffer();
        orgLookupTagName
                .append("methodToCall.performLookup.(!!org.kuali.kra.bo.Organization!!).")
                .append(
                        "(((organizationId:document.organizationId,congressionalDistrict:document.organization.congressionalDistrict")
                .append(
                        ",organizationName:document.organization.organizationName,rolodex.firstName:document.organization.rolodex.firstName")
                .append(
                        ",rolodex.lastName:document.organization.rolodex.lastName,rolodex.addressLine1:document.organization.rolodex.addressLine1")
                .append(
                        ",rolodex.addressLine2:document.organization.rolodex.addressLine2,rolodex.addressLine3:document.organization.rolodex.addressLine3")
                .append(
                        ",rolodex.city:document.organization.rolodex.city,rolodex.state:document.organization.rolodex.state))).((##)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).anchor");


        final HtmlPage page43 = lookup(webClient, (HtmlImageInput) kualiForm.getInputByName(orgLookupTagName.toString()), "000001",
                "proposalDevelopmentProposal.do?document.organization", "organizationId");
        final HtmlForm form1 = (HtmlForm) page43.getForms().get(0);
        assertEquals("000001", getFieldValue(form1, HIDDEN_INPUT, "document.organizationId"));
        assertTrue(page43.asText().contains("Congressional District: Eighth"));
        assertTrue(page43.asText().contains("Performing Organization Id: University"));
        assertTrue(page43.asText().contains("Applicant Organization: University"));
        assertTrue(page43.asText().contains("Authorized Representative Name & Address: First Name"));
        // default prop location created
        assertEquals("University", getFieldValue(form1, TEXT_INPUT, "document.propLocations[0].location"));
        // delete default line
        final HtmlPage page44 = clickButton(form1, "methodToCall.deleteLocation.line0.", IMAGE_INPUT);
        final HtmlForm form41 = (HtmlForm) page44.getForms().get(0);
        // save without location line
        // the default location line will be recreated
        final HtmlPage page45 = clickButton(form41, "methodToCall.save", IMAGE_INPUT);
        assertEquals("Kuali :: Proposal Development Document", page45.getTitleText());
        final HtmlForm form42 = (HtmlForm) page45.getForms().get(0);
        // one of the following to check save is OK
        assertTrue(page45.asText().contains(ERRORS_FOUND_ON_PAGE));
        assertFalse(page45.asText().contains("Document was successfully saved"));

        // performingorg lookup

        String lookupTagName = "methodToCall.performLookup.(!!org.kuali.kra.bo.Organization!!).(((organizationId:document.performingOrganizationId,organizationName:document.performingOrganization.organizationName))).((##)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).anchor";
        final HtmlPage page52 = lookup(webClient, (HtmlImageInput) form42.getInputByName(lookupTagName), "000002",
                "proposalDevelopmentProposal.do?document.performingOrganization", "organizationId");
        final HtmlForm form2 = (HtmlForm) page52.getForms().get(0);
        assertEquals("000002", getFieldValue(form2, HIDDEN_INPUT, "document.performingOrganizationId"));
        assertTrue(page52.asText().contains("Performing Organization Id: California Institute of Technology"));
        // California Institute of Technology

        // proplocations
        // set up and add first line
        setFieldValue(kualiForm, TEXT_INPUT, "newPropLocation.location", "location 1");

        // test rolodex lookup lookup
        StringBuffer rolodexIdName = new StringBuffer();
        rolodexIdName
                .append("methodToCall.performLookup.(!!org.kuali.kra.bo.Rolodex!!).")
                .append("(((rolodexId:newPropLocation.rolodexId,postalCode:newPropLocation.rolodex.postalCode")
                .append(",addressLine1:newPropLocation.rolodex.addressLine1")
                .append(",addressLine2:newPropLocation.rolodex.addressLine2,addressLine3:newPropLocation.rolodex.addressLine3")
                .append(
                        ",city:newPropLocation.rolodex.city,state:newPropLocation.rolodex.state))).((##)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).anchor");
        final HtmlPage page53 = lookup(webClient, (HtmlImageInput) form2.getInputByName(rolodexIdName.toString()), "1728",
                "proposalDevelopmentProposal.do?newPropLocation.rolodex.", "rolodexId");
        final HtmlForm form3 = (HtmlForm) page53.getForms().get(0);
        assertEquals("1728", getFieldValue(form3, HIDDEN_INPUT, "newPropLocation.rolodexId"));
        assertTrue(page53.asText().contains("National Center for Environmental Research and Quality Assurance"));

        final HtmlPage page54 = clickButton(form3, "methodToCall.addLocation", IMAGE_INPUT);
        final HtmlForm form4 = (HtmlForm) page54.getForms().get(0);

        assertEquals("0", getFieldValue(form4, HIDDEN_INPUT, "newPropLocation.rolodexId"));
        // how to check newlocation address is empty
        assertEquals("1728", getFieldValue(form4, HIDDEN_INPUT, "document.propLocations[1].rolodexId"));
        assertTrue(page54.asText().contains("National Center for Environmental Research and Quality Assurance"));

        // 2nd line
        // set up and add 2nd line
        setFieldValue(form4, TEXT_INPUT, "newPropLocation.location", "location 2");

        // test rolodex lookup
        final HtmlPage page6 = lookup(webClient, (HtmlImageInput) form4.getInputByName(rolodexIdName.toString()), "1727",
                "proposalDevelopmentProposal.do?newPropLocation.rolodex.", "rolodexId");
        final HtmlForm form5 = (HtmlForm) page6.getForms().get(0);
        assertEquals("1727", getFieldValue(form5, HIDDEN_INPUT, "newPropLocation.rolodexId"));
        assertTrue(page6.asText().contains("Organization 1126"));

        final HtmlPage page61 = clickButton(form5, "methodToCall.addLocation", IMAGE_INPUT);
        final HtmlForm form6 = (HtmlForm) page61.getForms().get(0);

        assertEquals("0", getFieldValue(form6, HIDDEN_INPUT, "newPropLocation.rolodexId"));
        // how to check newlocation address is empty
        assertEquals("1727", getFieldValue(form6, HIDDEN_INPUT, "document.propLocations[2].rolodexId"));
        assertTrue(page61.asText().contains("Organization 1126"));

        // clearaddress
        final HtmlPage page62 = clickButton(form6, "methodToCall.clearAddress.line1.", IMAGE_INPUT);
        final HtmlForm form7 = (HtmlForm) page62.getForms().get(0);
        assertEquals("0", getFieldValue(form7, HIDDEN_INPUT, "document.propLocations[1].rolodexId"));
        assertFalse(page62.asText().contains("National Center for Environmental Research and Quality Assurance"));
        // verify other fields too? location, proplocations[1] ?

        // delete lines
        final HtmlPage page63 = clickButton(form7, "methodToCall.deleteLocation.line1.", IMAGE_INPUT);
        final HtmlForm form8 = (HtmlForm) page63.getForms().get(0);
        assertEquals("1727", getFieldValue(form8, HIDDEN_INPUT, "document.propLocations[1].rolodexId"));
        assertTrue(page63.asText().contains("Organization 1126"));
        // how to check only one left
        final HtmlPage page7 = clickButton(form8, "methodToCall.save", IMAGE_INPUT);
        assertEquals("Kuali :: Proposal Development Document", page6.getTitleText());
        final HtmlForm form9 = (HtmlForm) page7.getForms().get(0);
        // one of the following to check save is OK
        assertFalse(page7.asText().contains(ERRORS_FOUND_ON_PAGE));
        assertTrue(page7.asText().contains("Document was successfully saved"));
        // verify for is still ok
        assertEquals("000001", getFieldValue(form9, HIDDEN_INPUT, "document.organizationId"));
        assertTrue(page7.asText().contains("Congressional District: Eighth"));
        assertTrue(page7.asText().contains("Applicant Organization: University"));
        assertTrue(page7.asText().contains("Authorized Representative Name & Address: First Name"));
        assertEquals("000002", getFieldValue(form9, HIDDEN_INPUT, "document.performingOrganizationId"));
        assertTrue(page7.asText().contains("Performing Organization Id: California Institute of Technology"));

        assertEquals("1727", getFieldValue(form9, HIDDEN_INPUT, "document.propLocations[1].rolodexId"));
        assertTrue(page7.asText().contains("Organization 1126"));
        assertEquals("0", getFieldValue(form9, HIDDEN_INPUT, "document.propLocations[0].rolodexId"));
        assertEquals("University", getFieldValue(form9, TEXT_INPUT, "document.propLocations[0].location"));

        // verify DB
        final HtmlHiddenInput documentNumber = (HtmlHiddenInput) form4.getInputByName("document.documentHeader.documentNumber");

        ProposalDevelopmentDocument doc = (ProposalDevelopmentDocument) getDocument(documentNumber.getDefaultValue());
        assertNotNull(doc);

        verifySavedRequiredFields(doc);
        assertEquals("000001", doc.getOrganizationId());
        assertEquals("000002", doc.getPerformingOrganizationId());
        assertEquals("University", doc.getPropLocations().get(0).getLocation());
        assertEquals(0, doc.getPropLocations().get(0).getRolodexId());
        assertEquals("location 2", doc.getPropLocations().get(1).getLocation());
        assertEquals(1727, doc.getPropLocations().get(1).getRolodexId());

    }


    @Test
    public void testDeliveryInfoPanel() throws Exception {
        final WebClient webClient = new WebClient();
        final URL url = new URL("http://localhost:" + getPort() + "/kra-dev/");
        final HtmlPage page3 = login(webClient, url,
                "proposalDevelopmentProposal.do?methodToCall=docHandler&command=initiate&docTypeName=ProposalDevelopmentDocument");
        assertEquals("Kuali :: Proposal Development Document", page3.getTitleText());

        final HtmlForm kualiForm = (HtmlForm) page3.getForms().get(0);
        setupProposalDevelopmentDocumentRequiredFields(kualiForm);


        // dropdowns
        setFieldValue(kualiForm, SELECTED_INPUT, "document.mailBy", "1", 3);
        setFieldValue(kualiForm, SELECTED_INPUT, "document.mailType", "2", 4);


        // input fields
        setFieldValue(kualiForm, TEXT_INPUT, "document.mailAccountNumber", "10-0001");
        setFieldValue(kualiForm, TEXT_INPUT, "document.numberOfCopies", "2");

        // test mailing address lookup
        StringBuffer mailingAddressIdName = new StringBuffer();
        mailingAddressIdName
                .append("methodToCall.performLookup.(!!org.kuali.kra.bo.Rolodex!!).")
                .append("(((rolodexId:document.mailingAddressId,firstName:document.rolodex.firstName")
                .append(",lastName:document.rolodex.lastName,addressLine1:document.rolodex.addressLine1")
                .append(",addressLine2:document.rolodex.addressLine2,addressLine3:document.rolodex.addressLine3")
                .append(
                        ",city:document.rolodex.city,state:document.rolodex.state))).((##)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).anchor");
        final HtmlPage page43 = lookup(webClient, (HtmlImageInput) kualiForm.getInputByName(mailingAddressIdName.toString()),
                "1728", "proposalDevelopmentProposal.do?document.rolodex.", "rolodexId");
        final HtmlForm form1 = (HtmlForm) page43.getForms().get(0);
        assertEquals("1728", getFieldValue(form1, HIDDEN_INPUT, "document.mailingAddressId"));
        assertTrue(page43.asText().contains("National Center for Environmental Research and Quality Assurance"));

        // mail description textarea
        setFieldValue(form1, TEXT_AREA, "document.mailDescription", "mail description");

        webClient.setJavaScriptEnabled(false);
        final HtmlPage page5 = clickButton(form1,
                "methodToCall.updateTextArea.((#document.mailDescription:proposalDevelopmentProposal:Mail Description#))",
                IMAGE_INPUT);
        final HtmlForm form2 = (HtmlForm) page5.getForms().get(0);
        assertEquals("mail description", getFieldValue(form2, TEXT_AREA, "document.mailDescription"));
        setFieldValue(form2, TEXT_AREA, "document.mailDescription", "mail description \n line2");

        final HtmlPage page51 = clickButton(form2, "methodToCall.postTextAreaToParent", IMAGE_INPUT);
        final HtmlForm form3 = (HtmlForm) page51.getForms().get(0);
        assertEquals("mail description \n line2", getFieldValue(form3, TEXT_AREA, "document.mailDescription"));


        // save and check
        final HtmlPage page6 = clickButton(form3, "methodToCall.save", IMAGE_INPUT);
        assertEquals("Kuali :: Proposal Development Document", page6.getTitleText());
        final HtmlForm form4 = (HtmlForm) page6.getForms().get(0);
        // one of the following to check save is OK
        assertFalse(page6.asText().contains(ERRORS_FOUND_ON_PAGE));
        assertTrue(page6.asText().contains("Document was successfully saved"));

        assertEquals("2", getFieldValue(form4, SELECTED_INPUT, "document.mailType"));
        assertEquals("1", getFieldValue(form4, SELECTED_INPUT, "document.mailBy"));

        assertEquals("10-0001", getFieldValue(form4, TEXT_INPUT, "document.mailAccountNumber"));
        assertEquals("2", getFieldValue(form4, TEXT_INPUT, "document.numberOfCopies"));


        assertEquals("1728", getFieldValue(form4, HIDDEN_INPUT, "document.mailingAddressId"));
        assertTrue(page6.asText().contains("National Center for Environmental Research and Quality Assurance"));

        assertEquals("mail description \n line2", getFieldValue(form4, TEXT_AREA, "document.mailDescription"));

        final HtmlHiddenInput documentNumber = (HtmlHiddenInput) form4.getInputByName("document.documentHeader.documentNumber");
        ProposalDevelopmentDocument doc = (ProposalDevelopmentDocument) getDocument(documentNumber.getDefaultValue());
        assertNotNull(doc);
        verifySavedRequiredFields(doc);

        assertEquals("1", doc.getMailBy());
        assertEquals("2", doc.getMailType());
        assertEquals(1728, doc.getMailingAddressId());
        assertEquals("10-0001", doc.getMailAccountNumber());
        assertEquals("2", doc.getNumberOfCopies());
        assertEquals("mail description \n line2", doc.getMailDescription());


    }


    @Test
    public void testSpecialReviewPage() throws Exception {
        final WebClient webClient = new WebClient();
        final URL url = new URL("http://localhost:" + getPort() + "/kra-dev/");

        final HtmlPage page3 = login(webClient, url,
                "proposalDevelopmentProposal.do?methodToCall=docHandler&command=initiate&docTypeName=ProposalDevelopmentDocument");
        assertEquals("Kuali :: Proposal Development Document", page3.getTitleText());

        final HtmlForm kualiForm = (HtmlForm) page3.getForms().get(0);
        setupProposalDevelopmentDocumentRequiredFields(kualiForm);
        final HtmlPage page4 = clickButton(kualiForm, "methodToCall.headerTab.headerDispatch.save.navigateTo.specialReview.x",
                SUBMIT_INPUT_BY_NAME);
        assertTrue(page4.asText().contains("Document was successfully saved"));
        assertTrue(page4.asText().contains("Approval Status Protocol # Application Date Approval Date Comments"));
        HtmlForm form1 = (HtmlForm) page4.getForms().get(0);

        webClient.setJavaScriptEnabled(false);
        final HtmlPage page5 = setSpecialReviewLine(form1, "08/01/2007;;123;1;2;comment1");

        final HtmlForm form2 = (HtmlForm) page5.getForms().get(0);
        assertEquals("comment1 \n line2", getFieldValue(form2, TEXT_AREA, "newPropSpecialReview.comments"));
        final HtmlPage page51 = clickButton(form2, "methodToCall.addSpecialReview", IMAGE_INPUT);
        final HtmlForm form3 = (HtmlForm) page51.getForms().get(0);
        validateSpecialReviewLine(form3, "document.propSpecialReviews[0]", "08/01/2007;;123;1;2;comment1");
        // 2nd line
        final HtmlPage page52 = setSpecialReviewLine(form3, "08/02/2007;;456;2;3;comment2");
        final HtmlForm form4 = (HtmlForm) page52.getForms().get(0);
        assertEquals("comment2 \n line2", getFieldValue(form4, TEXT_AREA, "newPropSpecialReview.comments"));
        final HtmlPage page53 = clickButton(form4, "methodToCall.addSpecialReview", IMAGE_INPUT);
        final HtmlForm form5 = (HtmlForm) page53.getForms().get(0);
        validateSpecialReviewLine(form5, "document.propSpecialReviews[0]", "08/01/2007;;123;1;2;comment1");
        validateSpecialReviewLine(form5, "document.propSpecialReviews[1]", "08/02/2007;;456;2;3;comment2");

        // delete special review line 0
        final HtmlPage page6 = clickButton(form5, "methodToCall.deleteSpecialReview.line0.", IMAGE_INPUT);
        final HtmlForm form6 = (HtmlForm) page6.getForms().get(0);
        validateSpecialReviewLine(form6, "document.propSpecialReviews[0]", "08/02/2007;;456;2;3;comment2");
        // save
        final HtmlPage page7 = clickButton(form6, "methodToCall.save", IMAGE_INPUT);
        assertEquals("Kuali :: Proposal Development Document", page6.getTitleText());
        final HtmlForm form7 = (HtmlForm) page7.getForms().get(0);
        // one of the following to check save is OK
        assertFalse(page7.asText().contains(ERRORS_FOUND_ON_PAGE));
        assertTrue(page7.asText().contains("Document was successfully saved"));
        validateSpecialReviewLine(form7, "document.propSpecialReviews[0]", "08/02/2007;;456;2;3;comment2");

    }

    @Test
    public void testExpandedTextArea() throws Exception {
        // remove it later
        String fieldName = "document.title";
        String fieldText = "project title";
        String methodToCall = "methodToCall.updateTextArea.((#" + fieldName + ":proposalDevelopmentProposal:Project Title#))";
        //final HtmlPage page5 = textAreaPop(fieldName, fieldText, methodToCall, true);
        final HtmlPage page5=textAreaPop(fieldName, fieldText, methodToCall,false);
        final HtmlForm form2 = (HtmlForm) page5.getForms().get(0);
        assertEquals(fieldText + " \n line2", getFieldValue(form2, TEXT_AREA, fieldName));

    }


    private HtmlPage textAreaPop(String fieldName, String fieldText, String methodToCall, boolean scriptEnabled) throws Exception {
        final WebClient webClient = new WebClient();
        final URL url = new URL("http://localhost:" + getPort() + "/kra-dev/");
        final HtmlPage page3 = login(webClient, url,
                "proposalDevelopmentProposal.do?methodToCall=docHandler&command=initiate&docTypeName=ProposalDevelopmentDocument");
        assertEquals("Kuali :: Proposal Development Document", page3.getTitleText());

        // collection of alerts from js
        //final List collectedAlerts = new ArrayList();
        //webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlForm kualiForm = (HtmlForm) page3.getForms().get(0);
        setFieldValue(kualiForm, TEXT_AREA, fieldName, fieldText);
        // js or non-js
        if (!scriptEnabled) {
            webClient.setJavaScriptEnabled(false);
        }
        final HtmlPage page4 = clickButton(kualiForm, methodToCall, IMAGE_INPUT);
        final HtmlForm form1 = (HtmlForm) page4.getForms().get(0);
        assertEquals(fieldText, getFieldValue(form1, TEXT_AREA, fieldName));
        setFieldValue(form1, TEXT_AREA, fieldName, fieldText + " \n line2");

        return clickButton(form1, "methodToCall.postTextAreaToParent", IMAGE_INPUT);
        // final HtmlForm form2 = (HtmlForm) page5.getForms().get(0);
        // final HtmlTextArea textArea1 = (HtmlTextArea) form2.getTextAreasByName(fieldName).get(0);
        // assertEquals(fieldText+" \n line2", textArea1.getText());


    }


    private HtmlPage login(WebClient webClient, URL url, String loginLocation) throws Exception {
        final HtmlPage page1 = (HtmlPage) webClient.getPage(url);
        assertEquals("Kuali Portal Index", page1.getTitleText());

        // LOGIN
        final HtmlPage page2 = (HtmlPage) webClient.getPage(url + loginLocation);

        // Get the form that we are dealing with and within that form,
        // find the submit button and the field that we want to change.
        final HtmlForm form = (HtmlForm) page2.getForms().get(0);

        // Now submit the form by clicking the button and get back the
        // second page.
        return clickButton(form, "Login", SUBMIT_INPUT_BY_VALUE);

    }


    // should be able to make one lookup method for all single value lookup
    private HtmlPage lookup(WebClient webClient, HtmlImageInput lookupButton, String selectedFieldValue, String returnProperty,
            String searchField) throws Exception {
        final URL url = new URL("http://localhost:" + getPort() + "/kra-dev/");

        final HtmlPage page41 = (HtmlPage) lookupButton.click();
        final HtmlForm lookupForm = (HtmlForm) page41.getForms().get(0);
        setFieldValue(lookupForm, TEXT_INPUT, searchField, selectedFieldValue);
        final HtmlPage page42 = clickButton(lookupForm, "methodToCall.search", IMAGE_INPUT);
        assertTrue(page42.asText().contains("Return value " + selectedFieldValue));
        int idx1 = page42.asXml().indexOf(returnProperty);
        int idx2 = page42.asXml().indexOf("anchor=topOfForm", idx1);
        String returnPath = page42.asXml().substring(idx1, idx2 + 16).replace("&amp;", "&");
        return (HtmlPage) webClient.getPage(url + returnPath);

    }

    private void setFieldValue(HtmlForm htmlForm, int type, String fieldName, String value) {
        setFieldValue(htmlForm, type, fieldName, value, -1);
    }

    private void setFieldValue(HtmlForm htmlForm, int type, String fieldName, String value, int optionSize) {
        switch (type) {
            case TEXT_INPUT:
                final HtmlTextInput text = (HtmlTextInput) htmlForm.getInputByName(fieldName);
                text.setValueAttribute(value);
                break;
            case TEXT_AREA:
                final HtmlTextArea textArea = (HtmlTextArea) htmlForm.getTextAreasByName(fieldName).get(0);
                textArea.setText(value);
                break;
            case SELECTED_INPUT:
                final HtmlSelect selected = (HtmlSelect) htmlForm.getSelectByName(fieldName);
                selected.setSelectedAttribute(value, true);
                if (optionSize != -1) {
                    assertEquals(optionSize, selected.getOptionSize());
                }
                break;

            default:
                assertTrue(false);
                break;
        }
    }

    private String getFieldValue(HtmlForm htmlForm, int type, String fieldName) {
        switch (type) {
            case TEXT_INPUT:
                final HtmlTextInput text = (HtmlTextInput) htmlForm.getInputByName(fieldName);
                return text.getValueAttribute();
            case HIDDEN_INPUT:
                final HtmlHiddenInput hiddenText = (HtmlHiddenInput) htmlForm.getInputByName(fieldName);
                return hiddenText.getValueAttribute();
            case TEXT_AREA:
                final HtmlTextArea textArea = (HtmlTextArea) htmlForm.getTextAreasByName(fieldName).get(0);
                return textArea.getText();
            case SELECTED_INPUT:
                final HtmlSelect selected = (HtmlSelect) htmlForm.getSelectByName(fieldName);
                return ((HtmlOption) (selected.getSelectedOptions().get(0))).getValueAttribute();

            default:
                assertTrue(false);
                return null;
        }
    }

    private HtmlPage clickButton(HtmlForm htmlForm, String buttonName, int type) throws Exception {
        switch (type) {
            case IMAGE_INPUT:
                final HtmlImageInput button = (HtmlImageInput) htmlForm.getInputByName(buttonName);
                return (HtmlPage) button.click();
            case SUBMIT_INPUT_BY_NAME:
                final HtmlSubmitInput button1 = (HtmlSubmitInput) htmlForm.getInputByName(buttonName);
                return (HtmlPage) button1.click();
            case SUBMIT_INPUT_BY_VALUE:
                final HtmlSubmitInput button2 = (HtmlSubmitInput) htmlForm.getInputByValue(buttonName);
                return (HtmlPage) button2.click();
            default:
                assertTrue(false);
                return null;
        }
    }

    private void setupProposalDevelopmentDocumentRequiredFields(HtmlForm kualiForm) throws Exception {

        setFieldValue(kualiForm, TEXT_INPUT, "document.documentHeader.financialDocumentDescription",
                "ProposalDevelopmentDocumentWebTest test");
        setFieldValue(kualiForm, TEXT_INPUT, "document.sponsorCode", "123456");
        setFieldValue(kualiForm, TEXT_AREA, "document.title", "project title");
        setFieldValue(kualiForm, TEXT_INPUT, "document.requestedStartDateInitial", "08/14/2007");
        setFieldValue(kualiForm, TEXT_INPUT, "document.requestedEndDateInitial", "08/21/2007");
        setFieldValue(kualiForm, SELECTED_INPUT, "document.activityTypeCode", "1", 10);
        setFieldValue(kualiForm, SELECTED_INPUT, "document.proposalTypeCode", "1", 10);
        setFieldValue(kualiForm, SELECTED_INPUT, "document.ownedByUnit", "000002", 3);

    }

    private void verifySavedRequiredFields(ProposalDevelopmentDocument doc) {

        assertEquals("1", doc.getActivityTypeCode());
        assertEquals("000002", doc.getOwnedByUnit());
        assertEquals("ProposalDevelopmentDocumentWebTest test", doc.getDocumentHeader().getFinancialDocumentDescription());
        assertEquals("123456", doc.getSponsorCode());
        assertEquals("project title", doc.getTitle());
        assertEquals("2007-08-14", doc.getRequestedStartDateInitial().toString());
        assertEquals("2007-08-21", doc.getRequestedEndDateInitial().toString());
        assertEquals("1", doc.getProposalTypeCode());

    }

    private void validateSpecialReviewLine(HtmlForm kualiForm, String prefix, String paramList) throws Exception {
        String[] params = paramList.split(";");
        assertEquals(params[0], getFieldValue(kualiForm, TEXT_INPUT, prefix + ".applicationDate"));
        assertEquals(params[1], getFieldValue(kualiForm, TEXT_INPUT, prefix + ".approvalDate"));
        assertEquals(params[2], getFieldValue(kualiForm, TEXT_INPUT, prefix + ".protocolNumber"));
        assertEquals(params[3], getFieldValue(kualiForm, SELECTED_INPUT, prefix + ".specialReviewCode"));
        assertEquals(params[4], getFieldValue(kualiForm, SELECTED_INPUT, prefix + ".approvalTypeCode"));

        // comments - textarea
        assertEquals(params[5] + " \n line2", getFieldValue(kualiForm, TEXT_AREA, prefix + ".comments"));
    }

    private HtmlPage setSpecialReviewLine(HtmlForm kualiForm, String paramList) throws Exception {
        String[] params = paramList.split(";");
        // in "application date; approval date; protocol#; special review code; approval type; comments" order
        setFieldValue(kualiForm, TEXT_INPUT, "newPropSpecialReview.applicationDate", params[0]);
        setFieldValue(kualiForm, TEXT_INPUT, "newPropSpecialReview.approvalDate", params[1]);
        setFieldValue(kualiForm, TEXT_INPUT, "newPropSpecialReview.protocolNumber", params[2]);
        setFieldValue(kualiForm, SELECTED_INPUT, "newPropSpecialReview.specialReviewCode", params[3], 13);
        setFieldValue(kualiForm, SELECTED_INPUT, "newPropSpecialReview.approvalTypeCode", params[4], 6);

        // comments - textarea
        setFieldValue(kualiForm, TEXT_AREA, "newPropSpecialReview.comments", params[5]);

        final HtmlPage page = clickButton(kualiForm,
                "methodToCall.updateTextArea.((#newPropSpecialReview.comments:proposalDevelopmentSpecialReview:Comments#))",
                IMAGE_INPUT);
        final HtmlForm form = (HtmlForm) page.getForms().get(0);
        assertEquals(params[5], getFieldValue(form, TEXT_AREA, "newPropSpecialReview.comments"));
        setFieldValue(form, TEXT_AREA, "newPropSpecialReview.comments", params[5] + " \n line2");
        return clickButton(form, "methodToCall.postTextAreaToParent", IMAGE_INPUT);

    }


}
