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
package org.kuali.kra.proposaldevelopment.hierarchy.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.kra.budget.BudgetDecimal;
import org.kuali.kra.budget.calculator.BudgetCalculationService;
import org.kuali.kra.budget.core.Budget;
import org.kuali.kra.budget.core.BudgetService;
import org.kuali.kra.budget.core.CostElement;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.nonpersonnel.BudgetLineItem;
import org.kuali.kra.budget.parameters.BudgetPeriod;
import org.kuali.kra.budget.personnel.BudgetPerson;
import org.kuali.kra.budget.personnel.BudgetPersonnelBudgetService;
import org.kuali.kra.budget.personnel.BudgetPersonnelDetails;
import org.kuali.kra.budget.versions.BudgetDocumentVersion;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.RoleConstants;
import org.kuali.kra.proposaldevelopment.bo.CongressionalDistrict;
import org.kuali.kra.proposaldevelopment.bo.DevelopmentProposal;
import org.kuali.kra.proposaldevelopment.bo.Narrative;
import org.kuali.kra.proposaldevelopment.bo.NarrativeAttachment;
import org.kuali.kra.proposaldevelopment.bo.PropScienceKeyword;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonBiography;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonBiographyAttachment;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonUnit;
import org.kuali.kra.proposaldevelopment.bo.ProposalSite;
import org.kuali.kra.proposaldevelopment.bo.ProposalSpecialReview;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.hierarchy.HierarchyStatusConstants;
import org.kuali.kra.proposaldevelopment.hierarchy.ProposalHierarchyErrorDto;
import org.kuali.kra.proposaldevelopment.hierarchy.ProposalHierarchyException;
import org.kuali.kra.proposaldevelopment.hierarchy.bo.HierarchyProposalSummary;
import org.kuali.kra.proposaldevelopment.hierarchy.dao.ProposalHierarchyDao;
import org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService;
import org.kuali.kra.proposaldevelopment.service.NarrativeService;
import org.kuali.kra.proposaldevelopment.service.ProposalPersonBiographyService;
import org.kuali.kra.service.KraAuthorizationService;
import org.kuali.rice.kew.exception.WorkflowException;
import org.kuali.rice.kew.util.KEWConstants;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.DocumentService;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.ObjectUtils;
import org.kuali.rice.kns.web.struts.form.KualiForm;
import org.kuali.rice.kns.workflow.service.KualiWorkflowDocument;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class...
 */
@Transactional
public class ProposalHierarchyServiceImpl implements ProposalHierarchyService {
    
    private static final Log LOG = LogFactory.getLog(ProposalHierarchyServiceImpl.class);
    private static final String ERROR_BUDGET_START_DATE_INCONSISTENT = "error.hierarchy.budget.startDateInconsistent";
    private static final String ERROR_BUDGET_PERIOD_DURATION_INCONSISTENT = "error.hierarchy.budget.periodDurationInconsistent";

    private BusinessObjectService businessObjectService;
    private DocumentService documentService;
    private KraAuthorizationService kraAuthorizationService;
    private ProposalHierarchyDao proposalHierarchyDao;
    private NarrativeService narrativeService;
    private BudgetService budgetService;
    private BudgetCalculationService budgetCalculationService;
    private ProposalPersonBiographyService propPersonBioService;

    /**
     * Sets the businessObjectService attribute value.
     * @param businessObjectService The businessObjectService to set.
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    /**
     * Sets the documentService attribute value.
     * @param documentService The documentService to set.
     */
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Sets the kraAuthorizationService attribute value.
     * @param kraAuthorizationService The kraAuthorizationService to set.
     */
    public void setKraAuthorizationService(KraAuthorizationService kraAuthorizationService) {
        this.kraAuthorizationService = kraAuthorizationService;
    }

    /**
     * Sets the proposalHierarchyDao attribute value.
     * @param proposalHierarchyDao The proposalHierarchyDao to set.
     */
    public void setProposalHierarchyDao(ProposalHierarchyDao proposalHierarchyDao) {
        this.proposalHierarchyDao = proposalHierarchyDao;
    }

    /**
     * Sets the narrativeService attribute value.
     * @param narrativeService The narrativeService to set.
     */
    public void setNarrativeService(NarrativeService narrativeService) {
        this.narrativeService = narrativeService;
    }

    /**
     * Sets the budgetService attribute value.
     * @param budgetService The budgetService to set.
     */
    public void setBudgetService(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    /**
     * Sets the budgetCalculationService attribute value.
     * @param budgetCalculationService The budgetCalculationService to set.
     */
    public void setBudgetCalculationService(BudgetCalculationService budgetCalculationService) {
        this.budgetCalculationService = budgetCalculationService;
    }

    /**
     * Sets the propPersonBioService attribute value.
     * @param propPersonBioService The propPersonBioService to set.
     */
    public void setPropPersonBioService(ProposalPersonBiographyService propPersonBioService) {
        this.propPersonBioService = propPersonBioService;
    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService#createHierarchy(java.lang.String)
     */
    public String createHierarchy(DevelopmentProposal initialChild) throws ProposalHierarchyException {
        LOG.info(String.format("***Create Hierarchy using Proposal #%s", initialChild.getProposalNumber()));
        if (initialChild.isInHierarchy()) {
            throw new ProposalHierarchyException("Cannot create hierarchy: proposal " + initialChild.getProposalNumber()
                    + " is already a member of a hierarchy.");
        }

        // create a new proposal document
        ProposalDevelopmentDocument newDoc;
        try {
            newDoc = (ProposalDevelopmentDocument) documentService.getNewDocument(ProposalDevelopmentDocument.class);
        }
        catch (WorkflowException x) {
            throw new ProposalHierarchyException("Error creating new document: " + x);
        }
        // copy the initial information to the new parent proposal
        DevelopmentProposal hierarchy = newDoc.getDevelopmentProposal();
        copyInitialData(hierarchy, initialChild);
        hierarchy.setHierarchyStatus(HierarchyStatusConstants.Parent.code());
        String docDescription = initialChild.getProposalDocument().getDocumentHeader()
                .getDocumentDescription();
        newDoc.getDocumentHeader().setDocumentDescription(docDescription);

        // persist the document and add a budget
        try {
            documentService.saveDocument(newDoc);
            budgetService.addBudgetVersion(newDoc, "Hierarchy Budget");
        }
        catch (WorkflowException x) {
            throw new ProposalHierarchyException("Error saving new document: " + x);
        }
        LOG.info(String.format("***New Hierarchy Parent (#%s) budget created", hierarchy.getProposalNumber()));
        
        // add aggregator to the document
        String userId = GlobalVariables.getUserSession().getPrincipalId();
        kraAuthorizationService.addRole(userId, RoleConstants.AGGREGATOR, newDoc);

        initializeBudget(hierarchy, initialChild);

        prepareHierarchySync(hierarchy);
        copyInitialAttachments(initialChild, hierarchy);

        // link the child to the parent
        linkChild(hierarchy, initialChild);
        setInitialPi(hierarchy, initialChild);
        LOG.info(String.format("***Initial Child (#%s) linked to Parent (#%s)", initialChild.getProposalNumber(), hierarchy.getProposalNumber()));
        
        finalizeHierarchySync(hierarchy);
        
        // return the parent id
        LOG.info(String.format("***Hierarchy creation (#%s) complete", hierarchy.getProposalNumber()));
        return hierarchy.getProposalNumber();
    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService#linkToHierarchy(java.lang.String,
     *      java.lang.String)
     */
    public void linkToHierarchy(DevelopmentProposal hierarchyProposal, DevelopmentProposal newChildProposal) throws ProposalHierarchyException {
        LOG.info(String.format("***Linking Child (#%s) linked to Parent (#%s)", newChildProposal.getProposalNumber(), hierarchyProposal.getProposalNumber()));
        if (!hierarchyProposal.isParent()) {
            throw new ProposalHierarchyException("Proposal " + hierarchyProposal.getProposalNumber()
                    + " is not a hierarchy parent");
        }
        if (newChildProposal.isInHierarchy()) {
            throw new ProposalHierarchyException("Proposal " + newChildProposal.getProposalNumber()
                    + " is already a member of a hierarchy");
        }
        prepareHierarchySync(hierarchyProposal);
        linkChild(hierarchyProposal, newChildProposal);
        finalizeHierarchySync(hierarchyProposal);
        LOG.info(String.format("***Linking Child (#%s) linked to Parent (#%s) complete", newChildProposal.getProposalNumber(), hierarchyProposal.getProposalNumber()));
    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService#removeFromHierarchy(java.lang.String)
     */
    public void removeFromHierarchy(DevelopmentProposal childProposal) throws ProposalHierarchyException {
        String hierarchyProposalNumber = childProposal.getHierarchyParentProposalNumber();
        DevelopmentProposal hierarchyProposal = getHierarchy(hierarchyProposalNumber);
        BudgetDocument hierarchyBudgetDoc = getHierarchyBudget(hierarchyProposal);
        Budget hierarchyBudget = hierarchyBudgetDoc.getBudget();
        LOG.info(String.format("***Removing Child (#%s) from Parent (#%s)", childProposal.getProposalNumber(), hierarchyProposal.getProposalNumber()));
        
        boolean isLast = proposalHierarchyDao.getHierarchyChildProposalNumbers(hierarchyProposalNumber).size()==1;
        
        childProposal.setHierarchyStatus(HierarchyStatusConstants.None.code());
        childProposal.setHierarchyParentProposalNumber(null);
        removeChildElements(hierarchyProposal, hierarchyBudget, childProposal.getProposalNumber());

        try {
            documentService.saveDocument(hierarchyBudgetDoc);
        }
        catch (WorkflowException e) {
            throw new ProposalHierarchyException(e);
        }
        
        if (isLast) {
            try {
                LOG.info(String.format("***Child (#%s) was last child, cancelling Parent (#%s)", childProposal.getProposalNumber(), hierarchyProposal.getProposalNumber()));
                Document doc = documentService.getByDocumentHeaderId(hierarchyProposal.getProposalDocument().getDocumentNumber());
                documentService.cancelDocument(doc, "Removed last child from Proposal Hierarchy");
            }
            catch (WorkflowException e) {
                throw new ProposalHierarchyException("Error cancelling empty parent proposal");
            }
        }
        else {
            synchronizeAllChildren(hierarchyProposal);
        }
        businessObjectService.save(childProposal);
        LOG.info(String.format("***Removing Child (#%s) from Parent (#%s) complete", childProposal.getProposalNumber(), hierarchyProposal.getProposalNumber()));
    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchySyncService#synchronizeAllChildren(java.lang.String)
     */
    public void synchronizeAllChildren(DevelopmentProposal hierarchyProposal) throws ProposalHierarchyException {
        LOG.info(String.format("***Synchronizing all Children of Parent (#%s)", hierarchyProposal.getProposalNumber()));
        if (!hierarchyProposal.isParent()) {
            throw new ProposalHierarchyException("Proposal " + hierarchyProposal.getProposalNumber()
                    + " is not a hierarchy parent");
        }
        prepareHierarchySync(hierarchyProposal);
        boolean changed = false;
        DevelopmentProposal childProposal;
        for (String childProposalNumber : proposalHierarchyDao.getHierarchyChildProposalNumbers(hierarchyProposal.getProposalNumber())) {
            childProposal = getDevelopmentProposal(childProposalNumber);
            changed |= synchronizeChild(hierarchyProposal, childProposal);
        }
        if (changed) {
            aggregateHierarchy(hierarchyProposal);
        }
        finalizeHierarchySync(hierarchyProposal);
        LOG.info(String.format("***Synchronizing all Children of Parent (#%s) complete", hierarchyProposal.getProposalNumber()));
    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchySyncService#synchronizeChild(java.lang.String)
     */
    public void synchronizeChild(DevelopmentProposal childProposal) throws ProposalHierarchyException {
        DevelopmentProposal hierarchy = getHierarchy(childProposal.getHierarchyParentProposalNumber());
        LOG.info(String.format("***Synchronizing Child (#%s) of Parent (#%s)", childProposal.getProposalNumber(), hierarchy.getProposalNumber()));
        
        prepareHierarchySync(hierarchy);
        boolean changed = synchronizeChild(hierarchy, childProposal);
        if (changed) {
            aggregateHierarchy(hierarchy);
        }
        finalizeHierarchySync(hierarchy);
        LOG.info(String.format("***Synchronizing Child (#%s) of Parent (#%s) complete", childProposal.getProposalNumber(), hierarchy.getProposalNumber()));
    }
    
    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService#lookupParent(org.kuali.kra.proposaldevelopment.bo.DevelopmentProposal)
     */
    public DevelopmentProposal lookupParent(DevelopmentProposal childProposal) throws ProposalHierarchyException {
        return getHierarchy(childProposal.getHierarchyParentProposalNumber());
    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService#getProposalSummaries(java.lang.String)
     */
    public List<HierarchyProposalSummary> getProposalSummaries(String proposalNumber) throws ProposalHierarchyException {
        List<HierarchyProposalSummary> summaries = new ArrayList<HierarchyProposalSummary>();
        List<String> proposalNumbers = new ArrayList<String>();
        DevelopmentProposal proposal = getDevelopmentProposal(proposalNumber);
        if (proposal.isParent()) {
            proposalNumbers.add(proposalNumber);
        }
        else if (proposal.isChild()) {
            proposalNumbers.add(proposal.getHierarchyParentProposalNumber());
        }
        else {
            throw new ProposalHierarchyException("Proposal " + proposalNumber + " is not a member of a hierarchy.");            
        }
        
        proposalNumbers.addAll(proposalHierarchyDao.getHierarchyChildProposalNumbers(proposalNumbers.get(0)));

        HierarchyProposalSummary summary;

        for (String number : proposalNumbers) {
            summary = proposalHierarchyDao.getProposalSummary(number);
            if (!StringUtils.equals(number, proposalNumbers.get(0))) {
                summary.setSynced(isSynchronized(number));
            }
            summaries.add(summary);
        }

        return summaries;
    }

    private void linkChild(DevelopmentProposal hierarchyProposal, DevelopmentProposal newChildProposal)
            throws ProposalHierarchyException {
        // set child to child status
        newChildProposal.setHierarchyStatus(HierarchyStatusConstants.Child.code());
        newChildProposal.setHierarchyParentProposalNumber(hierarchyProposal.getProposalNumber());
        // call synchronize
        synchronizeChild(hierarchyProposal, newChildProposal);
        // call aggregate
        aggregateHierarchy(hierarchyProposal);        
    }

    private void copyInitialData(DevelopmentProposal hierarchyProposal, DevelopmentProposal srcProposal)
            throws ProposalHierarchyException {
        // Required fields for saving document
        hierarchyProposal.setSponsor(srcProposal.getSponsor());
        hierarchyProposal.setSponsorCode(srcProposal.getSponsorCode());
        hierarchyProposal.setProposalTypeCode(srcProposal.getProposalTypeCode());
        hierarchyProposal.setRequestedStartDateInitial(srcProposal.getRequestedStartDateInitial());
        hierarchyProposal.setRequestedEndDateInitial(srcProposal.getRequestedEndDateInitial());
        hierarchyProposal.setOwnedByUnit(srcProposal.getOwnedByUnit());
        hierarchyProposal.setOwnedByUnitNumber(srcProposal.getOwnedByUnitNumber());
        hierarchyProposal.setActivityType(srcProposal.getActivityType());
        hierarchyProposal.setActivityTypeCode(srcProposal.getActivityTypeCode());
        hierarchyProposal.setTitle(srcProposal.getTitle());

        // Sponsor & program information
        hierarchyProposal.setDeadlineDate(srcProposal.getDeadlineDate());
        hierarchyProposal.setDeadlineType(srcProposal.getDeadlineType());
        hierarchyProposal.setNoticeOfOpportunityCode(srcProposal.getNoticeOfOpportunityCode());
        hierarchyProposal.setCfdaNumber(srcProposal.getCfdaNumber());
        hierarchyProposal.setPrimeSponsorCode(srcProposal.getPrimeSponsorCode());
        hierarchyProposal.setNsfCode(srcProposal.getNsfCode());
        hierarchyProposal.setSponsorProposalNumber(srcProposal.getSponsorProposalNumber());
        hierarchyProposal.setAgencyDivisionCode(srcProposal.getAgencyDivisionCode());
        hierarchyProposal.setAgencyProgramCode(srcProposal.getAgencyProgramCode());
        hierarchyProposal.setSubcontracts(srcProposal.getSubcontracts());
        hierarchyProposal.setProgramAnnouncementNumber(srcProposal.getProgramAnnouncementNumber());
        hierarchyProposal.setProgramAnnouncementTitle(srcProposal.getProgramAnnouncementTitle());

        // Organization/location
        ProposalSite newSite;
        hierarchyProposal.getProposalSites().clear();
        for (ProposalSite site : srcProposal.getProposalSites()) {
            newSite = (ProposalSite)ObjectUtils.deepCopy(site);
            newSite.setProposalNumber(null);
            newSite.setVersionNumber(null);
            for (CongressionalDistrict cd : newSite.getCongressionalDistricts()) {
                cd.setProposalNumber(null);
                cd.setCongressionalDistrictId(null);
                cd.setVersionNumber(null);
            }
            hierarchyProposal.addProposalSite(newSite);
        }
            
        // Delivery info
        hierarchyProposal.setMailBy(srcProposal.getMailBy());
        hierarchyProposal.setMailType(srcProposal.getMailType());
        hierarchyProposal.setMailAccountNumber(srcProposal.getMailAccountNumber());
        hierarchyProposal.setNumberOfCopies(srcProposal.getNumberOfCopies());
        hierarchyProposal.setMailingAddressId(srcProposal.getMailingAddressId());
        hierarchyProposal.setMailDescription(srcProposal.getMailDescription());
    }

    private boolean synchronizeChild(DevelopmentProposal hierarchyProposal, DevelopmentProposal childProposal)
            throws ProposalHierarchyException {
        
/*  TODO restore code below after testing
        if (isSynchronized(childProposal.getProposalNumber())) {
            return false;
        }
*/
        String principleInvestigatorId = null;
//        List<ProposalPerson> oldPersons = new ArrayList<ProposalPerson>();
        for (ProposalPerson person : hierarchyProposal.getProposalPersons()) {
//            if (StringUtils.equals(childProposal.getProposalNumber(), person.getHierarchyProposalNumber())) {
//                oldPersons.add(person);
                if (StringUtils.equals(person.getProposalPersonRoleId(), "PI")) {
                    principleInvestigatorId = person.getPersonId();
//                }
            }
        }
        List<PropScienceKeyword> oldKeywords = new ArrayList<PropScienceKeyword>();
        for (PropScienceKeyword keyword : hierarchyProposal.getPropScienceKeywords()) {
            if (StringUtils.equals(childProposal.getProposalNumber(), keyword.getHierarchyProposalNumber())) {
                oldKeywords.add(keyword);
            }
        }
        BudgetDocument hierarchyBudgetDocument = getHierarchyBudget(hierarchyProposal); 
        Budget hierarchyBudget = hierarchyBudgetDocument.getBudget();
        BudgetDocument childBudgetDocument = getFinalOrLatestChildBudget(childProposal);
        Budget childBudget = childBudgetDocument.getBudget();
        ObjectUtils.materializeAllSubObjects(hierarchyBudget);
        ObjectUtils.materializeAllSubObjects(childBudget);
        childProposal.setHierarchyLastSyncHashCode(computeHierarchyHashCode(childProposal, childBudget));
        
        removeChildElements(hierarchyProposal, hierarchyBudget, childProposal.getProposalNumber());
        
        // copy PropScienceKeywords
        for (PropScienceKeyword keyword : childProposal.getPropScienceKeywords()) {
            PropScienceKeyword newKeyword = new PropScienceKeyword(hierarchyProposal.getProposalNumber(), keyword.getScienceKeyword());
            int index = oldKeywords.indexOf(newKeyword);
            if (index > -1) {
                newKeyword = oldKeywords.get(index);
            }
            newKeyword.setHierarchyProposalNumber(childProposal.getProposalNumber());
            hierarchyProposal.addPropScienceKeyword(newKeyword);
        }
        
        // copy PropSpecialReviews
        for(ProposalSpecialReview review : childProposal.getPropSpecialReviews()) {
            ProposalSpecialReview newReview = (ProposalSpecialReview)ObjectUtils.deepCopy(review);
            newReview.setProposalNumber(hierarchyProposal.getProposalNumber());
            newReview.setSpecialReviewNumber(hierarchyProposal.getProposalDocument().getDocumentNextValue(Constants.PROPOSAL_SPECIALREVIEW_NUMBER));            
            newReview.setVersionNumber(null);
            newReview.setHierarchyProposalNumber(childProposal.getProposalNumber());
            hierarchyProposal.getPropSpecialReviews().add(newReview);
        }
        
        // copy Narratives
        for (Narrative narrative : childProposal.getNarratives()) {
            if (!StringUtils.equalsIgnoreCase(narrative.getNarrativeType().getAllowMultiple(), "N")) {
                Map<String,String> primaryKey = new HashMap<String,String>();            
                primaryKey.put("proposalNumber", narrative.getProposalNumber());
                primaryKey.put("moduleNumber", narrative.getModuleNumber()+"");
                NarrativeAttachment attachment = (NarrativeAttachment)businessObjectService.findByPrimaryKey(NarrativeAttachment.class, primaryKey);
                narrative.getNarrativeAttachmentList().clear();
                narrative.getNarrativeAttachmentList().add(attachment);
                
                Narrative newNarrative = (Narrative)ObjectUtils.deepCopy(narrative);
                newNarrative.setVersionNumber(null);
                newNarrative.setHierarchyProposalNumber(childProposal.getProposalNumber());
                narrativeService.addNarrative(hierarchyProposal.getProposalDocument(), newNarrative);
            }
        }

        // copy ProposalPersons
        for (ProposalPerson person : childProposal.getProposalPersons()) {
//            int index = oldPersons.indexOf(person);
            ProposalPerson newPerson;
//            if (index > -1) {
//                newPerson = oldPersons.get(index);
//            }
//            else {
                newPerson = (ProposalPerson)ObjectUtils.deepCopy(person);
                newPerson.setProposalNumber(hierarchyProposal.getProposalNumber());
                newPerson.getCreditSplits().clear();
                for (ProposalPersonUnit unit : newPerson.getUnits()) {
                    unit.getCreditSplits().clear();
                }
                newPerson.setProposalPersonNumber(null);
                newPerson.setVersionNumber(null);
                newPerson.setHierarchyProposalNumber(childProposal.getProposalNumber());
//            }
            
            if (StringUtils.equalsIgnoreCase(person.getProposalPersonRoleId(), "PI")) {
                newPerson.setProposalPersonRoleId("COI");
            }
            if (newPerson.getPersonId().equals(principleInvestigatorId)) {
                newPerson.setProposalPersonRoleId("PI");
            }
            hierarchyProposal.addProposalPerson(newPerson);
        }
        businessObjectService.save(childProposal);
        LOG.info(String.format("***Beginning Hierarchy Budget Sync for Parent %s and Child %s", hierarchyProposal.getProposalNumber(), childProposal.getProposalNumber()));
        synchronizeChildBudget(hierarchyBudget, childBudget, childProposal.getProposalNumber());
        try {
            documentService.saveDocument(hierarchyBudgetDocument);
        }
        catch (WorkflowException e) {
            throw new ProposalHierarchyException(e);
        }
        LOG.info(String.format("***Completed Hierarchy Budget Sync for Parent %s and Child %s", hierarchyProposal.getProposalNumber(), childProposal.getProposalNumber()));
        
        return true;
    }
    
    private void synchronizeChildBudget(Budget parentBudget, Budget childBudget, String childProposalNumber)
            throws ProposalHierarchyException {
        try {
            BudgetPerson newPerson;
            Map<Integer, BudgetPerson> personMap = new HashMap<Integer, BudgetPerson>();
            for (BudgetPerson person : childBudget.getBudgetPersons()) {
                newPerson = (BudgetPerson) ObjectUtils.deepCopy(person);
                newPerson.setPersonSequenceNumber(parentBudget.getBudgetDocument().getHackedDocumentNextValue(
                        Constants.PERSON_SEQUENCE_NUMBER));
                newPerson.setBudget(parentBudget);
                newPerson.setBudgetId(parentBudget.getBudgetId());
                newPerson.setHierarchyProposalNumber(childProposalNumber);
                newPerson.setVersionNumber(null);
                parentBudget.addBudgetPerson(newPerson);
                personMap.put(person.getPersonSequenceNumber(), newPerson);
            }
            int parentStartPeriod = getCorrespondingParentPeriod(parentBudget, childBudget);
            if (parentStartPeriod == -1) {
                throw new ProposalHierarchyException("Cannot find a parent budget period that corresponds to the child period.");
            }

            List<BudgetPeriod> parentPeriods = parentBudget.getBudgetPeriods();
            List<BudgetPeriod> childPeriods = childBudget.getBudgetPeriods();
            BudgetPeriod parentPeriod, childPeriod;
            Long budgetId = parentBudget.getBudgetId();
            Long budgetPeriodId;
            Integer budgetPeriod;
            for (int i = 0, j = parentStartPeriod; i < childPeriods.size(); i++, j++) {
                childPeriod = childPeriods.get(i);
                if (j >= parentPeriods.size()) {
                    parentPeriod = new BudgetPeriod();
                    parentPeriod.setBudgetPeriod(j + 1);
                    parentPeriod.setBudget(parentBudget);
                    parentPeriod.setStartDate(childPeriod.getStartDate());
                    parentPeriod.setEndDate(childPeriod.getEndDate());
                    parentPeriod.setBudgetId(budgetId);
                    parentBudget.add(parentPeriod);
                }
                else {
                    parentPeriod = parentPeriods.get(j);
                }
                
                budgetPeriodId = parentPeriod.getBudgetPeriodId();
                budgetPeriod = parentPeriod.getBudgetPeriod();
                BudgetLineItem parentLineItem;
                Integer lineItemNumber;
                
                if (true) { // switch to check for subproject or subbudget
                    for (BudgetLineItem childLineItem : childPeriod.getBudgetLineItems()) {
                        parentLineItem = (BudgetLineItem) ObjectUtils.deepCopy(childLineItem);
                        parentLineItem.setBudgetId(budgetId);
                        parentLineItem.setBudgetPeriodId(budgetPeriodId);
                        parentLineItem.setBudgetPeriod(budgetPeriod);
                        parentLineItem.setVersionNumber(null);
                        lineItemNumber = parentBudget.getBudgetDocument().getHackedDocumentNextValue(Constants.BUDGET_LINEITEM_NUMBER);
                        parentLineItem.setLineItemNumber(lineItemNumber);
                        parentLineItem.setHierarchyProposalNumber(childProposalNumber);
                        BudgetPerson budgetPerson;
                        for (BudgetPersonnelDetails details : parentLineItem.getBudgetPersonnelDetailsList()) {
                            budgetPerson = personMap.get(details.getPersonSequenceNumber());
                            details.setBudgetId(budgetId);
                            details.setBudgetPeriodId(budgetPeriodId);
                            details.setBudgetPeriod(budgetPeriod);
                            details.setBudgetPerson(budgetPerson);
                            details.setJobCode(budgetPerson.getJobCode());
                            details.setPersonId(budgetPerson.getPersonRolodexTbnId());
                            details.setPersonSequenceNumber(budgetPerson.getPersonSequenceNumber());
                            details.setPersonNumber(parentBudget.getBudgetDocument().getHackedDocumentNextValue(Constants.BUDGET_PERSON_LINE_NUMBER));
                            details.setLineItemNumber(lineItemNumber);
                            details.setVersionNumber(null);
                        }
                        parentPeriod.getBudgetLineItems().add(parentLineItem);
                    }
                }
//                else { // subproject budget
//                    Map primaryKeys;
//                    CostElement costElement;
//                    if (childPeriod.getTotalIndirectCost().isNonZero()) {
//                        primaryKeys = new HashMap();
//                        primaryKeys.put("costElement", "PHTID02");
//                        costElement = (CostElement)businessObjectService.findByPrimaryKey(CostElement.class, primaryKeys);
//                        parentLineItem = new BudgetLineItem();
//                        parentLineItem.setStartDate(parentPeriod.getStartDate());
//                        parentLineItem.setEndDate(parentPeriod.getEndDate());
//                        parentLineItem.setBudgetId(budgetId);
//                        parentLineItem.setBudgetPeriodId(budgetPeriodId);
//                        parentLineItem.setBudgetPeriod(budgetPeriod);
//                        parentLineItem.setVersionNumber(null);
//                        lineItemNumber = parentBudget.getBudgetDocument().getHackedDocumentNextValue(Constants.BUDGET_LINEITEM_NUMBER);
//                        parentLineItem.setLineItemNumber(lineItemNumber);
//                        parentLineItem.setHierarchyProposalNumber(childProposalNumber);
//                        parentLineItem.setLineItemCost(childPeriod.getTotalIndirectCost());
//                        parentLineItem.setIndirectCost(childPeriod.getTotalIndirectCost());
//                        parentLineItem.setCostElementBO(costElement);
//                        parentLineItem.setCostElement(costElement.getCostElement());
//                        parentLineItem.setBudgetCategoryCode(costElement.getBudgetCategoryCode());
//                        parentLineItem.setOnOffCampusFlag(costElement.getOnOffCampusFlag());
//                        parentLineItem.setApplyInRateFlag(true);
//                        parentPeriod.getBudgetLineItems().add(parentLineItem);
//                    }
//                    if (childPeriod.getTotalIndirectCost().isNonZero()) {
//                        primaryKeys = new HashMap();
//                        primaryKeys.put("costElement", "PHTD01");
//                        costElement = (CostElement)businessObjectService.findByPrimaryKey(CostElement.class, primaryKeys);
//                        parentLineItem = new BudgetLineItem();
//                        parentLineItem.setStartDate(parentPeriod.getStartDate());
//                        parentLineItem.setEndDate(parentPeriod.getEndDate());
//                        parentLineItem.setBudgetId(budgetId);
//                        parentLineItem.setBudgetPeriodId(budgetPeriodId);
//                        parentLineItem.setBudgetPeriod(budgetPeriod);
//                        parentLineItem.setVersionNumber(null);
//                        lineItemNumber = parentBudget.getBudgetDocument().getHackedDocumentNextValue(Constants.BUDGET_LINEITEM_NUMBER);
//                        parentLineItem.setLineItemNumber(lineItemNumber);
//                        parentLineItem.setHierarchyProposalNumber(childProposalNumber);
//                        parentLineItem.setLineItemCost(childPeriod.getTotalDirectCost());
//                        parentLineItem.setDirectCost(childPeriod.getTotalDirectCost());
//                        parentLineItem.setCostElementBO(costElement);
//                        parentLineItem.setCostElement(costElement.getCostElement());
//                        parentLineItem.setBudgetCategoryCode(costElement.getBudgetCategoryCode());
//                        parentLineItem.setOnOffCampusFlag(costElement.getOnOffCampusFlag());
//                        parentLineItem.setApplyInRateFlag(true);
//                        parentPeriod.getBudgetLineItems().add(parentLineItem);
//                    }
//                }
            }
        }
        catch (Exception e) {
            LOG.error("Problem copying line items to parent", e);
            throw new ProposalHierarchyException("Problem copying line items to parent", e);
        }

    }

    private void aggregateHierarchy(DevelopmentProposal hierarchy) throws ProposalHierarchyException {
        LOG.info(String.format("***Aggregating Proposal Hierarchy #%s", hierarchy.getProposalNumber()));
        ArrayList<ProposalPerson> persons = new ArrayList<ProposalPerson>();
        
        for (ProposalPerson person : hierarchy.getProposalPersons()) {
            if (!persons.contains(person)) {
                persons.add(person);
            }
            else if ((person.isInvestigator() != persons.get(persons.indexOf(person)).isInvestigator()) 
                    && (person.isInvestigator() != persons.get(persons.lastIndexOf(person)).isInvestigator())) {
                persons.add(person);
            }
            else person.setHiddenInHierarchy(true);
        }
        BudgetDocument hierarchyBudgetDocument = getHierarchyBudget(hierarchy); 
        Budget hierarchyBudget = hierarchyBudgetDocument.getBudget();
        KualiForm oldForm = GlobalVariables.getKualiForm();
        GlobalVariables.setKualiForm(null);
        budgetCalculationService.calculateBudget(hierarchyBudget);
        GlobalVariables.setKualiForm(oldForm);
        try {
            documentService.saveDocument(hierarchyBudgetDocument);
        }
        catch (WorkflowException e) {
            throw new ProposalHierarchyException(e);
        }
    }

    private DevelopmentProposal getHierarchy(String hierarchyProposalNumber) throws ProposalHierarchyException {
        DevelopmentProposal hierarchy = getDevelopmentProposal(hierarchyProposalNumber);
        if (hierarchy == null || !hierarchy.isParent())
            throw new ProposalHierarchyException("Proposal " + hierarchyProposalNumber + " is not a hierarchy");
        return hierarchy;
    }

    public DevelopmentProposal getDevelopmentProposal(String proposalNumber) {
        Map<String, String> pk = new HashMap<String, String>();
        pk.put("proposalNumber", proposalNumber);
        return (DevelopmentProposal) (businessObjectService.findByPrimaryKey(DevelopmentProposal.class, pk));
    }

    private boolean isSynchronized(String childProposalNumber) throws ProposalHierarchyException {
        DevelopmentProposal childProposal = getDevelopmentProposal(childProposalNumber);
        Budget childBudget = getFinalOrLatestChildBudget(childProposal).getBudget();
        ObjectUtils.materializeAllSubObjects(childBudget);
        int hc1 = computeHierarchyHashCode(childProposal, childBudget);
        int hc2 = childProposal.getHierarchyLastSyncHashCode();
        return hc1 == hc2;
    }
    
    private void setInitialPi(DevelopmentProposal hierarchy, DevelopmentProposal child) {
        ProposalPerson pi = null;
        for (ProposalPerson person : child.getProposalPersons()) {
            if (StringUtils.equalsIgnoreCase(person.getProposalPersonRoleId(), "PI")) {
                pi = person;
                break;
            }
        }
        if (pi != null) {
            int index = hierarchy.getProposalPersons().indexOf(pi);
            if (index > -1) hierarchy.getProposalPerson(index).setProposalPersonRoleId("PI");
        }
    }
    
    private BudgetDocument getHierarchyBudget(DevelopmentProposal hierarchyProposal) throws ProposalHierarchyException {
        String budgetDocumentNumber = hierarchyProposal.getProposalDocument().getBudgetDocumentVersions().get(0).getBudgetVersionOverview().getDocumentNumber();
        BudgetDocument budgetDocument = null;
        try {
            budgetDocument = (BudgetDocument) documentService.getByDocumentHeaderId(budgetDocumentNumber);
        }
        catch (WorkflowException e) {
            throw new ProposalHierarchyException(e);
        }
        return budgetDocument;//.getBudget();
    }
 
    private BudgetDocument getFinalOrLatestChildBudget(DevelopmentProposal childProposal) throws ProposalHierarchyException {
        String budgetDocumentNumber = null;
        for (BudgetDocumentVersion version : childProposal.getProposalDocument().getBudgetDocumentVersions()) {
            budgetDocumentNumber = version.getDocumentNumber();
            if (version.getBudgetVersionOverview().isFinalVersionFlag()) {
                break;
            }
        }
        BudgetDocument budgetDocument = null;
        try {
            budgetDocument = (BudgetDocument) documentService.getByDocumentHeaderId(budgetDocumentNumber);
        }
        catch (WorkflowException e) {
            throw new ProposalHierarchyException(e);
        }
        return budgetDocument;//.getBudget();
    }
    
    private void initializeBudget (DevelopmentProposal hierarchyProposal, DevelopmentProposal childProposal) throws ProposalHierarchyException {
        BudgetDocument parentBudgetDoc = getHierarchyBudget(hierarchyProposal);
        Budget parentBudget = parentBudgetDoc.getBudget();
        BudgetDocument childBudgetDocument = getFinalOrLatestChildBudget(childProposal); 
        Budget childBudget = childBudgetDocument.getBudget();
        BudgetPeriod parentPeriod, childPeriod;
        for (int i=0; i < childBudget.getBudgetPeriods().size(); i++) {
            parentPeriod = parentBudget.getBudgetPeriod(i);
            childPeriod = childBudget.getBudgetPeriod(i);
            parentPeriod.setStartDate(childPeriod.getStartDate());
            parentPeriod.setEndDate(childPeriod.getEndDate());
            parentPeriod.setBudgetPeriod(childPeriod.getBudgetPeriod());
        }
        
        parentBudget.setCostSharingAmount(new BudgetDecimal(0));
        parentBudget.setTotalCost(new BudgetDecimal(0));
        parentBudget.setTotalDirectCost(new BudgetDecimal(0));
        parentBudget.setTotalIndirectCost(new BudgetDecimal(0));
        parentBudget.setUnderrecoveryAmount(new BudgetDecimal(0));
        try {
            documentService.saveDocument(parentBudgetDoc);
        }
        catch (WorkflowException e) {
            throw new ProposalHierarchyException(e);
        }
    }
    
    public ProposalHierarchyErrorDto validateChildBudgetPeriods(DevelopmentProposal hierarchyProposal,
            DevelopmentProposal childProposal) throws ProposalHierarchyException {
        BudgetDocument parentBudgetDoc = getHierarchyBudget(hierarchyProposal);
        Budget parentBudget = parentBudgetDoc.getBudget();
        BudgetDocument childBudgetDocument = getFinalOrLatestChildBudget(childProposal); 
        Budget childBudget = childBudgetDocument.getBudget();
        return validateChildBudgetPeriods(parentBudget, childBudget);
    }
    
    private ProposalHierarchyErrorDto validateChildBudgetPeriods(Budget parentBudget,
            Budget childBudget) throws ProposalHierarchyException {
        ProposalHierarchyErrorDto retval = null;
        // check that child budget starts on one of the budget period starts
        int correspondingStart = getCorrespondingParentPeriod(parentBudget, childBudget);
        if (correspondingStart == -1) {
            retval = new ProposalHierarchyErrorDto(ERROR_BUDGET_START_DATE_INCONSISTENT);
        }
        // check that child budget periods map to parent periods
        else {
            List<BudgetPeriod> parentPeriods = parentBudget.getBudgetPeriods();
            List<BudgetPeriod> childPeriods = childBudget.getBudgetPeriods();
            BudgetPeriod parentPeriod, childPeriod;
            for (int i = correspondingStart, j = 0; i < parentPeriods.size() && j < childPeriods.size(); i++, j++) {
                parentPeriod = parentPeriods.get(i);
                childPeriod = childPeriods.get(j);
                if (!parentPeriod.getStartDate().equals(childPeriod.getStartDate())
                        || !parentPeriod.getEndDate().equals(childPeriod.getEndDate())) {
                    retval = new ProposalHierarchyErrorDto(ERROR_BUDGET_PERIOD_DURATION_INCONSISTENT, "" + j);
                    break;
                }
            }
        }

        return retval;
    }
    
    private int getCorrespondingParentPeriod(Budget parentBudget, Budget childBudget) {
        int correspondingStart = -1;
 
        Date childStart = childBudget.getStartDate();
        // check that child budget starts somewhere during parent budget
        if (childStart.compareTo(parentBudget.getStartDate()) >= 0
                && childStart.compareTo(parentBudget.getEndDate()) < 0) {
            // check that child budget starts on one of the budget period starts
            List<BudgetPeriod> parentPeriods = parentBudget.getBudgetPeriods();
            for (int i=0; i<parentPeriods.size(); i++) {
                if (childStart.equals(parentPeriods.get(i).getStartDate())) {
                    correspondingStart = i;
                    break;
                }
            }
        }
        return correspondingStart;
    }
    
    private void removeChildElements(DevelopmentProposal parentProposal, Budget parentBudget, String childProposalNumber) {
        List<PropScienceKeyword> keywords = parentProposal.getPropScienceKeywords();
        for (int i=keywords.size()-1; i>=0; i--) {
            if (StringUtils.equals(childProposalNumber, keywords.get(i).getHierarchyProposalNumber())) {
                keywords.remove(i);
            }
        }

        List<ProposalSpecialReview> reviews = parentProposal.getPropSpecialReviews();
        for (int i=reviews.size()-1; i>=0; i--) {
            if (StringUtils.equals(childProposalNumber, reviews.get(i).getHierarchyProposalNumber())) {
                reviews.remove(i);
            }
        }

        List<Narrative> narratives = parentProposal.getNarratives();
        for (int i=narratives.size()-1; i>=0; i--) {
            if (StringUtils.equals(childProposalNumber, narratives.get(i).getHierarchyProposalNumber())) {
                narratives.remove(i);
            }
        }

        List<ProposalPerson> persons = parentProposal.getProposalPersons();
        for (int i=persons.size()-1; i>=0; i--) {
            if (StringUtils.equals(childProposalNumber, persons.get(i).getHierarchyProposalNumber())) {
                persons.remove(i);
            }
        }

        BudgetPersonnelBudgetService budgetPersonnelBudgetService = KraServiceLocator.getService(BudgetPersonnelBudgetService.class);
        List<BudgetPeriod> periods = parentBudget.getBudgetPeriods();
        List<BudgetLineItem> lineItems;
        List<BudgetPersonnelDetails> personnelDetailsList;
        BudgetPeriod period = null;
        BudgetLineItem lineItem = null;
        for (int i = periods.size()-1; i>=0; i--) {
            period = periods.get(i);
            lineItems = period.getBudgetLineItems();
            System.err.println(lineItems.size());
            for (int j = lineItems.size()-1; j>=0; j--) {
                lineItem = lineItems.get(j);
                if (StringUtils.equals(childProposalNumber, lineItem.getHierarchyProposalNumber())) {
                    personnelDetailsList = lineItem.getBudgetPersonnelDetailsList();
                    for (int k = personnelDetailsList.size()-1; k>=0; k--) {
                        budgetPersonnelBudgetService.deleteBudgetPersonnelDetails(parentBudget, i, j, k);
                    }
                    lineItems.remove(j);
                    parentBudget.setBudgetLineItemDeleted(true);
                }
            }
            //if (lineItems.isEmpty()) {
            //    periods.remove(index);
            //}
            System.err.println(lineItems.size());
        }
        
        List<BudgetPerson> budgetPersons = parentBudget.getBudgetPersons();
        for (int i=budgetPersons.size()-1; i>=0; i--) {
            if (StringUtils.equals(childProposalNumber, budgetPersons.get(i).getHierarchyProposalNumber())) {
                budgetPersonnelBudgetService.deleteBudgetPersonnelDetailsForPerson(parentBudget, budgetPersons.get(i));
                budgetPersons.remove(i);
            }
        }
    }
    
    private void prepareHierarchySync(DevelopmentProposal hierarchyProposal) {
        hierarchyProposal.getProposalDocument().refreshReferenceObject("documentNextvalues");
    }
    
    private void finalizeHierarchySync(DevelopmentProposal hierarchyProposal) {
        businessObjectService.save(hierarchyProposal.getProposalDocument().getDocumentNextvalues());       
        businessObjectService.save(hierarchyProposal);
    }
        
    private void copyInitialAttachments(DevelopmentProposal srcProposal, DevelopmentProposal destProposal) {
        
        ProposalPersonBiography destPropPersonBio;
        for (ProposalPersonBiography srcPropPersonBio : srcProposal.getPropPersonBios()) {
            loadBioContent(srcPropPersonBio);
            destPropPersonBio = (ProposalPersonBiography)ObjectUtils.deepCopy(srcPropPersonBio);
            propPersonBioService.addProposalPersonBiography(destProposal.getProposalDocument(), destPropPersonBio);
        }

        Narrative destNarrative;
        for (Narrative srcNarrative : srcProposal.getNarratives()) {
            if (StringUtils.equalsIgnoreCase(srcNarrative.getNarrativeType().getAllowMultiple(), "N")) {
                loadAttachmentContent(srcNarrative);
                destNarrative = (Narrative)ObjectUtils.deepCopy(srcNarrative);
                destNarrative.setModuleStatusCode("I");
                narrativeService.addNarrative(destProposal.getProposalDocument(), destNarrative);
            }
        }
        
        Narrative destInstituteAttachment;
        for (Narrative srcInstituteAttachment : srcProposal.getInstituteAttachments()) {
            loadAttachmentContent(srcInstituteAttachment);
            destInstituteAttachment = (Narrative)ObjectUtils.deepCopy(srcInstituteAttachment);
            narrativeService.addInstituteAttachment(destProposal.getProposalDocument(), destInstituteAttachment);
        }
    }

    private void loadAttachmentContent(Narrative narrative){
        Map<String,String> primaryKey = new HashMap<String,String>();
        primaryKey.put("proposalNumber", narrative.getProposalNumber());
        primaryKey.put("moduleNumber", narrative.getModuleNumber()+"");
        NarrativeAttachment attachment = (NarrativeAttachment)businessObjectService.findByPrimaryKey(NarrativeAttachment.class, primaryKey);
        narrative.getNarrativeAttachmentList().clear();
        narrative.getNarrativeAttachmentList().add(attachment);
    }
    
    private void loadBioContent(ProposalPersonBiography bio){
        Map<String,String> primaryKey = new HashMap<String,String>();
        primaryKey.put("proposalNumber", bio.getProposalNumber());
        primaryKey.put("biographyNumber", bio.getBiographyNumber()+"");
        primaryKey.put("proposalPersonNumber", bio.getProposalPersonNumber()+"");
        ProposalPersonBiographyAttachment attachment = (ProposalPersonBiographyAttachment)businessObjectService.findByPrimaryKey(ProposalPersonBiographyAttachment.class, primaryKey);
        bio.getPersonnelAttachmentList().clear();
        bio.getPersonnelAttachmentList().add(attachment);
    }
    
    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService#getHierarchyChildRouteCode(java.lang.String, java.lang.String)
     */
    public String getHierarchyChildRouteCode( String oldStatus, String newStatus) {
        
        LOG.info( String.format( "Route status change %s:%s",oldStatus,newStatus));
        
        String retCd = null;
        if( StringUtils.equals(newStatus,KEWConstants.ROUTE_HEADER_ENROUTE_CD) && ( StringUtils.equals( oldStatus, KEWConstants.ROUTE_HEADER_INITIATED_CD) || StringUtils.equals(oldStatus, KEWConstants.ROUTE_HEADER_SAVED_CD) ) ) {
                retCd = PROPOSAL_HIERARCHY_PARENT_ENROUTE;
        } else if ( StringUtils.equals(newStatus, KEWConstants.ROUTE_HEADER_FINAL_CD)) {
                retCd = PROPOSAL_HIERARCHY_PARENT_FINAL;
        } else if( StringUtils.equals( newStatus, KEWConstants.ROUTE_HEADER_DISAPPROVED_CD )) {
                retCd = PROPOSAL_HIERARCHY_PARENT_DISAPPROVE;
        } else if( StringUtils.equals( newStatus, KEWConstants.ROUTE_HEADER_CANCEL_CD ) ) {
               retCd = PROPOSAL_HIERARCHY_PARENT_CANCEL;
        } else {
            LOG.warn(String.format("Do not know how to calculate hierarchy child status for %s to %s",oldStatus,newStatus) );
        }
  
        LOG.info(String.format("Route status for children:%s",retCd ));
        return retCd;
    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService#getParentWorkflowStatus(org.kuali.kra.proposaldevelopment.bo.DevelopmentProposal)
     */
    public KualiWorkflowDocument getParentWorkflowDocument(ProposalDevelopmentDocument child) throws ProposalHierarchyException {
        try {
            DevelopmentProposal parentProposal = getHierarchy(child.getDevelopmentProposal().getHierarchyParentProposalNumber());
            String parentDocumentNumber = parentProposal.getProposalDocument().getDocumentNumber();
            KualiWorkflowDocument pWorkflow = documentService.getByDocumentHeaderId(parentDocumentNumber).getDocumentHeader().getWorkflowDocument();
            return pWorkflow;
        } catch (WorkflowException e) {
            LOG.error( "Workflow exception thrown getting hierarchy routing status.", e );
            throw new ProposalHierarchyException( String.format("Could not lookup hierarchy workflow status for child:%s",child.getDocumentHeader().getDocumentNumber()),e);
        }
    }
    
    /**
     * Creates a hash of the data pertinent to a hierarchy for comparison during hierarchy syncing. 
     */
    private int computeHierarchyHashCode(DevelopmentProposal proposal, Budget budget) {
        int prime = 31;
        int result = 1;
        budgetCalculationService.calculateBudgetSummaryTotals(budget);
        for (ProposalPerson person : proposal.getProposalPersons()) {
            result = prime * result + person.hashCode();
        }
        for (Narrative narrative : proposal.getNarratives()) {
            result = prime * result + narrative.hierarchyHashCode();
        }
        for (PropScienceKeyword keyword : proposal.getPropScienceKeywords()) {
            result = prime * result + keyword.getScienceKeywordCode().hashCode();
        }
        for (ProposalSpecialReview review : proposal.getPropSpecialReviews()) {
            result = prime * result + review.hierarchyHashCode();
        }
        result = prime * result + budget.getBudgetSummaryTotals().hashCode();
        return result;
    }
}
