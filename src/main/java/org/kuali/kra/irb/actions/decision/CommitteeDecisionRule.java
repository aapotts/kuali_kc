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
package org.kuali.kra.irb.actions.decision;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.irb.ProtocolDocument;
import org.kuali.kra.rules.ResearchDocumentRuleBase;
import org.kuali.rice.kns.util.GlobalVariables;

/**
 * 
 * This class runs the rules needed for committee decision recording.
 */
public class CommitteeDecisionRule extends ResearchDocumentRuleBase implements ExecuteCommitteeDecisionRule {

    /**
     * 
     * @see org.kuali.kra.irb.actions.decision.ExecuteCommitteeDecisionRule#proccessCommitteeDecisionRule(org.kuali.kra.irb.ProtocolDocument, org.kuali.kra.irb.actions.decision.CommitteeDecision)
     */
    public boolean proccessCommitteeDecisionRule(ProtocolDocument document, CommitteeDecision committeeDecision) {
        boolean motionResults = proccessMotion(committeeDecision);
        return motionResults;
    }
    
    private boolean proccessMotion(CommitteeDecision committeeDecision) {
        boolean retVal = true;
        String motionToCompareFrom = committeeDecision.getMotion() == null ? null : committeeDecision.getMotion().trim();
        if (StringUtils.isBlank(motionToCompareFrom)) { 
            retVal = false;
            GlobalVariables.getErrorMap().putError(Constants.PROTOCOL_RECORD_COMMITTEE_KEY + ".motion", 
                    KeyConstants.ERROR_PROTOCOL_RECORD_COMMITEE_NO_MOTION);
        }
        
        boolean proccessCountsValue = proccessCounts(committeeDecision);
        retVal = retVal && proccessCountsValue;
        
        return retVal;
    }
    
    private boolean proccessCounts(CommitteeDecision committeeDecision) {
        boolean retVal = true;
        String motionToCompareFrom = committeeDecision.getMotion() == null ? null : committeeDecision.getMotion().trim(); 
        if (MotionValuesFinder.APPROVE.equals(motionToCompareFrom) && committeeDecision.getYesCount() == null) {
            GlobalVariables.getErrorMap().putError(Constants.PROTOCOL_RECORD_COMMITTEE_KEY + ".yesCount", 
                    KeyConstants.ERROR_PROTOCOL_RECORD_COMMITEE_NO_YES_VOTES);
            retVal = false;
        }
       if(MotionValuesFinder.DISAPPROVE.equals(motionToCompareFrom) && committeeDecision.getNoCount() == null) {
            GlobalVariables.getErrorMap().putError(Constants.PROTOCOL_RECORD_COMMITTEE_KEY + ".noCount", 
                    KeyConstants.ERROR_PROTOCOL_RECORD_COMMITEE_NO_NO_VOTES);
            retVal = false;
        }
        return retVal;
    }
}
