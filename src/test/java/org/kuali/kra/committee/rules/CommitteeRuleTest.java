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
package org.kuali.kra.committee.rules;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.core.UserSession;
import org.kuali.core.service.DocumentService;
import org.kuali.core.util.ErrorMap;
import org.kuali.core.util.GlobalVariables;
import org.kuali.kra.KraTestBase;
import org.kuali.kra.committee.bo.Committee;
import org.kuali.kra.committee.document.CommitteeDocument;
import org.kuali.kra.committee.rules.CommitteeDocumentRule;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.rice.KNSServiceLocator;
import org.kuali.rice.test.data.PerSuiteUnitTestData;
import org.kuali.rice.test.data.UnitTestData;
import org.kuali.rice.test.data.UnitTestFile;

import edu.iu.uis.eden.exception.WorkflowException;

/**
 * Test the Committee Rules.
 */
@PerSuiteUnitTestData(
    @UnitTestData(
        sqlFiles = {
            @UnitTestFile(filename = "classpath:sql/dml/load_committee_type.sql", delimiter = ";")
           ,@UnitTestFile(filename = "classpath:sql/dml/load_protocol_review_type.sql", delimiter = ";")
        }
    )
)
public class CommitteeRuleTest extends KraTestBase {

    protected DocumentService documentService = null;
    private CommitteeDocumentRule rule;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        GlobalVariables.setUserSession(new UserSession("aslusar"));
        GlobalVariables.setErrorMap(new ErrorMap());
        GlobalVariables.setAuditErrorMap(new HashMap());
        documentService = KNSServiceLocator.getDocumentService();
        rule = new CommitteeDocumentRule();
    }

    @After
    public void tearDown() throws Exception {
        GlobalVariables.setUserSession(null);
        GlobalVariables.setErrorMap(null);
        GlobalVariables.setAuditErrorMap(null);
        documentService = null;
        super.tearDown();
    }
    
    /**
     * Test the required fields in the committee.  
     * @throws Exception
     */
    @Test
    public void testRequiredFields() throws Exception {
        
        /*
         * Create a committee without setting any properties.
         */
        CommitteeDocument document = getNewCommitteeDocument();

        /*
         * Process the Save Document rule.  Since we didn't set
         * any properties, each of the required fields should 
         * result in an error.
         */
        boolean rulesPassed = rule.processSaveDocument(document);
        assertFalse(rulesPassed);
        
        /*
         * There should be nine required fields.
         */
        ErrorMap errorMap = GlobalVariables.getErrorMap();
        assertEquals(9, errorMap.getErrorCount());
        
        /*
         * Verify that the error keys for each of the required fields 
         * is in the ErrorMap.
         */
        assertTrue(errorMap.containsKey("document.documentHeader.documentDescription"));
        assertTrue(errorMap.containsKey("document.committeeList[0].committeeTypeCode"));
        assertTrue(errorMap.containsKey("document.committeeList[0].maxProtocols"));
        assertTrue(errorMap.containsKey("document.committeeList[0].homeUnitNumber"));
        assertTrue(errorMap.containsKey("document.committeeList[0].minimumMembersRequired"));
        assertTrue(errorMap.containsKey("document.committeeList[0].committeeName"));
        assertTrue(errorMap.containsKey("document.committeeList[0].advancedSubmissionDaysRequired"));
        assertTrue(errorMap.containsKey("document.committeeList[0].reviewTypeCode"));
        assertTrue(errorMap.containsKey("document.committeeList[0].committeeId"));
    }
    
    /**
     * The committee IDs are required to be unique. 
     * @throws Exception
     */
    @Test
    public void testDuplicateIds() throws Exception {
        
        CommitteeDocument document = getNewCommitteeDocument();
        setCommitteeProperties(document);
        documentService.saveDocument(document);
       
        document = getNewCommitteeDocument();
        setCommitteeProperties(document);
        
        /*
         * Verify that we can't save a committee with a duplicate Committee ID.
         */
        boolean rulesPassed = rule.processSaveDocument(document);
        assertFalse(rulesPassed);
        
        ErrorMap errorMap = GlobalVariables.getErrorMap();
        assertTrue(errorMap.containsMessageKey(KeyConstants.ERROR_COMMITTEE_DUPLICATE_ID));
    }
    
    /**
     * The home unit number must be valid.
     * @throws Exception
     */
    @Test
    public void testInvalidHomeUnit() throws Exception {
        
        CommitteeDocument document = getNewCommitteeDocument();
        setCommitteeProperties(document);
        document.getCommittee().setHomeUnitNumber("xxx");
       
        /*
         * Verify that we can't save a committee with a duplicate Committee ID.
         */
        boolean rulesPassed = rule.processSaveDocument(document);
        assertFalse(rulesPassed);
        
        ErrorMap errorMap = GlobalVariables.getErrorMap();
        assertTrue(errorMap.containsMessageKey(KeyConstants.ERROR_INVALID_UNIT));
    }
    
    /**
     * Set the required fields for a committee.
     * @param document
     */
    private void setCommitteeProperties(CommitteeDocument document) {
        Committee committee = document.getCommittee();
        document.getDocumentHeader().setDocumentDescription("test");
        committee.setCommitteeId("888");
        committee.setCommitteeName("test");
        committee.setCommitteeTypeCode("1");
        committee.setHomeUnitNumber("000001");
        committee.setCommitteeDescription("description");
        committee.setMaxProtocols(5);
        committee.setMinimumMembersRequired(4);
        committee.setAdvancedSubmissionDaysRequired(3);
        committee.setReviewTypeCode("1");
        committee.setScheduleDescription("schedule description");
    }
    
    /**
     * Get a new Committee Document.
     * 
     * @return a new Committee Document.
     * @throws WorkflowException
     */
    protected CommitteeDocument getNewCommitteeDocument() throws WorkflowException {
        return (CommitteeDocument) documentService.getNewDocument("CommitteeDocument");
    }
}