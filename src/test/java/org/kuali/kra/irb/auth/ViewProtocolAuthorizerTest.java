/*
 * Copyright 2005-2010 The Kuali Foundation
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.kra.infrastructure.PermissionConstants;
import org.kuali.kra.infrastructure.TaskName;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.auth.ProtocolTask;
import org.kuali.kra.irb.auth.ViewProtocolAuthorizer;
import org.kuali.kra.irb.protocol.location.ProtocolLocation;
import org.kuali.kra.irb.protocol.location.ProtocolLocationService;
import org.kuali.kra.service.KraAuthorizationService;

/**
 * Test the View Protocol Authorizer.
 */
@RunWith(JMock.class)
public class ViewProtocolAuthorizerTest {

    private static final String USERNAME = "quickstart";
    
    private Mockery context = new JUnit4Mockery();
    
    @Test
    public void testViewPermission() {
//from trunk
//        ViewProtocolAuthorizer authorizer = new ViewProtocolAuthorizer();
//        
//        final Protocol protocol = new Protocol(){
//            @Override
//            public void refreshReferenceObject(String referenceObjectName) {}
//            
//        };
//        final KraAuthorizationService protocolAuthorizationService = context.mock(KraAuthorizationService.class);
//        context.checking(new Expectations() {{
//            one(protocolAuthorizationService).hasPermission(USERNAME, protocol, PermissionConstants.VIEW_PROTOCOL); will(returnValue(true));
//        }});
//        authorizer.setKraAuthorizationService(protocolAuthorizationService);
//        
//        ProtocolTask task = new ProtocolTask(TaskName.VIEW_PROTOCOL, protocol);
//        assertEquals(true, authorizer.isAuthorized(USERNAME, task));

    }
    
    @Test
    public void testNotViewPermission() {
//from trunk
//        ViewProtocolAuthorizer authorizer = new ViewProtocolAuthorizer();
//        
//        final Protocol protocol = new Protocol(){
//            @Override
//            public void refreshReferenceObject(String referenceObjectName) {}
//            
//        };
//        
//        final KraAuthorizationService protocolAuthorizationService = context.mock(KraAuthorizationService.class);
//        context.checking(new Expectations() {{
//            one(protocolAuthorizationService).hasPermission(USERNAME, protocol, PermissionConstants.VIEW_PROTOCOL); will(returnValue(false));
//        }});
//        authorizer.setKraAuthorizationService(protocolAuthorizationService);
//        
//        ProtocolTask task = new ProtocolTask(TaskName.VIEW_PROTOCOL, protocol);
//        assertEquals(false, authorizer.isAuthorized(USERNAME, task));

    }
}
