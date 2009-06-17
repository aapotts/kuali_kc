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
package org.kuali.kra.lookup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.s2s.bo.S2sOpportunity;
import org.kuali.kra.s2s.service.S2SService;
import org.kuali.rice.kns.bo.BusinessObject;
import org.kuali.rice.kns.lookup.KualiLookupableHelperServiceImpl;
import org.kuali.rice.kns.lookup.LookupUtils;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.KNSConstants;
import org.kuali.rice.kns.web.struts.form.LookupForm;
import org.kuali.rice.kns.web.ui.Column;
import org.kuali.rice.kns.web.ui.ResultRow;
import org.springframework.transaction.annotation.Transactional;
/**
 * 
 * This class implements a custom lookup for S2S Grants.gov Opportunity Lookup
 */
@Transactional
public class S2sOpportunityLookupableHelperServiceImpl extends KualiLookupableHelperServiceImpl {
    
    private S2SService s2SService;
    
    /**
     * 
     * @see org.kuali.core.lookup.KualiLookupableHelperServiceImpl#getSearchResults(java.util.Map)
     * It calls the S2sService#searchOpportunity service to look up the opportunity
     */
    public List<? extends BusinessObject> getSearchResults(Map<String, String> fieldValues) {
        LookupUtils.removeHiddenCriteriaFields( getBusinessObjectClass(), fieldValues );
        setBackLocation(fieldValues.get(KNSConstants.BACK_LOCATION));
        setDocFormKey(fieldValues.get(KNSConstants.DOC_FORM_KEY));
        setReferencesToRefresh(fieldValues.get(KNSConstants.REFERENCES_TO_REFRESH));
        GlobalVariables.getMessageList().add(Constants.GRANTS_GOV_LINK);
        List<S2sOpportunity> s2sOpportunity=new ArrayList<S2sOpportunity>();        
        if(fieldValues!=null && (fieldValues.get(Constants.CFDA_NUMBER)!=null && !StringUtils.equals(fieldValues.get(Constants.CFDA_NUMBER).trim(),""))||(fieldValues.get(Constants.OPPORTUNITY_ID)!=null && !StringUtils.equals(fieldValues.get(Constants.OPPORTUNITY_ID).trim(),""))){
            s2sOpportunity = s2SService.searchOpportunity(fieldValues.get(Constants.CFDA_NUMBER),fieldValues.get(Constants.OPPORTUNITY_ID),"");
            if(s2sOpportunity!=null){
                return s2sOpportunity;
            }else{
                if(fieldValues.get(Constants.CFDA_NUMBER)!=null && !StringUtils.equals(fieldValues.get(Constants.CFDA_NUMBER).trim(),"")){
                    GlobalVariables.getErrorMap().putError(Constants.CFDA_NUMBER, KeyConstants.ERROR_IF_CFDANUMBER_IS_INVALID);
                }
                if(fieldValues.get(Constants.OPPORTUNITY_ID)!=null && !StringUtils.equals(fieldValues.get(Constants.OPPORTUNITY_ID).trim(),"")){
                    GlobalVariables.getErrorMap().putError(Constants.OPPORTUNITY_ID, KeyConstants.ERROR_IF_OPPORTUNITY_ID_IS_INVALID);
                }
            }
            return new ArrayList<S2sOpportunity>();
        }else{
            GlobalVariables.getErrorMap().putError(Constants.NO_FIELD, KeyConstants.ERROR_IF_CFDANUMBER_AND_OPPORTUNITY_ID_IS_NULL);
            return s2sOpportunity;
        }        
    }

    public S2SService getS2SService() {
        return s2SService;
    }

    public void setS2SService(S2SService service) {
        s2SService = service;
    }    
    
    public Collection performLookup(LookupForm lookupForm, Collection resultTable, boolean bounded){
        Collection displayList;
        displayList = super.performLookup(lookupForm, resultTable, bounded);
        ResultRow row;        
        for (Iterator iter = resultTable.iterator(); iter.hasNext();){            
            row = (ResultRow) iter.next();
            List<Column> columns  = row.getColumns();
            
            for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
                Column col = (Column) iterator.next();
                
                if(StringUtils.equalsIgnoreCase(col.getColumnTitle(), "Instruction Page")||StringUtils.equalsIgnoreCase(col.getColumnTitle(), "Schema URL")){
                    col.setPropertyURL(col.getPropertyValue());
                }
            }
        }
        return displayList;
    }
}
