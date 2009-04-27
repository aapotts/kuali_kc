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
package org.kuali.kra.award.bo;

import java.util.LinkedHashMap;

import org.kuali.kra.award.AwardAssociate;

/**
 * This class is the BO representation of an Award Custom Data.
 */
public class AwardCustomData extends AwardAssociate {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 4125090813618033094L;
    private Long awardCustomDataId;
    private String value;
    
    
    /**
     * Gets the awardCustomDataId attribute. 
     * @return Returns the awardCustomDataId.
     */
    public Long getAwardCustomDataId() {
        return awardCustomDataId;
    }
    /**
     * Sets the awardCustomDataId attribute value.
     * @param awardCustomDataId The awardCustomDataId to set.
     */
    public void setAwardCustomDataId(Long awardCustomDataId) {
        this.awardCustomDataId = awardCustomDataId;
    }
    /**
     * Gets the value attribute. 
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }
    /**
     * Sets the value attribute value.
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    
    /**
     * @see org.kuali.core.bo.BusinessObjectBase#toStringMapper()
     */
    @Override
    protected LinkedHashMap<String, Object> toStringMapper() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("awardCustomDataId", getAwardCustomDataId());
        map.put("value", getValue());
        return map;
    }
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((awardCustomDataId == null) ? 0 : awardCustomDataId.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final AwardCustomData other = (AwardCustomData) obj;
        if (awardCustomDataId == null) {
            if (other.awardCustomDataId != null)
                return false;
        }
        else if (!awardCustomDataId.equals(other.awardCustomDataId))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        }
        else if (!value.equals(other.value))
            return false;
        return true;
    }
    
}
