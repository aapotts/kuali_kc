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
package org.kuali.kra.budget.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.kuali.core.service.BusinessObjectService;
import org.kuali.core.service.KualiConfigurationService;
import org.kuali.kra.budget.BudgetDecimal;
import org.kuali.kra.budget.bo.BudgetPerson;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.bo.BudgetPersonnelDetails;
import org.kuali.kra.budget.service.BudgetPersonService;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;

/**
 * This class implements methods specified by <code>{@link BudgetPersonService}</code> interface
 */
public class BudgetPersonServiceImpl implements BudgetPersonService {
    
    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(BudgetPersonServiceImpl.class);
    
    private KualiConfigurationService kualiConfigurationService;
    private BusinessObjectService businessObjectService;
    /**
     * @see org.kuali.kra.budget.service.BudgetPersonService#populateBudgetPersonData(org.kuali.kra.budget.document.BudgetDocument, org.kuali.kra.budget.bo.BudgetPerson)
     */
    public void populateBudgetPersonData(BudgetDocument budgetDocument, BudgetPerson budgetPerson) {
        
        budgetPerson.setProposalNumber(budgetDocument.getProposalNumber());
        budgetPerson.setBudgetVersionNumber(budgetDocument.getBudgetVersionNumber());
        budgetDocument.refreshReferenceObject("documentNextvalues");
        budgetPerson.setPersonSequenceNumber(budgetDocument.getDocumentNextValue(Constants.PERSON_SEQUENCE_NUMBER));
        
        if (budgetDocument.getProposal() != null) {
            budgetPerson.setEffectiveDate(budgetDocument.getProposal().getRequestedStartDateInitial());
        }
        
        budgetPerson.setCalculationBase(new BudgetDecimal(kualiConfigurationService.getParameterValue(
                Constants.PARAMETER_MODULE_BUDGET, Constants.PARAMETER_COMPONENT_DOCUMENT, Constants.BUDGET_PERSON_DEFAULT_CALCULATION_BASE)));
        
    }
    
    /**
     * @see org.kuali.kra.budget.service.BudgetPersonService#synchBudgetPersonsToProposal(org.kuali.kra.budget.document.BudgetDocument)
     */
    public void synchBudgetPersonsToProposal(BudgetDocument budgetDocument) {
        for (ProposalPerson proposalPerson: budgetDocument.getProposal().getProposalPersons()) {
            boolean present = false;
            for (BudgetPerson budgetPerson: budgetDocument.getBudgetPersons()) {
                if (proposalPerson.getPersonId() != null && proposalPerson.getPersonId().equals(budgetPerson.getPersonId())) {
                    present = true;
                    break;
                } else if (proposalPerson.getRolodexId() != null && proposalPerson.getRolodexId().equals(budgetPerson.getRolodexId())) {
                    present = true;
                    break;
                }
            }
            if (!present) {
                BudgetPerson newBudgetPerson = new BudgetPerson(proposalPerson);
                populateBudgetPersonData(budgetDocument, newBudgetPerson);
                budgetDocument.addBudgetPerson(newBudgetPerson);
            }
        }
    }

    public void setKualiConfigurationService(KualiConfigurationService kualiConfigurationService) {
        this.kualiConfigurationService = kualiConfigurationService;
    }

    @SuppressWarnings("unchecked") 
    public BudgetPerson findBudgetPerson(BudgetPersonnelDetails budgetPersonnelDetails) {
        Map queryMap = new HashMap();
        queryMap.put("proposalNumber", budgetPersonnelDetails.getProposalNumber());
        queryMap.put("budgetVersionNumber", budgetPersonnelDetails.getBudgetVersionNumber());
        queryMap.put("personSequenceNumber", budgetPersonnelDetails.getPersonSequenceNumber());
        return (BudgetPerson)businessObjectService.findByPrimaryKey(BudgetPerson.class, queryMap);
    }

    /**
     * Gets the businessObjectService attribute. 
     * @return Returns the businessObjectService.
     */
    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    /**
     * Sets the businessObjectService attribute value.
     * @param businessObjectService The businessObjectService to set.
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
    
}
