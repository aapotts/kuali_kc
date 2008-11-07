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
package org.kuali.kra.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.core.service.BusinessObjectService;
import org.kuali.kra.bo.ResearchAreas;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.service.ResearchAreasService;
import org.kuali.rice.kns.util.KNSConstants;

public class ResearchAreasServiceImpl implements ResearchAreasService {
    private BusinessObjectService businessObjectService;
    private static final String COLUMN = ":";
    private static final String SEPARATOR = ";1;";

    public String getInitialResearchAreasList() {
        List<ResearchAreas> researchAreasList = getSubResearchAreas("000000");
        if (CollectionUtils.isEmpty(researchAreasList)) {
            return Constants.EMPTY_STRING;
        }
        ResearchAreas topResearcgArea = researchAreasList.get(0);
        String initialResearchAreas = topResearcgArea.getResearchAreaCode() +KNSConstants.BLANK_SPACE+COLUMN+KNSConstants.BLANK_SPACE+topResearcgArea.getDescription()+SEPARATOR;
        for (ResearchAreas researcgArea : getSubResearchAreas(topResearcgArea.getResearchAreaCode())) {
            initialResearchAreas = initialResearchAreas + researcgArea.getResearchAreaCode() +KNSConstants.BLANK_SPACE+COLUMN+KNSConstants.BLANK_SPACE+researcgArea.getDescription()+SEPARATOR;
        }
        initialResearchAreas = initialResearchAreas.substring(0, initialResearchAreas.length() - 3);

        return initialResearchAreas;
        
    }

    public String getSubResearchAreasForTreeView(String researchAreaCode) {
        String researchAreas = null;
        for (ResearchAreas researcgArea : getSubResearchAreas(researchAreaCode)) {
            if (StringUtils.isNotBlank(researchAreas)) {
                researchAreas = researchAreas +"," +researcgArea.getResearchAreaCode()+KNSConstants.BLANK_SPACE+COLUMN+KNSConstants.BLANK_SPACE+researcgArea.getDescription();
            } else {
                researchAreas = researcgArea.getResearchAreaCode()+KNSConstants.BLANK_SPACE+COLUMN+KNSConstants.BLANK_SPACE+researcgArea.getDescription();                
            }
        }
        return researchAreas;
        
    }
   
    public String getAscendantList(String researchAreaCode) {
        
        String retStr = Constants.EMPTY_STRING;
        Map<String, Object> fieldValues = new HashMap<String, Object>();
        fieldValues.put("researchAreaCode", researchAreaCode);
        // what if not found, can it be casted? is it return a pbo or null
        ResearchAreas researchArea = (ResearchAreas)businessObjectService.findByPrimaryKey(ResearchAreas.class, fieldValues);

        while (researchArea != null && !researchArea.getParentResearchAreaCode().equals("000000")) {
            fieldValues.put("researchAreaCode", researchArea.getParentResearchAreaCode());
            researchArea = (ResearchAreas)businessObjectService.findByPrimaryKey(ResearchAreas.class, fieldValues);            
            if (researchArea != null) {
                if (retStr.equals(Constants.EMPTY_STRING)) {
                    retStr = researchArea.getResearchAreaCode();
                } else {
                    retStr = researchArea.getResearchAreaCode()+ SEPARATOR + retStr;
                    
                }
                
            }
        }
        return retStr;
        
    }

    private List<ResearchAreas> getSubResearchAreas(String researchAreaCode) {
        List<ResearchAreas> researchAreasList = new ArrayList<ResearchAreas>();
        Map<String, Object> fieldValues = new HashMap<String, Object>();
        fieldValues.put("parentResearchAreaCode", researchAreaCode);
        researchAreasList.addAll(businessObjectService.findMatchingOrderBy(ResearchAreas.class, fieldValues, "researchAreaCode", true));
        return researchAreasList;
    }


    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }


    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

}
