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
package org.kuali.kra.proposaldevelopment.bo;

import javax.persistence.Version;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.Table;
import javax.persistence.Entity;

import java.util.LinkedHashMap;
import org.kuali.kra.bo.KraPersistableBusinessObjectBase;

/**
 * Class representation of the Person <code>{@link org.kuali.core.bo.BusinessObject}</code>
 *
 * @see org.kuali.core.bo.BusinessObject
 * @see org.kuali.core.bo.PersistableBusinessObject
 * $Id: InvestigatorCreditType.java,v 1.6 2008-07-23 19:16:37 gmcgrego Exp $
 */
@Entity
@Table(name="INV_CREDIT_TYPE")
public class InvestigatorCreditType extends KraPersistableBusinessObjectBase {
    @Id
	@Column(name="INV_CREDIT_TYPE_CODE")
	private String invCreditTypeCode;
    @Column(name="ADDS_TO_HUNDRED")
	private Boolean addsToHundred;
    private Boolean active;
    @Column(name="DESCRIPTION")
	private String description;

    /**
     * Retrieves the description attribute
     * 
     * @return String
     */
	public String getDescription() {
		return description;
	}
    
    /**
     * Assigns the description attribute
     *
     * @param description
     */
	public void setDescription(String description) {
		this.description = description;
	}

    /**
     * Retrieves the description attribute
     * 
     * @return Boolean
     */
	public Boolean addsToHundred() {
        return getAddsToHundred();
	}

    /**
     * Retrieves the description attribute
     * 
     * @return Boolean
     */
	public Boolean getAddsToHundred() {
		return addsToHundred;
	}
    
    /**
     * Assigns the description attribute
     *
     * @param argAddsToHundred
     */
	public void setAddsToHundred(Boolean argAddsToHundred) {
		this.addsToHundred = argAddsToHundred;
	}
    
    /**
     * Gets the value of invCreditTypeCode
     *
     * @return the value of invCreditTypeCode
     */
    public String getInvCreditTypeCode() {
        return invCreditTypeCode;
    }

    /**
     * Sets the value of invCreditTypeCode
     *
     * @param argInvCreditTypeCode Value to assign to this.invCreditTypeCode
     */
    public void setInvCreditTypeCode(String argInvCreditTypeCode) {
        invCreditTypeCode = argInvCreditTypeCode;
    }


    /**
     * @see org.kuali.core.bo.BusinessObjectBase#toStringMapper()
     */
	@Override 
	protected LinkedHashMap toStringMapper() {
   	    LinkedHashMap hashmap = new LinkedHashMap();
        hashmap.put("invCreditTypeCode", getDescription());
        hashmap.put("description", getDescription());
        hashmap.put("addsToHundred", getAddsToHundred());
		return hashmap;
	}

	/**
	 * Read access to the active flag
	 * 
	 * @return Boolean
	 */
    public Boolean getActive() {
        return active;
    }

    /**
     * Write access to the active flag
     * 
     * @param active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

}

