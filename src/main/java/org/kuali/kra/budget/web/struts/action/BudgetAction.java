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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.RiceConstants;
import org.kuali.core.rule.event.DocumentAuditEvent;
import org.kuali.core.service.DocumentService;
import org.kuali.core.service.KualiConfigurationService;
import org.kuali.core.service.KualiRuleService;
import org.kuali.core.util.GlobalVariables;
import org.kuali.core.util.ObjectUtils;
import org.kuali.core.util.WebUtils;
import org.kuali.core.web.ui.KeyLabelPair;
import org.kuali.kra.budget.bo.BudgetLineItem;
import org.kuali.kra.budget.bo.BudgetLineItemCalculatedAmount;
import org.kuali.kra.budget.bo.BudgetPeriod;
import org.kuali.kra.budget.bo.BudgetPerson;
import org.kuali.kra.budget.bo.BudgetPersonnelCalculatedAmount;
import org.kuali.kra.budget.bo.BudgetPersonnelDetails;
import org.kuali.kra.budget.bo.BudgetVersionOverview;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.lookup.keyvalue.BudgetCategoryTypeValuesFinder;
import org.kuali.kra.budget.service.BudgetDistributionAndIncomeService;
import org.kuali.kra.budget.service.BudgetModularService;
import org.kuali.kra.budget.service.BudgetPrintService;
import org.kuali.kra.budget.service.BudgetService;
import org.kuali.kra.budget.service.BudgetSubAwardService;
import org.kuali.kra.budget.service.impl.BudgetDistributionAndIncomeServiceImpl;
import org.kuali.kra.budget.web.struts.form.BudgetForm;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.proposaldevelopment.bo.AttachmentDataSource;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonRole;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.service.KeyPersonnelService;
import org.kuali.kra.web.struts.action.ProposalActionBase;

import edu.iu.uis.eden.clientapp.IDocHandler;

public class BudgetAction extends ProposalActionBase {
    private static final Log LOG = LogFactory.getLog(BudgetAction.class);
    private static final String PERSONNEL_CATEGORY_TYPE_DESC = "Personnel";
    
    /**
     * @see org.kuali.core.web.struts.action.KualiDocumentActionBase#docHandler(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward docHandler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionForward forward = super.docHandler(mapping, form, request, response);
        BudgetForm budgetForm = (BudgetForm) form;

        if (IDocHandler.INITIATE_COMMAND.equals(budgetForm.getCommand())) {
            budgetForm.getDocument().initialize();
        }else{
            budgetForm.initialize();
        }
        
        BudgetDocument budgetDocument = budgetForm.getDocument();
        // populate costelement and other shared field to personnel detail
        // primarily to prevent sync modular budget in case 'reload' on modular budget page
        copyLineItemToPersonnelDetails(budgetDocument);
        if (budgetDocument.getActivityTypeCode().equals("x")) {
            budgetDocument.setActivityTypeCode(KraServiceLocator.getService(BudgetService.class).getActivityTypeForBudget(budgetDocument));
        }
        if(budgetDocument.getOhRateClassCode()!=null && ((BudgetForm)GlobalVariables.getKualiForm())!=null){
            // this is to prevent item calamts to be regenerated again when load doc from doc search
            // getting uglier.  definitely candidate for refactoring
            ((BudgetForm)GlobalVariables.getKualiForm()).setOhRateClassCodePrevValue(budgetDocument.getOhRateClassCode());
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
        
        
        if (actionForward != null) {
            if (("summaryTotals").equals(actionForward.getName())) { 
                ((BudgetForm)form).suppressButtonsForTotalPage();
            }               
        }
        // check if audit rule check is done from PD
        if (((BudgetForm)form).isAuditActivated()) {
            KraServiceLocator.getService(KualiRuleService.class).applyRules(new DocumentAuditEvent(((BudgetForm)form).getDocument()));
        }
        
        //Set the Additional Document header info
        BudgetForm budgetForm = (BudgetForm) form;
        BudgetDocument budgetDocument = budgetForm.getDocument();
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



    /**
     * {@inheritDoc}
     */
    @Override
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BudgetForm budgetForm = (BudgetForm) form;
        final BudgetDocument budgetDoc = budgetForm.getDocument();

        budgetDoc.getBudgetSummaryService().calculateBudget(budgetDoc);

        ActionForward forward = super.save(mapping, form, request, response);

        if (budgetForm.getMethodToCall().equals("save") && budgetForm.isAuditActivated()) {
            final DocumentService docService = KraServiceLocator.getService(DocumentService.class);
            final ProposalDevelopmentDocument pdDoc =
                (ProposalDevelopmentDocument) docService.getByDocumentHeaderId(budgetForm.getDocument().getProposal().getDocumentNumber());
            final String tempForwardUrl = buildForwardUrl(pdDoc.getDocumentHeader().getWorkflowDocument().getRouteHeaderId());
            final String forwardUrl = StringUtils.replace(tempForwardUrl, "Proposal.do?", "Actions.do?auditActivated=true&");
            forward = new ActionForward(forwardUrl, true);
        }

        final BudgetTDCValidator tdcValidator = new BudgetTDCValidator(request);
        if (budgetForm.toBudgetVersionsPage()
            || "BudgetVersionsAction".equals(budgetForm.getActionName())) {
            GlobalVariables.getErrorMap().addToErrorPath(RiceConstants.DOCUMENT_PROPERTY_NAME + ".proposal");
            tdcValidator.validateGeneratingErrorsAndWarnings(budgetDoc.getProposal());
        } else {
            tdcValidator.validateGeneratingWarnings(budgetDoc.getProposal());
        }

        return forward;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionForward reload(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        final ActionForward forward = super.reload(mapping, form, request, response);
        final BudgetForm budgetForm = (BudgetForm) form;

        budgetForm.setFinalBudgetVersion(getFinalBudgetVersion(budgetForm.getDocument().getProposal().getBudgetVersionOverviews()));
        setBudgetStatuses(budgetForm.getDocument().getProposal());

        final BudgetTDCValidator tdcValidator = new BudgetTDCValidator(request);
        tdcValidator.validateGeneratingWarnings(budgetForm.getDocument().getProposal());

        return forward;
    }
    
    public ActionForward versions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BudgetForm budgetForm = (BudgetForm) form;
        budgetForm.getDocument().getProposal().refreshReferenceObject(Constants.BUDGET_VERSION_OVERVIEWS);
        budgetForm.setFinalBudgetVersion(getFinalBudgetVersion(budgetForm.getDocument().getProposal().getBudgetVersionOverviews()));
        setBudgetStatuses(budgetForm.getDocument().getProposal());
        return mapping.findForward(Constants.BUDGET_VERSIONS_PAGE);
    }

    public ActionForward parameters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        reconcileBudgetStatus((BudgetForm) form);
        ((BudgetForm) form).getDocument().getBudgetSummaryService().setupOldStartEndDate(((BudgetForm) form).getDocument(),false);
        return mapping.findForward(Constants.BUDGET_PERIOD_PAGE);
    }

    public ActionForward personnel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BudgetForm budgetForm = (BudgetForm) form;
        populatePersonnelCategoryTypeCodes(budgetForm);
        reconcilePersonnelRoles(budgetForm.getDocument());
        
        for(BudgetPeriod period : budgetForm.getDocument().getBudgetPeriods()) {
            for(BudgetLineItem lineItem : period.getBudgetLineItems()) {
                for(BudgetPersonnelDetails budgetPersonnelDetails : lineItem.getBudgetPersonnelDetailsList()) {
                    budgetPersonnelDetails.refreshReferenceObject("budgetPerson");
                    ObjectUtils.materializeObjects(budgetPersonnelDetails.getBudgetPersonnelCalculatedAmounts());
                    for(BudgetPersonnelCalculatedAmount budgetPersonnelCalculatedAmount:budgetPersonnelDetails.getBudgetPersonnelCalculatedAmounts()){
                        if(budgetPersonnelCalculatedAmount.getRateClass() == null) {
                            budgetPersonnelCalculatedAmount.refreshReferenceObject("rateClass");
                        }
                    }
                }
                
                for(BudgetLineItemCalculatedAmount lineItemCalculatedAmount:lineItem.getBudgetLineItemCalculatedAmounts()){
                    if(lineItemCalculatedAmount.getRateClass() == null) {
                        lineItemCalculatedAmount.refreshReferenceObject("rateClass");
                    }
                }
            }
        }
        return mapping.findForward(Constants.BUDGET_PERSONNEL_PAGE);
    }

    protected void populatePersonnelCategoryTypeCodes(BudgetForm budgetForm) {
        BudgetDocument budgetDocument = budgetForm.getDocument();
        
        BudgetCategoryTypeValuesFinder budgetCategoryTypeValuesFinder = new BudgetCategoryTypeValuesFinder();
        List<KeyLabelPair> budgetCategoryTypes = new ArrayList<KeyLabelPair>();      
        
        for(KeyLabelPair budgetCategoryType: budgetCategoryTypeValuesFinder.getKeyValues()){
            if(budgetCategoryType.getLabel().equalsIgnoreCase(PERSONNEL_CATEGORY_TYPE_DESC)) {
                budgetCategoryTypes.add(budgetCategoryType);
                BudgetLineItem newBudgetLineItem = new BudgetLineItem();
                budgetForm.getNewBudgetLineItems().add(newBudgetLineItem);
            }
        }
        budgetDocument.setBudgetCategoryTypeCodes(budgetCategoryTypes); 
    }

    protected void populateNonPersonnelCategoryTypeCodes(BudgetForm budgetForm) {
        BudgetDocument budgetDocument = budgetForm.getDocument();
        
        BudgetCategoryTypeValuesFinder budgetCategoryTypeValuesFinder = new BudgetCategoryTypeValuesFinder();
        List<KeyLabelPair> budgetCategoryTypes = new ArrayList<KeyLabelPair>();      
        
        for(KeyLabelPair budgetCategoryType: budgetCategoryTypeValuesFinder.getKeyValues()){
            if(!budgetCategoryType.getLabel().equalsIgnoreCase(PERSONNEL_CATEGORY_TYPE_DESC)) {
                budgetCategoryTypes.add(budgetCategoryType);
                BudgetLineItem newBudgetLineItem = new BudgetLineItem();
                budgetForm.getNewBudgetLineItems().add(newBudgetLineItem);
            }
        }
        budgetDocument.setBudgetCategoryTypeCodes(budgetCategoryTypes); 
    }
    
    public ActionForward expenses(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BudgetForm budgetForm = (BudgetForm) form;
        
        populateNonPersonnelCategoryTypeCodes(budgetForm);
        
        BudgetDocument budgetDocument = budgetForm.getDocument();
        budgetDocument.refreshReferenceObject("budgetPeriods");       
        
        return mapping.findForward(Constants.BUDGET_EXPENSES_PAGE);
    }

    public ActionForward rates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward(Constants.BUDGET_RATES_PAGE);
    }

    public ActionForward distributionAndIncome(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BudgetDistributionAndIncomeService budgetDistributionAndIncomeService = new BudgetDistributionAndIncomeServiceImpl();
        budgetDistributionAndIncomeService.initializeCollectionDefaults(((BudgetForm) form).getDocument());
        
        return mapping.findForward(Constants.BUDGET_DIST_AND_INCOME_PAGE);
    }

    public ActionForward modularBudget(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BudgetForm budgetForm = (BudgetForm) form;
        BudgetModularService budgetModularService = KraServiceLocator.getService(BudgetModularService.class);
        budgetForm.setBudgetModularSummary(budgetModularService.generateModularSummary(budgetForm.getDocument()));
        return mapping.findForward(Constants.BUDGET_MODULAR_PAGE);
    }

    protected void populatePersonnelRoles(BudgetDocument budgetDocument) {
        KeyPersonnelService keyPersonnelServiec = KraServiceLocator.getService(KeyPersonnelService.class);
        ProposalDevelopmentDocument proposal = budgetDocument.getProposal();
        boolean nihSponsorProposal = keyPersonnelServiec.isSponsorNIH(proposal);
        Map roleDescriptions = budgetDocument.getProposal().getNihDescription();
        
        List<BudgetPerson> budgetPersons = budgetDocument.getBudgetPersons();
        for (BudgetPerson budgetPerson: budgetPersons) {
            String roleDesc = "";
            if (budgetPerson.getRolodexId() != null) {
                ProposalPerson person = proposal.getProposalNonEmployee(budgetPerson.getRolodexId());
                ProposalPersonRole role = proposal.getProposalNonEmployeeRole(budgetPerson.getRolodexId());
                if (role != null) { 
                    roleDesc = (nihSponsorProposal && roleDescriptions.get(role.getProposalPersonRoleId()) != null) ? roleDescriptions.get(role.getProposalPersonRoleId()).toString() : role.getDescription();
                    if(person != null && StringUtils.equals(Constants.KEY_PERSON_ROLE, role.getProposalPersonRoleId()) && StringUtils.isNotEmpty(person.getProjectRole())) {
                        roleDesc = person.getProjectRole();
                    }
                }
            } else if (budgetPerson.getPersonId() != null) {
                ProposalPerson person = proposal.getProposalEmployee(budgetPerson.getPersonId());  
                ProposalPersonRole role = proposal.getProposalEmployeeRole(budgetPerson.getPersonId());
                if (role != null) { 
                    roleDesc = (nihSponsorProposal && roleDescriptions.get(role.getProposalPersonRoleId()) != null) ? roleDescriptions.get(role.getProposalPersonRoleId()).toString() : role.getDescription();
                    if(person != null && StringUtils.equals(Constants.KEY_PERSON_ROLE, role.getProposalPersonRoleId()) && StringUtils.isNotEmpty(person.getProjectRole())) {
                        roleDesc = person.getProjectRole();
                    }
                }
            }
            
            budgetPerson.setRole(roleDesc);
        }
    }
    
    public ActionForward summaryTotals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BudgetForm budgetForm = (BudgetForm) form;
        BudgetDocument budgetDocument = budgetForm.getDocument();
        populatePersonnelRoles(budgetDocument);
        
        for(BudgetPeriod period : budgetForm.getDocument().getBudgetPeriods()) {
            for(BudgetLineItem lineItem : period.getBudgetLineItems()) {
                for(BudgetPersonnelDetails budgetPersonnelDetails : lineItem.getBudgetPersonnelDetailsList()) {
                    budgetPersonnelDetails.refreshReferenceObject("budgetPerson");
                }
            }
        }
        
        budgetDocument.getBudgetTotals();
        return mapping.findForward(Constants.BUDGET_SUMMARY_TOTALS_PAGE);
    }

    public ActionForward proposalHierarchy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward(Constants.PROPOSAL_HIERARCHY_PAGE);
    }

    public ActionForward budgetActions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BudgetForm budgetForm = (BudgetForm) form;
        BudgetDocument budgetDocument = budgetForm.getDocument();
        if(budgetDocument.getBudgetPrintForms().isEmpty()){
            BudgetPrintService budgetPrintService = KraServiceLocator.getService(BudgetPrintService.class);
            budgetPrintService.populateBudgetPrintForms(budgetDocument);
        }
        KraServiceLocator.getService(BudgetSubAwardService.class).populateBudgetSubAwardAttachments(budgetDocument);
        return mapping.findForward(Constants.BUDGET_ACTIONS_PAGE);
    }
    
    public ActionForward returnToProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BudgetForm budgetForm = (BudgetForm) form;
        DocumentService docService = KraServiceLocator.getService(DocumentService.class);
        String docNumber = budgetForm.getDocument().getProposal().getDocumentNumber();
        ProposalDevelopmentDocument pdDoc = (ProposalDevelopmentDocument) docService.getByDocumentHeaderId(docNumber);
        
        // JIRA KRACOEUS-1441
        save(mapping, form, request, response);
        
        String forwardUrl = buildForwardUrl(pdDoc.getDocumentHeader().getWorkflowDocument().getRouteHeaderId());
        if(budgetForm.isAuditActivated()) {
            forwardUrl = StringUtils.replace(forwardUrl, "Proposal.do?", "Actions.do?auditActivated=true&");
        }
        
        return new ActionForward(forwardUrl, true);
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
    
    protected void reconcileBudgetStatus(BudgetForm budgetForm) {
        BudgetDocument budgetDocument = budgetForm.getDocument();
        if (budgetDocument.getFinalVersionFlag() != null && budgetDocument.getFinalVersionFlag()) {
            budgetDocument.setBudgetStatus(budgetDocument.getProposal().getBudgetStatus());
        } else {
            String budgetStatusIncompleteCode = KraServiceLocator.getService(KualiConfigurationService.class).getParameterValue(
                    Constants.PARAMETER_MODULE_BUDGET, Constants.PARAMETER_COMPONENT_DOCUMENT, Constants.BUDGET_STATUS_INCOMPLETE_CODE);
            budgetDocument.setBudgetStatus(budgetStatusIncompleteCode);
        }
    }
    
    
    /**
     * 
     * Sets periodTypeCode in BudgetPersonnelDetails to system variable BUDGET_PERSON_DEFAULT_PERIOD_TYPE
     */
//    public void setBudgetPersonDefaultPeriodTypeCode(BudgetForm budgetForm){
//        KualiConfigurationService kualiConfigurationService = KraServiceLocator.getService(KualiConfigurationService.class);
//        budgetForm.setNewBudgetPersonnelDetails(new BudgetPersonnelDetails());
//        budgetForm.getNewBudgetPersonnelDetails().setPeriodTypeCode(kualiConfigurationService.getParameterValue(
//                Constants.PARAMETER_MODULE_BUDGET, Constants.PARAMETER_COMPONENT_DOCUMENT, Constants.BUDGET_PERSON_DEFAULT_PERIOD_TYPE));
//        
//    }

    /**
     * 
     * Handy method to stream the byte array to response object
     * @param attachmentDataSource
     * @param response
     * @throws Exception
     */
    public void streamToResponse(AttachmentDataSource attachmentDataSource,HttpServletResponse response) throws Exception{
        byte[] xbts = attachmentDataSource.getContent();
        ByteArrayOutputStream baos = null;
        try{
            baos = new ByteArrayOutputStream(xbts.length);
            baos.write(xbts);
            
            WebUtils.saveMimeOutputStreamAsFile(response, attachmentDataSource.getContentType(), baos, attachmentDataSource.getFileName());
            
        }finally{
            try{
                if(baos!=null){
                    baos.close();
                    baos = null;
                }
            }catch(IOException ioEx){
                LOG.warn(ioEx.getMessage(), ioEx);
            }
        }
    }
    
    private void copyLineItemToPersonnelDetails(BudgetDocument budgetDocument) {
        for (BudgetPeriod budgetPeriod : budgetDocument.getBudgetPeriods()) {
            if (budgetPeriod.getBudgetLineItems() != null && !budgetPeriod.getBudgetLineItems().isEmpty()) {
                for (BudgetLineItem budgetLineItem : budgetPeriod.getBudgetLineItems()) {
        
                    if (budgetLineItem.getBudgetPersonnelDetailsList() != null && !budgetLineItem.getBudgetPersonnelDetailsList().isEmpty()) {
                        for (BudgetPersonnelDetails budgetPersonnelDetails : budgetLineItem.getBudgetPersonnelDetailsList()) {
                            budgetPersonnelDetails.setProposalNumber(budgetLineItem.getProposalNumber());
                            budgetPersonnelDetails.setBudgetVersionNumber(budgetLineItem.getBudgetVersionNumber());
                            budgetPersonnelDetails.setBudgetPeriod(budgetLineItem.getBudgetPeriod());
                            budgetPersonnelDetails.setLineItemNumber(budgetLineItem.getLineItemNumber());
                            budgetPersonnelDetails.setCostElement(budgetLineItem.getCostElement());
                            budgetPersonnelDetails.setCostElementBO(budgetLineItem.getCostElementBO());
                       }
                    }
                }
            }
        }
    }


}
