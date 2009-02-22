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
package org.kuali.kra.award.paymentreports.specialapproval.approvedequipment;

import java.util.List;

import org.kuali.core.service.KualiRuleService;
import org.kuali.kra.award.bo.Award;
import org.kuali.kra.award.document.AwardDocument;
import org.kuali.kra.award.web.struts.form.AwardForm;
import org.kuali.kra.infrastructure.KraServiceLocator;

/**
 * This class supports the AwardForm class
 */
public class ApprovedEquipmentBean {
    private EquipmentCapitalizationMinimumLoader capitalizationMinimumLoader;
    private AwardApprovedEquipment newAwardApprovedEquipment;
    private KualiRuleService ruleService;
    private AwardForm form;
    
    /**
     * Constructs a ApprovedEquipmentBean
     * @param parent
     */
    public ApprovedEquipmentBean(AwardForm form) {
        this(form, new EquipmentCapitalizationMinimumLoader());
    }
    
    /**
     * Constructs a ApprovedEquipmentBean
     * @param parent
     */
    ApprovedEquipmentBean(AwardForm form, EquipmentCapitalizationMinimumLoader capitalizationMinimumLoader) {
        this.form = form;
        this.capitalizationMinimumLoader = capitalizationMinimumLoader;
    }
    
    /**
     * This method is called when adding a new apprved equipment item
     * @param formHelper
     * @return
     */
    public boolean addApprovedEquipmentItem() {
        AddAwardApprovedEquipmentRuleEvent event = generateAddEvent();
        boolean success = getRuleService().applyRules(event);
        if(success){
            getAward().add(getNewAwardApprovedEquipment());
            init();
        }
        return success;
    }

    /**
     * This method delets a selected equipment item
     * @param formHelper
     * @param deletedItemIndex
     */
    public void deleteApprovedEquipmentItem(int deletedItemIndex) {
        List<AwardApprovedEquipment> items = getAward().getApprovedEquipmentItems();
        if(deletedItemIndex >= 0 && deletedItemIndex < items.size()) {
            items.remove(deletedItemIndex);
        }        
    }

    /**
     * @return
     */
    public Award getAward() {
        return form.getAwardDocument().getAward();
    }

    /**
     * @return
     */
    public AwardDocument getAwardDocument() {
        return form.getAwardDocument();
    }
    
    /**
     * @return
     */
    public Object getData() {
        return getNewAwardApprovedEquipment();
    }
    
    /**
     * Gets the newAwardApprovedEquipment attribute. 
     * @return Returns the newAwardApprovedEquipment.
     */
    public AwardApprovedEquipment getNewAwardApprovedEquipment() {
        return newAwardApprovedEquipment;
    }
    
    /**
     * Initialize subform
     */
    public void init() {
        newAwardApprovedEquipment = new AwardApprovedEquipment(); 
    }

    /**
     * Sets the newAwardApprovedEquipment attribute value.
     * @param newAwardApprovedEquipment The newAwardApprovedEquipment to set.
     */
    public void setNewAwardApprovedEquipment(AwardApprovedEquipment newAwardApprovedEquipment) {
        this.newAwardApprovedEquipment = newAwardApprovedEquipment;
    }
    
    protected KualiRuleService getRuleService() {
        if(ruleService == null) {
            ruleService = (KualiRuleService) KraServiceLocator.getService("kualiRuleService"); 
        }
        return ruleService;
    }
    
    protected void setRuleService(KualiRuleService ruleService) {
        this.ruleService = ruleService;
    }
    
    AddAwardApprovedEquipmentRuleEvent generateAddEvent() {        
        AddAwardApprovedEquipmentRuleEvent event = new AddAwardApprovedEquipmentRuleEvent(
                                                            "newAwardApprovedEquipment",
                                                            getAwardDocument(),
                                                            getAward(),
                                                            getNewAwardApprovedEquipment(),
                                                            capitalizationMinimumLoader.getMinimumCapitalization());
        return event;
    }
}
