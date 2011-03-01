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
package org.kuali.kra.award.budget;

import java.util.Collection;
import java.util.List;

import org.kuali.kra.award.budget.document.AwardBudgetDocument;
import org.kuali.kra.award.document.AwardDocument;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.budget.core.BudgetCommonService;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.document.BudgetParentDocument;
import org.kuali.kra.budget.parameters.BudgetPeriod;
import org.kuali.rice.kew.exception.WorkflowException;

public interface AwardBudgetService extends BudgetCommonService<Award> {

    /**
     * 
     */
    public void processSubmision(AwardBudgetDocument awardBudgetDocument);
    
    /**
     * 
     */
    public void processApproval(AwardBudgetDocument awardBudgetDocument);
    
    /**
     * 
     */
    public void processDisapproval(AwardBudgetDocument awardBudgetDocument);
    
    /**
     * 
     */
    public void post(AwardBudgetDocument awardBudgetDocument);

    /**
     * 
     */
    public void toggleStatus(AwardBudgetDocument awardBudgetDocument);
 
    /**
     * 
     */
    public AwardBudgetDocument rebudget(AwardDocument awardDocument,String documentDescription) throws WorkflowException;

    /**
     * 
     * Copies all line items from the BudgetPeriods included in rawValues into awardBudgetPeriod fixing
     * dates and making sure personnel referenced are also added to the awardBudget.
     * @param rawValues Collection of BudgetPeriods with line items to be copied to the awardBudgetPeriod
     * @param awardBudgetPeriod
     */
    public void copyLineItemsFromProposalPeriods(Collection rawValues, BudgetPeriod awardBudgetPeriod) throws WorkflowException;
    
    /**
     * Gets all budget periods from proposals that are funding this award.
     * @param awardNumber
     * @return
     */
    public List<BudgetPeriod> findBudgetPeriodsFromLinkedProposal(String awardNumber);
    
    /**
     * Return a list of the award budget status codes that are considered inactive,
     * currently cancelled, rejected and do not post. This is used to determine
     * which budgets to display by default.
     * @return
     */
    public List<String> getInactiveBudgetStatus();
}
