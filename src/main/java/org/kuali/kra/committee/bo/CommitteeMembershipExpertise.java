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
package org.kuali.kra.committee.bo;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;
import org.kuali.kra.bo.ResearchArea;

import java.util.LinkedHashMap;

import javax.persistence.Column;
import javax.persistence.Id;

public class CommitteeMembershipExpertise extends KraPersistableBusinessObjectBase { 
    
    @Id
    @Column(name = "COMM_MEMBER_ROLES_ID")
    private Long committeeMembershipExpertiseId;
    
    @Column(name = "COMM_MEMBERSHIPS_ID")
    private Long committeeMembershipId;

    @Column(name = "MEMBERSHIP_ID")
    private String membershipId;

    @Column(name = "SEQUENCE_NUMBER")
    private Integer sequenceNumber;
    
    @Column(name = "RESEARCH_AREA_CODE")
    private String researchAreaCode; 
    
    private ResearchArea researchArea; 
    
    public CommitteeMembershipExpertise() { 

    } 
    
    public Long getCommitteeMembershipExpertiseId() {
        return committeeMembershipExpertiseId;
    }

    public void setCommitteeMembershipExpertiseId(Long committeeMembershipExpertiseId) {
        this.committeeMembershipExpertiseId = committeeMembershipExpertiseId;
    }

    public Long getCommitteeMembershipId() {
        return committeeMembershipExpertiseId;
    }

    public void setCommitteeMembershipId(Long committeeMembershipExpertiseId) {
        this.committeeMembershipExpertiseId = committeeMembershipExpertiseId;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getResearchAreaCode() {
        return researchAreaCode;
    }

    public void setResearchAreaCode(String researchAreaCode) {
        this.researchAreaCode = researchAreaCode;
    }

    public ResearchArea getResearchArea() {
        return researchArea;
    }

    public void setResearchAreas(ResearchArea researchArea) {
        this.researchArea = researchArea;
    }

    @Override 
    protected LinkedHashMap<String, Object> toStringMapper() {
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        hashMap.put("committeeMembershipExpertiseId", getCommitteeMembershipExpertiseId());
        hashMap.put("committeeMembershipId", getCommitteeMembershipId());
        hashMap.put("membershipId", getMembershipId());
        hashMap.put("sequenceNumber", getSequenceNumber());
        hashMap.put("researchAreaCode", getResearchAreaCode());
        return hashMap;
    }
    
    public void init(CommitteeMembership committeeMembership) {
        setMembershipId(committeeMembership.getMembershipId());
    }
}