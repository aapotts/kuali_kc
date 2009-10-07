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
package org.kuali.kra.irb.web;

import org.junit.Before;
import org.junit.Test;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.irb.ProtocolDocument;
import org.kuali.kra.irb.ProtocolVersionService;
import org.kuali.kra.irb.test.ProtocolFactory;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.test.data.PerSuiteUnitTestData;
import org.kuali.rice.test.data.UnitTestData;
import org.kuali.rice.test.data.UnitTestFile;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Test the simple Request Actions for a Protocol.  These actions are:
 * Request Close, Request a Suspension, Request Close Enrollment, Request Re-open Enrollment,
 * and Request Data Analysis.
 */
@PerSuiteUnitTestData(
        @UnitTestData(
            sqlFiles = {
                @UnitTestFile(filename = "classpath:sql/dml/load_SUBMISSION_TYPE.sql", delimiter = ";")
               ,@UnitTestFile(filename = "classpath:sql/dml/load_protocol_review_type.sql", delimiter = ";")
               ,@UnitTestFile(filename = "classpath:sql/dml/load_PROTOCOL_ACTION_TYPE.sql", delimiter = ";")
            }
        )
    )
public class ProtocolSummaryWebTest extends ProtocolWebTestBase {
    
    private ProtocolVersionService versionService;
    private ProtocolDocument protocolDocument1;
    private ProtocolDocument protocolDocument2;
    private ProtocolDocument protocolDocument3;
    
    @Before
    public void setUp() throws Exception {
        super.setUp();    
        versionService = KraServiceLocator.getService(ProtocolVersionService.class);
        loadProtocolVersions();
    }
    
    private void loadProtocolVersions() throws Exception {
        TransactionTemplate template = new TransactionTemplate(KNSServiceLocator.getTransactionManager());
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
      
        template.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
                try {
                    protocolDocument1 = ProtocolFactory.createProtocolDocument();
                    getDocumentService().saveDocument(protocolDocument1);
                        
                    protocolDocument2 = versionService.versionProtocolDocument(protocolDocument1);
                    getDocumentService().saveDocument(protocolDocument2);
                       
                    protocolDocument3 = versionService.versionProtocolDocument(protocolDocument2);
                    getDocumentService().saveDocument(protocolDocument3);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                    
                return null;
            }
        });
    }
    
    /**
     * Open up the third (latest) protocol version and go to the 
     * Action tab.  From there click on the "previous" button to traverse
     * back to the previous versions.  Then click on the "next" button
     * to get back to the third version again.
     * @throws Exception
     */
    @Test
    public void testSummaryTraversal() throws Exception {
       
        String docNbr = protocolDocument3.getDocumentNumber();
        
        HtmlPage protocolPage = docSearch(docNbr);
        assertNotNull(protocolPage);
        
        HtmlPage actionsPage = clickOnTab(protocolPage, PROTOCOL_ACTIONS_LINK_NAME);
        
        HtmlElement element = getElementById(actionsPage, "summarySequence");
        assertNotNull(element);
        String text = element.asText().trim();
        assertEquals("Sequence 3/3:", text);
        
        HtmlElement viewPrev = getElementByName(actionsPage, "methodToCall.viewPreviousProtocolSummary", true);
        actionsPage = clickOn(viewPrev);
        element = getElement(actionsPage, "summarySequence");
        text = element.asText().trim();
        assertEquals("Sequence 2/3:", text);
        
        viewPrev = getElementByName(actionsPage, "methodToCall.viewPreviousProtocolSummary", true);
        actionsPage = clickOn(viewPrev);
        element = getElement(actionsPage, "summarySequence");
        text = element.asText().trim();
        assertEquals("Sequence 1/3:", text);
        
        HtmlElement viewNext = getElementByName(actionsPage, "methodToCall.viewNextProtocolSummary", true);
        actionsPage = clickOn(viewNext);
        element = getElement(actionsPage, "summarySequence");
        text = element.asText().trim();
        assertEquals("Sequence 2/3:", text);
        
        viewNext = getElementByName(actionsPage, "methodToCall.viewNextProtocolSummary", true);
        actionsPage = clickOn(viewNext);
        element = getElement(actionsPage, "summarySequence");
        text = element.asText().trim();
        assertEquals("Sequence 3/3:", text);
    }
}
