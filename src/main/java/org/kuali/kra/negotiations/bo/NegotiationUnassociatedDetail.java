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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.kuali.kra.bo.KcPerson;
import org.kuali.kra.bo.KraPersistableBusinessObjectBase;
import org.kuali.kra.bo.Organization;
import org.kuali.kra.bo.Rolodex;
import org.kuali.kra.bo.Sponsor;
import org.kuali.kra.bo.Unit;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.service.RolodexService;

/**
 * 
 * This class handles the attributes needed for an unassociated negotiation.
 */
public class NegotiationUnassociatedDetail extends KraPersistableBusinessObjectBase implements Negotiable {
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 989159429578390915L;
    private Long negotiationUnassociatedDetailId;
    private Long negotiationId;
    private String title;
    private String piPersonId;
    private String piRolodexId;
    private String leadUnitNumber;
    private String sponsorCode;
    private String primeSponsorCode;
    private String sponsorAwardNumber;
    private String contactAdminPersonId;
    private String subAwardOrganizationId;
    
    private transient String proposalTypeCode;
    
    private Negotiation negotiation;
    private Unit leadUnit;
    private Sponsor sponsor;
    private Sponsor primeSponsor;
    private Organization subAwardOrganization;
    
    /**
     * 
     * Constructs a NegotiationUnassociatedDetail.java.
     */
    public NegotiationUnassociatedDetail() {
        super();
    }
    
    

    public Long getNegotiationUnassociatedDetailId() {
        return negotiationUnassociatedDetailId;
    }



    public void setNegotiationUnassociatedDetailId(Long negotiationUnassociatedDetailId) {
        this.negotiationUnassociatedDetailId = negotiationUnassociatedDetailId;
    }



    public Long getNegotiationId() {
        return negotiationId;
    }



    public void setNegotiationId(Long negotiationId) {
        this.negotiationId = negotiationId;
    }



    public String getTitle() {
        return title;
    }



    public void setTitle(String title) {
        this.title = title;
    }



    public String getPiPersonId() {
        return piPersonId;
    }



    public void setPiPersonId(String piPersonId) {
        this.piPersonId = piPersonId;
    }



    public String getPiRolodexId() {
        return piRolodexId;
    }



    public void setPiRolodexId(String piRolodexId) {
        this.piRolodexId = piRolodexId;
    }



    public String getLeadUnitNumber() {
        return leadUnitNumber;
    }



    public void setLeadUnitNumber(String leadUnitNumber) {
        this.leadUnitNumber = leadUnitNumber;
    }



    public String getSponsorCode() {
        return sponsorCode;
    }



    public void setSponsorCode(String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }



    public String getPrimeSponsorCode() {
        return primeSponsorCode;
    }



    public void setPrimeSponsorCode(String primeSponsorCode) {
        this.primeSponsorCode = primeSponsorCode;
    }



    public String getSponsorAwardNumber() {
        return sponsorAwardNumber;
    }



    public void setSponsorAwardNumber(String sponsorAwardNumber) {
        this.sponsorAwardNumber = sponsorAwardNumber;
    }



    public String getContactAdminPersonId() {
        return contactAdminPersonId;
    }



    public void setContactAdminPersonId(String contactAdminPersonId) {
        this.contactAdminPersonId = contactAdminPersonId;
    }



    public String getSubAwardOrganizationId() {
        return subAwardOrganizationId;
    }



    public void setSubAwardOrganizationId(String subAwardOrganizationId) {
        this.subAwardOrganizationId = subAwardOrganizationId;
    }



    public Negotiation getNegotiation() {
        return negotiation;
    }



    public void setNegotiation(Negotiation negotiation) {
        this.negotiation = negotiation;
    }



    public Unit getLeadUnit() {
        return leadUnit;
    }



    public void setLeadUnit(Unit leadUnit) {
        this.leadUnit = leadUnit;
    }



    public Sponsor getSponsor() {
        return sponsor;
    }



    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
    }



    public Sponsor getPrimeSponsor() {
        return primeSponsor;
    }



    public void setPrimeSponsor(Sponsor primeSponsor) {
        this.primeSponsor = primeSponsor;
    }



    public Organization getSubAwardOrganization() {
        return subAwardOrganization;
    }



    public void setSubAwardOrganization(Organization subAwardOrganization) {
        this.subAwardOrganization = subAwardOrganization;
    }



    @Override
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap map = new LinkedHashMap();
        map.put("NegotiationUnassociatedDetailId", this.getNegotiationUnassociatedDetailId());
        return map;
    }
    
    public KcPerson getPIEmployee() {
        if (this.getPiPersonId() == null) {
            return null;
        }
        else {
            return getKcPersonService().getKcPersonByPersonId(this.getPiPersonId());
        }
    }
    
    public Rolodex getPINonEmployee() {
        if (this.getPiRolodexId() == null) {
            return null;
        } else {
            return getRolodexService().getRolodex(Integer.parseInt(this.getPiRolodexId()));
        }
    }
    
    protected RolodexService getRolodexService() {
            return KraServiceLocator.getService(RolodexService.class);        
    }
    
    public KcPerson getContactAdmin() {
        if (this.getContactAdminPersonId() == null) {
            return null;
        }
        else {
            return getKcPersonService().getKcPersonByPersonId(this.getContactAdminPersonId());
        }
    }
    
    @Override
    public String getLeadUnitName() {
        String name = getLeadUnit() == null ? EMPTY_STRING : getLeadUnit().getUnitName();
        return name;
    }

    @Override
    public String getPiName() {
        if (getPIEmployee() != null) {
            return getPiEmployeeName();
        } else {
            return getPiNonEmployeeName();
        }
    }


    @Override
    public String getPiEmployeeName() {
        String name = getPIEmployee() == null ? EMPTY_STRING : getPIEmployee().getFullName();
        return name;
    }



    @Override
    public String getPiNonEmployeeName() {
        String name = getPINonEmployee() == null ? EMPTY_STRING : getPINonEmployee().getFullName();
        return name;
    }



    @Override
    public String getAdminPersonName() {
        String name = getContactAdmin() == null ? EMPTY_STRING : getContactAdmin().getFullName();
        return name;
    }



    @Override
    public String getSponsorName() {
        String name = getSponsor() == null ? EMPTY_STRING : getSponsor().getSponsorName();
        return name;
    }



    @Override
    public String getPrimeSponsorName() {
        String name = getPrimeSponsor() == null ? EMPTY_STRING : getPrimeSponsor().getSponsorName();
        return name;
    }



    @Override
    public String getSubAwardOrganizationName() {
        String name = getSubAwardOrganization() == null ? EMPTY_STRING : getSubAwardOrganization().getOrganizationName();
        return name;
    }
    
    @Override
    public List<KcPerson> getProjectKcPeople() {
        List<KcPerson> kcPeople = new ArrayList<KcPerson>();
        if (this.getContactAdmin() != null) {
            kcPeople.add(this.getContactAdmin());
        }
        if (this.getPIEmployee() != null) {
            kcPeople.add(this.getPIEmployee());
        }
        return kcPeople;
    }



    public String getProposalTypeCode() {
        return proposalTypeCode;
    }



    public void setProposalTypeCode(String proposalTypeCode) {
        this.proposalTypeCode = proposalTypeCode;
    }
}