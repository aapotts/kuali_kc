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
package org.kuali.kra.lookup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.RiceConstants;
import org.kuali.core.bo.BusinessObject;
import org.kuali.core.dao.LookupDao;
import org.kuali.core.lookup.KualiLookupableHelperServiceImpl;
import org.kuali.core.lookup.LookupUtils;
import org.kuali.core.util.GlobalVariables;
import org.kuali.core.web.struts.form.LookupForm;
import org.kuali.core.web.ui.ResultRow;
import org.kuali.kra.budget.bo.CostElement;
import org.kuali.kra.infrastructure.Constants;
import org.springframework.transaction.annotation.Transactional;

import edu.iu.uis.eden.lookupable.Column;
/**
 * 
 * This class implements a custom lookup for S2S Grants.gov Opportunity Lookup
 */
@Transactional
public class BudgetExpenseLookupableHelperServiceImpl extends KualiLookupableHelperServiceImpl {
    
    /**
     * 
     * @see org.kuali.core.lookup.KualiLookupableHelperServiceImpl#getSearchResults(java.util.Map)
     * It calls the S2sService#searchOpportunity service to look up the opportunity
     */
    public List<? extends BusinessObject> getSearchResults(Map<String, String> fieldValues) {
        //LookupUtils.removeHiddenCriteriaFields( getBusinessObjectClass(), fieldValues );
        setBackLocation(fieldValues.get(RiceConstants.BACK_LOCATION));
        setDocFormKey(fieldValues.get(RiceConstants.DOC_FORM_KEY));
        setReferencesToRefresh(fieldValues.get(RiceConstants.REFERENCES_TO_REFRESH));
        String budgetCategoryTypeCode = fieldValues.get("budgetCategoryTypeCode");
        fieldValues.remove("budgetCategoryTypeCode");
        List searchResults;
        List searchResultsReturn = new ArrayList();
        String categoryTypeName = null;
        searchResults = super.getSearchResults(fieldValues);
        
        for (Iterator iterator = searchResults.iterator(); iterator.hasNext();) {
            CostElement costElement = (CostElement) iterator.next();
            costElement.refreshReferenceObject("budgetCategory");
            if(StringUtils.equalsIgnoreCase(costElement.getBudgetCategory().getBudgetCategoryTypeCode(),budgetCategoryTypeCode)){
                searchResultsReturn.add(costElement);
                if(categoryTypeName==null){
                    categoryTypeName = costElement.getBudgetCategory().getBudgetCategoryType().getDescription();
                }
            }
        }       
        
        GlobalVariables.getMessageList().add(Constants.BUDGET_EXPENSE_LOOKUP_MESSAGE1);        
        //GlobalVariables.getMessageList().add(categoryTypeName);
        //GlobalVariables.getMessageList().add(Constants.BUDGET_EXPENSE_LOOKUP_MESSAGE2);
        return searchResultsReturn;
    }
}
