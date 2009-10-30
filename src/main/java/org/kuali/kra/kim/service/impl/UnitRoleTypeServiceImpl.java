/*
 * Copyright 2008 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
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
package org.kuali.kra.kim.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.kim.bo.KcKimAttributes;
import org.kuali.rice.kim.bo.types.dto.AttributeSet;
import org.kuali.rice.kim.service.support.impl.KimRoleTypeServiceBase;

public class UnitRoleTypeServiceImpl extends KimRoleTypeServiceBase {

    @Override
    public boolean performMatch(AttributeSet qualification, AttributeSet roleQualifier) {
        if(roleQualifiedByProposalKey(roleQualifier)) {
            return StringUtils.equals(qualification.get(KcKimAttributes.PROPOSAL), 
                    roleQualifier.get(KcKimAttributes.PROPOSAL));
        } else if(roleQualifiedByProtocolKey(roleQualifier)) {
            return StringUtils.equals(qualification.get(KcKimAttributes.PROTOCOL), 
                    roleQualifier.get(KcKimAttributes.PROTOCOL));
        } else if(roleQualifiedByCommitteeKey(roleQualifier)) {
            return StringUtils.equals(qualification.get(KcKimAttributes.COMMITTEE), 
                    roleQualifier.get(KcKimAttributes.COMMITTEE));
        } else if(roleQualifiedByAwardKey(roleQualifier)) {
            return StringUtils.equals(qualification.get(KcKimAttributes.AWARD), 
                    roleQualifier.get(KcKimAttributes.AWARD));
        } else if(roleQualifiedByTimeAndMoneyKey(roleQualifier)) {
            return StringUtils.equals(qualification.get(KcKimAttributes.TIMEANDMONEY), 
                    roleQualifier.get(KcKimAttributes.TIMEANDMONEY));
        } else if(roleQualifiedByUnitOnly(roleQualifier)) {
            return (StringUtils.equals(qualification.get(KcKimAttributes.UNIT_NUMBER), 
                    roleQualifier.get(KcKimAttributes.UNIT_NUMBER)) || performWildCardMatching(qualification, roleQualifier));
        } 
        
        return false; 
    }

    private boolean roleQualifiedByProposalKey(AttributeSet roleQualifier) {
        return roleQualifier.containsKey(KcKimAttributes.PROPOSAL);
    }
    
    private boolean roleQualifiedByProtocolKey(AttributeSet roleQualifier) {
        return roleQualifier.containsKey(KcKimAttributes.PROTOCOL);
    }
    
    private boolean roleQualifiedByCommitteeKey(AttributeSet roleQualifier) {
        return roleQualifier.containsKey(KcKimAttributes.COMMITTEE);
    }
    
    private boolean roleQualifiedByAwardKey(AttributeSet roleQualifier) {
        return roleQualifier.containsKey(KcKimAttributes.AWARD);
    }
    
    private boolean roleQualifiedByTimeAndMoneyKey(AttributeSet roleQualifier) {
        return roleQualifier.containsKey(KcKimAttributes.TIMEANDMONEY);
    }
    
    private boolean roleQualifiedByUnitOnly(AttributeSet roleQualifier) {
        return roleQualifier.containsKey(KcKimAttributes.UNIT_NUMBER) && !roleQualifier.containsKey(KcKimAttributes.SUBUNITS);
    }
    
    private boolean performWildCardMatching(AttributeSet qualification, AttributeSet roleQualifier) {
        if(qualification.containsKey(KcKimAttributes.UNIT_NUMBER) && qualification.get(KcKimAttributes.UNIT_NUMBER).equalsIgnoreCase("*")) {
            return true;
        }
        //If necessary, we can include logic for other pattern matching later.
        //Not found necessary at this time.
        return false;
    }
}
