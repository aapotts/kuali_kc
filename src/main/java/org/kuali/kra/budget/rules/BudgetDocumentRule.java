/*
 * Copyright 2006-2008 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.budget.rules;

import static org.kuali.core.util.GlobalVariables.getAuditErrorMap;
import static org.kuali.kra.infrastructure.Constants.AUDIT_ERRORS;

import java.util.ArrayList;
import java.util.List;

import org.kuali.core.document.Document;
import org.kuali.core.rule.DocumentAuditRule;
import org.kuali.core.util.AuditCluster;
import org.kuali.core.util.AuditError;
import org.kuali.core.util.ErrorMap;
import org.kuali.core.util.GlobalVariables;
import org.kuali.core.util.ObjectUtils;
import org.kuali.kra.budget.BudgetDecimal;
import org.kuali.kra.budget.bo.BudgetCostShare;
import org.kuali.kra.budget.bo.BudgetLineItem;
import org.kuali.kra.budget.bo.BudgetPeriod;
import org.kuali.kra.budget.bo.BudgetPerson;
import org.kuali.kra.budget.bo.BudgetPersonnelDetails;
import org.kuali.kra.budget.bo.BudgetProposalLaRate;
import org.kuali.kra.budget.bo.BudgetProposalRate;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.rule.AddBudgetCostShareRule;
import org.kuali.kra.budget.rule.AddBudgetPeriodRule;
import org.kuali.kra.budget.rule.AddBudgetProjectIncomeRule;
import org.kuali.kra.budget.rule.DeleteBudgetPeriodRule;
import org.kuali.kra.budget.rule.GenerateBudgetPeriodRule;
import org.kuali.kra.budget.rule.SaveBudgetPeriodRule;
import org.kuali.kra.budget.rule.SyncModularBudgetRule;
import org.kuali.kra.budget.rule.event.AddBudgetCostShareEvent;
import org.kuali.kra.budget.rule.event.AddBudgetPeriodEvent;
import org.kuali.kra.budget.rule.event.AddBudgetProjectIncomeEvent;
import org.kuali.kra.budget.rule.event.DeleteBudgetPeriodEvent;
import org.kuali.kra.budget.rule.event.GenerateBudgetPeriodEvent;
import org.kuali.kra.budget.rule.event.SaveBudgetPeriodEvent;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.rules.ResearchDocumentRuleBase;

public class BudgetDocumentRule extends ResearchDocumentRuleBase implements AddBudgetPeriodRule, AddBudgetCostShareRule, AddBudgetProjectIncomeRule, SaveBudgetPeriodRule, DeleteBudgetPeriodRule, GenerateBudgetPeriodRule, DocumentAuditRule, SyncModularBudgetRule {

    /** 
     * @see org.kuali.kra.budget.rule.AddBudgetCostShareRule#processAddBudgetCostShareBusinessRules(org.kuali.kra.budget.rule.event.AddBudgetCostShareEvent)
     */
    public boolean processAddBudgetCostShareBusinessRules(AddBudgetCostShareEvent addBudgetCostShareEvent) {
        return new BudgetCostShareRuleImpl().processAddBudgetCostShareBusinessRules(addBudgetCostShareEvent);
    }
    
    /**
     * @see org.kuali.kra.budget.rule.AddBudgetPeriodRule#processAddBudgetPeriodBusinessRules(org.kuali.kra.budget.document.BudgetDocument,org.kuali.kra.budget.bo.BudgetPeriod)
     */
    public boolean processAddBudgetPeriodBusinessRules(AddBudgetPeriodEvent addBudgetPeriodEvent) {
        return new BudgetPeriodRule().processAddBudgetPeriodBusinessRules(addBudgetPeriodEvent);    
    }

    /**
     * @see org.kuali.kra.budget.rule.AddBudgetProjectIncomeRule#processAddBudgetProjectIncomeBusinessRules(org.kuali.kra.budget.rule.event.AddBudgetProjectIncomeEvent)
     */
    public boolean processAddBudgetProjectIncomeBusinessRules(AddBudgetProjectIncomeEvent addBudgetIncomeEvent) {
        return new BudgetProjectIncomeRuleImpl().processAddBudgetProjectIncomeBusinessRules(addBudgetIncomeEvent);
    }
    
    public boolean processSaveBudgetPeriodBusinessRules(SaveBudgetPeriodEvent saveBudgetPeriodEvent) {
        return new BudgetPeriodRule().processSaveBudgetPeriodBusinessRules(saveBudgetPeriodEvent);    
    }
    
    public boolean processDeleteBudgetPeriodBusinessRules(DeleteBudgetPeriodEvent deleteBudgetPeriodEvent) {
        return new BudgetPeriodRule().processDeleteBudgetPeriodBusinessRules(deleteBudgetPeriodEvent);    
    }

    public boolean processGenerateBudgetPeriodBusinessRules(GenerateBudgetPeriodEvent generateBudgetPeriodEvent) {
        return new BudgetPeriodRule().processGenerateBudgetPeriodBusinessRules(generateBudgetPeriodEvent);    
    }
    
    @Override
    protected boolean processCustomSaveDocumentBusinessRules(Document document) {
        if (!(document instanceof BudgetDocument)) {
            return false;
        }

        boolean valid = true;
        
        BudgetDocument budgetDocument = (BudgetDocument) document;
        
        GlobalVariables.getErrorMap().addToErrorPath("document");
       
        getDictionaryValidationService().validateDocumentAndUpdatableReferencesRecursively(document, getMaxDictionaryValidationDepth(), true, true);
        
        GlobalVariables.getErrorMap().addToErrorPath("proposal");
        if (ObjectUtils.isNull(budgetDocument.getProposal())) {
            budgetDocument.refreshReferenceObject("proposal");
        }
        
        valid &= processBudgetVersionsBusinessRule(budgetDocument.getProposal().getBudgetVersionOverviews(), true);
        GlobalVariables.getErrorMap().removeFromErrorPath("proposal");
        
        valid &= processBudgetPersonnelBusinessRules(budgetDocument);
        
        valid &= processBudgetExpenseBusinessRules(budgetDocument);
        
        valid &= processBudgetPersonnelBudgetBusinessRules(budgetDocument);
        
        valid &= processBudgetRatesBusinessRule(budgetDocument);
        
        valid &= processBudgetProjectIncomeBusinessRule(budgetDocument);

        GlobalVariables.getErrorMap().removeFromErrorPath("document");
        
        
        return valid;
    }

    /**
    *
    * Validate budget project income. 
    * costshare percentage must be between 0 and 999.99
    * @param budgetDocument
    * @return
    */
    protected boolean processBudgetProjectIncomeBusinessRule(BudgetDocument budgetDocument) {
        boolean valid = true;
        ErrorMap errorMap = GlobalVariables.getErrorMap();
        int i = 0;
        for (BudgetCostShare budgetCostShare : budgetDocument.getBudgetCostShares()) {
            String errorPath = "budgetCostShare[" + i + "]";
            errorMap.addToErrorPath(errorPath);
            if(budgetCostShare.getSharePercentage()!=null && (budgetCostShare.getSharePercentage().isLessThan(new BudgetDecimal(0)) || 
               budgetCostShare.getSharePercentage().isGreaterThan(new BudgetDecimal(100)))) {
                errorMap.putError("sharePercentage", KeyConstants.ERROR_COST_SHARE_PERCENTAGE);
                valid = false;
            }
            errorMap.removeFromErrorPath(errorPath);
            i++;
        }
        
        return valid;
    }
    
    /**
    *
    * Validate budget rates. 
    * Applicable rates are mandatory
    * @param budgetDocument
    * @return
    */
    protected boolean processBudgetRatesBusinessRule(BudgetDocument budgetDocument) {
        boolean valid = true;
        final int APPLICABLE_RATE_LENGTH_EXCEEDED = 1;
        final int APPLICABLE_RATE_NEGATIVE = -1;

        ErrorMap errorMap = GlobalVariables.getErrorMap();
        int i = 0;
        for (BudgetProposalRate budgetProposalRate : budgetDocument.getBudgetProposalRates()) {
            String rateClassType = budgetProposalRate.getRateClass().getRateClassTypeT().getDescription();
            String errorPath = "budgetProposalRate[" + rateClassType + "][" + i + "]";
            errorMap.addToErrorPath(errorPath);
            /* look for applicable rate */
            if(budgetProposalRate.isApplicableRateNull()) {
                valid = false;
                errorMap.putError("applicableRate", KeyConstants.ERROR_REQUIRED_APPLICABLE_RATE);
            }else if(!BudgetDecimal.isNumeric(budgetProposalRate.getApplicableRate().toString())) {
                valid = false;
                errorMap.putError("applicableRate", KeyConstants.ERROR_APPLICABLE_RATE_NOT_NUMERIC);
            }else {
                switch(verifyApplicableRate(budgetProposalRate.getApplicableRate().toString())) {
                    case APPLICABLE_RATE_LENGTH_EXCEEDED :
                        valid = false;
                        errorMap.putError("applicableRate", KeyConstants.ERROR_APPLICABLE_RATE_LIMIT, Constants.APPLICABLE_RATE_LIMIT);
                        break;
                    case APPLICABLE_RATE_NEGATIVE :
                        valid = false;
                        errorMap.putError("applicableRate", KeyConstants.ERROR_APPLICABLE_RATE_NEGATIVE);
                        break;
                        
                }
            }
            errorMap.removeFromErrorPath(errorPath);
            i++;
        }

        i = 0;
        for (BudgetProposalLaRate budgetProposalLaRate : budgetDocument.getBudgetProposalLaRates()) {
            String rateClassType = "";
            if (ObjectUtils.isNotNull(budgetProposalLaRate.getRateClass()) && ObjectUtils.isNotNull(budgetProposalLaRate.getRateClass().getRateClassTypeT())) {
                rateClassType = budgetProposalLaRate.getRateClass().getRateClassTypeT().getDescription();
            }
            String errorPath = "budgetProposalRate[" + rateClassType + "][" + i + "]";
            errorMap.addToErrorPath(errorPath);
            /* look for applicable rate */
            if(budgetProposalLaRate.isApplicableRateNull()) {
                valid = false;
                errorMap.putError("applicableRate", KeyConstants.ERROR_REQUIRED_APPLICABLE_RATE);
            }else if(!BudgetDecimal.isNumeric(budgetProposalLaRate.getApplicableRate().toString())) {
                valid = false;
                errorMap.putError("applicableRate", KeyConstants.ERROR_APPLICABLE_RATE_NOT_NUMERIC);
            }else {
                switch(verifyApplicableRate(budgetProposalLaRate.getApplicableRate().toString())) {
                    case APPLICABLE_RATE_LENGTH_EXCEEDED :
                        valid = false;
                        errorMap.putError("applicableRate", KeyConstants.ERROR_APPLICABLE_RATE_LIMIT, Constants.APPLICABLE_RATE_LIMIT);
                        break;
                    case APPLICABLE_RATE_NEGATIVE :
                        valid = false;
                        errorMap.putError("applicableRate", KeyConstants.ERROR_APPLICABLE_RATE_NEGATIVE);
                        break;
                        
                }
            }
            errorMap.removeFromErrorPath(errorPath);
            i++;
        }

        return valid;
    }

    /**
     * This method checks for a valid applicable rate
     * 
     * @param applicableRate
     * @return
     */
    private int verifyApplicableRate(String applicableRate) {
        int decimalIndex = applicableRate.indexOf(Constants.APPLICABLE_RATE_DECIMAL_CHAR);
        int rateValue = 0;
        String precision = applicableRate.substring(0, decimalIndex);
        String scale = applicableRate.substring(decimalIndex+1, applicableRate.length());
        
        if(precision.length() > Constants.APPLICABLE_RATE_PRECISION || scale.length() > Constants.APPLICABLE_RATE_SCALE) {
            rateValue = 1;
        }else if(Integer.parseInt(precision) < 0) {
            rateValue = -1;
        }
        return rateValue;
    }
    /**
     * This method checks business rules related to Budget Personnel functionality
     * 
     * @param budgetDocument
     * @return
     */
    protected boolean processBudgetPersonnelBusinessRules(BudgetDocument budgetDocument) {
        boolean valid = true;
        
        ErrorMap errorMap = GlobalVariables.getErrorMap();
        
        List<BudgetPerson> budgetPersons = budgetDocument.getBudgetPersons();
        for (int i = 0; i < budgetPersons.size(); i++) {
            BudgetPerson budgetPerson = (BudgetPerson) budgetPersons.get(i);
            for (int j = i + 1; j < budgetPersons.size(); j++) {
                BudgetPerson budgetPersonCompare = (BudgetPerson) budgetPersons.get(j);
                if (budgetPerson.isDuplicatePerson(budgetPersonCompare)) {
                    errorMap.putError("budgetPersons[" + j + "].personName", KeyConstants.ERROR_DUPLICATE_BUDGET_PERSON, budgetPerson.getPersonName());
                    valid = false;
                }

            }
            
        }
        
        return valid;
    }
    
    /**
     * This method checks business rules related to Budget Expenses functionality
     * 
     * @param budgetDocument
     * @return
     */
    protected boolean processBudgetExpenseBusinessRules(BudgetDocument budgetDocument) {
        boolean valid = true;
        //TODO - put budget expense validation rules here.
        ErrorMap errorMap = GlobalVariables.getErrorMap();
        
        List<BudgetPeriod> budgetPeriods = budgetDocument.getBudgetPeriods();
        List<BudgetLineItem> budgetLineItems = new ArrayList<BudgetLineItem>();
        int i=0;
        int j=0;
        for(BudgetPeriod budgetPeriod: budgetPeriods){
            j=0;
            budgetLineItems = budgetPeriod.getBudgetLineItems();
            for(BudgetLineItem budgetLineItem: budgetLineItems){
                if(budgetLineItem!=null && budgetLineItem.getStartDate()!=null && budgetLineItem.getStartDate().before(budgetPeriod.getStartDate())){
                    errorMap.putError("budgetCategoryTypes[" + budgetLineItem.getBudgetCategory().getBudgetCategoryTypeCode() + "].budgetPeriods[" + i +"].budgetLineItems[" + j + "].startDate",KeyConstants.ERROR_LINEITEM_STARTDATE_BEFORE_PERIOD_STARTDATE);
                    valid = false;
                }
                if(budgetLineItem!=null && budgetLineItem.getEndDate()!=null && budgetLineItem.getEndDate().after(budgetPeriod.getEndDate())){
                    errorMap.putError("budgetCategoryTypes[" + budgetLineItem.getBudgetCategory().getBudgetCategoryTypeCode() + "].budgetPeriods[" + i +"].budgetLineItems[" + j + "].endDate",KeyConstants.ERROR_LINEITEM_ENDDATE_AFTER_PERIOD_ENDDATE);
                    valid = false;
                }
//                if(budgetLineItem.getEndDate().compareTo(budgetLineItem.getStartDate()) <=0 ) {                        
//                        errorMap.putError("budgetPeriod["+i+"].budgetLineItem["+j+"].endDate", KeyConstants.ERROR_LINE_ITEM_DATES);
//                        return false;
//                }
         
                j++;
            }
            i++;
        }
        return valid;
    }
    
    /**
     * This method checks business rules related to Budget Personnel Budget functionality
     * 
     * @param budgetDocument
     * @return
     */
    protected boolean processBudgetPersonnelBudgetBusinessRules(BudgetDocument budgetDocument) {
        boolean valid = true;

        ErrorMap errorMap = GlobalVariables.getErrorMap();
        
        List<BudgetPeriod> budgetPeriods = budgetDocument.getBudgetPeriods();
        List<BudgetLineItem> budgetLineItems = new ArrayList<BudgetLineItem>();
        int i=0;
        int j=0;
        int k=0;
        for(BudgetPeriod budgetPeriod: budgetPeriods){
            j=0;
            budgetLineItems = budgetPeriod.getBudgetLineItems();
            k=0;
            for(BudgetLineItem budgetLineItem: budgetLineItems){
                for(BudgetPersonnelDetails budgetPersonnelDetails: budgetLineItem.getBudgetPersonnelDetailsList()){
                    if(budgetPersonnelDetails!=null && budgetPersonnelDetails.getStartDate()!=null && budgetPersonnelDetails.getStartDate().before(budgetLineItem.getStartDate())){
                        errorMap.putError("budgetPeriod[" + i +"].budgetLineItems[" + j + "].budgetPersonnelDetailsList[" + k + "].startDate",KeyConstants.ERROR_PERSONNELBUDGETLINEITEM_STARTDATE_BEFORE_LINEITEM_STARTDATE);
                        valid = false;
                    }
                    if(budgetPersonnelDetails!=null && budgetPersonnelDetails.getEndDate()!=null && budgetPersonnelDetails.getEndDate().after(budgetLineItem.getEndDate())){
                        errorMap.putError("budgetPeriod[" + i +"].budgetLineItems[" + j + "].budgetPersonnelDetailsList[" + k + "].endDate",KeyConstants.ERROR_PERSONNELBUDGETLINEITEM_ENDDATE_AFTER_LINEITEM_ENDDATE);
                        valid = false;
                    }                    
                    if(budgetPersonnelDetails.getPercentEffort().isLessThan(new BudgetDecimal(0)) 
                            || budgetPersonnelDetails.getPercentEffort().isGreaterThan(new BudgetDecimal(100))){
                        errorMap.putError("budgetPeriod[" + i +"].budgetLineItems[" + j + "].budgetPersonnelDetailsList[" + k + "].percentEffort",KeyConstants.ERROR_PERCENT_EFFORT_INVALID);
                    }
                    if(budgetPersonnelDetails.getPercentCharged().isLessThan(new BudgetDecimal(0)) 
                            || budgetPersonnelDetails.getPercentCharged().isGreaterThan(new BudgetDecimal(100))){
                        errorMap.putError("budgetPeriod[" + i +"].budgetLineItems[" + j + "].budgetPersonnelDetailsList[" + k + "].percentCharged",KeyConstants.ERROR_PERCENT_CHARGED_INVALID);
                    }
                    if(budgetPersonnelDetails.getPercentCharged().isGreaterThan(budgetPersonnelDetails.getPercentEffort())){
                        errorMap.putError("budgetPeriod[" + i +"].budgetLineItems[" + j + "].budgetPersonnelDetailsList[" + k + "].percentCharged",KeyConstants.ERROR_PERCENT_EFFORT_LESS_THAN_PERCENT_CHARGED);
                    }
                    k++;
                }
                j++;
            }
            i++;
        }
        return valid;
    }
    
    /**
     * @see org.kuali.core.rule.DocumentAuditRule#processRunAuditBusinessRules(org.kuali.core.document.Document)
     */
    public boolean processRunAuditBusinessRules(Document document) {
        boolean retval = true;
        
        retval &= super.processRunAuditBusinessRules(document);
        // TODO : add this one for testing jira 780 - remove this when audit rules are complete
        // comment out these lines before committed to rel-1-0
//        if (((BudgetDocument)document).getBudgetPersons() == null || ((BudgetDocument)document).getBudgetPersons().size() < 1) {
//            getAuditErrors().add(new AuditError("document.budgetPerson*", KeyConstants.ERROR_NO_BUDGET_PERSON , "budgetPersonnel.BudgetPersonnel" ));
//            retval = false;
//        }
        retval &= new BudgetPeriodAuditRule().processRunAuditBusinessRules(document);
        
        retval &= new BudgetExpensesAuditRule().processRunAuditBusinessRules(document);
        
        retval &= new BudgetPersonnelAuditRule().processRunPersonnelAuditBusinessRules(document);

        return retval;
    }
    
    public boolean processSyncModularBusinessRules(Document document) {
        if (!(document instanceof BudgetDocument)) {
            return false;
        }
        
        boolean valid = true;
        
        BudgetDocument budgetDocument = (BudgetDocument) document;
        
        GlobalVariables.getErrorMap().addToErrorPath("document");
        
        List budgetPeriods = budgetDocument.getBudgetPeriods();
        if (ObjectUtils.isNotNull(budgetPeriods) || budgetPeriods.size() >= 1) {
            BudgetPeriod period1 = (BudgetPeriod) budgetPeriods.get(0);
            if (ObjectUtils.isNull(period1.getBudgetLineItems()) || period1.getBudgetLineItems().isEmpty()) {
                valid = false;
            }
        } else {
            valid = false;
        }
        
        if (!valid) {
            GlobalVariables.getErrorMap().putError("modularBudget", KeyConstants.ERROR_NO_DETAILED_BUDGET);
        }
        
        GlobalVariables.getErrorMap().removeFromErrorPath("document");
        
        return valid;
    }

    /**
     * This method should only be called if an audit error is intending to be added because it will actually add a <code>{@link List<AuditError>}</code>
     * to the auditErrorMap.
     * 
     * @return List of AuditError instances
     */
    private List<AuditError> getAuditErrors() {
        List<AuditError> auditErrors = auditErrors = new ArrayList<AuditError>();
        
        if (!getAuditErrorMap().containsKey("budgetPersonnelAuditErrors")) {
            getAuditErrorMap().put("budgetPersonnelAuditErrors", new AuditCluster("Budget Personnel Information", auditErrors, AUDIT_ERRORS));
        }
        else {
            auditErrors = ((AuditCluster) getAuditErrorMap().get("budgetPersonnelAuditErrors")).getAuditErrorList();
        }
        
        return auditErrors;
    }

}
