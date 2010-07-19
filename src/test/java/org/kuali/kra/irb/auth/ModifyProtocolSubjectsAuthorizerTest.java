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

import org.jmock.integration.junit4.JMock;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.kuali.kra.infrastructure.TaskName;
import org.kuali.kra.irb.actions.amendrenew.ProtocolModule;

/**
 * Test the Modify Protocol Subjects Authorizer.
 */
@Ignore
@RunWith(JMock.class)
public class ModifyProtocolSubjectsAuthorizerTest extends ModifyProtocolModuleAuthorizerTest {

    @Override
    protected ModifyAmendmentAuthorizer createAuthorizer() {
        return new ModifyProtocolSubjectsAuthorizer();
    }
    
    @Override
    protected String getTestModuleTypeCode() {
        return ProtocolModule.SUBJECTS;
    }
    
    @Override
    protected String getFalseModuleTypeCode() {
        return ProtocolModule.ADD_MODIFY_ATTACHMENTS;
    }

    @Override
    protected String getTaskName() {
        return TaskName.MODIFY_PROTOCOL_SUBJECTS;
    }
}
