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

package org.kuali.kra.award.bo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 
 * This class represents the AwardReportTerm business object 
 * 
 */
public class AwardReportTerm extends AwardReportTermBase { 
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -3117988810554700250L;
    private Long awardReportTermId;
    private String awardNumber; 
    private Integer sequenceNumber; 
    private Award award; 
    private List<AwardReportTermRecipient> awardReportTermRecipients; 
    
    /**
     * 
     * Constructs a AwardReportTerm.java.
     */
    public AwardReportTerm() {
        awardReportTermRecipients = new ArrayList<AwardReportTermRecipient>();
    } 
    
    /**
     * 
     * @return
     */
    public Long getAwardReportTermId() {
        return awardReportTermId;
    }

    /**
     * 
     * @param awardReportTermId
     */
    public void setAwardReportTermId(Long awardReportTermId) {
        this.awardReportTermId = awardReportTermId;
    }

    /**
     * 
     * @return
     */
    public String getAwardNumber() {
        return awardNumber;
    }

    /**
     * 
     * @param awardNumber
     */
    public void setAwardNumber(String awardNumber) {
        // do nothing
    }

    /**
     * 
     * @return
     */
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * 
     * @param sequenceNumber
     */
    public void setSequenceNumber(Integer sequenceNumber) {
        // do nothing
    }


    /**
     *
     * @return
     */
    public Award getAward() {
        return award;
    }

    /**
     *
     * @param award
     */
    public void setAward(Award award) {
        this.award = award;
        if(award == null) {
            sequenceNumber = null;
            awardNumber = null;
        } else {
            sequenceNumber = award.getSequenceNumber();
            awardNumber = award.getAwardNumber();
        }
    }    


    
    /**
     * 
     * @see org.kuali.core.bo.BusinessObjectBase#toStringMapper()
     */
    @Override 
    protected LinkedHashMap<String, Object> toStringMapper() {
        LinkedHashMap<String, Object> hashMap = super.toStringMapper();
        hashMap.put("awardReportTermId", getAwardReportTermId());        
        hashMap.put("awardNumber", getAwardNumber());
        hashMap.put("sequenceNumber", getSequenceNumber());
        return hashMap;
    }

    /**
     * 
     * 
     * @return
     */
    public List<AwardReportTermRecipient> getAwardReportTermRecipients() {
        return awardReportTermRecipients;
    }

    /**
     *
     * @param awardReportTermRecipients
     */
    public void setAwardReportTermRecipients(List<AwardReportTermRecipient> awardReportTermRecipients) {
        this.awardReportTermRecipients = awardReportTermRecipients;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((award == null) ? 0 : award.hashCode());
        result = prime * result + ((awardNumber == null) ? 0 : awardNumber.hashCode());
        result = prime * result + ((awardReportTermId == null) ? 0 : awardReportTermId.hashCode());
        result = prime * result + ((awardReportTermRecipients == null) ? 0 : awardReportTermRecipients.hashCode());
        result = prime * result + ((sequenceNumber == null) ? 0 : sequenceNumber.hashCode());
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
        AwardReportTerm other = (AwardReportTerm) obj;
        if (award == null) {
            if (other.award != null)
                return false;
        }
        else if (!award.equals(other.award))
            return false;
        if (awardNumber == null) {
            if (other.awardNumber != null)
                return false;
        }
        else if (!awardNumber.equals(other.awardNumber))
            return false;
        if (awardReportTermId == null) {
            if (other.awardReportTermId != null)
                return false;
        }
        else if (!awardReportTermId.equals(other.awardReportTermId))
            return false;
        if (awardReportTermRecipients == null) {
            if (other.awardReportTermRecipients != null)
                return false;
        }
        else if (!awardReportTermRecipients.equals(other.awardReportTermRecipients))
            return false;
        if (sequenceNumber == null) {
            if (other.sequenceNumber != null)
                return false;
        }
        else if (!sequenceNumber.equals(other.sequenceNumber))
            return false;
        return true;
    }

   
}