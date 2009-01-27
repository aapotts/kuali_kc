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
package org.kuali.kra.award.web.struts.form;

import org.kuali.kra.award.bo.AwardApprovedEquipment;
import org.kuali.kra.award.document.AwardDocument;

/**
 * This class supports the AwardForm class
 */
public class ApprovedEquipmentFormHelper { //implements AwardFormHelper {
    private AwardForm parent;
    
    private AwardApprovedEquipment newAwardApprovedEquipment;
    
    /**
     * Constructs a ApprovedEquipmentFormHelper
     * @param parent
     */
    ApprovedEquipmentFormHelper(AwardForm parent) {
        this.parent = parent;
    }
    
    /**
     * Initialize subform
     */
    public void init() {
        newAwardApprovedEquipment = new AwardApprovedEquipment(); 
    }

    /**
     * Gets the newAwardApprovedEquipment attribute. 
     * @return Returns the newAwardApprovedEquipment.
     */
    public AwardApprovedEquipment getNewAwardApprovedEquipment() {
        return newAwardApprovedEquipment;
    }

    /**
     * Sets the newAwardApprovedEquipment attribute value.
     * @param newAwardApprovedEquipment The newAwardApprovedEquipment to set.
     */
    public void setNewAwardApprovedEquipment(AwardApprovedEquipment newAwardApprovedEquipment) {
        this.newAwardApprovedEquipment = newAwardApprovedEquipment;
    }

    public AwardDocument getAwardDocument() {
        return parent.getAwardDocument();
    }
    
    public Object getData() {
        return getNewAwardApprovedEquipment();
    }
}
