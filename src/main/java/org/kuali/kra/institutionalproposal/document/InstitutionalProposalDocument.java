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
package org.kuali.kra.institutionalproposal.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.kra.authorization.KraAuthorizationConstants;
import org.kuali.kra.bo.CustomAttributeDocument;
import org.kuali.kra.bo.versioning.VersionStatus;
import org.kuali.kra.document.ResearchDocumentBase;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.institutionalproposal.InstitutionalProposalConstants;
import org.kuali.kra.institutionalproposal.contacts.InstitutionalProposalPerson;
import org.kuali.kra.institutionalproposal.contacts.InstitutionalProposalPersonCreditSplit;
import org.kuali.kra.institutionalproposal.contacts.InstitutionalProposalPersonUnit;
import org.kuali.kra.institutionalproposal.home.InstitutionalProposal;
import org.kuali.kra.institutionalproposal.home.InstitutionalProposalSpecialReview;
import org.kuali.kra.institutionalproposal.home.InstitutionalProposalSpecialReviewExemption;
import org.kuali.kra.institutionalproposal.service.InstitutionalProposalVersioningService;
import org.kuali.kra.service.InstitutionalProposalCustomAttributeService;
import org.kuali.rice.kew.dto.DocumentRouteStatusChangeDTO;
import org.kuali.rice.kew.util.KEWConstants;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kns.service.ParameterConstants.COMPONENT;
import org.kuali.rice.kns.service.ParameterConstants.NAMESPACE;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.ObjectUtils;

/**
 * 
 * This class represents the InstitutionalProposalDocument Object.
 * InstitutionalProposalDocument has a 1:1 relationship with InstitutionalProposal Business Object.
 * We have declared a list of InstitutionalProposal BOs in the InstitutionalProposalDocument at the same time to
 * get around the OJB anonymous keys issue of primary keys of different data types.
 * Also we have provided convenient getter and setter methods so that to the outside world;
 * InstitutionalProposal and InstitutionalProposalDocument can have a 1:1 relationship.
 */
@NAMESPACE(namespace=InstitutionalProposalConstants.INSTITUTIONAL_PROPOSAL_NAMESPACE)
@COMPONENT(component=Constants.PARAMETER_COMPONENT_DOCUMENT)
public class InstitutionalProposalDocument extends ResearchDocumentBase {
    private static final Log LOG = LogFactory.getLog(InstitutionalProposalDocument.class);
    
    /**
     * Comment for <code>DOCUMENT_TYPE_CODE</code>
     */
    public static final String DOCUMENT_TYPE_CODE = "INPR";
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 5101782927161970631L;
    
    
    private List<InstitutionalProposal> institutionalProposalList;
    
    /**
     * Constructs a InstitutionalProposalDocument object
     */
    public InstitutionalProposalDocument(){        
        super();        
        init();
    }
    
    /**
     * 
     * This method is a convenience method for facilitating a 1:1 relationship between InstitutionalProposalDocument 
     * and InstitutionalProposal to the outside world - aka a single InstitutionalProposal field associated with InstitutionalProposalDocument
     * @return
     */
    public InstitutionalProposal getInstitutionalProposal() {
        return institutionalProposalList.get(0);
    }

    /**
     * 
     * This method is a convenience method for facilitating a 1:1 relationship between InstitutionalProposalDocument 
     * and InstitutionalProposal to the outside world - aka a single InstitutionalProposal field associated with InstitutionalProposalDocument
     * @param institutionalProposal
     */
    public void setInstitutionalProposal(InstitutionalProposal institutionalProposal) {
        institutionalProposalList.set(0, institutionalProposal);
    }
   
    /**
     *
     * @return
     */
    public List<InstitutionalProposal> getInstitutionalProposalList() {
        return institutionalProposalList;
    }

    /**
     *
     * @param institutionalProposalList
     */
    public void setInstitutionalProposalList(List<InstitutionalProposal> institutionalProposalList) {
        this.institutionalProposalList = institutionalProposalList;
    }

    /**
     * @see org.kuali.kra.document.ResearchDocumentBase#getDocumentTypeCode()
     */
    @Override
    public String getDocumentTypeCode() {
        return DOCUMENT_TYPE_CODE;
    }
    
    protected void init() {
        institutionalProposalList = new ArrayList<InstitutionalProposal>();
        institutionalProposalList.add(new InstitutionalProposal());
        populateCustomAttributes();
    }
    
    /**
     * 
     * @see org.kuali.core.bo.PersistableBusinessObjectBase#buildListOfDeletionAwareLists()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List buildListOfDeletionAwareLists() {
        List managedLists = super.buildListOfDeletionAwareLists();       
        
        InstitutionalProposal institutionalProposal = getInstitutionalProposal();
        List <InstitutionalProposalSpecialReviewExemption> institutionalProposalSpecialReviewExemptions = new ArrayList<InstitutionalProposalSpecialReviewExemption>();
        
        for (InstitutionalProposalSpecialReview institutionalProposalSpecialReview : getInstitutionalProposal().getSpecialReviews()) {
            institutionalProposalSpecialReviewExemptions.addAll(institutionalProposalSpecialReview.getSpecialReviewExemptions());            
        }
        
        managedLists.add(institutionalProposalSpecialReviewExemptions);
        managedLists.add(institutionalProposal.getSpecialReviews());
        managedLists.add(institutionalProposal.getInstitutionalProposalScienceKeywords());
        managedLists.add(institutionalProposal.getInstitutionalProposalCostShares());
        managedLists.add(institutionalProposal.getInstitutionalProposalUnrecoveredFandAs());
        managedLists.add(institutionalProposal.getAwardFundingProposals());
        
        // For project personnel 
        List<InstitutionalProposalPersonUnit> units = new ArrayList<InstitutionalProposalPersonUnit>();
        List<InstitutionalProposalPersonCreditSplit> creditSplits = new ArrayList<InstitutionalProposalPersonCreditSplit>();
        for (InstitutionalProposalPerson person : institutionalProposal.getProjectPersons()) {
            units.addAll(person.getUnits());
            creditSplits.addAll(person.getCreditSplits());
        }
        managedLists.add(units);
        managedLists.add(creditSplits);
        managedLists.add(institutionalProposal.getProjectPersons());

        return managedLists;
    }
    
    /**
     * This method populates the customAttributes for this document.
     */
    @Override
    public void populateCustomAttributes() {
        InstitutionalProposalCustomAttributeService institutionalProposalCustomAttributeService = KraServiceLocator.getService(InstitutionalProposalCustomAttributeService.class);
        Map<String, CustomAttributeDocument> customAttributeDocuments = institutionalProposalCustomAttributeService.getDefaultInstitutionalProposalCustomAttributeDocuments();
        setCustomAttributeDocuments(customAttributeDocuments);
    }
    
    /**
     * @see org.kuali.rice.kns.document.DocumentBase#doRouteStatusChange(org.kuali.rice.kew.dto.DocumentRouteStatusChangeDTO)
     */
    @Override
    public void doRouteStatusChange(DocumentRouteStatusChangeDTO statusChangeEvent) {
        super.doRouteStatusChange(statusChangeEvent);
        
        String newStatus = statusChangeEvent.getNewRouteStatus();
        
        if(LOG.isDebugEnabled()) {
            LOG.debug(String.format("********************* Status Change: from %s to %s", statusChangeEvent.getOldRouteStatus(), newStatus));
        }
        
        if(KEWConstants.ROUTE_HEADER_FINAL_CD.equalsIgnoreCase(newStatus) || KEWConstants.ROUTE_HEADER_PROCESSED_CD.equalsIgnoreCase(newStatus)) {
            getInstitutionalProposalVersioningService().updateInstitutionalProposalVersionStatus(this.getInstitutionalProposal(), VersionStatus.ACTIVE);
            //getVersionHistoryService().createVersionHistory(getAward(), VersionStatus.ACTIVE, GlobalVariables.getUserSession().getPrincipalName());
        }
        if(newStatus.equalsIgnoreCase(KEWConstants.ROUTE_HEADER_CANCEL_CD) || newStatus.equalsIgnoreCase(KEWConstants.ROUTE_HEADER_DISAPPROVED_CD)) {
            getInstitutionalProposalVersioningService().updateInstitutionalProposalVersionStatus(this.getInstitutionalProposal(), VersionStatus.CANCELED);
            //getVersionHistoryService().createVersionHistory(getAward(), VersionStatus.CANCELED, GlobalVariables.getUserSession().getPrincipalName());
        }
    }
    
    private InstitutionalProposalVersioningService getInstitutionalProposalVersioningService() {
        return KraServiceLocator.getService(InstitutionalProposalVersioningService.class);
    }
    
    @Override
    public void prepareForSave() {
        super.prepareForSave();
        if (ObjectUtils.isNull(this.getVersionNumber())) {
            this.setVersionNumber(new Long(0));
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean useCustomLockDescriptors() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String getCustomLockDescriptor(Person user) {
        String activeLockRegion = (String) GlobalVariables.getUserSession().retrieveObject(KraAuthorizationConstants.ACTIVE_LOCK_REGION);
        if (StringUtils.isNotEmpty(activeLockRegion)) {
            return this.getDocumentNumber() + "-" + activeLockRegion; 
        }

        return null;
    }
}
