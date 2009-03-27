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
package org.kuali.kra.committee.rule;

import org.kuali.core.rule.BusinessRule;
import org.kuali.kra.committee.rule.event.AddCommitteeMembershipExpertiseEvent;

/**
 * 
 * This interface addresses the adds rule for adding a new <code>CommitteeMembershipExpertise</code>
 * 
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
public interface AddCommitteeMembershipExpertiseRule extends BusinessRule {

    /**
     * 
     * Processes the validation rules for an <code>{@link AddCommitteeMembershipExpertiseEvent}</code>
     * 
     * @param addCommitteeMembershipExpertiseEvent
     * @return <code>true</code> if valid, <code>false</code> otherwise
     */
    public boolean processAddCommitteeMembershipExpertiseBusinessRules(AddCommitteeMembershipExpertiseEvent addCommitteeMembershipExpertiseEvent);
}
