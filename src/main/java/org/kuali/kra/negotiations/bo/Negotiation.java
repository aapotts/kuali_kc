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
package org.kuali.kra.negotiations.bo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.bo.KcPerson;
import org.kuali.kra.bo.KraPersistableBusinessObjectBase;
import org.kuali.kra.common.permissions.Permissionable;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.RoleConstants;
import org.kuali.kra.institutionalproposal.home.InstitutionalProposal;
import org.kuali.kra.institutionalproposal.proposallog.ProposalLog;
import org.kuali.kra.negotiations.service.NegotiationService;
import org.kuali.rice.kns.bo.BusinessObject;

/**
 * 
 * This class handles the negotiation BO.
 */
public class Negotiation extends KraPersistableBusinessObjectBase implements Permissionable {

    private static final long MILLISECS_PER_DAY = 24*60*60*1000;
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 2529772854773433195L;
    
    private Long negotiationId;
    private String documentNumber;
    private Long negotiationStatusId;
    private Long negotiationAgreementTypeId;
    private Long negotiationAssociationTypeId;
    private String negotiatorPersonId;
    private Date negotiationStartDate;
    private Date negotiationEndDate;
    private Date anticipatedAwardDate;
    private String documentFolder;
    
    //transient
    private String negotiatorUserName;
    
    private NegotiationUnassociatedDetail unAssociatedDetail;
    
    /**
     * Long awardId - award
     * String proposalNumber -developmentProposal
     * Long proposalId - institutionalProposal
     */
    private String associatedDocumentId;
    
    private NegotiationStatus negotiationStatus;
    private NegotiationAgreementType negotiationAgreementType;
    private NegotiationAssociationType negotiationAssociationType;
    
    private List<NegotiationActivity> activities;
    
    public Negotiation() {
        super();
        activities = new ArrayList<NegotiationActivity>();
    }
    
    public String getNegotiationAge() {
        if (getNegotiationStartDate() == null) {
            return "";
        } else {
            long start = getNegotiationStartDate().getTime();
            long end = 0L;
            if (getNegotiationEndDate() == null) {
                end = Calendar.getInstance().getTimeInMillis();
            } else {
                end = getNegotiationEndDate().getTime();
            }
            
            return ((end - start) / MILLISECS_PER_DAY) + " days";
        }
    }
    
    public Long getNegotiationId() {
        return negotiationId;
    }



    public void setNegotiationId(Long negotiationId) {
        this.negotiationId = negotiationId;
    }



    public String getDocumentNumber() {
        return documentNumber;
    }



    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }



    public Long getNegotiationStatusId() {
        return negotiationStatusId;
    }



    public void setNegotiationStatusId(Long negotiationStatusId) {
        this.negotiationStatusId = negotiationStatusId;
    }



    public Long getNegotiationAgreementTypeId() {
        return negotiationAgreementTypeId;
    }



    public void setNegotiationAgreementTypeId(Long negotiationAgreementTypeId) {
        this.negotiationAgreementTypeId = negotiationAgreementTypeId;
    }



    public Long getNegotiationAssociationTypeId() {
        return negotiationAssociationTypeId;
    }



    public void setNegotiationAssociationTypeId(Long negotiationAssociationTypeId) {
        this.negotiationAssociationTypeId = negotiationAssociationTypeId;
    }



    public String getNegotiatorPersonId() {
        return negotiatorPersonId;
    }



    public void setNegotiatorPersonId(String negotiatorPersonId) {
        this.negotiatorPersonId = negotiatorPersonId;
    }
    
    public KcPerson getNegotiator() {
        if (this.getNegotiatorPersonId() == null) {
            return null;
        } else {
            return getKcPersonService().getKcPersonByPersonId(this.getNegotiatorPersonId());
        }
    }
    
    public String getNegotiatorUserName() {
        KcPerson negotiator = getNegotiator();
        if (negotiator == null) {
            return negotiatorUserName;
        } else {
            return negotiator.getUserName();
        }
    }

    public void setNegotiatorUserName(String negotiatorUserName) {
        this.negotiatorUserName = negotiatorUserName;
        KcPerson negotiator = null;
        try {
            negotiator = getKcPersonService().getKcPersonByUserName(negotiatorUserName);
        } catch (IllegalArgumentException e) {
            //invalid username, will be caught by validation routines
        }
        if (negotiator != null) {
            setNegotiatorPersonId(negotiator.getPersonId());
        } else {
            setNegotiatorPersonId(null);
        }
    }

    public Date getNegotiationStartDate() {
        return negotiationStartDate;
    }



    public void setNegotiationStartDate(Date negotiationStartDate) {
        this.negotiationStartDate = negotiationStartDate;
    }



    public Date getNegotiationEndDate() {
        return negotiationEndDate;
    }



    public void setNegotiationEndDate(Date negotiationEndDate) {
        this.negotiationEndDate = negotiationEndDate;
    }



    public Date getAnticipatedAwardDate() {
        return anticipatedAwardDate;
    }



    public void setAnticipatedAwardDate(Date anticipatedAwardDate) {
        this.anticipatedAwardDate = anticipatedAwardDate;
    }



    public String getDocumentFolder() {
        return documentFolder;
    }



    public void setDocumentFolder(String documentFolder) {
        this.documentFolder = documentFolder;
    }



    public String getAssociatedDocumentId() {
        return associatedDocumentId;
    }



    public void setAssociatedDocumentId(String associatedDocumentId) {
        this.associatedDocumentId = associatedDocumentId;
    }



    public NegotiationStatus getNegotiationStatus() {
        return negotiationStatus;
    }



    public void setNegotiationStatus(NegotiationStatus negotiationStatus) {
        this.negotiationStatus = negotiationStatus;
    }



    public NegotiationAgreementType getNegotiationAgreementType() {
        return negotiationAgreementType;
    }



    public void setNegotiationAgreementType(NegotiationAgreementType negotiationAgreementType) {
        this.negotiationAgreementType = negotiationAgreementType;
    }



    public NegotiationAssociationType getNegotiationAssociationType() {
        return negotiationAssociationType;
    }



    public void setNegotiationAssociationType(NegotiationAssociationType negotiationAssociationType) {
        this.negotiationAssociationType = negotiationAssociationType;
    }

    public NegotiationUnassociatedDetail getUnAssociatedDetail() {
        return unAssociatedDetail;
    }

    public void setUnAssociatedDetail(NegotiationUnassociatedDetail unAssociatedDetail) {
        this.unAssociatedDetail = unAssociatedDetail;
    }


    @Override
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap map = new LinkedHashMap();
        map.put("negotiationId", this.getNegotiationId());
        return map;
    }

    public List<NegotiationActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<NegotiationActivity> activities) {
        this.activities = activities;
    }

    @Override
    public String getDocumentNumberForPermission() {
        return this.negotiationId != null ? this.negotiationId.toString() : "NOT_SET";
    }

    @Override
    public String getDocumentKey() {
        return Permissionable.NEGOTIATION_KEY;
    }

    @Override
    public List<String> getRoleNames() {
        List<String> roleNames = new ArrayList<String>();
        roleNames.add(RoleConstants.NEGOTIATION_COI);
        roleNames.add(RoleConstants.NEGOTIATION_KP);
        roleNames.add(RoleConstants.NEGOTIATION_NEGOTIATION_ADMINISTRATOR);
        roleNames.add(RoleConstants.NEGOTIATION_NEGOTIATOR);
        roleNames.add(RoleConstants.NEGOTIATION_PI);
        return roleNames;
    }

    @Override
    public String getNamespace() {
        return Constants.MODULE_NAMESPANCE_NEGOTIATION;
    }

    @Override
    public String getLeadUnitNumber() {
        BusinessObject bo = getNegotiationService().getAssociatedObject(this);
        String leadUnitNumber;
        if (this.getNegotiationAssociationType() != null) {
            if (StringUtils.equals(this.getNegotiationAssociationType().getCode(), NegotiationAssociationType.AWARD_ASSOCIATION)) {
                Award award = (Award) bo;
                leadUnitNumber = award.getLeadUnitNumber();
            } else if (StringUtils.equals(this.getNegotiationAssociationType().getCode(), 
                    NegotiationAssociationType.INSTITUATIONAL_PROPOSAL_ASSOCIATION)) {
                InstitutionalProposal ip = (InstitutionalProposal) bo;
                leadUnitNumber = ip.getLeadUnitNumber();
            } else if (StringUtils.equals(this.getNegotiationAssociationType().getCode(), 
                    NegotiationAssociationType.NONE_ASSOCIATION)) {
                leadUnitNumber = this.getUnAssociatedDetail().getLeadUnit().getUnitNumber();
            } else if (StringUtils.equals(this.getNegotiationAssociationType().getCode(), 
                    NegotiationAssociationType.PROPOSAL_LOG_ASSOCIATION)) {
                ProposalLog pl = (ProposalLog) bo;
                leadUnitNumber = pl.getLeadUnit();
            } else if (StringUtils.equals(this.getNegotiationAssociationType().getCode(), 
                    NegotiationAssociationType.SUB_AWARD_ASSOCIATION)) {
                leadUnitNumber = "";
            } else {
                throw new IllegalArgumentException(this.getNegotiationAssociationType().getCode() + " is an invalid code, should never gete here!");
            }
        } else {
            return null;
        }
        return leadUnitNumber;
    }
    
    private NegotiationService getNegotiationService() {
        return KraServiceLocator.getService(NegotiationService.class);
    }

    @Override
    public String getDocumentRoleTypeCode() {
        return RoleConstants.NEGOTIATION_ROLE_TYPE;
    }

    @Override
    public void populateAdditionalQualifiedRoleAttributes(Map<String, String> qualifiedRoleAttributes) {
        return;
    }
}
