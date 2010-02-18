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
package org.kuali.kra.award.budget;

import org.kuali.kra.award.budget.document.AwardBudgetDocument;
import org.kuali.kra.award.document.AwardDocument;
import org.kuali.rice.kns.workflow.service.KualiWorkflowDocument;

public interface AwardBudgetService {

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
    public void rebudget(AwardBudgetDocument awardBudgetDocument);
    
    /**
     * 
     */
    public AwardBudgetDocument createNew(AwardDocument awardDocument,String versionName);
    
    /**
     * 
     */
    public AwardBudgetDocument copy(AwardBudgetDocument awardBudgetDocument);
    
}
