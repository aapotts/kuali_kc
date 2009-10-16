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

import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.kra.budget.BudgetDecimal;
import org.kuali.kra.budget.calculator.BudgetCalculationService;
import org.kuali.kra.budget.core.Budget;
import org.kuali.kra.budget.core.BudgetService;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.nonpersonnel.BudgetLineItem;
import org.kuali.kra.budget.parameters.BudgetPeriod;
import org.kuali.kra.budget.personnel.BudgetPerson;
import org.kuali.kra.budget.personnel.BudgetPersonnelDetails;
import org.kuali.kra.budget.versions.BudgetDocumentVersion;
import org.kuali.kra.budget.web.struts.form.BudgetForm;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.RoleConstants;
import org.kuali.kra.proposaldevelopment.bo.DevelopmentProposal;
import org.kuali.kra.proposaldevelopment.bo.Narrative;
import org.kuali.kra.proposaldevelopment.bo.NarrativeAttachment;
import org.kuali.kra.proposaldevelopment.bo.PropScienceKeyword;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.bo.ProposalSpecialReview;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.hierarchy.HierarchyStatusConstants;
import org.kuali.kra.proposaldevelopment.hierarchy.ProposalHierarchyErrorDto;
import org.kuali.kra.proposaldevelopment.hierarchy.ProposalHierarchyException;
import org.kuali.kra.proposaldevelopment.hierarchy.bo.HierarchyProposalSummary;
import org.kuali.kra.proposaldevelopment.hierarchy.bo.ProposalHierarchyChild;
import org.kuali.kra.proposaldevelopment.hierarchy.dao.ProposalHierarchyDao;
import org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService;
import org.kuali.kra.proposaldevelopment.service.NarrativeService;
import org.kuali.kra.rice.shim.UniversalUser;
import org.kuali.kra.service.KraAuthorizationService;
import org.kuali.rice.kew.exception.WorkflowException;
import org.kuali.rice.kns.bo.PersistableBusinessObject;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.DocumentService;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.ObjectUtils;
import org.kuali.rice.kns.web.struts.form.KualiForm;
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
        UniversalUser user = new UniversalUser(GlobalVariables.getUserSession().getPerson());
        String username = user.getPersonUserIdentifier();
        kraAuthorizationService.addRole(username, RoleConstants.AGGREGATOR, newDoc);
        
        initializeBudget(hierarchy, initialChild);

        // link the child to the parent
        linkChild(hierarchy, initialChild);
        setInitialPi(hierarchy, initialChild);
        LOG.info(String.format("***Initial Child (#%s) linked to Parent (#%s)", initialChild.getProposalNumber(), hierarchy.getProposalNumber()));
        
        businessObjectService.save(hierarchy);
        businessObjectService.save(initialChild);
        
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
        linkChild(hierarchyProposal, newChildProposal);
        businessObjectService.save(newChildProposal);
        businessObjectService.save(hierarchyProposal);
        businessObjectService.save(hierarchyProposal.getProposalDocument().getDocumentNextvalues());
        LOG.info(String.format("***Linking Child (#%s) linked to Parent (#%s) complete", newChildProposal.getProposalNumber(), hierarchyProposal.getProposalNumber()));
    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService#removeFromHierarchy(java.lang.String)
     */
    public void removeFromHierarchy(DevelopmentProposal childProposal) throws ProposalHierarchyException {
        ProposalHierarchyChild hierarchyChild = getHierarchyChild(childProposal.getProposalNumber());
        String hierarchyProposalNumber = hierarchyChild.getHierarchyProposalNumber();
        DevelopmentProposal hierarchyProposal = getHierarchy(hierarchyProposalNumber);
        Budget hierarchyBudget = getHierarchyBudget(hierarchyProposal);
        LOG.info(String.format("***Removing Child (#%s) from Parent (#%s)", childProposal.getProposalNumber(), hierarchyProposal.getProposalNumber()));
        
        childProposal.setHierarchyStatus(HierarchyStatusConstants.None.code());
        removeChildElements(hierarchyProposal, hierarchyBudget, hierarchyChild);
        hierarchyProposal.getChildren().remove(hierarchyChild);

        businessObjectService.delete(hierarchyChild);
        businessObjectService.save(hierarchyBudget);

        if (hierarchyProposal.getChildren().size() == 0) {
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
        boolean changed = false;
        for (ProposalHierarchyChild child : hierarchyProposal.getChildren()) {
            changed |= synchronizeChild(hierarchyProposal, child, getDevelopmentProposal(child.getProposalNumber()));
        }
        if (changed) {
            aggregateHierarchy(hierarchyProposal);
        }
        businessObjectService.save(hierarchyProposal);
        businessObjectService.save(hierarchyProposal.getProposalDocument().getDocumentNextvalues());
        LOG.info(String.format("***Synchronizing all Children of Parent (#%s) complete", hierarchyProposal.getProposalNumber()));
    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchySyncService#synchronizeChild(java.lang.String)
     */
    public void synchronizeChild(DevelopmentProposal childProposal) throws ProposalHierarchyException {
        ProposalHierarchyChild hierarchyChild = getHierarchyChild(childProposal.getProposalNumber());
        DevelopmentProposal hierarchy = getHierarchy(hierarchyChild.getHierarchyProposalNumber());
        LOG.info(String.format("***Synchronizing Child (#%s) of Parent (#%s)", childProposal.getProposalNumber(), hierarchy.getProposalNumber()));
        
        // the next statement is to avoid an OJB optimistic locking exception because of different object references
        hierarchyChild = hierarchy.getChildren().get(hierarchy.getChildren().indexOf(hierarchyChild));
        
        boolean changed = synchronizeChild(hierarchy, hierarchyChild, childProposal);
        if (changed) {
            aggregateHierarchy(hierarchy);
        }
        businessObjectService.save(hierarchy);
        businessObjectService.save(hierarchy.getProposalDocument().getDocumentNextvalues());
        LOG.info(String.format("***Synchronizing Child (#%s) of Parent (#%s) complete", childProposal.getProposalNumber(), hierarchy.getProposalNumber()));
    }
    
    

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService#lookupParent(org.kuali.kra.proposaldevelopment.bo.DevelopmentProposal)
     */
    public DevelopmentProposal lookupParent(DevelopmentProposal childProposal) throws ProposalHierarchyException {
        ProposalHierarchyChild hierarchyChild = getHierarchyChild(childProposal.getProposalNumber());
        DevelopmentProposal hierarchy = getHierarchy(hierarchyChild.getHierarchyProposalNumber());
        return hierarchy;
    }

    /**
     * @see org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService#getProposalSummaries(java.lang.String)
     */
    public List<HierarchyProposalSummary> getProposalSummaries(String proposalNumber) throws ProposalHierarchyException {
        List<HierarchyProposalSummary> summaries = new ArrayList<HierarchyProposalSummary>();
        DevelopmentProposal hierarchy = null;
        try {
            hierarchy = getHierarchy(proposalNumber);
        }
        catch (ProposalHierarchyException x) {
            try {
                ProposalHierarchyChild child = getHierarchyChild(proposalNumber);
                hierarchy = getHierarchy(child.getHierarchyProposalNumber());
            }
            catch (ProposalHierarchyException phx) {
                throw new ProposalHierarchyException("Proposal " + proposalNumber + " is not a member of a hierarchy.");
            }
        }
        List<String> proposalNumbers = new ArrayList<String>();
        proposalNumbers.add(hierarchy.getProposalNumber());
        List<ProposalHierarchyChild> children = hierarchy.getChildren();
        HierarchyProposalSummary summary;
        for (ProposalHierarchyChild child : children) {
            proposalNumbers.add(child.getProposalNumber());
        }

        for (String number : proposalNumbers) {
            summary = proposalHierarchyDao.getProposalSummary(number);
            if (!StringUtils.equals(number, hierarchy.getProposalNumber())) {
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
        // create and populate child bo
        ProposalHierarchyChild childBO = new ProposalHierarchyChild();
        childBO.setProposalNumber(newChildProposal.getProposalNumber());
        childBO.setHierarchyProposalNumber(hierarchyProposal.getProposalNumber());
        // add bo to parent
        hierarchyProposal.getChildren().add(childBO);
        // call synchronize
        synchronizeChild(hierarchyProposal, childBO, newChildProposal);
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
        hierarchyProposal.setProposalSites(srcProposal.getProposalSites());
            
        // Delivery info
        hierarchyProposal.setMailBy(srcProposal.getMailBy());
        hierarchyProposal.setMailType(srcProposal.getMailType());
        hierarchyProposal.setMailAccountNumber(srcProposal.getMailAccountNumber());
        hierarchyProposal.setNumberOfCopies(srcProposal.getNumberOfCopies());
        hierarchyProposal.setMailingAddressId(srcProposal.getMailingAddressId());
        hierarchyProposal.setMailDescription(srcProposal.getMailDescription());
    }

    private boolean synchronizeChild(DevelopmentProposal hierarchyProposal, ProposalHierarchyChild hierarchyChild, DevelopmentProposal childProposal)
            throws ProposalHierarchyException {
        
/*  TODO uncomment after testing
        if (childProposal.hierarchyChildHashCode() == hierarchyChild.getProposalHashCode()) {
            return false;
        }
*/
        List<PropScienceKeyword> oldKeywords = new ArrayList<PropScienceKeyword>(hierarchyProposal.getPropScienceKeywords());
        Budget hierarchyBudget = getHierarchyBudget(hierarchyProposal);
        Budget childBudget = getFinalOrLatestChildBudget(childProposal);
        ObjectUtils.materializeAllSubObjects(childBudget);

        String principleInvestigatorId = null;
        for (ProposalPerson person : hierarchyChild.getProposalPersons()) {
            if (StringUtils.equals(person.getProposalPersonRoleId(), "PI")) {
                principleInvestigatorId = person.getPersonId();
                break;
            }
        }
        
        removeChildElements(hierarchyProposal, hierarchyBudget, hierarchyChild);
        
        // copy PropScienceKeywords
        for (PropScienceKeyword keyword : childProposal.getPropScienceKeywords()) {
            PropScienceKeyword newKeyword = new PropScienceKeyword(hierarchyProposal.getProposalNumber(), keyword.getScienceKeyword());
            if (oldKeywords.contains(newKeyword)) {
                newKeyword = oldKeywords.get(oldKeywords.indexOf(newKeyword));
            }
            newKeyword.setHierarchyProposalNumber(childProposal.getProposalNumber());
            hierarchyProposal.addPropScienceKeyword(newKeyword);
            hierarchyChild.getPropScienceKeywords().add(newKeyword);
        }
        
        // copy PropSpecialReviews
        for(ProposalSpecialReview review : childProposal.getPropSpecialReviews()) {
            ProposalSpecialReview newReview = (ProposalSpecialReview)ObjectUtils.deepCopy(review);
            newReview.setProposalNumber(hierarchyProposal.getProposalNumber());
            newReview.setSpecialReviewNumber(hierarchyProposal.getProposalDocument().getDocumentNextValue(Constants.PROPOSAL_SPECIALREVIEW_NUMBER));            
            newReview.setVersionNumber(null);
            newReview.setHierarchyProposalNumber(childProposal.getProposalNumber());
            hierarchyProposal.getPropSpecialReviews().add(newReview);
            hierarchyChild.getPropSpecialReviews().add(newReview);
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
                hierarchyChild.getNarratives().add(newNarrative);
            }
        }

        // copy ProposalPersons
        for (ProposalPerson person : childProposal.getProposalPersons()) {
            ProposalPerson newPerson = (ProposalPerson)ObjectUtils.deepCopy(person);
            if (StringUtils.equalsIgnoreCase(newPerson.getProposalPersonRoleId(), "PI")) {
                newPerson.setProposalPersonRoleId("COI");
            }
            if (newPerson.getPersonId().equals(principleInvestigatorId)) {
                newPerson.setProposalPersonRoleId("PI");
            }
            newPerson.setProposalNumber(hierarchyProposal.getProposalNumber());
            newPerson.setProposalPersonNumber(null);
            newPerson.setVersionNumber(null);
            newPerson.setHierarchyProposalNumber(childProposal.getProposalNumber());
            hierarchyProposal.addProposalPerson(newPerson);
            hierarchyChild.getProposalPersons().add(newPerson);
        }
        
        LOG.info(String.format("***Beginning Hierarchy Budget Sync for Parent %s and Child %s", hierarchyProposal.getProposalNumber(), childProposal.getProposalNumber()));
        hierarchyBudget.getBudgetDocument().refreshReferenceObject("documentNextvalues");
        synchronizeChildBudget(hierarchyBudget, hierarchyChild, childBudget);
        businessObjectService.save(hierarchyBudget);
        businessObjectService.save(hierarchyBudget.getBudgetDocument().getDocumentNextvalues());
        LOG.info(String.format("***Completed Hierarchy Budget Sync for Parent %s and Child %s", hierarchyProposal.getProposalNumber(), childProposal.getProposalNumber()));
        
        hierarchyChild.setProposalHashCode(childProposal.hierarchyChildHashCode());

        return true;
    }
    
    private void synchronizeChildBudget(Budget parentBudget, ProposalHierarchyChild hierarchyChild, Budget childBudget)
            throws ProposalHierarchyException {

//        for (BudgetPerson person : childBudget.getBudgetPersons()) {
//            System.err.println("---- " + person);
//        }
//        for (BudgetPeriod period : childBudget.getBudgetPeriods()) {
//            System.err.println("**** " + period);
//            for (BudgetLineItem item : period.getBudgetLineItems()) {
//                System.err.println("******** " + item);
//                for (BudgetPersonnelDetails details : item.getBudgetPersonnelDetailsList()) {
//                    System.err.println("************ " + details);
//                }
//            }
//        }

        
        try {
            BudgetPerson newPerson;
            Map<Integer, BudgetPerson> personMap = new HashMap<Integer, BudgetPerson>();
            for (BudgetPerson person : childBudget.getBudgetPersons()) {
                newPerson = (BudgetPerson) ObjectUtils.deepCopy(person);
                newPerson.setPersonSequenceNumber(parentBudget.getBudgetDocument().getHackedDocumentNextValue(
                        Constants.PERSON_SEQUENCE_NUMBER));
                newPerson.setBudget(parentBudget);
                newPerson.setBudgetId(parentBudget.getBudgetId());
                newPerson.setHierarchyProposalNumber(hierarchyChild.getProposalNumber());
                newPerson.setVersionNumber(null);
                parentBudget.addBudgetPerson(newPerson);
                hierarchyChild.getBudgetPersons().add(newPerson);
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
                for (BudgetLineItem childLineItem : childPeriod.getBudgetLineItems()) {
                    //ObjectUtils.materializeAllSubObjects(childLineItem);
                    parentLineItem = (BudgetLineItem) ObjectUtils.deepCopy(childLineItem);
                    parentLineItem.setBudgetId(budgetId);
                    parentLineItem.setBudgetPeriodId(budgetPeriodId);
                    parentLineItem.setBudgetPeriod(budgetPeriod);
                    parentLineItem.setVersionNumber(null);
                    lineItemNumber = parentBudget.getBudgetDocument().getHackedDocumentNextValue(Constants.BUDGET_LINEITEM_NUMBER);
                    //ObjectUtils.setObjectPropertyDeep(parentLineItem, "lineItemNumber", Integer.class, lineItemNumber);
                    parentLineItem.setLineItemNumber(lineItemNumber);
                    parentLineItem.setHierarchyProposalNumber(hierarchyChild.getProposalNumber());
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
                        //details.setSequenceNumber(parentBudget.getBudgetDocument().getHackedDocumentNextValue(Constants.BUDGET_PERSON_LINE_SEQUENCE_NUMBER));
                        details.setLineItemNumber(lineItemNumber);
                        details.setVersionNumber(null);
                    }
                    parentPeriod.getBudgetLineItems().add(parentLineItem);
                    hierarchyChild.getBudgetLineItems().add(parentLineItem);
                }
                //ObjectUtils.setObjectPropertyDeep(parentPeriod, "budgetPeriodId", Long.class, parentPeriod.getBudgetPeriodId());
                //ObjectUtils.setObjectPropertyDeep(parentPeriod, "budgetPeriod", Integer.class, parentPeriod.getBudgetPeriod());
                //ObjectUtils.setObjectPropertyDeep(parentPeriod, "budgetId", Long.class, parentBudget.getBudgetId());
            }
        }
        catch (Exception e) {
            LOG.error("Problem copying line items to parent", e);
            throw new ProposalHierarchyException("Problem copying line items to parent", e);
        }

        
//        for (BudgetPerson person : parentBudget.getBudgetPersons()) {
//            System.err.println("---- " + person);
//        }
//        for (BudgetPeriod period : parentBudget.getBudgetPeriods()) {
//            System.err.println("**** " + period);
//            for (BudgetLineItem item : period.getBudgetLineItems()) {
//                System.err.println("******** " + item);
//                for (BudgetPersonnelDetails details : item.getBudgetPersonnelDetailsList()) {
//                    System.err.println("************ " + details);
//                }
//            }
//        }


//        try { Thread.sleep(1000*30); } catch (Exception e) {}
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
        Budget hierarchyBudget = getHierarchyBudget(hierarchy);
        KualiForm oldForm = GlobalVariables.getKualiForm();
        GlobalVariables.setKualiForm(new BudgetForm());
        budgetCalculationService.calculateBudget(hierarchyBudget);
        GlobalVariables.setKualiForm(oldForm);
        businessObjectService.save(hierarchyBudget);

    }

    private DevelopmentProposal getHierarchy(String hierarchyProposalNumber) throws ProposalHierarchyException {
        DevelopmentProposal hierarchy = getDevelopmentProposal(hierarchyProposalNumber);
        if (hierarchy == null || !hierarchy.isParent())
            throw new ProposalHierarchyException("Proposal " + hierarchyProposalNumber + " is not a hierarchy");
        return hierarchy;
    }

    private ProposalHierarchyChild getHierarchyChild(String childProposalNumber) throws ProposalHierarchyException {
        Map<String, String> pk = new HashMap<String, String>();
        pk.put("proposalNumber", childProposalNumber);
        ProposalHierarchyChild child = (ProposalHierarchyChild) (businessObjectService.findByPrimaryKey(
                ProposalHierarchyChild.class, pk));
        if (child == null)
            throw new ProposalHierarchyException("Proposal " + childProposalNumber + " not found or not a child in a hierarchy");
        return child;
    }

    public DevelopmentProposal getDevelopmentProposal(String proposalNumber) {
        Map<String, String> pk = new HashMap<String, String>();
        pk.put("proposalNumber", proposalNumber);
        return (DevelopmentProposal) (businessObjectService.findByPrimaryKey(DevelopmentProposal.class, pk));
    }

    private boolean isSynchronized(String childProposalNumber) throws ProposalHierarchyException {
        DevelopmentProposal childProposal = getDevelopmentProposal(childProposalNumber);
        ProposalHierarchyChild hierarchyChild = getHierarchyChild(childProposalNumber);
        return childProposal.hierarchyChildHashCode() == hierarchyChild.getProposalHashCode();
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
    
    private Budget getHierarchyBudget(DevelopmentProposal hierarchyProposal) throws ProposalHierarchyException {
        String budgetDocumentNumber = hierarchyProposal.getProposalDocument().getBudgetDocumentVersions().get(0).getBudgetVersionOverview().getDocumentNumber();
        BudgetDocument budgetDocument = null;
        try {
            budgetDocument = (BudgetDocument) documentService.getByDocumentHeaderId(budgetDocumentNumber);
        }
        catch (WorkflowException e) {
            throw new ProposalHierarchyException(e);
        }
        return budgetDocument.getBudget();
    }
 
    private Budget getFinalOrLatestChildBudget(DevelopmentProposal childProposal) throws ProposalHierarchyException {
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
        return budgetDocument.getBudget();
    }
    
    private void initializeBudget (DevelopmentProposal hierarchyProposal, DevelopmentProposal childProposal) throws ProposalHierarchyException {
        Budget parentBudget = getHierarchyBudget(hierarchyProposal);
        Budget childBudget = getFinalOrLatestChildBudget(childProposal);
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
        businessObjectService.save(parentBudget);
    }
    
    public ProposalHierarchyErrorDto validateChildBudgetPeriods(DevelopmentProposal hierarchyProposal,
            DevelopmentProposal childProposal) throws ProposalHierarchyException {
        Budget parentBudget = getHierarchyBudget(hierarchyProposal);
        Budget childBudget = getFinalOrLatestChildBudget(childProposal);
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
    
    private void removeChildElements(DevelopmentProposal parentProposal, Budget parentBudget, ProposalHierarchyChild hierarchyChild) {
        /*
        for (PropScienceKeyword keyword : hierarchyChild.getPropScienceKeywords()) {
            parentProposal.getPropScienceKeywords().remove(keyword);
        }
        hierarchyChild.getPropScienceKeywords().clear();

        for (ProposalSpecialReview review : hierarchyChild.getPropSpecialReviews()) {
            parentProposal.getPropSpecialReviews().remove(review);
        }
        hierarchyChild.getPropSpecialReviews().clear();

        for (Narrative narrative : hierarchyChild.getNarratives()) {
            parentProposal.getNarratives().remove(narrative);
        }
        hierarchyChild.getNarratives().clear();

        for (ProposalPerson person : hierarchyChild.getProposalPersons()) {
            parentProposal.getProposalPersons().remove(person);
        }
        hierarchyChild.getProposalPersons().clear();
        */
        
        parentProposal.getPropScienceKeywords().removeAll(hierarchyChild.getPropScienceKeywords());
        hierarchyChild.getPropScienceKeywords().clear();
        parentProposal.getPropSpecialReviews().removeAll(hierarchyChild.getPropSpecialReviews());
        hierarchyChild.getPropSpecialReviews().clear();
        parentProposal.getNarratives().removeAll(hierarchyChild.getNarratives());
        hierarchyChild.getNarratives().clear();
        parentProposal.getProposalPersons().removeAll(hierarchyChild.getProposalPersons());
        hierarchyChild.getProposalPersons().clear();

        parentBudget.getBudgetPersons().removeAll(hierarchyChild.getBudgetPersons());
        hierarchyChild.getBudgetPersons().clear();
        List<BudgetPeriod> periods = parentBudget.getBudgetPeriods();
        BudgetPeriod period = null;
        for (int i=periods.size()-1; i>=0; i--) {
            period = periods.get(i);
            period.getBudgetLineItems().removeAll(hierarchyChild.getBudgetLineItems());
            //if (period.getBudgetLineItems().isEmpty()) {
            //    periods.remove(i);
            //}
        }
        hierarchyChild.getBudgetLineItems().clear();
    }
        
}
