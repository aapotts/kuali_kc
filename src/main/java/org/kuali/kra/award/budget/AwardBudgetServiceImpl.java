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
package org.kuali.kra.award.budget;


import org.kuali.kra.award.budget.document.AwardBudgetDocument;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.budget.core.Budget;
import org.kuali.kra.budget.core.BudgetParent;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.document.BudgetParentDocument;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.proposaldevelopment.rule.event.AddBudgetVersionEvent;
import org.kuali.kra.proposaldevelopment.rules.BudgetVersionRule;
import org.kuali.rice.kew.exception.WorkflowException;
import org.kuali.rice.kns.bo.DocumentHeader;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.DocumentService;
import org.kuali.rice.kns.service.ParameterService;
import org.kuali.rice.kns.workflow.service.KualiWorkflowDocument;

/**
 * This class is to process all basic services required for AwardBudget
 */
public class AwardBudgetServiceImpl implements AwardBudgetService {

    private ParameterService parameterService;
    private BusinessObjectService businessObjectService;
    private DocumentService documentService;


    /**
     * @see org.kuali.kra.award.budget.AwardBudgetService#post(org.kuali.kra.award.budget.document.AwardBudgetDocument)
     */
    public void post(AwardBudgetDocument awardBudgetDocument) {
        processStatusChange(awardBudgetDocument, KeyConstants.AWARD_BUDGET_STATUS_POSTED);
        saveDocument(awardBudgetDocument);
    }

    /**
     * This method...
     * @param awardBudgetDocument
     */
    private void saveDocument(AwardBudgetDocument awardBudgetDocument) {
        try {
            getDocumentService().saveDocument(awardBudgetDocument);
        }catch (WorkflowException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see org.kuali.kra.award.budget.AwardBudgetService#processApproval(org.kuali.kra.award.budget.document.AwardBudgetDocument)
     */
    public void processApproval(AwardBudgetDocument awardBudgetDocument) {
        KualiWorkflowDocument workFlowDocument = getWorkflowDocument(awardBudgetDocument);
        if(workFlowDocument.stateIsFinal()){
            processStatusChange(awardBudgetDocument, KeyConstants.AWARD_BUDGET_STATUS_TO_BE_POSTED);
        }
        saveDocument(awardBudgetDocument);
    }

    /**
     * @see org.kuali.kra.award.budget.AwardBudgetService#processDisapproval(org.kuali.kra.award.budget.document.AwardBudgetDocument)
     */
    public void processDisapproval(AwardBudgetDocument awardBudgetDocument) {
        processStatusChange(awardBudgetDocument, KeyConstants.AWARD_BUDGET_STATUS_REJECTED);
    }

    /**
     * @see org.kuali.kra.award.budget.AwardBudgetService#processSubmision(org.kuali.kra.award.budget.document.AwardBudgetDocument)
     */
    public void processSubmision(AwardBudgetDocument awardBudgetDocument) {
        processStatusChange(awardBudgetDocument, KeyConstants.AWARD_BUDGET_STATUS_SUBMITTED);
    }
    
    private void processStatusChange(AwardBudgetDocument awardBudgetDocument,String routingStatus){
        KualiWorkflowDocument workflowDocument = getWorkflowDocument(awardBudgetDocument);
        String submittedStatusCode = getParameterValue(routingStatus);
        String submittedStatus = findStatusDescription(submittedStatusCode);
        awardBudgetDocument.getAwardBudget().setAwardBudgetStatusCode(submittedStatusCode);
        workflowDocument.getRouteHeader().setAppDocStatus(submittedStatus);
    }

    private String getParameterValue(String awardBudgetParameter) {
        return  getParameterService().getParameterValue(AwardBudgetDocument.class, awardBudgetParameter);
    }


    private String findStatusDescription(String statusCode) {
        AwardBudgetStatus budgetStatus = getBusinessObjectService().findBySinglePrimaryKey(AwardBudgetStatus.class, statusCode);
        return budgetStatus.getDescription();
    }

    /**
     * @see org.kuali.kra.award.budget.AwardBudgetService#rebudget(org.kuali.kra.award.budget.document.AwardBudgetDocument)
     */
    public void rebudget(AwardBudgetDocument awardBudgetDocument) {
        // TODO Auto-generated method stub

    }
    /**
     * Get the corresponding workflow document.  
     * @param doc the document
     * @return the workflow document or null if there is none
     */
    protected KualiWorkflowDocument getWorkflowDocument(Document doc) {
        KualiWorkflowDocument workflowDocument = null;
        if (doc != null) {
            DocumentHeader header = doc.getDocumentHeader();
            if (header != null) {
                try {
                    workflowDocument = header.getWorkflowDocument();
                } 
                catch (RuntimeException ex) {
                    // do nothing; there is no workflow document
                }
            }
        }
        return workflowDocument;
    }

    /**
     * Gets the parameterService attribute. 
     * @return Returns the parameterService.
     */
    public ParameterService getParameterService() {
        return parameterService;
    }

    /**
     * Sets the parameterService attribute value.
     * @param parameterService The parameterService to set.
     */
    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
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

    /**
     * Gets the documentService attribute. 
     * @return Returns the documentService.
     */
    public DocumentService getDocumentService() {
        return documentService;
    }

    /**
     * Sets the documentService attribute value.
     * @param documentService The documentService to set.
     */
    public void setDocumentService(DocumentService documentservice) {
        this.documentService = documentservice;
    }


    /**
     * 
     * @see org.kuali.kra.budget.core.BudgetCommonService#getNewBudgetVersion(org.kuali.kra.budget.document.BudgetParentDocument, java.lang.String)
     */
    public BudgetDocument<Award> getNewBudgetVersion(BudgetParentDocument<Award> parentDocument, String documentDescription)
            throws WorkflowException {
        BudgetDocument<Award> budgetDocument;
        Integer budgetVersionNumber = parentDocument.getNextBudgetVersionNumber();
        budgetDocument = (BudgetDocument) documentService.getNewDocument(AwardBudgetDocument.class);
        
        budgetDocument.setParentDocument(parentDocument);
        budgetDocument.setParentDocumentKey(parentDocument.getDocumentNumber());
        budgetDocument.setParentDocumentTypeCode(parentDocument.getDocumentTypeCode());
        budgetDocument.getDocumentHeader().setDocumentDescription(documentDescription);
        
        Budget budget = budgetDocument.getBudget();
        budget.setBudgetVersionNumber(budgetVersionNumber);
        budget.setBudgetDocument(budgetDocument);
        
        BudgetParent budgetParent = parentDocument.getBudgetParent();
        budget.setStartDate(budgetParent.getRequestedStartDateInitial());
        budget.setEndDate(budgetParent.getRequestedEndDateInitial());
        budget.setOhRateTypeCode(this.parameterService.getParameterValue(BudgetDocument.class, Constants.BUDGET_DEFAULT_OVERHEAD_RATE_TYPE_CODE));
        budget.setOhRateClassCode(this.parameterService.getParameterValue(BudgetDocument.class, Constants.BUDGET_DEFAULT_OVERHEAD_RATE_CODE));
        budget.setUrRateClassCode(this.parameterService.getParameterValue(BudgetDocument.class, Constants.BUDGET_DEFAULT_UNDERRECOVERY_RATE_CODE));
        budget.setModularBudgetFlag(this.parameterService.getIndicatorParameter(BudgetDocument.class, Constants.BUDGET_DEFAULT_MODULAR_FLAG));
        budget.setBudgetStatus(this.parameterService.getParameterValue(BudgetDocument.class, budgetParent.getDefaultBudgetStatusParameter()));
        boolean success = new BudgetVersionRule().processAddBudgetVersion(new AddBudgetVersionEvent("document.startDate",budgetDocument.getParentDocument(),budget));
        if(!success)
            return null;

        //Rates-Refresh Scenario-1
        budget.setRateClassTypesReloaded(true);
        
        saveBudgetDocument(budgetDocument);
        budgetDocument = (BudgetDocument) documentService.getByDocumentHeaderId(budgetDocument.getDocumentNumber());
        parentDocument.refreshReferenceObject("budgetDocumentVersions");
        return budgetDocument;
    }
    /**
     * This method...
     * @param budgetDocument
     * @param isProposalBudget
     * @param budget
     * @param budgetParent
     * @throws WorkflowException
     */
    private void saveBudgetDocument(BudgetDocument<Award> budgetDocument) throws WorkflowException {
        Budget budget = budgetDocument.getBudget();
        BudgetParentDocument<Award> parentDocument = budgetDocument.getParentDocument(); 
        BudgetParent budgetParent = parentDocument.getBudgetParent();
        AwardBudgetExt budgetExt = (AwardBudgetExt)budget;
        budgetExt.setAwardBudgetStatusCode(this.parameterService.getParameterValue(AwardBudgetDocument.class, KeyConstants.AWARD_BUDGET_STATUS_IN_PROGRESS));
        budgetExt.setAwardBudgetTypeCode(this.parameterService.getParameterValue(AwardBudgetDocument.class, KeyConstants.AWARD_BUDGET_TYPE_NEW));
        documentService.saveDocument(budgetDocument);
    }

    public BudgetDocument<Award> copyBudgetVersion(BudgetDocument<Award> budgetDocument) throws WorkflowException {
        return null;
    }

}
