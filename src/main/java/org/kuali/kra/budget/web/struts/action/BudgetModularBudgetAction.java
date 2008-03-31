/*
 * Copyright 2007 The Kuali Foundation.
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
package org.kuali.kra.budget.web.struts.action;

import static org.kuali.RiceConstants.QUESTION_INST_ATTRIBUTE_NAME;
import static org.kuali.kra.infrastructure.Constants.MAPPING_BASIC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.core.util.ObjectUtils;
import org.kuali.kra.budget.bo.BudgetModular;
import org.kuali.kra.budget.bo.BudgetModularIdc;
import org.kuali.kra.budget.bo.BudgetPeriod;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.service.BudgetModularService;
import org.kuali.kra.budget.web.struts.form.BudgetForm;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.web.struts.action.StrutsConfirmation;

public class BudgetModularBudgetAction extends BudgetAction {
    private static final Log LOG = LogFactory.getLog(BudgetModularBudgetAction.class);
    
    private static final String CONFIRM_SYNC_BUDGET_MODULAR = "confirmSyncBudgetModular";
    
    public ActionForward updateView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BudgetForm budgetForm = (BudgetForm) form;
        BudgetDocument budgetDocument = budgetForm.getBudgetDocument();
        BudgetModularService budgetModularService = KraServiceLocator.getService(BudgetModularService.class);
        
        if (budgetForm.getModularSelectedPeriod().equals(0)) {
            budgetForm.setBudgetModularSummary(budgetModularService.generateModularSummary(budgetDocument));
            return mapping.findForward(Constants.MAPPING_BASIC);
        }
        
        BudgetPeriod budgetPeriod = budgetDocument.getBudgetPeriods().get(budgetForm.getModularSelectedPeriod() - 1);
        budgetModularService.generateModularPeriod(budgetPeriod);
        return mapping.findForward(Constants.MAPPING_BASIC);
   
    }
    
    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BudgetForm budgetForm = (BudgetForm) form;
        BudgetDocument budgetDocument = budgetForm.getBudgetDocument();
        BudgetModularIdc newBudgetModularIdc = budgetForm.getNewBudgetModularIdc();
        budgetDocument.refreshReferenceObject("documentNextvalues");
        newBudgetModularIdc.setRateNumber(budgetForm.getBudgetDocument().getDocumentNextValue("rateNumber"));
        newBudgetModularIdc.calculateFundsRequested();
        BudgetModular budgetModular = budgetForm.getBudgetDocument().getBudgetPeriods().get(budgetForm.getModularSelectedPeriod() - 1).getBudgetModular();
        budgetModular.addNewBudgetModularIdc(newBudgetModularIdc);
        generateModularPeriod(budgetForm);
        budgetForm.setNewBudgetModularIdc(new BudgetModularIdc());
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BudgetForm budgetForm = (BudgetForm) form;
        BudgetModular budgetModular = budgetForm.getBudgetDocument().getBudgetPeriods().get(budgetForm.getModularSelectedPeriod() - 1).getBudgetModular();
        budgetModular.getBudgetModularIdcs().remove(getLineToDelete(request));
        generateModularPeriod(budgetForm);
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward recalculate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BudgetForm budgetForm = (BudgetForm) form;
        if (!budgetForm.getModularSelectedPeriod().equals(0)) {
            generateModularPeriod(budgetForm);
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward sync(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return confirm(buildSyncBudgetModularConfirmationQuestion(mapping, form, request, response), CONFIRM_SYNC_BUDGET_MODULAR, "");
    }
    
    /**
     * 
     * This method is used to synch the modular budget
     * @param mapping The mapping associated with this action.
     * @param form The Budget form.
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the destination (always the original proposal web page that caused this action to be invoked)
     * @throws Exception
     */
    public ActionForward confirmSyncBudgetModular(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Object question = request.getParameter(QUESTION_INST_ATTRIBUTE_NAME);
        if (CONFIRM_SYNC_BUDGET_MODULAR.equals(question)) {
            BudgetModularService budgetModularService = KraServiceLocator.getService(BudgetModularService.class);
            BudgetForm budgetForm = (BudgetForm) form;
            budgetModularService.synchModularBudget(budgetForm.getBudgetDocument());
            budgetForm.setBudgetModularSummary(budgetModularService.generateModularSummary(budgetForm.getBudgetDocument()));
        }
        
        return mapping.findForward(MAPPING_BASIC);
    }
    
    private StrutsConfirmation buildSyncBudgetModularConfirmationQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return buildParameterizedConfirmationQuestion(mapping, form, request, response, CONFIRM_SYNC_BUDGET_MODULAR, KeyConstants.QUESTION_SYNC_BUDGET_MODULAR, "");
    }
    
    private void generateModularPeriod(BudgetForm budgetForm) {
        BudgetDocument budgetDocument = budgetForm.getBudgetDocument();
        BudgetModularService budgetModularService = KraServiceLocator.getService(BudgetModularService.class);
        BudgetPeriod budgetPeriod = budgetDocument.getBudgetPeriods().get(budgetForm.getModularSelectedPeriod() - 1);
        budgetModularService.generateModularPeriod(budgetPeriod);
        // Also update project totals
        budgetForm.setBudgetModularSummary(budgetModularService.generateModularSummary(budgetForm.getBudgetDocument()));
    }
    
}
