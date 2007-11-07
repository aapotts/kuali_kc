/*
 * Copyright 2007 The Kuali Foundation.
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
package org.kuali.kra.proposaldevelopment.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.core.service.BusinessObjectService;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.service.ProposalPersonService;
/**
 * 
 * This class...
 */
public class ProposalPersonServiceImpl implements ProposalPersonService {
    private BusinessObjectService businessObjectService;
    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
    public String getPersonName(ProposalDevelopmentDocument doc, String userId) {
        String propPersonName = null;
        List<ProposalPerson> proposalPersons = doc.getProposalPersons();
        if(proposalPersons.isEmpty()){
            Map<String,Integer> queryMap = new HashMap<String,Integer>();
            queryMap.put("proposalNumber", doc.getProposalNumber());
            proposalPersons = (List)getBusinessObjectService().findMatching(ProposalPerson.class, queryMap);
        }
        for (ProposalPerson proposalPerson : proposalPersons) {
            if(proposalPerson.getPersonId().equals(userId)){
                propPersonName = proposalPerson.getFullName();
                break;
            }
        }
        return propPersonName;
    }
    
}
