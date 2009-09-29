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
package org.kuali.kra.irb.auth;

import static org.junit.Assert.assertEquals;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.kra.infrastructure.PermissionConstants;
import org.kuali.kra.infrastructure.TaskName;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.actions.ProtocolActionType;
import org.kuali.kra.irb.actions.submit.ProtocolActionService;
import org.kuali.kra.service.KraAuthorizationService;

/**
 * Test the Protocol Withdraw Authorizer.
 */
@RunWith(JMock.class)
public class ProtocolWithdrawAuthorizerTest {

    private static final String USERNAME = "quickstart";
    private static final String PROTOCOL_NUMBER = "0906000001";
    
    private Mockery context = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
    @Test
    public void testHasPermission() {
        runTest(PROTOCOL_NUMBER, true, true, true);
    }
    
    @Test
    public void testNotAProtocol() {
        runTest(PROTOCOL_NUMBER + "A001", true, true, false);
    }
    
    @Test
    public void testNoPermission() {
        runTest(PROTOCOL_NUMBER, false, true, false);
    }
    
    @Test
    public void testActionNotAllowed() {
        runTest(PROTOCOL_NUMBER, true, false, false);
    }
    
    private void runTest(final String protocolNumber, final boolean hasPermission, final boolean isActionAllowed, boolean expected) {
        ProtocolWithdrawAuthorizer authorizer = new ProtocolWithdrawAuthorizer();
        
        final Protocol protocol = context.mock(Protocol.class);
        context.checking(new Expectations() {{
            allowing(protocol).getProtocolNumber(); will(returnValue(protocolNumber));
        }});
        
        final KraAuthorizationService authorizationService = context.mock(KraAuthorizationService.class);
        context.checking(new Expectations() {{
            allowing(authorizationService).hasPermission(USERNAME, protocol, PermissionConstants.SUBMIT_PROTOCOL); will(returnValue(hasPermission));
        }});
        authorizer.setKraAuthorizationService(authorizationService);
        
        final ProtocolActionService actionService = context.mock(ProtocolActionService.class);
        context.checking(new Expectations() {{
            allowing(actionService).isActionAllowed(ProtocolActionType.WITHDRAWN, protocol); will(returnValue(isActionAllowed));
        }});
        authorizer.setProtocolActionService(actionService);
        
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_WITHDRAW, protocol);
        assertEquals(expected, authorizer.isAuthorized(USERNAME, task));
    }
}
