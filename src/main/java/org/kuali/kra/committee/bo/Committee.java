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
package org.kuali.kra.committee.bo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;
import org.kuali.kra.bo.Unit;
import org.kuali.kra.committee.document.CommitteeDocument;
import org.kuali.kra.irb.bo.ProtocolReviewType;

/**
 * Represents a single committee within an institution.
 */
@SuppressWarnings("serial")
public class Committee extends KraPersistableBusinessObjectBase {

    @Id
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "COMMITTEE_ID")
    private String committeeId;
    
    @Column(name = "COMMITTEE_NAME")
    private String committeeName;
    
    @Column(name = "HOME_UNIT_NUMBER")
    private String homeUnitNumber;
    
    @Column(name = "DESCRIPTION")
    private String committeeDescription;
    
    @Column(name = "SCHEDULE_DESCRIPTION")
    private String scheduleDescription;
    
    @Column(name = "COMMITTEE_TYPE_CODE")
    private String committeeTypeCode;
    
    @Column(name = "MIN_MEMBERS_REQUIRED")
    private Integer minimumMembersRequired;
    
    @Column(name = "MAX_PROTOCOLS")
    private Integer maxProtocols;
    
    @Column(name = "ADV_SUBMISSION_DAYS_REQ")
    private Integer advancedSubmissionDaysRequired;
    
    @Column(name = "APPLICABLE_REVIEW_TYPE_CODE")
    private String reviewTypeCode;
    
    private Unit homeUnit;
    private CommitteeType committeeType;
    private ProtocolReviewType reviewType;
    
    private CommitteeDocument committeeDocument;
    
    private List<CommitteeMembership> committeeMemberships;
    private List<CommitteeSchedule> committeeSchedules;
    
    private List<CommitteeResearchArea> committeeResearchAreas;
    
    // transient lookup fields
    private String membershipRoleCode;
    private String committeeChair;
    private String unitName;
    private String memberName;
    private String researchAreaCode;
    /**
     * Constructs a Committee.
     */
    public Committee() {
        setCommitteeResearchAreas(new ArrayList<CommitteeResearchArea>());
        setCommitteeMemberships(new ArrayList<CommitteeMembership>());
        setCommitteeSchedules(new ArrayList<CommitteeSchedule>());
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCommitteeId() {
        return committeeId;
    }

    public void setCommitteeId(String committeeId) {
        this.committeeId = committeeId;
    }

    public String getCommitteeName() {
        return committeeName;
    }

    public void setCommitteeName(String committeeName) {
        this.committeeName = committeeName;
    }

    public String getHomeUnitNumber() {
        return homeUnitNumber;
    }

    public void setHomeUnitNumber(String homeUnitNumber) {
        this.homeUnitNumber = homeUnitNumber;
    }

    public String getCommitteeDescription() {
        return committeeDescription;
    }

    public void setCommitteeDescription(String committeeDescription) {
        this.committeeDescription = committeeDescription;
    }

    public String getScheduleDescription() {
        return scheduleDescription;
    }

    public void setScheduleDescription(String scheduleDescription) {
        this.scheduleDescription = scheduleDescription;
    }

    public String getCommitteeTypeCode() {
        return committeeTypeCode;
    }

    public void setCommitteeTypeCode(String committeeTypeCode) {
        this.committeeTypeCode = committeeTypeCode;
    }

    public Integer getMinimumMembersRequired() {
        return minimumMembersRequired;
    }

    public void setMinimumMembersRequired(Integer minimumMembersRequired) {
        this.minimumMembersRequired = minimumMembersRequired;
    }

    public Integer getMaxProtocols() {
        return maxProtocols;
    }

    public void setMaxProtocols(Integer maxProtocols) {
        this.maxProtocols = maxProtocols;
    }

    public Integer getAdvancedSubmissionDaysRequired() {
        return advancedSubmissionDaysRequired;
    }

    public void setAdvancedSubmissionDaysRequired(Integer advancedSubmissionDaysRequired) {
        this.advancedSubmissionDaysRequired = advancedSubmissionDaysRequired;
    }

    public String getReviewTypeCode() {
        return reviewTypeCode;
    }

    public void setReviewTypeCode(String reviewTypeCode) {
        this.reviewTypeCode = reviewTypeCode;
    }

    public Unit getHomeUnit() {
        return homeUnit;
    }

    public void setHomeUnit(Unit homeUnit) {
        this.homeUnit = homeUnit;
    }

    public CommitteeType getCommitteeType() {
        return committeeType;
    }

    public void setCommitteeType(CommitteeType committeeType) {
        this.committeeType = committeeType;
    }

    public ProtocolReviewType getReviewType() {
        return reviewType;
    }

    public void setReviewType(ProtocolReviewType reviewType) {
        this.reviewType = reviewType;
    }
    
    public CommitteeDocument getCommitteeDocument() {
        return committeeDocument;
    }

    public void setCommitteeDocument(CommitteeDocument committeeDocument) {
        this.committeeDocument = committeeDocument;
    }

    public List<CommitteeMembership> getCommitteeMemberships() {
        return committeeMemberships;
    }

    public void setCommitteeMemberships(List<CommitteeMembership> committeeMemberships) {
        this.committeeMemberships = committeeMemberships;
    }

    public void setCommitteeSchedules(List<CommitteeSchedule> committeeSchedules) {
        this.committeeSchedules = committeeSchedules;
    }

    public List<CommitteeSchedule> getCommitteeSchedules() {
        return committeeSchedules;
    }
    
    public List<CommitteeResearchArea> getCommitteeResearchAreas() {
        return committeeResearchAreas;
    }

    public void setCommitteeResearchAreas(List<CommitteeResearchArea> committeeResearchAreas) {
        this.committeeResearchAreas = committeeResearchAreas;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap map = new LinkedHashMap();
        map.put("id", getId());
        map.put("committeeId", getCommitteeId());
        map.put("committeeName", getCommitteeName());
        map.put("homeUnitNumber", getHomeUnitNumber());
        map.put("reviewTypeCode", getReviewTypeCode());
        map.put("committeeTypeCode", getCommitteeTypeCode());
        map.put("committeeDescription", getCommitteeDescription());
        map.put("scheduleDescription", getScheduleDescription());
        map.put("minimumMembersRequired", getMinimumMembersRequired());
        map.put("maxProtocols", getMaxProtocols());
        map.put("advancedSubmissionDaysRequired", getAdvancedSubmissionDaysRequired());
        return map;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List buildListOfDeletionAwareLists() {
        List managedLists = super.buildListOfDeletionAwareLists();
        managedLists.add(getCommitteeResearchAreas());
        managedLists.add(this.committeeMemberships);
        managedLists.add(this.committeeSchedules);
        return managedLists;
    }

    public String getMembershipRoleCode() {
        return membershipRoleCode;
    }

    public void setMembershipRoleCode(String membershipRoleCode) {
        this.membershipRoleCode = membershipRoleCode;
    }

    public String getCommitteeChair() {
        return committeeChair;
    }

    public void setCommitteeChair(String committeeChair) {
        this.committeeChair = committeeChair;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getResearchAreaCode() {
        return researchAreaCode;
    }

    public void setResearchAreaCode(String researchAreaCode) {
        this.researchAreaCode = researchAreaCode;
    }

}
