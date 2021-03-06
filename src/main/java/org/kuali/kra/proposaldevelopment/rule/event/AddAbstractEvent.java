/*
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.kra.proposaldevelopment.rule.event;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.proposaldevelopment.bo.ProposalAbstract;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.rule.AbstractsRule;
import org.kuali.kra.rule.event.KraDocumentEventBase;
import org.kuali.rice.krad.rules.rule.BusinessRule;
import org.kuali.rice.krad.util.ObjectUtils;

/**
 * The AddAbstractEvent is generated when a Proposal Abstract is to be added to
 * a Proposal Development Document.
 * 
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
public class AddAbstractEvent extends KraDocumentEventBase {
    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(AddAbstractEvent.class);
    
    private ProposalAbstract proposalAbstract;
    
    /**
     * Constructs an AddAbstractEvent.
     * 
     * @param document proposal development document
     * @param proposalAbstract the proposal abstract that is being added
     */
    public AddAbstractEvent(ProposalDevelopmentDocument document, ProposalAbstract proposalAbstract) {
        super("adding abstract to document " + getDocumentId(document), "", document);
    
        // by doing a deep copy, we are ensuring that the business rule class can't update
        // the original object by reference
        this.proposalAbstract = (ProposalAbstract) ObjectUtils.deepCopy(proposalAbstract);
    
        logEvent();
    }
    
    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEventBase#validate()
     */
    public void validate() {
        super.validate();
        if (this.proposalAbstract == null) {
            throw new IllegalArgumentException("invalid (null) proposal abstract");
        }
    }
    
    /**
     * @see org.kuali.kra.rule.event.KraDocumentEventBase#logEvent()
     */
    @Override
    protected void logEvent() {
        StringBuffer logMessage = new StringBuffer(StringUtils.substringAfterLast(this.getClass().getName(), "."));
        logMessage.append(" with ");

        // vary logging detail as needed
        if (this.proposalAbstract == null) {
            logMessage.append("null proposalAbstract");
        }
        else {
            logMessage.append(this.proposalAbstract.toString());
        }

        LOG.debug(logMessage);
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#getRuleInterfaceClass()
     */
    public Class getRuleInterfaceClass() {
        return AbstractsRule.class;
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#invokeRuleMethod(org.kuali.rice.krad.rules.rule.BusinessRule)
     */
    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((AbstractsRule) rule).processAddAbstractBusinessRules((ProposalDevelopmentDocument) this.getDocument(), 
                                                                        this.proposalAbstract);
    }
}
