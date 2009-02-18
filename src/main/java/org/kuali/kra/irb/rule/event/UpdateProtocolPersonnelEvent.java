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
package org.kuali.kra.irb.rule.event;

import org.kuali.core.document.Document;
import org.kuali.core.rule.BusinessRule;
import org.kuali.kra.irb.bo.ProtocolPerson;
import org.kuali.kra.irb.document.ProtocolDocument;
import org.kuali.kra.irb.rule.AddProtocolPersonnelRule;
import org.kuali.kra.irb.rule.UpdateProtocolPersonnelRule;

/**
 * This class represents the UpdateProtocolPersonnelEvent
 */
public class UpdateProtocolPersonnelEvent extends ProtocolPersonnelEventBase {
    
    public UpdateProtocolPersonnelEvent(String errorPathPrefix, ProtocolDocument document, int PersonIndex) {
        super("updating ProtocolPerson  " + getDocumentId(document), errorPathPrefix, document, PersonIndex);
    }

    public UpdateProtocolPersonnelEvent(String errorPathPrefix, Document document, int PersonIndex) {
        this(errorPathPrefix, (ProtocolDocument) document, PersonIndex);
    }
    
    /**
     * @see org.kuali.core.rule.event.KualiDocumentEvent#getRuleInterfaceClass()
     */
    public Class getRuleInterfaceClass() {
        return AddProtocolPersonnelRule.class;
    }

    /**
     * @see org.kuali.core.rule.event.KualiDocumentEvent#invokeRuleMethod(org.kuali.core.rule.BusinessRule)
     */
    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((UpdateProtocolPersonnelRule) rule).processUpdateProtocolPersonnelBusinessRules(this);
    }

}
