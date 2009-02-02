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
package org.kuali.kra.award.rules;

import org.kuali.kra.award.bo.AwardCostShare;
import org.kuali.kra.award.rule.AwardCostShareRule;
import org.kuali.kra.award.rule.event.AwardCostShareRuleEvent;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.rules.ResearchDocumentRuleBase;

/**
 * This class...
 */
public class AwardCostShareRuleImpl extends ResearchDocumentRuleBase implements AwardCostShareRule {

    
    private static final String NEW_AWARD_COST_SHARE = "newAwardCostShare";
    
    /**
     * @see org.kuali.kra.award.rule.AwardCostShareRule#processCostShareBusinessRules
     * (org.kuali.kra.award.rule.event.AwardCostShareRuleEvent)
     */
    public boolean processCostShareBusinessRules(AwardCostShareRuleEvent awardCostShareRuleEvent) {
        return processCommonValidations(awardCostShareRuleEvent);
    }
    
    public boolean processAddCostShareBusinessRules(AwardCostShareRuleEvent awardCostShareRuleEvent) {
        return processCommonValidations(awardCostShareRuleEvent);
    }
    
    /**
     * This method processes common validations for business rules
     * @param event
     * @return
     */
    private boolean processCommonValidations(AwardCostShareRuleEvent event) {
        AwardCostShare awardCostShare = event.getCostShareForValidation();
        boolean validSourceAndDestination = testCostShareSourceAndDestinationForEquality(awardCostShare);
        boolean validFiscalYearRange = testCostShareFiscalYearRange(awardCostShare);
        
        return validSourceAndDestination && validFiscalYearRange;
    }
    
    /**
    *
    * Test source and destination for equality in AwardCostShare.
    * @param AwardCostShare, ErrorMap
    * @return Boolean
    */
    public boolean testCostShareSourceAndDestinationForEquality(AwardCostShare awardCostShare){
        boolean valid = true;
        if(awardCostShare.getSource().equals(awardCostShare.getDestination())) {
            valid = false;
            reportError(NEW_AWARD_COST_SHARE+".source", 
                    KeyConstants.ERROR_SOURCE_DESTINATION);
        }
        return valid;
    }
    
    /**
    *
    * Test fiscal year for valid range.
    * @param AwardCostShare, ErrorMap
    * @return Boolean
    */
    public boolean testCostShareFiscalYearRange(AwardCostShare awardCostShare){
        boolean valid = true;
        int fiscalYear = Integer.parseInt(awardCostShare.getFiscalYear());
        if(fiscalYear < Constants.MIN_FISCAL_YEAR || fiscalYear > Constants.MAX_FISCAL_YEAR) {
            valid = false;
            reportError(NEW_AWARD_COST_SHARE+".fiscalYear", 
                    KeyConstants.ERROR_FISCAL_YEAR_RANGE);
        }
        return valid;
    }
    

}
