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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.bo.State;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.proposaldevelopment.rule.event.AddProposalCongressionalDistrictEvent;
import org.kuali.kra.proposaldevelopment.rule.event.DeleteProposalCongressionalDistrictEvent;
import org.kuali.kra.proposaldevelopment.web.struts.form.CongressionalDistrictHelper;

/**
 * This class implements rule checks for adding and deleting congressional districts to a Proposal Site.
 */
public class ProposalDevelopmentCongressionalDistrictRule extends ProposalSiteRule {

    /**
     * Checks that the site index is valid, and that a valid state code and district number has been entered.
     * @see org.kuali.kra.proposaldevelopment.rule.AddCongressionalDistrictRule#processAddCongressionalDistrictRules(org.kuali.kra.proposaldevelopment.rule.event.AddProposalCongressionalDistrictEvent)
     */
    public boolean processAddCongressionalDistrictRules(AddProposalCongressionalDistrictEvent addCongressionalDistrictEvent) {
        String siteIndexStr = addCongressionalDistrictEvent.getSiteIndex();
        boolean isValid = isIndexValid(siteIndexStr, "Site Index");
        
        CongressionalDistrictHelper proposalSiteHelper = null;
        int siteIndex = -1;
        if (isValid) {
            siteIndex = new Integer(siteIndexStr);
            proposalSiteHelper = addCongressionalDistrictEvent.getCongressionalDistrictHelpers().get(siteIndex);
        }

        String stateCode = proposalSiteHelper.getNewState();
        if (isValid) {
            isValid = isStateCodeValid(stateCode);
        }

        if (isValid) {
            String districtNumber = proposalSiteHelper.getNewDistrictNumber();
            isValid &= isDistrictNumberValid(stateCode, districtNumber);
        }

        return isValid;
    }

    /**
     * Checks that site index and district index are valid.
     * @param deleteCongressionalDistrictEvent
     * @return
     */
    public boolean processDeleteCongressionalDistrictRules(DeleteProposalCongressionalDistrictEvent deleteCongressionalDistrictEvent) {
        String siteIndexStr = deleteCongressionalDistrictEvent.getSiteIndex();
        String districtIndexStr = deleteCongressionalDistrictEvent.getDistrictIndex();
        
        return isIndexValid(siteIndexStr, "Site Index") && isIndexValid(districtIndexStr, "District Index");
    }
        
    private boolean isStateCodeValid(String stateCode) {
        boolean isValid = false;

        // test against the two special state codes
        if (StringUtils.equalsIgnoreCase("00", stateCode) || StringUtils.equalsIgnoreCase("US", stateCode)) {
            isValid = true;
        }
        
        // test against regular state codes
        if (!isValid) {
            Map<String,String> fieldValues = new HashMap<String,String>();
            fieldValues.put("stateCode", stateCode);
            if (getBusinessObjectService().countMatching(State.class, fieldValues) == 1) {
                isValid = true;
            }
        }
        
        if (!isValid) {
            reportError("newPropLocation.location", KeyConstants.ERROR_PROPOSAL_SITES_STATE_CODE_INVALID);
        }
        
        return isValid;
    }
    
    private boolean isDistrictNumberValid(String stateCode, String districtNumber) {
        boolean isValid = true;
        
        String propertyName = "newDistrictNumber";
        try {
            // test against the two special state code /district number combinations
            if (StringUtils.equals("00", stateCode)) {
                isValid = StringUtils.equals("000", districtNumber);
                if (!isValid) {
                    reportError(propertyName, KeyConstants.ERROR_PROPOSAL_SITES_DISTRICT_NUMBER_FOR_00);
                }
            }
            else if (StringUtils.equalsIgnoreCase("US", stateCode)) {
                isValid = StringUtils.equalsIgnoreCase("all", districtNumber);
                if (!isValid) {
                    reportError(propertyName, KeyConstants.ERROR_PROPOSAL_SITES_DISTRICT_NUMBER_FOR_US);
                }
            }
            // test against regular district numbers
            else if (Long.parseLong(districtNumber) < 1) {
                reportError(propertyName, KeyConstants.ERROR_PROPOSAL_SITES_DISTRICT_NUMBER_LESS_THAN_ONE, "District Number");
                isValid = false;
            }
        }
        catch (NumberFormatException e) {
            reportError(propertyName, KeyConstants.ERROR_PROPOSAL_SITES_DISTRICT_NUMBER_INVALID_FORMAT, "The value '" + districtNumber + "' for District Number");
            isValid = false;
        }
        
        return isValid;
    }
}