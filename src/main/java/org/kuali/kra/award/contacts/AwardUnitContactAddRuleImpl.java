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
package org.kuali.kra.award.contacts;

import org.kuali.kra.award.home.Award;
import org.kuali.rice.kns.util.GlobalVariables;

/**
 * This class implements the specified rule
 */
public class AwardUnitContactAddRuleImpl extends BaseAwardContactAddRule {
    public static final String AWARD_UNIT_CONTACT_LIST_ERROR_KEY = "unitContactsBean.newAwardContact";
    public static final String ERROR_AWARD_UNIT_CONTACT_EXISTS = "error.awardUnitContact.person.exists";
    
    /**
     * @param event
     * @return
     */
    public boolean processAddAwardUnitContactBusinessRules(Award award, AwardUnitContact newUnitContact) {
        return checkForSelectedContactAdministratorTypeCode(newUnitContact) && checkForDuplicatePerson(award, newUnitContact);
    }

    public boolean checkForSelectedContactAdministratorTypeCode(AwardUnitContact newContact) {
        AwardUnitContact awardUnitContact = (AwardUnitContact) newContact;
        boolean valid = awardUnitContact.getUnitAdministratorTypeCode() != null;

        if(!valid) {
            GlobalVariables.getMessageMap().putError(AWARD_UNIT_CONTACT_LIST_ERROR_KEY, ERROR_AWARD_CONTACT_ROLE_REQUIRED);
        }

        return valid;
    }
    
    boolean checkForDuplicatePerson(Award award, AwardUnitContact newUnitContact) {
        boolean valid = true;
        for(AwardUnitContact unitContact: award.getAwardUnitContacts()) {
            valid = !unitContact.getPersonId().equals(newUnitContact.getPersonId());
            if(!valid) {
                registerError(newUnitContact);
                break;
            }
        }
        
        return valid;
    }

    private void registerError(AwardUnitContact newUnitContact) {
        GlobalVariables.getErrorMap().putError(AWARD_UNIT_CONTACT_LIST_ERROR_KEY, ERROR_AWARD_UNIT_CONTACT_EXISTS, 
                                                newUnitContact.getContact().getFullName());
    }
}
