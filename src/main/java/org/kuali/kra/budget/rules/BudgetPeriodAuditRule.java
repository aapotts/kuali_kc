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
import org.kuali.core.util.GlobalVariables;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.kuali.core.document.Document;
import org.kuali.core.rule.DocumentAuditRule;
import org.kuali.core.util.AuditCluster;
import org.kuali.core.util.AuditError;
import org.kuali.kra.budget.bo.BudgetPeriod;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.rules.ResearchDocumentRuleBase;

public class BudgetPeriodAuditRule extends ResearchDocumentRuleBase implements DocumentAuditRule {
    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(BudgetPeriodAuditRule.class);
    public static final String BUDGET_PERIOD_DATE_AUDIT_ERROR_KEY = "budgetPeriodProjectDateAuditErrors";

    /**
     * 
     * This method is to validate budget period start/end date against project start/end date if
     * project start/end date have been adjusted.
     */
    public boolean processRunAuditBusinessRules(Document document) {
        BudgetDocument budgetDocument = (BudgetDocument) document;
        boolean retval = true;
        Date projectStartDate = budgetDocument.getProposal().getRequestedStartDateInitial();
        Date projectEndDate = budgetDocument.getProposal().getRequestedEndDateInitial();
        int i = 0;
        for (BudgetPeriod budgetPeriod : budgetDocument.getBudgetPeriods()) {
            if (budgetPeriod.getStartDate().before(projectStartDate)) {
                retval = false;
                getAuditErrors().add(new AuditError("document.budgetPeriods[" + i + "].startDate", KeyConstants.AUDIT_ERROR_BUDGETPERIOD_START_BEFORE_PROJECT_START_DATE, Constants.BUDGET_PERIOD_PAGE + "." + Constants.BUDGET_PERIOD_PANEL_ANCHOR, new String[] {Integer.toString(i+1)}));
            }
            if (budgetPeriod.getEndDate().after(projectEndDate)) {
                retval = false;
                getAuditErrors().add(new AuditError("document.budgetPeriods[" + i + "].endDate", KeyConstants.AUDIT_ERROR_BUDGETPERIOD_END_AFTER_PROJECT_END_DATE, Constants.BUDGET_PERIOD_PAGE + "." + Constants.BUDGET_PERIOD_PANEL_ANCHOR, new String[] {Integer.toString(i+1)}));              
            }
            List<AuditError> auditErrors = new ArrayList<AuditError>();
            if (i == 0 && budgetPeriod.getStartDate().after(projectStartDate)) {
                getAuditWarnings().add(new AuditError("document.budgetPeriods[" + i + "].startDate", KeyConstants.AUDIT_WARNING_BUDGETPERIOD_START_AFTER_PROJECT_START_DATE, Constants.BUDGET_PERIOD_PAGE + "." + Constants.BUDGET_PERIOD_PANEL_ANCHOR, new String[] {Integer.toString(i+1)}));
            }
            if (i == budgetDocument.getBudgetPeriods().size() - 1 && budgetPeriod.getEndDate().before(projectEndDate)) {
                getAuditWarnings().add(new AuditError("document.budgetPeriods[" + i + "].endDate", KeyConstants.AUDIT_WARNING_BUDGETPERIOD_END_BEFORE_PROJECT_END_DATE, Constants.BUDGET_PERIOD_PAGE + "." + Constants.BUDGET_PERIOD_PANEL_ANCHOR, new String[] {Integer.toString(i+1)}));
            }
            
            i++;
        }
              
        return retval;
    }
    
    /**
     * This method is a convenience method for obtaining audit errors.
     * @return List of AuditError instances
     */    
    private List<AuditError> getAuditErrors() {
        return getAuditProblems(Constants.AUDIT_ERRORS);
    }
    
    /**
     * This method is a convenience method for obtaining audit warnings.
     * @return List of AuditError instances
     */
    private List<AuditError> getAuditWarnings() {
        return getAuditProblems(Constants.AUDIT_WARNINGS);
    }
    
    /**
     * This method should only be called if an audit error is intending to be added because it will actually add a <code>{@link List<AuditError>}</code>
     * to the auditErrorMap.
     *  TODO : should this method move up to parent class
     * @return List of AuditError instances
     */
    private List<AuditError> getAuditProblems(String problemType) {
        List<AuditError> auditErrors = auditErrors = new ArrayList<AuditError>();
        
        if (!getAuditErrorMap().containsKey(BUDGET_PERIOD_DATE_AUDIT_ERROR_KEY)) {
            getAuditErrorMap().put(BUDGET_PERIOD_DATE_AUDIT_ERROR_KEY, new AuditCluster(Constants.BUDGET_PERIOD_PANEL_NAME, auditErrors, problemType));
        }
        else {
            auditErrors = ((AuditCluster) getAuditErrorMap().get(BUDGET_PERIOD_DATE_AUDIT_ERROR_KEY)).getAuditErrorList();
        }
        
        return auditErrors;
    }

}


