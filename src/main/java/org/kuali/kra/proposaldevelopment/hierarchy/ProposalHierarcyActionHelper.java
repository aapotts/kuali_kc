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
package org.kuali.kra.proposaldevelopment.hierarchy;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.kra.budget.versions.BudgetDocumentVersion;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.proposaldevelopment.bo.DevelopmentProposal;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.hierarchy.bo.HierarchyProposalSummary;
import org.kuali.kra.proposaldevelopment.hierarchy.service.ProposalHierarchyService;
import org.kuali.kra.proposaldevelopment.service.ProposalStatusService;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.MessageMap;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * This class...
 */
public class ProposalHierarcyActionHelper {
    private static final Log LOG = LogFactory.getLog(ProposalHierarcyActionHelper.class);

    public static final String FIELD_GENERIC = "newHierarchyProposal.x";
    public static final String FIELD_PARENT_NUMBER = "newHierarchyProposalNumber";
    public static final String FIELD_CHILD_NUMBER = "newHierarchyChildProposalNumber";

    private static final String MESSAGE_LINK_SUCCESS = "message.hierarchy.link.success";
    private static final String MESSAGE_CREATE_SUCCESS = "message.hierarchy.create.success";
    private static final String MESSAGE_SYNC_SUCCESS = "message.hierarchy.sync.success";
    private static final String MESSAGE_REMOVE_SUCCESS = "message.hierarchy.remove.success";

    private static final String WARNING_LINK_NO_FINAL_BUDGET = "warning.hierarchy.link.noFinalBudget";
    private static final String WARNING_LINK_DIFFERENT_SPONSOR = "warning.hierarchy.link.differentSponsor";
    
    private static final String ERROR_LINK_ALREADY_MEMBER = "error.hierarchy.link.alreadyHierarchyMember";
    private static final String ERROR_LINK_NOT_PARENT = "error.hierarchy.link.notParent";
    private static final String ERROR_LINK_NO_PRINCIPLE_INVESTIGATOR = "error.hierarchy.link.noPrincipleInvestigator";
    private static final String ERROR_LINK_NO_BUDGET_VERSION = "error.hierarchy.link.noBudgetVersion";
    private static final String ERROR_LINK_PARENT_BUDGET_COMPLETE = "error.hierarchy.link.parentBudgetComplete";
    private static final String ERROR_REMOVE_PARENT_BUDGET_COMPLETE = "error.hierarchy.remove.parentBudgetComplete";
    private static final String ERROR_UNEXPECTED = "error.hierarchy.unexpected";

    ProposalHierarchyService hierarchyService;
    
    public void syncAllHierarchy(DevelopmentProposal hierarchyProposal) {
        // TODO rules
        try {
            getProposalHierarchyService().synchronizeAllChildren(hierarchyProposal);
            GlobalVariables.getMessageList().add(MESSAGE_SYNC_SUCCESS);

        }
        catch (Exception e) {
            doUnexpectedError(e, FIELD_GENERIC, true);
        }
    }
    
    public void removeFromHierarchy(DevelopmentProposal childProposal) {
        if (validateChildForRemoval(childProposal)) {
            try {
                getProposalHierarchyService().removeFromHierarchy(childProposal);
                GlobalVariables.getMessageList().add(MESSAGE_REMOVE_SUCCESS);
    
            }
            catch (Exception e) {
                doUnexpectedError(e, FIELD_GENERIC, true);
            }
        }
    }
    
    public void syncToHierarchyParent(DevelopmentProposal childProposal) {
        // TODO rules
        try {
            getProposalHierarchyService().synchronizeChild(childProposal);
            GlobalVariables.getMessageList().add(MESSAGE_SYNC_SUCCESS);

        }
        catch (Exception e) {
            doUnexpectedError(e, FIELD_GENERIC, true);
        }
    }

    public void createHierarchy(DevelopmentProposal initialChildProposal) {
        LOG.info(String.format("createHierarchy called with Proposal $s", initialChildProposal.getProposalNumber()));
        if (validateChildCandidate(initialChildProposal)) {
            try {
                // FIXME: Saving and restoring message map because the document save that occurs in createHierarchy clears the message map
                MessageMap messageMap = GlobalVariables.getMessageMap();
                String parentProposalNumber = getProposalHierarchyService().createHierarchy(initialChildProposal);
                GlobalVariables.setMessageMap(messageMap);
                GlobalVariables.getMessageList().add(MESSAGE_CREATE_SUCCESS, parentProposalNumber);
            }
            catch (Exception e) {
                doUnexpectedError(e, FIELD_GENERIC, true);
            }
        }
        LOG.info(String.format("createHierarchy completed", initialChildProposal.getProposalNumber()));
    }
    
    public void linkToHierarchy(DevelopmentProposal hierarchyProposal, DevelopmentProposal newChildProposal, String hierarchyBudgetTypeCode) {
        if (validateParent(hierarchyProposal)) {
            boolean valid = true;
            valid &= validateChildCandidate(newChildProposal);
            if (valid && validateChildCandidateForHierarchy(hierarchyProposal, newChildProposal)) {
                try {
                    getProposalHierarchyService().linkToHierarchy(hierarchyProposal, newChildProposal, hierarchyBudgetTypeCode);
                    GlobalVariables.getMessageList().add(MESSAGE_LINK_SUCCESS, newChildProposal.getProposalNumber(), hierarchyProposal.getProposalNumber());
                }
                catch (Exception e) {
                    doUnexpectedError(e, FIELD_GENERIC, true);
                }
            }
        }
    }

    public List<HierarchyProposalSummary> getHierarchySummaries(String proposalNumber) {
        List<HierarchyProposalSummary> retval = null;
        try {
            retval = getProposalHierarchyService().getProposalSummaries(proposalNumber);
        }
        catch (Exception e) {
            doUnexpectedError(e, FIELD_GENERIC, false);
        }
        return retval;
    }

    public DevelopmentProposal getDevelopmentProposal(String proposalNumber) {
        return getProposalHierarchyService().getDevelopmentProposal(proposalNumber);
    }
    
    private ProposalHierarchyService getProposalHierarchyService() {
        if (hierarchyService == null) {
            hierarchyService = KraServiceLocator.getService(ProposalHierarchyService.class);
        }
        return hierarchyService;
    }
    
    private boolean validateParent(DevelopmentProposal proposal) {
        boolean valid = true;
        if (!proposal.isParent()) {
            GlobalVariables.getMessageMap().putError(FIELD_PARENT_NUMBER, ERROR_LINK_NOT_PARENT, new String[0]);
            valid = false;
        }
        else if (hasCompleteBudget(proposal)) {
            GlobalVariables.getMessageMap().putError(FIELD_PARENT_NUMBER, ERROR_LINK_PARENT_BUDGET_COMPLETE, new String[0]);
            valid = false;
        }
        return valid;        
    }
    
    private boolean validateChildCandidate(DevelopmentProposal proposal) {
        boolean valid = true;
        proposal.getProposalDocument().refreshReferenceObject("budgetDocumentVersions");
        if (proposal.isInHierarchy()) {
            GlobalVariables.getMessageMap().putError(FIELD_CHILD_NUMBER, ERROR_LINK_ALREADY_MEMBER, new String[0]);
            return false;
        }
        if (proposal.getProposalDocument().getBudgetDocumentVersions().isEmpty()) {
            GlobalVariables.getMessageMap().putError(FIELD_CHILD_NUMBER, ERROR_LINK_NO_BUDGET_VERSION, new String[0]);
            valid = false;
        }
        else {
            if (!hasFinalBudget(proposal)) {
                GlobalVariables.getMessageMap().putWarning(FIELD_CHILD_NUMBER, WARNING_LINK_NO_FINAL_BUDGET, new String[] {proposal.getProposalNumber()});
            }
        }
        boolean principleInvestigatorPresent = false;
        for (ProposalPerson person : proposal.getProposalPersons()) {
           if (StringUtils.equalsIgnoreCase(person.getProposalPersonRoleId(), "PI")) {
               principleInvestigatorPresent = true;
               break;
           }
        }
        if (!principleInvestigatorPresent) {
            GlobalVariables.getMessageMap().putError(FIELD_CHILD_NUMBER, ERROR_LINK_NO_PRINCIPLE_INVESTIGATOR, new String[0]);
            valid = false;
        }
        return valid;
    }

    private boolean validateChildCandidateForHierarchy(DevelopmentProposal hierarchy, DevelopmentProposal child) {
        boolean valid = true;
        if (!StringUtils.equalsIgnoreCase(hierarchy.getSponsorCode(), child.getSponsorCode())) {
            GlobalVariables.getMessageMap().putWarning(FIELD_CHILD_NUMBER, WARNING_LINK_DIFFERENT_SPONSOR, new String[0]);
        }
        try {
            ProposalHierarchyErrorDto budgetError = getProposalHierarchyService().validateChildBudgetPeriods(hierarchy, child);
            if (budgetError != null) {
                valid = false;
                GlobalVariables.getMessageMap().putError(FIELD_CHILD_NUMBER, budgetError.getErrorKey(), budgetError.getErrorParameters());
            }
        }
        catch (Exception e) {
            GlobalVariables.getMessageMap().putError(FIELD_GENERIC, ERROR_UNEXPECTED, e.getMessage());
            valid = false;            
        }
        return valid;
    }
    
    private boolean validateChildForRemoval(DevelopmentProposal child) {
        boolean valid = true;
        try {
            DevelopmentProposal hierarchy = getProposalHierarchyService().lookupParent(child);
            if (hasCompleteBudget(hierarchy)) {
                GlobalVariables.getMessageMap().putError(FIELD_GENERIC, ERROR_REMOVE_PARENT_BUDGET_COMPLETE, new String[0]);
                valid = false;
            }
        
        }
        catch (Exception e) {
            GlobalVariables.getMessageMap().putError(FIELD_GENERIC, ERROR_UNEXPECTED, e.getMessage());
            valid = false;
        }
        return valid;
    }
    
    private boolean hasFinalBudget(DevelopmentProposal proposal) {
        boolean retval = false;
        for (BudgetDocumentVersion version : proposal.getProposalDocument().getBudgetDocumentVersions()) {
            if (version.getBudgetVersionOverview().isFinalVersionFlag()) {
                retval = true;
                break;
            }
        }
        return retval;
    }

    private boolean hasCompleteBudget(DevelopmentProposal proposal) {
        boolean retval = false;
        for (BudgetDocumentVersion version : proposal.getProposalDocument().getBudgetDocumentVersions()) {
            if (version.getBudgetVersionOverview().isFinalVersionFlag()) {
                KraServiceLocator.getService(ProposalStatusService.class).loadBudgetStatus(proposal);
                if (proposal.isProposalComplete()) {
                    retval = true;
                }
                break;
            }
        }
        return retval;
    }
    
    private void doUnexpectedError (Exception e, String field, boolean rollback) {
        LOG.error(String.format("Unexpected error in Proposal Hierarchy handling: %s", e.toString()), e);
        if (rollback) {
            PlatformTransactionManager txMgr = KraServiceLocator.getService("transactionManager");
            txMgr.rollback(txMgr.getTransaction(null));
        }
        GlobalVariables.getMessageMap().putError(field, ERROR_UNEXPECTED, e.toString());
    }
}
