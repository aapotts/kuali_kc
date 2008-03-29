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
package org.kuali.kra.proposaldevelopment.rule.event;

import org.kuali.core.bo.BusinessObject;
import org.kuali.core.rule.BusinessRule;
import org.kuali.core.rule.event.KualiDocumentEvent;
import org.kuali.kra.logging.Traceable;
import org.kuali.kra.logging.TraceLogProxyFactory;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.rule.ChangeKeyPersonRule;


/**
 * Event class for actions that trigger modification of a <code>{@link ProposalPerson}</code> added to a <code>{@link ProposalDocument}</code>
 * 
 */
public class ChangeKeyPersonEvent extends KeyPersonEventBase implements KualiDocumentEvent, Traceable<ChangeKeyPersonEvent> {
    
    private BusinessObject source;
        
    /**
     * Default Constructor
     * 
     * @param person
     * @param source
     */
    public ChangeKeyPersonEvent(ProposalDevelopmentDocument document, ProposalPerson person, BusinessObject source) {
        super("add BusinessObject to person " + person.getProposalPersonNumber(), document, person);
        setSource(source);
    }

    /**
     * Read access to source
     * 
     * @return source of the event
     */
    public BusinessObject getSource() {
        return source;
    }

    /**
     * Write access to source
     * 
     * @param source
     */
    public void setSource(BusinessObject source) {
        this.source = source;
    }

    /**
     * @see org.kuali.core.rule.event.KualiDocumentEvent#getRuleInterfaceClass()
     */
    public Class<?> getRuleInterfaceClass() {
        return ChangeKeyPersonRule.class;
    }

    /**
     * @see org.kuali.core.rule.event.KualiDocumentEvent#invokeRuleMethod(org.kuali.core.rule.BusinessRule)
     */
    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((ChangeKeyPersonRule) rule).processChangeKeyPersonBusinessRules(getProposalPerson(), getSource());
    }
    
    /**
     * 
     * @see org.kuali.kra.logging.Traceable#getProxy(java.lang.Object)
     */
    public ChangeKeyPersonEvent getProxy(ChangeKeyPersonEvent archetype) {
        if (archetype == null) {
            archetype = this;
        }
        return TraceLogProxyFactory.getProxyFor(archetype);
    }
}
