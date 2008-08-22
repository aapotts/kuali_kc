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
package org.kuali.kra.proposaldevelopment.rules;

import org.kuali.core.util.GlobalVariables;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.proposaldevelopment.rule.AddProposalBudgetVersionRule;
import org.kuali.kra.proposaldevelopment.rule.event.AddProposalBudgetVersionEvent;
import org.springframework.util.StringUtils;

public class AddProposalBudgetVersionRuleImpl implements AddProposalBudgetVersionRule {
    

    public boolean processAddProposalBudgetVersion(AddProposalBudgetVersionEvent addBudgetOverviewEvent) {

        boolean ret;
        if(!(ret = StringUtils.hasText(addBudgetOverviewEvent.getNewBudgetVersionName()))) {
            GlobalVariables.getErrorMap().putError("document.proposal.budgetVersionOverview.newBudgetVersionName", 
                    KeyConstants.ERROR_BUDGET_NAME_MISSING, "Name");
        }

        
        return ret;
         
    }

}
