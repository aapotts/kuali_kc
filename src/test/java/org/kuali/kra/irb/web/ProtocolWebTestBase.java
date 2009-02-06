/*
 * Copyright 2006-2008 The Kuali Foundation
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
package org.kuali.kra.irb.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.kuali.core.service.DocumentService;
import org.kuali.kra.KraWebTestBase;
import org.kuali.kra.irb.bo.Protocol;
import org.kuali.kra.irb.bo.ProtocolInvestigator;
import org.kuali.kra.irb.document.ProtocolDocument;
import org.kuali.rice.KNSServiceLocator;
import org.kuali.rice.test.data.PerSuiteUnitTestData;
import org.kuali.rice.test.data.UnitTestData;
import org.kuali.rice.test.data.UnitTestFile;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import edu.iu.uis.eden.exception.WorkflowException;

/**
 * Abstract Protocol Web Test base class provides common functionalities required by extended class.
 */
@PerSuiteUnitTestData(@UnitTestData(sqlFiles = {
        @UnitTestFile(filename = "classpath:sql/dml/load_system_params.sql", delimiter = ";"),
        @UnitTestFile(filename = "classpath:sql/dml/load_protocol_status.sql", delimiter = ";"),
        @UnitTestFile(filename = "classpath:sql/dml/load_PROTOCOL_ORG_TYPE.sql", delimiter = ";"),
        @UnitTestFile(filename = "classpath:sql/dml/load_PROTOCOL_PERSON_ROLES.sql", delimiter = ";"),
        @UnitTestFile(filename = "classpath:sql/dml/load_protocol_type.sql", delimiter = ";") }))
public abstract class ProtocolWebTestBase extends KraWebTestBase {
    

    /* check for save success - any errors found in the page */
    protected static final String ERRORS_FOUND_ON_PAGE = "error(s) found on page";
    protected static final String SAVE_SUCCESS_MESSAGE = "Document was successfully saved";
    
    // KEW Struts Constants
    protected static final String KUALI_FORM_NAME = "KualiForm";
    protected static final String KUALI_DOCUMENT_NUMBER = "document.documentHeader.documentNumber";
    protected static final String SAVE_PAGE = "methodToCall.save";    
    protected static final String DEFAULT_DOCUMENT_DESCRIPTION = "Protocol Document";
    protected static final String PROTOCOL_STATUS_STR = "100"; //test of option "Pending/In Progress";
    protected static final String PROTOCOL_TYPE_CODE_STR = "1";//test of option "Standard";
    protected static final String PROTOCOL_TITLE_STR = "New protocol test";

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ProtocolWebTestBase.class);

    // Services
    private DocumentService documentService;

    // Set by child class
    private ProtocolDocument protocolDocument;

    /**
     * protocol required fields
     */
    protected enum ProtocolRequiredFields {       
        DOCUMENT_DESCRIPTION("document.documentHeader.documentDescription", DEFAULT_DOCUMENT_DESCRIPTION),
        PROTOCOL_TYPE_CODE("document.protocol.protocolTypeCode", PROTOCOL_TYPE_CODE_STR),
        PROTOCOL_TITLE("document.protocol.title", PROTOCOL_TITLE_STR),
     //   PROTOCOL_STATUS_ID("document.protocol.protocolStatusCode"),
        PROTOCOL_PI_ID("protocolHelper.personId", "000000001"),
        PROTOCOL_PI_NAME("protocolHelper.principalInvestigatorName", "Terry Durkin"),
        PROTOCOL_LEAD_UNIT_NUM("protocolHelper.leadUnitNumber", "BL-BL");
                
        private final String code;   
        private final String value;

        ProtocolRequiredFields(String code, String value){
            this.code = code;
            this.value = value;          
        }

        public String getCode() {   
            return code; 
        }

        public String getValue() { 
            return value; 
        }

    }
    


    
    private HtmlPage protocolHomePage;    

    /**
     * Web test setup overloading. Sets up DocumentService and asserts.
     * @see org.kuali.kra.KraWebTestBase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        setDocumentService(KNSServiceLocator.getDocumentService());
        // Assert documentService, required to test save feature of form
        assertNotNull(getDocumentService());
        setProtocolHomePage(buildProtocolDocumentPage());
    }

    /**
     * Web test tear down overloading.
     * @see org.kuali.kra.KraWebTestBase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * @see org.kuali.kra.KraTestBase#setDocumentService(org.kuali.core.service.DocumentService)
     */
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }
    
    /**
     * @see org.kuali.kra.KraTestBase#getDocumentService()
     */
    public DocumentService getDocumentService() {
        return documentService;
    }
    
    /**
     * This method sets ProtocolDoucment, if null is passed along, it tries to get handle using Kuali Struts constant.
     * @param protocolDocument <code>{@link ProtocolDocument}</code>
     * @throws WorkflowException
     */
    public void setProtocolDocument(ProtocolDocument protocolDocument, HtmlPage page) throws WorkflowException {
        if (null == protocolDocument) {
            HtmlHiddenInput documentNumber = (HtmlHiddenInput) page.getFormByName(KUALI_FORM_NAME).getInputByName(
                    KUALI_DOCUMENT_NUMBER);
            this.protocolDocument = (ProtocolDocument) getDocumentService().getByDocumentHeaderId(documentNumber.getDefaultValue());
        }
        assertNotNull(getProtocolDocument());
    }

    /**
     * This method returns PrtocolDocument
     * @return <code>{@link ProtocolDocument}</code>
     */
    public ProtocolDocument getProtocolDocument() {
        return protocolDocument;
    }
    
    /**
     * Create a new instance of the protocol document page by clicking on the link to the portal page. 
     * The resulting page of the click through is a frame, so it is important to get the inner page.
     * 
     * @return <code>{@link HtmlPage}</code> instance of the protocol document page
     * @throws IOException
     */
    protected final HtmlPage buildProtocolDocumentPage() throws Exception {
        HtmlPage retval = clickOn(getPortalPage(), "Create Protocol", "Kuali Portal Index");
        retval = getInnerPages(retval).get(0);
        System.out.println(retval.getTitleText());
        assertTrue("Kuali :: Protocol Document".equals(retval.getTitleText()));
        return retval;
    }
    
    /**
     * Sets the protocol page for tests. Typically, run out of <code>{@link #setUp()}</code>
     * 
     * @param protocolHomePage <code>{@link HtmlPage}</code> instance for the test
     */
    protected final void setProtocolHomePage(HtmlPage protocolHomePage) {
        this.protocolHomePage = protocolHomePage;
    }

    /**
     * Gets the Protocol Home web page for creating a new Protocol document.
     * We don't want to test within the Portal.  This means that we will extract the
     * protocol web page from within the Portal's Inline Frame (iframe).
     * 
     * @return the Protocol Home web page.
     */
    protected final HtmlPage getProtocolHomePage() {
        return this.protocolHomePage;
    }

    /**
     * Gets the Protocol Page after saving all required fields - creating a new Protocol document.
     * 
     * @return the Protocol page after saving all required fields.
     */
    protected final HtmlPage getProtocolSavedRequiredFieldsPage() throws Exception {
        HtmlPage protocolPage = getProtocolHomePage();
        setProtocolRequiredFields(protocolPage);
        protocolPage = savePage(protocolPage);
        validateSavedPage(protocolPage);
        return protocolPage;
    }

    /**
     * Sets the required fields for a Protocol document.
     * 
     * @param page - protocol web page.
     */
    protected void setProtocolRequiredFields(HtmlPage page){
        setFieldValues(page, getProtocolRequiredFieldsMap());
    }

    /**
     * This method is to construct a map of required fields
     * linked to enum ProtocolRequiredFields declared on top
     * @return protocol document required fields and values
     */
    protected Map<String,String> getProtocolRequiredFieldsMap(){
        Map<String,String> requiredFieldMap = new HashMap<String,String>(); 
        for (ProtocolRequiredFields protocolRequiredFields : ProtocolRequiredFields.values()) {
            requiredFieldMap.put(protocolRequiredFields.getCode(), protocolRequiredFields.getValue());
        }
        return requiredFieldMap;
    }

    /**
     * 
     * This method gets the values mentioned in the map and set it to the page.
     * It uses setFieldValue(HtmlPage,string,string) method to set the value to the page by using key.
     * @param page
     * @param keyValues
     */
    protected void setFieldValues(HtmlPage page, Map<String, String> keyValues) {
        Iterator<String> it = keyValues.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            setFieldValue(page, key, keyValues.get(key));
        }
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
     * This method is to select a tag by given name
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
     * 
     * This method is to test the <code>ExtendedTextArea</code> tag
     * @param page
     * @param textAreaFieldName
     * @param moreTextToBeAdded
     * @param action
     * @param textAreaLabel
     * @param tabIndex
     * @throws Exception
     */
    protected void testTextAreaPopup(HtmlPage page, String textAreaFieldName,String moreTextToBeAdded,String action,String textAreaLabel,String tabIndex) throws Exception{
        HtmlPage textAreaPopupPage = clickOn(page, "methodToCall.updateTextArea.((#"+textAreaFieldName+":"+action+":"+textAreaLabel+"#))"+tabIndex);
        String currentValue = getFieldValue(textAreaPopupPage, textAreaFieldName);
        String completeText = currentValue+moreTextToBeAdded;
        setFieldValue(textAreaPopupPage, textAreaFieldName, completeText);
        super.assertContains(textAreaPopupPage, textAreaLabel);
        HtmlPage textAreasAddedPage = clickOn(textAreaPopupPage,"methodToCall.postTextAreaToParent.anchor"+tabIndex);
        assertEquals(getFieldValue(textAreasAddedPage, textAreaFieldName), completeText);
    }
    
    
    /**
     * This method asserts whether required fields where saved
     */
    protected void verifySavedRequiredFields() {
        assertEquals(DEFAULT_DOCUMENT_DESCRIPTION, getProtocolDocument().getDocumentHeader().getDocumentDescription()); 
        Protocol theProtocol = getProtocolDocument().getProtocol();
        assertEquals(PROTOCOL_STATUS_STR, theProtocol.getProtocolStatus().getProtocolStatusCode());
        assertEquals(PROTOCOL_TYPE_CODE_STR, theProtocol.getProtocolType().getProtocolTypeCode());
        assertEquals(PROTOCOL_TITLE_STR, theProtocol.getTitle());
        ProtocolInvestigator thePi = theProtocol.getPrincipalInvestigator();
        assertTrue(ProtocolRequiredFields.PROTOCOL_PI_NAME.value.compareTo(thePi.getPersonName())==0);
        assertTrue(ProtocolRequiredFields.PROTOCOL_PI_ID.value.compareTo( thePi.getPersonId())==0);        
    }
    
    private void removedCode() {
    }
}
