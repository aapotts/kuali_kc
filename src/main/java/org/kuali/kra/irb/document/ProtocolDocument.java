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

package org.kuali.kra.irb.document;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.kuali.core.document.Copyable;
import org.kuali.core.document.SessionDocument;
import org.kuali.core.util.TypedArrayList;
import org.kuali.kra.document.ResearchDocumentBase;
import org.kuali.kra.irb.bo.ProtocolParticipant;
import org.kuali.kra.irb.bo.ProtocolResearchAreas;
import org.kuali.kra.irb.bo.ProtocolRiskLevels;
import org.kuali.kra.irb.bo.ProtocolStatus;
import org.kuali.kra.irb.bo.ProtocolType;

public class ProtocolDocument extends ResearchDocumentBase implements Copyable, SessionDocument { 
	
	private Long protocolId; 
	private String protocolNumber; 
	private Integer sequenceNumber; 
	private String protocolTypeCode; 
	private String protocolStatusCode; 
	private String title; 
	private String description; 
	private Date applicationDate; 
	private Date approvalDate; 
	private Date expirationDate; 
	private Date lastApprovalDate; 
	private String fdaApplicationNumber; 
	private String referenceNumber1; 
	private String referenceNumber2; 
	private boolean billable; 
	private String specialReviewIndicator; 
	private String vulnerableSubjectIndicator; 
	private String keyStudyPersonIndicator; 
	private String fundingSourceIndicator; 
	private String correspondentIndicator; 
	private String referenceIndicator; 
	private String relatedProjectsIndicator; 
	
    private ProtocolStatus protocolStatus; 
    private ProtocolType protocolType; 
    
    private List<ProtocolRiskLevels> riskLevels;
    private List<ProtocolParticipant> protocolParticipants;
    
    private List<ProtocolResearchAreas> protocolResearchAreas;
    
    //Is transient, used for lookup select option in UI by KNS 
    private String newDescription;
    
	/*
	private ProtocolVulnerableSub protocolVulnerableSub; 
	private ProtocolVoteAbstainees protocolVoteAbstainees; 
	private ProtocolSpecialReview protocolSpecialReview; 
	private ProtocolReviewers protocolReviewers; 
	private ProtocolResearchAreas protocolResearchAreas; 
	private ProtocolLocation protocolLocation; 
	private ProtocolKeyPersons protocolKeyPersons; 
	private ProtocolFundingSource protocolFundingSource; 
	private ProtocolCorrespondents protocolCorrespondents; 
	private ProtocolNotepad protocolNotepad; 
	private ProtocolCustomData protocolCustomData; 
	private ProtocolRelatedProjects protocolRelatedProjects; 
	private ProtocolReferences protocolReferences; 
	private ProtocolUserRoles protocolUserRoles; 
	private ProtoAmendRenewal protoAmendRenewal; 
	private ProtocolSubmission protocolSubmission; 
	private ProtocolInvestigators protocolInvestigators; 
	private ProtocolDocuments protocolDocuments; 
	private ProtocolActions protocolActions; 
	private ProtocolLinks protocolLinks; 
	*/
	
	public ProtocolDocument() { 
        super();
        riskLevels = new ArrayList<ProtocolRiskLevels>();
        protocolParticipants = new TypedArrayList(ProtocolParticipant.class);
        protocolResearchAreas = new ArrayList<ProtocolResearchAreas>();// new TypedArrayList(ProtocolResearchAreas.class);  
        newDescription = getDefaultNewDescription();
	} 
	
    public void initialize() {
    }

    public Long getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Long protocolId) {
		this.protocolId = protocolId;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getProtocolTypeCode() {
		return protocolTypeCode;
	}

	public void setProtocolTypeCode(String protocolTypeCode) {
		this.protocolTypeCode = protocolTypeCode;
	}

	public String getProtocolStatusCode() {
		return protocolStatusCode;
	}

	public void setProtocolStatusCode(String protocolStatusCode) {
		this.protocolStatusCode = protocolStatusCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getLastApprovalDate() {
		return lastApprovalDate;
	}

	public void setLastApprovalDate(Date lastApprovalDate) {
		this.lastApprovalDate = lastApprovalDate;
	}

	public String getFdaApplicationNumber() {
		return fdaApplicationNumber;
	}

	public void setFdaApplicationNumber(String fdaApplicationNumber) {
		this.fdaApplicationNumber = fdaApplicationNumber;
	}

	public String getReferenceNumber1() {
		return referenceNumber1;
	}

	public void setReferenceNumber1(String referenceNumber1) {
		this.referenceNumber1 = referenceNumber1;
	}

	public String getReferenceNumber2() {
		return referenceNumber2;
	}

	public void setReferenceNumber2(String referenceNumber2) {
		this.referenceNumber2 = referenceNumber2;
	}

	public boolean isBillable() {
		return billable;
	}

	public void setBillable(boolean billable) {
		this.billable = billable;
	}

	public String getSpecialReviewIndicator() {
		return specialReviewIndicator;
	}

	public void setSpecialReviewIndicator(String specialReviewIndicator) {
		this.specialReviewIndicator = specialReviewIndicator;
	}

	public String getVulnerableSubjectIndicator() {
		return vulnerableSubjectIndicator;
	}

	public void setVulnerableSubjectIndicator(String vulnerableSubjectIndicator) {
		this.vulnerableSubjectIndicator = vulnerableSubjectIndicator;
	}

	public String getKeyStudyPersonIndicator() {
		return keyStudyPersonIndicator;
	}

	public void setKeyStudyPersonIndicator(String keyStudyPersonIndicator) {
		this.keyStudyPersonIndicator = keyStudyPersonIndicator;
	}

	public String getFundingSourceIndicator() {
		return fundingSourceIndicator;
	}

	public void setFundingSourceIndicator(String fundingSourceIndicator) {
		this.fundingSourceIndicator = fundingSourceIndicator;
	}

	public String getCorrespondentIndicator() {
		return correspondentIndicator;
	}

	public void setCorrespondentIndicator(String correspondentIndicator) {
		this.correspondentIndicator = correspondentIndicator;
	}

	public String getReferenceIndicator() {
		return referenceIndicator;
	}

	public void setReferenceIndicator(String referenceIndicator) {
		this.referenceIndicator = referenceIndicator;
	}

	public String getRelatedProjectsIndicator() {
		return relatedProjectsIndicator;
	}

	public void setRelatedProjectsIndicator(String relatedProjectsIndicator) {
		this.relatedProjectsIndicator = relatedProjectsIndicator;
	}

	@Override 
	protected LinkedHashMap toStringMapper() {
		LinkedHashMap hashMap = new LinkedHashMap();
		hashMap.put("protocolId", getProtocolId());
		hashMap.put("protocolNumber", getProtocolNumber());
		hashMap.put("sequenceNumber", getSequenceNumber());
		hashMap.put("protocolTypeCode", getProtocolTypeCode());
		hashMap.put("protocolStatusCode", getProtocolStatusCode());
		hashMap.put("title", getTitle());
		hashMap.put("description", getDescription());
		hashMap.put("applicationDate", getApplicationDate());
		hashMap.put("approvalDate", getApprovalDate());
		hashMap.put("expirationDate", getExpirationDate());
		hashMap.put("lastApprovalDate", getLastApprovalDate());
		hashMap.put("fdaApplicationNumber", getFdaApplicationNumber());
		hashMap.put("referenceNumber1", getReferenceNumber1());
		hashMap.put("referenceNumber2", getReferenceNumber2());
		hashMap.put("isBillable", isBillable());
		hashMap.put("specialReviewIndicator", getSpecialReviewIndicator());
		hashMap.put("vulnerableSubjectIndicator", getVulnerableSubjectIndicator());
		hashMap.put("keyStudyPersonIndicator", getKeyStudyPersonIndicator());
		hashMap.put("fundingSourceIndicator", getFundingSourceIndicator());
		hashMap.put("correspondentIndicator", getCorrespondentIndicator());
		hashMap.put("referenceIndicator", getReferenceIndicator());
		hashMap.put("relatedProjectsIndicator", getRelatedProjectsIndicator());
		return hashMap;
	}

    public ProtocolStatus getProtocolStatus() {
        return protocolStatus;
    }

    public void setProtocolStatus(ProtocolStatus protocolStatus) {
        this.protocolStatus = protocolStatus;
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    public List<ProtocolRiskLevels> getRiskLevels() {
        return riskLevels;
    }

    public void setRiskLevels(List<ProtocolRiskLevels> riskLevels) {
        this.riskLevels = riskLevels;
    }

    public List<ProtocolParticipant> getProtocolParticipants() {
        return protocolParticipants;
    }

    public void setProtocolParticipants(List<ProtocolParticipant> protocolParticipants) {
        this.protocolParticipants = protocolParticipants;
    }
    
    /**
     * Gets index i from the protocol participant list.
     * 
     * @param index
     * @return protocol participant at index i
     */
    public ProtocolParticipant getProtocolParticipant(int index) {
        return getProtocolParticipants().get(index);
    }

    public String getNewDescription() {
        return newDescription;
    }
    
    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }

    public String getDefaultNewDescription() {
        return "(select)";
    }
    public void setProtocolResearchAreas(List<ProtocolResearchAreas> protocolResearchAreas) {
        this.protocolResearchAreas = protocolResearchAreas;
    }

    public List<ProtocolResearchAreas> getProtocolResearchAreas() {
        return protocolResearchAreas;
    }
    
    public void addProtocolResearchAreas(ProtocolResearchAreas protocolResearchArea) {
        getProtocolResearchAreas().add(protocolResearchArea);
    }

    public ProtocolResearchAreas getProtocolResearchAreas(int index) {
        while (getProtocolResearchAreas().size() <= index) {
            getProtocolResearchAreas().add(new ProtocolResearchAreas());
        }
        return (ProtocolResearchAreas) getProtocolResearchAreas().get(index);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List buildListOfDeletionAwareLists() {
        List managedLists = super.buildListOfDeletionAwareLists();
        managedLists.add(getProtocolResearchAreas());
        return managedLists;

    }
}