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

import static org.kuali.kra.infrastructure.Constants.CO_INVESTIGATOR_ROLE;
import static org.kuali.kra.infrastructure.Constants.PRINCIPAL_INVESTIGATOR_ROLE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.core.bo.user.UniversalUser;
import org.kuali.core.rule.event.DocumentAuditEvent;
import org.kuali.core.service.DocumentService;
import org.kuali.core.service.KualiConfigurationService;
import org.kuali.core.service.KualiRuleService;
import org.kuali.core.util.GlobalVariables;
import org.kuali.core.web.struts.form.KualiForm;
import org.kuali.core.web.ui.KeyLabelPair;
import org.kuali.kra.authorization.Task;
import org.kuali.kra.budget.bo.BudgetLineItem;
import org.kuali.kra.budget.bo.BudgetPerson;
import org.kuali.kra.budget.bo.BudgetVersionOverview;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.lookup.keyvalue.BudgetCategoryTypeValuesFinder;
import org.kuali.kra.budget.service.BudgetDistributionAndIncomeService;
import org.kuali.kra.budget.service.BudgetModularService;
import org.kuali.kra.budget.service.impl.BudgetDistributionAndIncomeServiceImpl;
import org.kuali.kra.budget.web.struts.form.BudgetForm;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonRole;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.document.authorization.ProposalTask;
import org.kuali.kra.proposaldevelopment.web.struts.form.ProposalDevelopmentForm;
import org.kuali.kra.web.struts.action.ProposalActionBase;

import edu.iu.uis.eden.clientapp.IDocHandler;

public class BudgetAction extends ProposalActionBase {
    private static final Log LOG = LogFactory.getLog(BudgetAction.class);

    /**
     * @see org.kuali.core.web.struts.action.KualiDocumentActionBase#docHandler(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward docHandler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionForward forward = super.docHandler(mapping, form, request, response);
        BudgetForm budgetForm = (BudgetForm) form;

        if (IDocHandler.INITIATE_COMMAND.equals(budgetForm.getCommand())) {
            budgetForm.getBudgetDocument().initialize();
        }else{
            budgetForm.initialize();
        }
        
        reconcileBudgetStatus(budgetForm);
        return forward;
    }

    /**
     * Need to suppress buttons here when 'Totals' tab is clicked.
     * @see org.kuali.core.web.struts.action.KualiDocumentActionBase#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = super.execute(mapping, form, request, response);
        if (("totals").equals(actionForward.getName())) { 
            ((BudgetForm)form).suppressButtonsForTotalPage();
        }        		
        // check if audit rule check is done from PD
        if (((BudgetForm)form).isAuditActivated()) {
            KraServiceLocator.getService(KualiRuleService.class).applyRules(new DocumentAuditEvent(((BudgetForm)form).getBudgetDocument()));
        }
        
        //Set the Additional Document header info
        BudgetForm budgetForm = (BudgetForm) form;
        BudgetDocument budgetDocument = budgetForm.getBudgetDocument();
        if(budgetDocument != null) {
            for (BudgetVersionOverview budgetVersion: budgetDocument.getProposal().getBudgetVersionOverviews()) {
                if (budgetVersion.getBudgetVersionNumber().intValue() == budgetDocument.getBudgetVersionNumber().intValue()) {
                    budgetForm.setAdditionalDocInfo1(new KeyLabelPair("DataDictionary.KraAttributeReferenceDummy.attributes.budgetName", budgetVersion.getDocumentDescription()));
                    break;
                }
            }
            if(budgetForm.getAdditionalDocInfo1() == null) {
                budgetForm.setAdditionalDocInfo1(new KeyLabelPair("DataDictionary.KraAttributeReferenceDummy.attributes.budgetName", Constants.EMPTY_STRING));
            }
            
            if (budgetDocument.getBudgetVersionNumber() != null) {
                budgetForm.setAdditionalDocInfo2(new KeyLabelPair("DataDictionary.BudgetDocument.attributes.budgetVersionNumber", Integer.toString(budgetDocument.getBudgetVersionNumber())));
            } else {
                budgetForm.setAdditionalDocInfo2(new KeyLabelPair("DataDictionary.KraAttributeReferenceDummy.attributes.budgetName", Constants.EMPTY_STRING));
            } 
        }
        
        return actionForward;
    }


    @Override
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = super.save(mapping, form, request, response);
        BudgetForm budgetForm = (BudgetForm) form;
        if (budgetForm.getMethodToCall().equals("save") && budgetForm.isAuditActivated()) {
            DocumentService docService = KraServiceLocator.getService(DocumentService.class);
            ProposalDevelopmentDocument pdDoc = 
                (ProposalDevelopmentDocument) docService.getByDocumentHeaderId(budgetForm.getBudgetDocument().getProposal().getDocumentNumber());
            String forwardUrl = buildForwardUrl(pdDoc.getDocumentHeader().getWorkflowDocument().getRouteHeaderId());
            forwardUrl = StringUtils.replace(forwardUrl, "Proposal.do?", "Actions.do?auditActivated=true&");
            forward = new ActionForward(forwardUrl, true);
        }

        return forward;
    }

    public ActionForward versions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BudgetForm budgetForm = (BudgetForm) form;
        budgetForm.getBudgetDocument().getProposal().refreshReferenceObject(Constants.BUDGET_VERSION_OVERVIEWS);
        budgetForm.setFinalBudgetVersion(getFinalBudgetVersion(budgetForm.getBudgetDocument().getProposal().getBudgetVersionOverviews()));
        setBudgetStatuses(budgetForm.getBudgetDocument().getProposal());
        return mapping.findForward("versions");
    }

    public ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        reconcileBudgetStatus((BudgetForm) form);
        return mapping.findForward("summary");
    }

    public ActionForward personnel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BudgetForm budgetForm = (BudgetForm) form;
        reconcilePersonnelRoles(budgetForm.getBudgetDocument());
        
        return mapping.findForward("personnel");
    }

    public ActionForward expenses(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BudgetForm budgetForm = (BudgetForm) form;
        BudgetCategoryTypeValuesFinder budgetCategoryTypeValuesFinder = new BudgetCategoryTypeValuesFinder();
        List<KeyLabelPair> budgetCategoryTypes = new ArrayList<KeyLabelPair>();        
        budgetCategoryTypes = budgetCategoryTypeValuesFinder.getKeyValues();
        for(int i=0;i<budgetCategoryTypes.size();i++){
            budgetForm.getNewBudgetLineItems().add(new BudgetLineItem());
        }
        budgetForm.getBudgetDocument().setBudgetCategoryTypeCodes(budgetCategoryTypes);
        BudgetDocument budgetDocument = (BudgetDocument) budgetForm.getBudgetDocument();
        budgetDocument.refreshReferenceObject("budgetPeriods");       
        
        return mapping.findForward("expenses");
    }

    public ActionForward rates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("rates");
    }

    public ActionForward distributionAndIncome(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BudgetDistributionAndIncomeService budgetDistributionAndIncomeService = new BudgetDistributionAndIncomeServiceImpl();
        budgetDistributionAndIncomeService.initializeCollectionDefaults(((BudgetForm) form).getBudgetDocument());
        
        return mapping.findForward("distributionAndIncome");
    }

    public ActionForward modularBudget(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BudgetForm budgetForm = (BudgetForm) form;
        BudgetModularService budgetModularService = KraServiceLocator.getService(BudgetModularService.class);
        budgetForm.setBudgetModularSummary(budgetModularService.generateModularSummary(budgetForm.getBudgetDocument()));
        return mapping.findForward("modularBudget");
    }

    public ActionForward totals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BudgetForm budgetForm = (BudgetForm) form;
        budgetForm.getBudgetDocument().getBudgetTotals();
        return mapping.findForward("totals");
    }

    public ActionForward proposalHierarchy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("proposalHierarchy");
    }

    public ActionForward budgetActions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("budgetActions");
    }
    
    public ActionForward returnToProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BudgetForm budgetForm = (BudgetForm) form;
        DocumentService docService = KraServiceLocator.getService(DocumentService.class);
        ProposalDevelopmentDocument pdDoc = 
            (ProposalDevelopmentDocument) docService.getByDocumentHeaderId(budgetForm.getBudgetDocument().getProposal().getDocumentNumber());
        String forward = buildForwardUrl(pdDoc.getDocumentHeader().getWorkflowDocument().getRouteHeaderId());
        return new ActionForward(forward, true);
    }
    
    public void reconcilePersonnelRoles(BudgetDocument budgetDocument) {
//      Populate the person's proposal roles, if they exist
        List<BudgetPerson> budgetPersons = budgetDocument.getBudgetPersons();
        for (BudgetPerson budgetPerson: budgetPersons) {
            if (budgetPerson.getRolodexId() != null) {
                ProposalPersonRole role = budgetDocument.getProposal().getProposalNonEmployeeRole(budgetPerson.getRolodexId());
                if (role != null) { budgetPerson.setRole(role.getDescription()); }
            } else if (budgetPerson.getPersonId() != null) {
                ProposalPersonRole role = budgetDocument.getProposal().getProposalEmployeeRole(budgetPerson.getPersonId());
                if (role != null) { budgetPerson.setRole(role.getDescription()); }
            }
        }
    }

    /**
     * Build a Proposal Task.  We build a Proposal Task because budget permissions are
     * based upon its Proposal.  
     * @param taskName the name of the task
     * @param form the Budget Form
     * @return the Proposal Task
     */
    protected Task buildTask(String actionName, String taskName, ActionForm form, HttpServletRequest request) {
        BudgetForm budgetForm = (BudgetForm) form;
        ProposalDevelopmentDocument proposalDoc = budgetForm.getBudgetDocument().getProposal();
        return new ProposalTask(actionName, taskName, proposalDoc);
    }
    
    protected void reconcileBudgetStatus(BudgetForm budgetForm) {
        BudgetDocument budgetDocument = budgetForm.getBudgetDocument();
        if (budgetDocument.getFinalVersionFlag() != null && budgetDocument.getFinalVersionFlag()) {
            budgetDocument.setBudgetStatus(budgetDocument.getProposal().getBudgetStatus());
        } else {
            String budgetStatusIncompleteCode = KraServiceLocator.getService(KualiConfigurationService.class).getParameterValue(
                    Constants.PARAMETER_MODULE_BUDGET, Constants.PARAMETER_COMPONENT_DOCUMENT, Constants.BUDGET_STATUS_INCOMPLETE_CODE);
            budgetDocument.setBudgetStatus(budgetStatusIncompleteCode);
        }
    }
}
