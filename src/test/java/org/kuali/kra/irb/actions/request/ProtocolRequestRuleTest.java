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
package org.kuali.kra.irb.actions.request;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.irb.ProtocolDocument;
import org.kuali.kra.irb.actions.ProtocolActionType;
import org.kuali.kra.irb.actions.submit.ProtocolSubmissionType;
import org.kuali.kra.irb.test.ProtocolRuleTestBase;
import org.kuali.rice.kew.exception.WorkflowException;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kns.service.ParameterService;
import org.kuali.rice.kns.util.GlobalVariables;

/**
 * Test the business rules for Protocol Requests: close, suspend, open enrollment,
 * re-open enrollment, and data analysis.  The only business rule to be tested
 * is based upon whether or not the selection of a committee is required.  If required,
 * the system param for irb is set to "M" for mandatory.  Since the business
 * rule is the same for each of the mentioned requests, only the close request 
 * will be tested.
 * 
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
public class ProtocolRequestRuleTest extends ProtocolRuleTestBase {

    private ProtocolRequestRule rule = null;
 
    private static final String COMMITTEE_ID = "1";
    private static final String MANDATORY = "M";
    private static final String OPTIONAL = "O";
    private ParameterService parameterService;
    
    @Before
    public void setUpServices() {
        this.parameterService = KraServiceLocator.getService(ParameterService.class);
        this.parameterService.clearCache();
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        rule = new ProtocolRequestRule();
    }

    @After
    public void tearDown() throws Exception {
        rule = null;
        super.tearDown();
    }

    /**
     * Test a request with the system param set to optional, which is loaded
     * at startup time.
     * @throws Exception
     */
    @SuppressWarnings({ "deprecation", "unchecked" })
    @Test
    public void testOK() throws Exception {
        ProtocolDocument document = getNewProtocolDocument();
        ProtocolRequestBean requestBean = new ProtocolRequestBean(ProtocolActionType.REQUEST_TO_CLOSE,
                                                                  ProtocolSubmissionType.REQUEST_TO_CLOSE, "protocolCloseRequestBean");
        ProtocolRequestEvent event = new ProtocolRequestEvent(document, Constants.PROTOCOL_CLOSE_REQUEST_PROPERTY_KEY, requestBean);
        
        assertTrue(rule.processRules(event));
        assertEquals(GlobalVariables.getErrorMap().size(), 0);
    }
    
    /**
     * If the mandatory flag has been set, we should get no
     * errors if the committee id has been set.
     * @throws WorkflowException
     */
    @SuppressWarnings({ "deprecation", "unchecked" })
    @Test
    public void testMandatoryOK() throws WorkflowException {
        setParameter(MANDATORY);
        
        ProtocolDocument document = getNewProtocolDocument();
        ProtocolRequestBean requestBean = new ProtocolRequestBean(ProtocolActionType.REQUEST_TO_CLOSE,
                                                                  ProtocolSubmissionType.REQUEST_TO_CLOSE, "protocolCloseRequestBean");
        requestBean.setCommitteeId(COMMITTEE_ID);
        ProtocolRequestEvent event = new ProtocolRequestEvent(document, Constants.PROTOCOL_CLOSE_REQUEST_PROPERTY_KEY, requestBean);
       
        assertTrue(rule.processRules(event));
        assertEquals(0, GlobalVariables.getErrorMap().size());
        
        setParameter(OPTIONAL);
    }
    
    /**
     * If the mandatory flag has been set, we should get an error message
     * if the committee id has not been set.
     * @throws WorkflowException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testMandatoryCommittee() throws WorkflowException {
        setParameter(MANDATORY);
        
        ProtocolDocument document = getNewProtocolDocument();
        ProtocolRequestBean requestBean = new ProtocolRequestBean(ProtocolActionType.REQUEST_TO_CLOSE,
                                                                  ProtocolSubmissionType.REQUEST_TO_CLOSE, "protocolCloseRequestBean");
        ProtocolRequestEvent event = new ProtocolRequestEvent(document, Constants.PROTOCOL_CLOSE_REQUEST_PROPERTY_KEY, requestBean);
       
        assertFalse(rule.processRules(event));
        assertError(Constants.PROTOCOL_CLOSE_REQUEST_PROPERTY_KEY + ".committeeId", 
                    KeyConstants.ERROR_PROTOCOL_COMMITTEE_NOT_SELECTED);
        
        setParameter(OPTIONAL);
    }
    
    /*
     * Set the IRB parameter for submission in order to make the committee
     * either mandatory or optional.
     */
    private void setParameter(String value) {
        // the tranaction handling is not really saved to db.
        // it is ok for testMandatoryOK, but in testMandatoryCommittee, OLE was thrown.
        // so have to try this to force it to save to db.
//        try {
//            super.transactionalLifecycle.stop();
//        }
//        catch (Exception e) {
//
//        }
        this.parameterService.setParameterForTesting(ProtocolDocument.class,
                Constants.PARAMETER_IRB_COMM_SELECTION_DURING_SUBMISSION, value);
        
//        try {
//            super.transactionalLifecycle.start();
//        }
//        catch (Exception e) {
//
//        }
    }
}
