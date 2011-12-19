/*
 * Copyright 2005-2010 The Kuali Foundation
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
package org.kuali.kra.coi;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;
import java.util.LinkedHashMap;

public class CoiDisclosureHistory extends KraPersistableBusinessObjectBase { 
    

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 6373301098038646558L;
    private Long coiDisclosureHistoryId; 
    private Long coiDisclosureId; 
    private String coiDisclosureNumber; 
    private Integer sequenceNumber; 
    private String disclosureStatus; 
    private String disclosureDispositionStatus; 
    
    
    public CoiDisclosureHistory() { 

    } 
    
    public String getCoiDisclosureNumber() {
        return coiDisclosureNumber;
    }

    public void setCoiDisclosureNumber(String coiDisclosureNumber) {
        this.coiDisclosureNumber = coiDisclosureNumber;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getDisclosureStatus() {
        return disclosureStatus;
    }

    public void setDisclosureStatus(String disclosureStatus) {
        this.disclosureStatus = disclosureStatus;
    }

    public String getDisclosureDispositionStatus() {
        return disclosureDispositionStatus;
    }

    public void setDisclosureDispositionStatus(String disclosureDispositionStatus) {
        this.disclosureDispositionStatus = disclosureDispositionStatus;
    }

    /** {@inheritDoc} */
    @Override 
    protected LinkedHashMap<String, Object> toStringMapper() {
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        hashMap.put("coiDisclosureNumber", this.getCoiDisclosureNumber());
        hashMap.put("sequenceNumber", this.getSequenceNumber());
        hashMap.put("disclosureStatus", this.getDisclosureStatus());
        hashMap.put("disclosureDispositionStatus", this.getDisclosureDispositionStatus());
        return hashMap;
    }

    public Long getCoiDisclosureHistoryId() {
        return coiDisclosureHistoryId;
    }

    public void setCoiDisclosureHistoryId(Long coiDisclosureHistoryId) {
        this.coiDisclosureHistoryId = coiDisclosureHistoryId;
    }

    public Long getCoiDisclosureId() {
        return coiDisclosureId;
    }

    public void setCoiDisclosureId(Long coiDisclosureId) {
        this.coiDisclosureId = coiDisclosureId;
    }
    
}