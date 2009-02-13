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
package org.kuali.kra.budget.web.struts.action;

import static org.kuali.RiceConstants.QUESTION_INST_ATTRIBUTE_NAME;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.core.util.GlobalVariables;
import org.kuali.core.web.struts.form.KualiForm;
import org.kuali.kra.budget.bo.BudgetProposalRate;
import org.kuali.kra.budget.bo.RateClassType;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.web.struts.form.BudgetForm;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.web.struts.action.StrutsConfirmation;

public class BudgetRatesAction extends BudgetAction {
    private static final Log LOG = LogFactory.getLog(BudgetRatesAction.class);

    private static final String CONFIRM_SYNC_RATES = "confirmSyncRates";
    private static final String CONFIRM_SYNC_ALL_RATES = "confirmSyncAllRates";
    private static final String CONFIRM_RESET_RATES = "confirmResetRates";
    private static final String CONFIRM_RESET_ALL_RATES = "confirmResetAllRates";

    @Override
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BudgetForm budgetForm = (BudgetForm) form;
        BudgetDocument budgetDocument = budgetForm.getDocument();
        // Temporary fix to resolve budget form NULL issue
        if(GlobalVariables.getKualiForm() == null) {
            GlobalVariables.setKualiForm((KualiForm)form);
        }
        budgetDocument.setRateSynced(false);
        for (BudgetProposalRate budgetProposalRate : budgetDocument.getBudgetProposalRates()) {
            //if (budgetProposalRate.getActivityTypeCode().equals(budgetDocument.getProposal().getActivityTypeCode()) ) {
//                if (budgetProposalRate.getRateClassCode().equals("4") && budgetProposalRate.getRateTypeCode().equals("2")) {
//                    budgetProposalRate.setVersionNumber(null);
//                }
            //}
        } 

        super.save(mapping, form, request, response);
        return mapping.findForward("rates_save");
    }
    
    /**
     * 
     */
    public ActionForward resetRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        return confirm(buildResetRatesConfirmationQuestion(mapping, form, request, response), CONFIRM_RESET_RATES, "");
    }
    
    /**
     * 
     */
    
    private StrutsConfirmation buildResetRatesConfirmationQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        return buildParameterizedConfirmationQuestion(mapping, form, request, response, CONFIRM_RESET_RATES, 
                KeyConstants.QUESTION_RESET_RATES, "");
    }
    
    /**
     * Action called to reset budget rates for each panel.
     * 
     * @param mapping 
     * @param form
     * @param request
     * @param response
     * @return ActionForward instance for forwarding to the tab.
     */
    public ActionForward confirmResetRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        Object question = request.getParameter(QUESTION_INST_ATTRIBUTE_NAME);
        if (CONFIRM_RESET_RATES.equals(question)) {
            BudgetForm budgetForm = (BudgetForm) form;
            BudgetDocument budgetDocument = budgetForm.getDocument();
            RateClassType rateClassT = budgetDocument.getRateClassTypes().get(getSelectedLine(request));
            String rateClassType = rateClassT.getRateClassType();
            budgetDocument.getBudgetRatesService().resetBudgetRatesForRateClassType(rateClassType, budgetDocument);
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * 
     */
    public ActionForward syncRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        return confirm(buildSyncRatesConfirmationQuestion(mapping, form, request, response), CONFIRM_SYNC_RATES, "");
    }
    
    /**
     * 
     */
    private StrutsConfirmation buildSyncRatesConfirmationQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        return buildParameterizedConfirmationQuestion(mapping, form, request, response, CONFIRM_SYNC_RATES, 
                KeyConstants.QUESTION_SYNC_RATES, "");
    }

    /**
     * Action called to sync budget rates for each panel.
     * 
     * @param mapping 
     * @param form
     * @param request
     * @param response
     * @return ActionForward instance for forwarding to the tab.
     */
    public ActionForward confirmSyncRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        Object question = request.getParameter(QUESTION_INST_ATTRIBUTE_NAME);
        if (CONFIRM_SYNC_RATES.equals(question)) {
            BudgetForm budgetForm = (BudgetForm) form;
            BudgetDocument budgetDocument = budgetForm.getDocument();
            RateClassType rateClassT = budgetDocument.getRateClassTypes().get(getSelectedLine(request));
            String rateClassType = rateClassT.getRateClassType();
            budgetDocument.getBudgetRatesService().syncBudgetRatesForRateClassType(rateClassType, budgetDocument);
            budgetDocument.setRateClassTypesReload(false);
            if (rateClassType.equals("O")) {
                budgetDocument.setRateSynced(true);
            }
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * 
     */
    public ActionForward syncAllRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        return confirm(buildSyncAllRatesConfirmationQuestion(mapping, form, request, response), CONFIRM_SYNC_ALL_RATES, "");
    }
    
    /**
     * 
     */
     private StrutsConfirmation buildSyncAllRatesConfirmationQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
             HttpServletResponse response) throws Exception {
        return buildParameterizedConfirmationQuestion(mapping, form, request, response, CONFIRM_SYNC_ALL_RATES, 
                KeyConstants.QUESTION_SYNC_ALL_RATES, "");
    }

    /**
     * Action called to sync all budget rates.
     * 
     * @param mapping 
     * @param form
     * @param request
     * @param response
     * @return ActionForward instance for forwarding to the tab.
     */
    public ActionForward confirmSyncAllRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        Object question = request.getParameter(QUESTION_INST_ATTRIBUTE_NAME);
        if (CONFIRM_SYNC_ALL_RATES.equals(question)) {
            BudgetForm budgetForm = (BudgetForm) form;
            BudgetDocument budgetDocument = budgetForm.getDocument();

            //Rates-Refresh Scenario-4
            budgetDocument.setRateClassTypesReload(true);
            budgetDocument.getBudgetRatesService().syncAllBudgetRates(budgetDocument);
            
            budgetDocument.setRateSynced(true);
            if (!budgetDocument.getActivityTypeCode().equals(budgetDocument.getProposal().getActivityTypeCode())) {
                budgetDocument.setActivityTypeCode(budgetDocument.getProposal().getActivityTypeCode());
            }
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * 
     */
    public ActionForward resetAllRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        return confirm(buildResetAllRatesConfirmationQuestion(mapping, form, request, response), CONFIRM_RESET_ALL_RATES, "");
    }
    
    /**
     * 
     */
    private StrutsConfirmation buildResetAllRatesConfirmationQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        return buildParameterizedConfirmationQuestion(mapping, form, request, response, 
                CONFIRM_RESET_ALL_RATES, KeyConstants.QUESTION_RESET_ALL_RATES, "");
    }
    
    /**
     * Action called to reset all budget rates.
     * 
     * @param mapping 
     * @param form
     * @param request
     * @param response
     * @return ActionForward instance for forwarding to the tab.
     */
    public ActionForward confirmResetAllRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        Object question = request.getParameter(QUESTION_INST_ATTRIBUTE_NAME);
        if (CONFIRM_RESET_ALL_RATES.equals(question)) {
            BudgetForm budgetForm = (BudgetForm) form;
            BudgetDocument budgetDocument = budgetForm.getDocument();
            budgetDocument.getBudgetRatesService().resetAllBudgetRates(budgetDocument);
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * Action called to filter on off campus.
     * 
     * @param mapping 
     * @param form
     * @param request
     * @param response
     * @return ActionForward instance for forwarding to the tab.
     */
    public ActionForward updateRatesView(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        BudgetForm budgetForm = (BudgetForm) form;
        BudgetDocument budgetDocument = budgetForm.getDocument();
        budgetDocument.getBudgetRatesService().viewLocation(budgetForm.getViewLocation(),budgetForm.getViewBudgetPeriod(), budgetDocument);
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

}

