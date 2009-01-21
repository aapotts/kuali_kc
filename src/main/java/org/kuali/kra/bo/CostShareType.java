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
package org.kuali.kra.bo;

import java.util.LinkedHashMap;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;

public class CostShareType extends KraPersistableBusinessObjectBase {
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -4625330898428160836L;
    private Integer costShareTypeCode;
    private String description;
    
    /**
     * 
     * Constructs a CostShareType.java.
     */
    public CostShareType() {
        super();                
    }

    /**
     * This method...
     * @return
     */
    public Integer getCostShareTypeCode() {
        return costShareTypeCode;
    }

    /**
     * This method...
     * @param costShareTypeCode
     */
    public void setCostShareTypeCode(Integer costShareTypeCode) {
        this.costShareTypeCode = costShareTypeCode;
    }

    /**
     * This method...
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method...
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * @see org.kuali.core.bo.BusinessObjectBase#toStringMapper()
     */
    @Override
    protected LinkedHashMap<String,Object> toStringMapper() {        
        LinkedHashMap<String,Object> hashMap = new LinkedHashMap<String,Object>();
        hashMap.put("costShareTypeCode", getCostShareTypeCode());
        hashMap.put("description", getDescription());
        return hashMap;
    }


}

