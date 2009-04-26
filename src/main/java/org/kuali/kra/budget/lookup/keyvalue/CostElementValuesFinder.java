/*
 * Copyright 2006-2008 The Kuali Foundation
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
package org.kuali.kra.budget.lookup.keyvalue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.budget.bo.BudgetCategory;
import org.kuali.kra.budget.bo.CostElement;
import org.kuali.kra.budget.web.struts.form.BudgetForm;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.lookup.keyvalue.KeyValueFinderService;
import org.kuali.rice.kns.lookup.keyvalues.KeyValuesBase;
import org.kuali.rice.kns.service.KeyValuesService;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.web.struts.form.KualiForm;
import org.kuali.rice.kns.web.ui.KeyLabelPair;

public class CostElementValuesFinder extends KeyValuesBase{
    KeyValueFinderService keyValueFinderService= (KeyValueFinderService)KraServiceLocator.getService("keyValueFinderService");
    
    private String budgetCategoryTypeCode;
    
    /**
     * Constructs the list of Budget Periods.  Each entry
     * in the list is a &lt;key, value&gt; pair, where the "key" is the unique
     * status code and the "value" is the textual description that is viewed
     * by a user.  The list is obtained from the BudgetDocument if any are defined there. 
     * Otherwise, it is obtained from a lookup of the BUDGET_PERIOD database table
     * via the "KeyValueFinderService".
     * 
     * @return the list of &lt;key, value&gt; pairs of abstract types.  The first entry
     * is always &lt;"", "select:"&gt;.
     * @see org.kuali.core.lookup.keyvalues.KeyValuesFinder#getKeyValues()
     */
    public List<KeyLabelPair> getKeyValues() {
        KeyValuesService keyValuesService = (KeyValuesService) KraServiceLocator.getService("keyValuesService");
        List<KeyLabelPair> keyValues = new ArrayList<KeyLabelPair>();        
        Collection costElements= keyValuesService.findAll(CostElement.class);
        Collection budgetCategoryCodes = keyValuesService.findAll(BudgetCategory.class);
        KualiForm form = GlobalVariables.getKualiForm();        
        
        keyValues.add(new KeyLabelPair("", "select"));
        
        BudgetForm budgetForm = (BudgetForm)form;
        for (Iterator iter = costElements.iterator(); iter.hasNext();) {
            CostElement costElement = (CostElement) iter.next();
            for(Iterator iter1 = budgetCategoryCodes.iterator(); iter1.hasNext();){
                BudgetCategory budgetCategory = (BudgetCategory) iter1.next();
                if(costElement.getBudgetCategoryCode().equalsIgnoreCase(budgetCategory.getBudgetCategoryCode())){
                    if(StringUtils.equalsIgnoreCase(budgetCategory.getBudgetCategoryTypeCode(),getBudgetCategoryTypeCode())){
                        keyValues.add(new KeyLabelPair(costElement.getCostElement().toString(), costElement.getDescription()));
                    }
                }
            } 
        }        
        return keyValues;
    }
    
    public String getBudgetCategoryTypeCode() {
        return budgetCategoryTypeCode;
    }
    public void setBudgetCategoryTypeCode(String budgetCategoryTypeCode) {
        this.budgetCategoryTypeCode = budgetCategoryTypeCode;
    }
}
