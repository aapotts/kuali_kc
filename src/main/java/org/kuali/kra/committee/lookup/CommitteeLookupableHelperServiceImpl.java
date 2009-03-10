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
package org.kuali.kra.committee.lookup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.kuali.core.bo.BusinessObject;
import org.kuali.kra.committee.dao.CommitteeLookupDao;
import org.kuali.kra.lookup.KraLookupableHelperServiceImpl;

public class CommitteeLookupableHelperServiceImpl extends KraLookupableHelperServiceImpl {

    private static final String MEMBERSHIP_ROLE_CODE = "membershipRoleCode";
    private static final String MEMBERSHIP_NAME = "memberName";
    private static final String RESEARCH_AREA_CODE = "researchAreaCode";

    @Override
    public List<? extends BusinessObject> getSearchResults(Map<String, String> fieldValues) {

        // super.setBackLocationDocFormKey(fieldValues);
        return super.getSearchResults(setupCritMaps(fieldValues));
        //return committeeLookupDao.getCommittees(fieldValues);
    }

    private Map<String,String> setupCritMaps(Map<String, String> fieldValues) {

        Map <String,String> baseLookupFields = new HashMap<String,String>();
        for (Entry<String, String> entry : fieldValues.entrySet()) {
            if (entry.getKey().equals(MEMBERSHIP_NAME)) {
                baseLookupFields.put("committeeMemberships.personName",entry.getValue());
            } else if (entry.getKey().equals(MEMBERSHIP_ROLE_CODE)) {
                baseLookupFields.put("committeeMemberships.committeeMembershipRoles.membershipRoleCode",entry.getValue());
            } else if (entry.getKey().equals(RESEARCH_AREA_CODE)) {
                baseLookupFields.put("committeeMemberships.committeeExpertise.researchAreaCode",entry.getValue());
            } else {
                baseLookupFields.put(entry.getKey(),entry.getValue());                
            }
        }
        return baseLookupFields;
    }
    
    protected String getHtmlAction() {
        return "committeeCommittee.do";
    }
    
    protected String getDocumentTypeName() {
        return "CommitteeDocument";
    }
    
    protected String getKeyFieldName() {
        return "committeeId";
    }

}
