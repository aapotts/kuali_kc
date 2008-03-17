/*
 * Copyright 2008 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
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
package org.kuali.kra.proposaldevelopment.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.core.service.BusinessObjectService;
import org.kuali.core.util.ObjectUtils;
import org.kuali.kra.budget.bo.BudgetVersionOverview;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.proposaldevelopment.bo.ProposalBudgetStatus;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.service.ProposalStatusService;

public class ProposalStatusServiceImpl implements ProposalStatusService {
    
    private BusinessObjectService businessObjectService;
    
    public void saveBudgetFinalVersionStatus(ProposalDevelopmentDocument pdDocument) {
        ProposalBudgetStatus proposalStatus = getProposalStatus(pdDocument.getProposalNumber());
        
        if (proposalStatus == null) {
            if (pdDocument.getProposalNumber() != null) {
                proposalStatus = new ProposalBudgetStatus();
                proposalStatus.setProposalNumber(pdDocument.getProposalNumber());
                proposalStatus.setBudgetStatusCode(pdDocument.getBudgetStatus());
                businessObjectService.save(proposalStatus);
            }
        } else if (proposalStatus.getBudgetStatusCode() == null || 
                !proposalStatus.getBudgetStatusCode().equals(pdDocument.getBudgetStatus())) {
            proposalStatus.setBudgetStatusCode(pdDocument.getBudgetStatus());
            businessObjectService.save(proposalStatus);
        } // else there is no change - do nothing.
    }
    
    public void saveBudgetFinalVersionStatus(BudgetDocument budgetDocument) {
        ProposalBudgetStatus proposalStatus = getProposalStatus(budgetDocument.getProposalNumber());
        
        if (proposalStatus == null) {
            if (budgetDocument.getProposalNumber() != null && budgetDocument.getProposal() != null) {
                proposalStatus = new ProposalBudgetStatus();
                proposalStatus.setProposalNumber(budgetDocument.getProposalNumber());
                proposalStatus.setBudgetStatusCode(budgetDocument.getProposal().getBudgetStatus());
                businessObjectService.save(proposalStatus);
            }
        } else if (!ObjectUtils.isNull(budgetDocument.getProposal())) {
            if (proposalStatus.getBudgetStatusCode() == null && budgetDocument.getProposal().getBudgetStatus() != null) {
                proposalStatus.setBudgetStatusCode(budgetDocument.getProposal().getBudgetStatus());
                businessObjectService.save(proposalStatus);
            } else if (proposalStatus.getBudgetStatusCode() != null 
                    && !proposalStatus.getBudgetStatusCode().equals(budgetDocument.getProposal().getBudgetStatus())) {
                proposalStatus.setBudgetStatusCode(budgetDocument.getProposal().getBudgetStatus());
                businessObjectService.save(proposalStatus);
            }
        } // else no change or brand-new document; do nothing
        
        // Also save other budget versions. Can't map this in ORM because we don't want to save this version.
        if (!ObjectUtils.isNull(budgetDocument.getProposal())) {
            List<BudgetVersionOverview> budgetVersionOverviews = budgetDocument.getProposal().getBudgetVersionOverviews();
            for (BudgetVersionOverview version: budgetVersionOverviews) {
                if (!version.getBudgetVersionNumber().equals(budgetDocument.getBudgetVersionNumber())) {
                    BudgetVersionOverview dbVersion = getBudgetVersion(version);
                    if (dbVersion == null || dbVersion.isFinalVersionFlag() != version.isFinalVersionFlag()) {
                        businessObjectService.save(version);
                    }
                }
            }
        }
    }
    
    public void loadBudgetStatus(ProposalDevelopmentDocument pdDocument) {
        ProposalBudgetStatus proposalStatus = getProposalStatus(pdDocument.getProposalNumber());
        if (proposalStatus != null) {
            pdDocument.setBudgetStatus(proposalStatus.getBudgetStatusCode());
        }
    }
    
    /**
     * This method returns the ProposalStatus that corresponds to the given proposal.
     * @param proposalNumber
     * @return ProposalStatus
     */
    private ProposalBudgetStatus getProposalStatus(String proposalNumber) {
        BusinessObjectService boService = KraServiceLocator.getService(BusinessObjectService.class);
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(Constants.PROPOSAL_NUMBER, proposalNumber);
        ProposalBudgetStatus proposalStatus = (ProposalBudgetStatus) boService.findByPrimaryKey(ProposalBudgetStatus.class, keyMap);
        return proposalStatus;
    }
    
    private BudgetVersionOverview getBudgetVersion(BudgetVersionOverview version) {
        BusinessObjectService boService = KraServiceLocator.getService(BusinessObjectService.class);
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(Constants.PROPOSAL_NUMBER, version.getProposalNumber());
        keyMap.put(Constants.BUDGET_VERSION_NUMBER, version.getBudgetVersionNumber());
        BudgetVersionOverview dbVersion = (BudgetVersionOverview) boService.findByPrimaryKey(BudgetVersionOverview.class, keyMap);
        return dbVersion;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
    
}
