/*
 * Copyright 2006-2009 The Kuali Foundation
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
package org.kuali.kra.budget.bo;

import java.util.LinkedHashMap;

import org.kuali.kra.bo.AbstractInstituteRate;
import org.kuali.kra.bo.InstituteLaRate;
import org.kuali.kra.bo.InstituteRate;
import org.kuali.kra.budget.BudgetDecimal;

public class BudgetProposalLaRate extends AbstractBudgetRate {

    public BudgetProposalLaRate() {
        super();
    }
    
    public BudgetProposalLaRate(String unitNumber, InstituteLaRate instituteLaRate) {
        super(unitNumber, instituteLaRate);
    }
    
	@Override 
	protected LinkedHashMap toStringMapper() {
		LinkedHashMap hashMap = super.toStringMapper();
		hashMap.put("proposalNumber", getProposalNumber());
		hashMap.put("budgetVersionNumber", getBudgetVersionNumber());
		hashMap.put("applicableRate", getApplicableRate());
		return hashMap;
	}

}
