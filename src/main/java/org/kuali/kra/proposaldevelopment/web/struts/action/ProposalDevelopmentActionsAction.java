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
package org.kuali.kra.proposaldevelopment.web.struts.action;

import static org.kuali.kra.infrastructure.Constants.MAPPING_BASIC;
import static org.kuali.kra.infrastructure.KraServiceLocator.getService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.RiceConstants;
import org.kuali.core.bo.user.UniversalUser;
import org.kuali.core.question.ConfirmationQuestion;
import org.kuali.core.rule.event.DocumentAuditEvent;
import org.kuali.core.service.BusinessObjectService;
import org.kuali.core.service.DocumentService;
import org.kuali.core.service.KualiConfigurationService;
import org.kuali.core.service.KualiRuleService;
import org.kuali.core.service.PersistenceStructureService;
import org.kuali.core.service.PessimisticLockService;
import org.kuali.core.util.AuditCluster;
import org.kuali.core.util.GlobalVariables;
import org.kuali.core.util.ObjectUtils;
import org.kuali.core.util.WebUtils;
import org.kuali.core.web.struts.action.AuditModeAction;
import org.kuali.core.web.struts.form.KualiDocumentFormBase;
import org.kuali.core.web.struts.form.KualiForm;
import org.kuali.core.workflow.service.KualiWorkflowDocument;
import org.kuali.kra.bo.SponsorFormTemplate;
import org.kuali.kra.budget.bo.BudgetVersionOverview;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.proposaldevelopment.bo.AttachmentDataSource;
import org.kuali.kra.proposaldevelopment.bo.ProposalChangedData;
import org.kuali.kra.proposaldevelopment.bo.ProposalCopyCriteria;
import org.kuali.kra.proposaldevelopment.bo.ProposalOverview;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.rule.event.CopyProposalEvent;
import org.kuali.kra.proposaldevelopment.rule.event.ProposalDataOverrideEvent;
import org.kuali.kra.proposaldevelopment.service.ProposalCopyService;
import org.kuali.kra.proposaldevelopment.service.ProposalStateService;
import org.kuali.kra.proposaldevelopment.web.struts.form.ProposalDevelopmentForm;
import org.kuali.kra.s2s.bo.S2sAppSubmission;
import org.kuali.kra.s2s.bo.S2sSubmissionHistory;
import org.kuali.kra.s2s.service.PrintService;
import org.kuali.kra.s2s.service.S2SService;
import org.kuali.kra.service.KraPersistenceStructureService;
import org.kuali.kra.web.struts.action.StrutsConfirmation;
import org.kuali.rice.KNSServiceLocator;

import edu.iu.uis.eden.EdenConstants;
import edu.iu.uis.eden.KEWServiceLocator;
import edu.iu.uis.eden.clientapp.WorkflowInfo;
import edu.iu.uis.eden.clientapp.vo.ActionRequestVO;
import edu.iu.uis.eden.clientapp.vo.DocumentDetailVO;
import edu.iu.uis.eden.clientapp.vo.NetworkIdVO;
import edu.iu.uis.eden.clientapp.vo.ReportCriteriaVO;
import edu.iu.uis.eden.clientapp.vo.UserIdVO;
import edu.iu.uis.eden.clientapp.vo.UserVO;
import edu.iu.uis.eden.clientapp.vo.WorkgroupVO;
import edu.iu.uis.eden.engine.node.KeyValuePair;
import edu.iu.uis.eden.engine.node.RouteNodeInstance;

/**
 * Handles all of the actions from the Proposal Development Actions web page.
 * 
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
public class ProposalDevelopmentActionsAction extends ProposalDevelopmentAction implements AuditModeAction {
    private static final Log LOG = LogFactory.getLog(ProposalDevelopmentActionsAction.class);
    private static final String DOCUMENT_APPROVE_QUESTION = "DocApprove";
    private static final String DOCUMENT_ROUTE_QUESTION="DocRoute";
    private static final String DOCUMENT_REJECT_QUESTION="DocReject";
    
    private static final String CONFIRM_SUBMISSION_WITH_WARNINGS_KEY = "submitApplication";
    private static final String EMPTY_STRING = "";
    
    private static final int OK = 0;
    private static final int WARNING = 1;
    private static final int ERROR = 2;
    
    /**
     * Struts mapping for the Proposal web page.  
     */
    private static final String MAPPING_PROPOSAL = "proposal";
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = super.execute(mapping, form, request, response);
        return actionForward;
    }

    /**
     * Calls the document service to approve the document
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception
     */
    @Override
    public ActionForward approve(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        KualiDocumentFormBase kualiDocumentFormBase = (KualiDocumentFormBase) form;
        KualiWorkflowDocument workflowDoc = kualiDocumentFormBase.getDocument().getDocumentHeader().getWorkflowDocument();

        if (canGenerateRequestsInFuture(workflowDoc)) {
            return promptUserForInput(workflowDoc, mapping, form, request, response);
        }

        return super.approve(mapping, form, request, response);
    }

    private ActionForward promptUserForInput(KualiWorkflowDocument workflowDoc, ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object question = request.getParameter(RiceConstants.QUESTION_INST_ATTRIBUTE_NAME);
        Object buttonClicked = request.getParameter(RiceConstants.QUESTION_CLICKED_BUTTON);
        String methodToCall = ((KualiForm) form).getMethodToCall();
        
        if (question == null) {
            // ask question if not already asked
              return this.performQuestionWithoutInput(mapping, form, request, response, DOCUMENT_APPROVE_QUESTION, KNSServiceLocator
                      .getKualiConfigurationService().getPropertyString(KeyConstants.QUESTION_APPROVE_FUTURE_REQUESTS),
                       RiceConstants.CONFIRMATION_QUESTION, methodToCall, "");             
        }
        else if(DOCUMENT_APPROVE_QUESTION.equals(question) && ConfirmationQuestion.NO.equals(buttonClicked)) {
            workflowDoc.setDoNotReceiveFutureRequests();
        }
        else if(DOCUMENT_APPROVE_QUESTION.equals(question)){ 
            workflowDoc.setReceiveFutureRequests();  
        }
        
        return super.approve(mapping, form, request, response);
    }   

    private boolean canGenerateRequestsInFuture(KualiWorkflowDocument workflowDoc) throws Exception {
        String loggedInUserID = GlobalVariables.getUserSession().getUniversalUser().getPersonUniversalIdentifier();
        NetworkIdVO networkId = new NetworkIdVO(loggedInUserID);
        ReportCriteriaVO reportCriteria = new ReportCriteriaVO(new Long(workflowDoc.getRouteHeaderId()));
        reportCriteria.setTargetUsers(new UserIdVO[] { networkId });

        boolean receiveFutureRequests = false;
        boolean doNotReceiveFutureRequests = false;    

        List variables = workflowDoc.getRouteHeader().getVariables();
        if (CollectionUtils.isNotEmpty(variables)) {
            for (Object variable : variables) {
                KeyValuePair kvp = (KeyValuePair) variable;
                if (kvp.getKey().startsWith(EdenConstants.RECEIVE_FUTURE_REQUESTS_BRANCH_STATE_KEY)
                        && kvp.getValue().toUpperCase().equals(EdenConstants.RECEIVE_FUTURE_REQUESTS_BRANCH_STATE_VALUE)
                        && kvp.getKey().contains(networkId.getNetworkId())) {
                    receiveFutureRequests = true; 
                    break;
                }
                else if (kvp.getKey().startsWith(EdenConstants.RECEIVE_FUTURE_REQUESTS_BRANCH_STATE_KEY)
                      && kvp.getValue().toUpperCase().equals(EdenConstants.DONT_RECEIVE_FUTURE_REQUESTS_BRANCH_STATE_VALUE)
                      && kvp.getKey().contains(networkId.getNetworkId())) {
                    doNotReceiveFutureRequests = true; 
                    break;
                }
            }
        } 

        return ((receiveFutureRequests == false && doNotReceiveFutureRequests == false) && canGenerateMultipleApprovalRequests(reportCriteria, networkId));
    }
    
    private boolean canGenerateMultipleApprovalRequests(ReportCriteriaVO reportCriteria, NetworkIdVO networkId) throws Exception {
        int approvalRequestsCount = 0;
        WorkflowInfo info = new WorkflowInfo();
        
        DocumentDetailVO results1 = info.routingReport(reportCriteria);
        for(ActionRequestVO actionRequest : results1.getActionRequests() ){
            if(!actionRequest.isRoleRequest() && actionRequest.isPending() && actionRequest.getActionRequested().equalsIgnoreCase(EdenConstants.ACTION_REQUEST_APPROVE_REQ) && 
                    recipientMatchesUser(actionRequest, networkId)) {
                approvalRequestsCount+=1; 
            }
        }
          
        return (approvalRequestsCount > 1);
    }
    
    private boolean recipientMatchesUser(ActionRequestVO actionRequest, NetworkIdVO networkId) {
        if(actionRequest != null && networkId != null) {
            UserVO recipientUser = actionRequest.getUserVO();
            if(recipientUser != null && recipientUser.getNetworkId().equals(networkId.getNetworkId())) {
                return true;
            } else {
                WorkgroupVO recipientGroup = actionRequest.getWorkgroupVO();
                if(recipientGroup != null && recipientGroup.getMembers().length > 0) {
                    for(UserVO member: recipientGroup.getMembers()) {
                        if(member != null && member.getNetworkId().equals(networkId.getNetworkId())) {
                            return true;
                        } 
                    }
                }
            }
        }
        
        return false;  
    }
    
    /**
     * Updates an Editable Proposal Field and 
     * adds it to the Proposal Change History
     * 
     * @param mapping the Struct's Action Mapping.
     * @param form the Proposal Development Form.
     * @param request the HTTP request.
     * @param response the HTTP response
     * @return the next web page to display
     * @throws Exception
     */
    public ActionForward addProposalChangedData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        BusinessObjectService boService = KraServiceLocator.getService(BusinessObjectService.class);
        KraPersistenceStructureService kraPersistenceStructureService = KraServiceLocator.getService(KraPersistenceStructureService.class);

        ProposalDevelopmentForm pdForm = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument pdDocument = pdForm.getProposalDevelopmentDocument();
        ProposalChangedData newProposalChangedData = pdForm.getNewProposalChangedData();
        
        newProposalChangedData.setProposalNumber(pdDocument.getProposalNumber());
        newProposalChangedData.setChangeNumber(getNextChangeNumber(boService, newProposalChangedData.getProposalNumber(), newProposalChangedData.getColumnName()));
        if(StringUtils.isEmpty(newProposalChangedData.getDisplayValue()) && StringUtils.isNotEmpty(newProposalChangedData.getChangedValue())) {
            newProposalChangedData.setDisplayValue(newProposalChangedData.getChangedValue());
        }
        
        String tmpLookupReturnPk = "";
        if(newProposalChangedData.getEditableColumn() != null) {
            tmpLookupReturnPk = newProposalChangedData.getEditableColumn().getLookupPkReturn();
        }
        
        newProposalChangedData.refreshReferenceObject("editableColumn");
        newProposalChangedData.getEditableColumn().setLookupPkReturn(tmpLookupReturnPk);
        
        if(newProposalChangedData.getEditableColumn() != null) {
            if(!newProposalChangedData.getEditableColumn().getHasLookup()) {
                newProposalChangedData.setDisplayValue(newProposalChangedData.getChangedValue());
            }
        }
            
        if(getKualiRuleService().applyRules(new ProposalDataOverrideEvent(pdForm.getProposalDevelopmentDocument(), newProposalChangedData))){
            boService.save(newProposalChangedData);
        
            ProposalOverview proposalWrapper = createProposalWrapper(pdDocument);
            Map<String, String> columnToAttributesMap = kraPersistenceStructureService.getDBColumnToObjectAttributeMap(ProposalOverview.class);
            String proposalAttributeToPersist = columnToAttributesMap.get(newProposalChangedData.getColumnName());
            ObjectUtils.setObjectProperty(proposalWrapper, proposalAttributeToPersist, newProposalChangedData.getChangedValue());
            ObjectUtils.setObjectProperty(pdDocument, proposalAttributeToPersist, newProposalChangedData.getChangedValue());
            
            boService.save(proposalWrapper);
            pdForm.getProposalDevelopmentDocument().setVersionNumber(proposalWrapper.getVersionNumber());
            
            pdForm.setNewProposalChangedData(new ProposalChangedData());
            growProposalChangedHistory(pdDocument, newProposalChangedData);
        }
        
        return mapping.findForward("basic");
    }
    
    private void growProposalChangedHistory(ProposalDevelopmentDocument pdDocument, ProposalChangedData newProposalChangedData) {
        Map<String, List<ProposalChangedData>> changeHistory = pdDocument.getProposalChangeHistory();
        if(changeHistory.get(newProposalChangedData.getEditableColumn().getColumnLabel()) == null) {
            changeHistory.put(newProposalChangedData.getEditableColumn().getColumnLabel(), new ArrayList<ProposalChangedData>());
        } 
        
        changeHistory.get(newProposalChangedData.getEditableColumn().getColumnLabel()).add(0, newProposalChangedData);
    }
    
    private ProposalOverview createProposalWrapper(ProposalDevelopmentDocument pdDocument) throws Exception {
        ProposalOverview proposalWrapper = new ProposalOverview();
        PersistenceStructureService persistentStructureService = KraServiceLocator.getService(PersistenceStructureService.class);
        List<String> fieldsToUpdate = (List<String>) persistentStructureService.listFieldNames(ProposalOverview.class);
        Object tempVal;
        for(String field: fieldsToUpdate) {
            tempVal = ObjectUtils.getPropertyValue(pdDocument, field);
            ObjectUtils.setObjectProperty(proposalWrapper, field, (tempVal != null) ? tempVal.toString() : null);
        }
        return proposalWrapper;
    } 
    
    private int getNextChangeNumber(BusinessObjectService boService, String proposalNumber, String columnName) {
        int changeNumber = 0;
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put("proposalNumber", proposalNumber);
        keyMap.put("columnName", columnName);
        List<ProposalChangedData> changedDataList = (List<ProposalChangedData>) boService.findMatchingOrderBy(ProposalChangedData.class, keyMap, "changeNumber", true);
        if(CollectionUtils.isNotEmpty(changedDataList)) {
            changeNumber = ((ProposalChangedData) changedDataList.get(changedDataList.size()-1)).getChangeNumber();
        }
        
        return ++changeNumber;
    }

    /**
     * Copies a Proposal Development Document based upon user-specified criteria.
     * 
     * @param mapping the Struct's Action Mapping.
     * @param form the Proposal Development Form.
     * @param request the HTTP request.
     * @param response the HTTP response
     * @return the next web page to display
     * @throws Exception
     */
    public ActionForward copyProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionForward nextWebPage = null;

        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument doc = proposalDevelopmentForm.getProposalDevelopmentDocument();
        ProposalCopyCriteria criteria = proposalDevelopmentForm.getCopyCriteria();
        
        // check any business rules
        boolean rulePassed = getKualiRuleService().applyRules(new CopyProposalEvent(doc, criteria));

        if (!rulePassed) {
            nextWebPage = mapping.findForward(Constants.MAPPING_BASIC);
        }
        else {
        // Use the Copy Service to copy the proposal.

        ProposalCopyService proposalCopyService = (ProposalCopyService) KraServiceLocator.getService("proposalCopyService");
        if (proposalCopyService == null) {

            // Something bad happened. The errors are in the Global Error Map
            // which will be displayed to the user.

            nextWebPage = mapping.findForward(Constants.MAPPING_BASIC);
        }
        else {
            String newDocId = proposalCopyService.copyProposal(doc, criteria);
            KraServiceLocator.getService(PessimisticLockService.class).releaseAllLocksForUser(doc.getPessimisticLocks(), GlobalVariables.getUserSession().getUniversalUser());
            
            // Switch over to the new proposal development document and
            // go to the Proposal web page.

            proposalDevelopmentForm.setDocId(newDocId);
            this.loadDocument(proposalDevelopmentForm);
            
            ProposalDevelopmentDocument copiedDocument = proposalDevelopmentForm.getProposalDevelopmentDocument();
            copiedDocument.setS2sAppSubmission(new ArrayList<S2sAppSubmission>());            
            DocumentService docService = KraServiceLocator.getService(DocumentService.class);
            docService.saveDocument(copiedDocument);
            
            nextWebPage = mapping.findForward(MAPPING_PROPOSAL);
            
            proposalDevelopmentForm.setCopyCriteria(new ProposalCopyCriteria());
            }
        }

        return nextWebPage;
    }

    /**
     * @see org.kuali.core.web.struts.action.AuditModeAction#activate(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward activate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        proposalDevelopmentForm.setAuditActivated(true);
        
        ProposalDevelopmentDocument proposalDevelopmentDocument = (ProposalDevelopmentDocument)proposalDevelopmentForm.getDocument();
        S2SService s2sService = ((S2SService) KraServiceLocator.getService(S2SService.class));
        boolean errorExists = false;
        boolean warningExists = false;
        if(proposalDevelopmentDocument.getS2sOpportunity()!=null){
            if(!(KraServiceLocator.getService(KualiRuleService.class).applyRules(
                    new DocumentAuditEvent(proposalDevelopmentForm.getDocument())) & s2sService.validateApplication(proposalDevelopmentDocument.getS2sOpportunity().getProposalNumber()))){
                for (Iterator iter = GlobalVariables.getAuditErrorMap().keySet().iterator(); iter.hasNext();){     
                    AuditCluster auditCluster = (AuditCluster)GlobalVariables.getAuditErrorMap().get(iter.next());
                    if(StringUtils.equalsIgnoreCase(auditCluster.getCategory(),Constants.AUDIT_ERRORS)){
                        errorExists=true;
                        break;
                    }
                    if(StringUtils.equalsIgnoreCase(auditCluster.getCategory(),Constants.GRANTSGOV_ERRORS)){
                        errorExists = true;
                        break;
                    }
                    if(StringUtils.equalsIgnoreCase(auditCluster.getCategory(),Constants.AUDIT_WARNINGS)){
                        warningExists = true;
                    }
                }
                if(errorExists){
                    GlobalVariables.getErrorMap().putError("noKey", KeyConstants.VALIDATTION_ERRORS_BEFORE_GRANTS_GOV_SUBMISSION);
                }           
            }
        }

        // TODO : this rull will be called again in proposaldevelopmentaction.execute
        // should we comment out here
        //KraServiceLocator.getService(KualiRuleService.class).applyRules(
        //        new DocumentAuditEvent(proposalDevelopmentForm.getDocument()));
       
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    /**
     * @see org.kuali.core.web.struts.action.AuditModeAction#deactivate(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward deactivate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        proposalDevelopmentForm.setAuditActivated(false);

        return mapping.findForward((RiceConstants.MAPPING_BASIC));
    }

    
    public ActionForward reject(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String NodeName = null;
        KualiDocumentFormBase kualiDocumentFormBase = (KualiDocumentFormBase) form;
        KualiWorkflowDocument workflowdocument=kualiDocumentFormBase.getDocument().getDocumentHeader().getWorkflowDocument();
        Long routeHeaderId=kualiDocumentFormBase.getDocument().getDocumentHeader().getWorkflowDocument().getRouteHeaderId();
        List currentNodeInstances = KEWServiceLocator.getRouteNodeService().getCurrentNodeInstances(routeHeaderId);
        List<RouteNodeInstance> nodeInstances = new ArrayList<RouteNodeInstance>();
        for (Iterator iterator = currentNodeInstances.iterator(); iterator.hasNext();) {
           RouteNodeInstance nodeInstance = (RouteNodeInstance) iterator.next();
           NodeName= nodeInstance.getRouteNode().getRouteNodeName();
        }
        Object question = request.getParameter(RiceConstants.QUESTION_INST_ATTRIBUTE_NAME);
        Object buttonClicked = request.getParameter(RiceConstants.QUESTION_CLICKED_BUTTON);
        String reason = request.getParameter(RiceConstants.QUESTION_REASON_ATTRIBUTE_NAME);
        String methodToCall = ((KualiForm) form).getMethodToCall();
        String rejectNoteText = "";
        if(question == null){
            return this.performQuestionWithInput(mapping, form, request, response, DOCUMENT_REJECT_QUESTION,"Are you sure you want to reject this document?" , RiceConstants.CONFIRMATION_QUESTION, methodToCall, "");
         } 
        else if((DOCUMENT_REJECT_QUESTION.equals(question)) && ConfirmationQuestion.NO.equals(buttonClicked))  {
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        }
        else
        {
            String introNoteMessage = "Proposal Rejected" + RiceConstants.BLANK_SPACE;
            rejectNoteText = introNoteMessage + reason;
            workflowdocument.returnToPreviousNode(rejectNoteText, NodeName);
            return super.returnToSender(mapping, kualiDocumentFormBase);
        }
        
       
    }
    
    
    /**
     * route the document using the document service
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception
     */
    @Override
    public ActionForward route(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean success;
        
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        proposalDevelopmentForm.setAuditActivated(true);
        success=KraServiceLocator.getService(KualiRuleService.class).applyRules(new DocumentAuditEvent(proposalDevelopmentForm.getDocument()));
        HashMap map=GlobalVariables.getAuditErrorMap();
        Object question = request.getParameter(RiceConstants.QUESTION_INST_ATTRIBUTE_NAME);
        Object buttonClicked = request.getParameter(RiceConstants.QUESTION_CLICKED_BUTTON);
        String methodToCall = ((KualiForm) form).getMethodToCall();
        if((map.size()==1) &&  map.containsKey("sponsorProgramInformationAuditWarnings"))
        {

            if(question == null){
                return this.performQuestionWithoutInput(mapping, form, request, response, DOCUMENT_ROUTE_QUESTION, "Validation Warning Exists.Are you sure want to submit to workflow routing.", RiceConstants.CONFIRMATION_QUESTION, methodToCall, "");
                
            } 
            else if(DOCUMENT_ROUTE_QUESTION.equals(question) && ConfirmationQuestion.YES.equals(buttonClicked)) {
                ActionForward actionForward = super.route(mapping, form, request, response);
                return actionForward;
            }

            else{
                return mapping.findForward((RiceConstants.MAPPING_BASIC));
            }    

        }
        if(success){
            ActionForward actionForward = super.route(mapping, form, request, response);
            return actionForward;
        }else   {
        GlobalVariables.getErrorMap().putError("datavalidation",KeyConstants.ERROR_WORKFLOW_SUBMISSION,  new String[] {});
        return mapping.findForward((RiceConstants.MAPPING_BASIC));
         }
        
}
    
    /**
     * Submit a proposal to a sponsor.  
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward submitToSponsor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response)throws Exception{
      
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument proposalDevelopmentDocument = (ProposalDevelopmentDocument)proposalDevelopmentForm.getDocument();

        proposalDevelopmentForm.setAuditActivated(true);
        
        ActionForward forward = mapping.findForward(Constants.MAPPING_BASIC);
        int status = isValidSubmission(proposalDevelopmentDocument);
        if (status == OK) {
            forward = submitApplication(mapping, form, request, response);
        } 
        else if (status == WARNING) {
            StrutsConfirmation question = buildSubmitToGrantsGovWithWarningsQuestion(mapping, form, request, response);
            forward = confirm(question, CONFIRM_SUBMISSION_WITH_WARNINGS_KEY, EMPTY_STRING);
        }
       
        return forward;      
    }
    
    /**
     * Is this a valid submission?  
     * @param proposalDevelopmentDocument
     * @return OK, WARNING or ERROR
     */
    private int isValidSubmission(ProposalDevelopmentDocument proposalDevelopmentDocument) {
        int state = OK;
        
        /*
         * If this proposal is a continuation from another proposal, it is illegal for
         * it to be a New Proposal Type or for it to be an S2S Submission Application Type.
         */
        if ((proposalDevelopmentDocument.getContinuedFrom() != null) && 
            (isNewProposalType(proposalDevelopmentDocument) || isSubmissionApplication(proposalDevelopmentDocument))) {
            state = ERROR;
            if (isNewProposalType(proposalDevelopmentDocument)) {
                GlobalVariables.getErrorMap().putError("noKey", KeyConstants.ERROR_RESUBMISSION_PROPOSALTYPE_IS_NEW);
            } else if (isSubmissionApplication(proposalDevelopmentDocument)) {
                GlobalVariables.getErrorMap().putError("noKey", KeyConstants.ERROR_RESUBMISSION_OPPORTUNITY_IS_APPLICATION);
            }
        }
        else {
            /* 
             * Don't know what to do about the Audit Rules.  The parent class invokes the Audit rules
             * from its execute() method.  It will stay this way for the time being because I this is
             * what the code did before it was refactored.
             */
            KualiRuleService ruleService = KraServiceLocator.getService(KualiRuleService.class);
            boolean auditPassed = ruleService.applyRules(new DocumentAuditEvent(proposalDevelopmentDocument));
            
            /*
             * Check for S2S validation if we have a Grants.gov Opportunity.  If there is no Grants.gov
             * Opportunity, then the user wants to submit the proposal manually (printing and sending via
             * snail mail).
             */
            boolean s2sPassed = true;
            if (proposalDevelopmentDocument.getS2sOpportunity() != null) {
                S2SService s2sService = (S2SService) KraServiceLocator.getService(S2SService.class);
                s2sPassed = s2sService.validateApplication(proposalDevelopmentDocument.getProposalNumber());
            }
            
            /*
             * If either reported an error, then we have to check to see if we only have warnings or
             * if we also have some errors.
             */
            if (!auditPassed || !s2sPassed) {
                state = WARNING;
                for (Iterator iter = GlobalVariables.getAuditErrorMap().keySet().iterator(); iter.hasNext();) {
                    AuditCluster auditCluster = (AuditCluster)GlobalVariables.getAuditErrorMap().get(iter.next());
                    if (!StringUtils.equalsIgnoreCase(auditCluster.getCategory(), Constants.AUDIT_WARNINGS)) {
                        state = ERROR;
                        GlobalVariables.getErrorMap().putError("noKey", KeyConstants.VALIDATTION_ERRORS_BEFORE_GRANTS_GOV_SUBMISSION);
                        break;
                    }
                }
            }
        }
            
        return state;
    }
        
    /**
     * Is the proposal classified as NEW according to its proposal type?
     * @param doc the proposal development document
     * @return true if new; otherwise false
     */
    private boolean isNewProposalType(ProposalDevelopmentDocument doc) {
        String newProposalTypeValue = getProposalParameterValue(KeyConstants.PROPOSALDEVELOPMENT_PROPOSALTYPE_NEW);
        return StringUtils.equalsIgnoreCase(doc.getProposalTypeCode(), newProposalTypeValue);
    }
                
    /**
     * Does the proposal have a Grants.gov Opportunity with a S2S submission type of APPLICATION?
     * @param doc the proposal development document
     * @return true if proposal has grants.gov with submission type of APPLICATION; otherwise false
     */
    private boolean isSubmissionApplication(ProposalDevelopmentDocument doc) {
        if (doc.getS2sOpportunity() != null) {
            String applicationSubmissionValue = getProposalParameterValue(KeyConstants.S2S_SUBMISSIONTYPE_APPLICATION);
            return StringUtils.equalsIgnoreCase(doc.getS2sOpportunity().getS2sSubmissionTypeCode(), applicationSubmissionValue);
        }
        return false;
    }
    
    /**
     * Get a Proposal parameter value from the Kuali System Parameters.
     * @param parameterName the name of the parameter
     * @return the parameter's value
     */
    private String getProposalParameterValue(String parameterName) {
        KualiConfigurationService configurationService = KraServiceLocator.getService(KualiConfigurationService.class);
        return configurationService.getParameter(Constants.PARAMETER_MODULE_PROPOSAL_DEVELOPMENT, 
                                                 Constants.PARAMETER_COMPONENT_DOCUMENT, 
                                                 parameterName).getParameterValue();
    }
    
    /**
     * Submit the proposal to the sponsor without any further error checking.  Can be invoked if the user
     * wishes to ignore an warnings that occurred when first trying to submit the proposal.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward submitApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response)throws Exception{
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument proposalDevelopmentDocument = (ProposalDevelopmentDocument)proposalDevelopmentForm.getDocument();
        
        /*
         * If there is an opportunity, then this is a Grants.gov submission.  
         */
        if (proposalDevelopmentDocument.getS2sOpportunity() != null) {
            submitS2sApplication(proposalDevelopmentDocument);
            createSubmissionHistory(proposalDevelopmentDocument);
        }
       
        proposalDevelopmentDocument.setSubmitFlag(true);
        
        ProposalStateService proposalStateService = KraServiceLocator.getService(ProposalStateService.class);
        proposalDevelopmentDocument.setProposalStateTypeCode(proposalStateService.getProposalStateTypeCode(proposalDevelopmentDocument));
        
        DocumentService documentService = KNSServiceLocator.getDocumentService();
        documentService.saveDocument(proposalDevelopmentDocument);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Submit a proposal to Grants.gov.
     * @param proposalDevelopmentDocument
     * @throws Exception
     */
    private void submitS2sApplication(ProposalDevelopmentDocument proposalDevelopmentDocument) throws Exception{
        S2SService s2sService = ((S2SService) KraServiceLocator.getService(S2SService.class));
        s2sService.submitApplication(proposalDevelopmentDocument.getS2sOpportunity().getProposalNumber());
    }
        
    /**
     * Create the submission history for a proposal.
     * @param proposalDevelopmentDocument
     * @throws Exception
     */
    private void createSubmissionHistory(ProposalDevelopmentDocument proposalDevelopmentDocument)throws Exception{
        proposalDevelopmentDocument.refreshReferenceObject("s2sAppSubmission");
        GlobalVariables.getMessageList().add(Constants.GRANTS_GOV_SUBMISSION_SUCCESSFUL_MESSAGE);
        S2sSubmissionHistory s2sSubmissionHistory = createSubmissionHistory(proposalDevelopmentDocument.getProposalNumber(), proposalDevelopmentDocument);
        proposalDevelopmentDocument.getS2sSubmissionHistory().add(s2sSubmissionHistory);
               
        BusinessObjectService businessObjectService = KraServiceLocator.getService(BusinessObjectService.class);
        
        Map queryPDD = new HashMap();
        String continuedFrom = proposalDevelopmentDocument.getContinuedFrom(); 
        while (continuedFrom!=null) {
            queryPDD.put("proposalNumber", continuedFrom);
            ProposalDevelopmentDocument pd = (ProposalDevelopmentDocument)businessObjectService.findByPrimaryKey(ProposalDevelopmentDocument.class, queryPDD);
            
            S2sSubmissionHistory newS2sSubmissionHistory = createSubmissionHistory(pd.getProposalNumber(), proposalDevelopmentDocument);
            pd.getS2sSubmissionHistory().add(newS2sSubmissionHistory);
            businessObjectService.save(pd.getS2sSubmissionHistory());
            continuedFrom = pd.getContinuedFrom();
        }
        businessObjectService.save(proposalDevelopmentDocument.getS2sSubmissionHistory());
    }
    
    /**
     * Create a single Submission History entry.
     * @param proposalNumber
     * @param proposalDevelopmentDocument
     * @return
     */
    private S2sSubmissionHistory createSubmissionHistory(String proposalNumber, ProposalDevelopmentDocument proposalDevelopmentDocument) {
        S2sSubmissionHistory s2sSubmissionHistory = new S2sSubmissionHistory();
        s2sSubmissionHistory.setProposalNumber(proposalNumber);
        s2sSubmissionHistory.setProposalNumberOrig(proposalDevelopmentDocument.getProposalNumber());
        s2sSubmissionHistory.setOriginalProposalId(proposalDevelopmentDocument.getContinuedFrom());
        List<S2sAppSubmission> submissions = proposalDevelopmentDocument.getS2sAppSubmission();
        s2sSubmissionHistory.setFederalIdentifier(submissions.get(submissions.size()-1).getGgTrackingId());
        if (proposalDevelopmentDocument.getS2sOpportunity() != null) {
            s2sSubmissionHistory.setS2sRevisionTypeCode(proposalDevelopmentDocument.getS2sOpportunity().getRevisionCode());
            s2sSubmissionHistory.setS2sSubmissionTypeCode(proposalDevelopmentDocument.getS2sOpportunity().getS2sSubmissionTypeCode());
        }            
        s2sSubmissionHistory.setSubmittedBy(GlobalVariables.getUserSession().getUniversalUser().getPersonUniversalIdentifier());
        s2sSubmissionHistory.setSubmissionTime(submissions.get(submissions.size()-1).getReceivedDate());
        return s2sSubmissionHistory;
    }
    
    /**
     * 
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    private StrutsConfirmation buildSubmitToGrantsGovWithWarningsQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return buildParameterizedConfirmationQuestion(mapping, form, request, response, CONFIRM_SUBMISSION_WITH_WARNINGS_KEY, KeyConstants.QUESTION_SUMBMIT_OPPORTUNITY_WITH_WARNINGS_CONFIRMATION);
    }

    
    
    /**
     * 
     * This method is called to print forms
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward printSponsorForms(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument proposalDevelopmentDocument = (ProposalDevelopmentDocument)proposalDevelopmentForm.getDocument();
        ActionForward actionForward = mapping.findForward(MAPPING_BASIC);
        String proposalNumber = proposalDevelopmentDocument.getProposalNumber();
        List<SponsorFormTemplate> printFormTemplates = new ArrayList<SponsorFormTemplate>();  //proposalDevelopmentDocument.getSponsorForm(0).getSponsorFormTemplates();
        
        List<SponsorFormTemplate> sponsorFormTemplates = proposalDevelopmentDocument.getSponsorFormTemplates();
        for(SponsorFormTemplate sponsorFormTemplate : sponsorFormTemplates) {
            if(sponsorFormTemplate.getSelectToPrint()) {
                printFormTemplates.add(sponsorFormTemplate);
            }
        }
        if(!printFormTemplates.isEmpty()) {
            String contentType = Constants.PDF_REPORT_CONTENT_TYPE;
            String ReportName = proposalNumber.concat("_" + proposalDevelopmentDocument.getSponsorCode()).concat(Constants.PDF_FILE_EXTENSION);
            streamToResponse(printFormTemplates, proposalNumber, contentType, ReportName, response);
            actionForward = null;
        }
        return actionForward;
    }

    public void streamToResponse(List<SponsorFormTemplate> printFormTemplates, String proposalNumber, String contentType, String ReportName, HttpServletResponse response) throws Exception{
        
        
        byte[] sftByteStream = KraServiceLocator.getService(PrintService.class).printProposalSponsorForms(proposalNumber, printFormTemplates);
        
        if(sftByteStream == null) {
            return;
        }
        ByteArrayOutputStream baos = null;
        try{
            baos = new ByteArrayOutputStream(sftByteStream.length);
            baos.write(sftByteStream);
            
            WebUtils.saveMimeOutputStreamAsFile(response, contentType, baos, ReportName);
            
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

    
    /**
     * 
     * This method is called to print forms
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward printForms(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument proposalDevelopmentDocument = (ProposalDevelopmentDocument)proposalDevelopmentForm.getDocument();
        super.save(mapping, form, request, response);
        boolean grantsGovErrorExists = false;
        boolean errorExists = false;
        boolean warningExists = false;
        AttachmentDataSource attachmentDataSource = KraServiceLocator.getService(S2SService.class).printForm(proposalDevelopmentDocument);
        if(attachmentDataSource==null || attachmentDataSource.getContent()==null){
            for (Iterator iter = GlobalVariables.getAuditErrorMap().keySet().iterator(); iter.hasNext();){     
                AuditCluster auditCluster = (AuditCluster)GlobalVariables.getAuditErrorMap().get(iter.next());
                if(StringUtils.equalsIgnoreCase(auditCluster.getCategory(),Constants.AUDIT_ERRORS)){
                    errorExists=true;
                    break;
                }
                if(StringUtils.equalsIgnoreCase(auditCluster.getCategory(),Constants.GRANTSGOV_ERRORS)){
                    grantsGovErrorExists = true;
                    break;
                }
                if(StringUtils.equalsIgnoreCase(auditCluster.getCategory(),Constants.AUDIT_WARNINGS)){
                    warningExists = true;
                }
            }
            if(grantsGovErrorExists){
                GlobalVariables.getErrorMap().putError("document.noKey", KeyConstants.VALIDATTION_ERRORS_BEFORE_GRANTS_GOV_SUBMISSION);
                proposalDevelopmentForm.setAuditActivated(true);
                return mapping.findForward(Constants.MAPPING_PROPOSAL_ACTIONS);
            }
            return mapping.findForward(Constants.MAPPING_BASIC);
        }
        ByteArrayOutputStream baos = null;
        try{
            baos = new ByteArrayOutputStream(attachmentDataSource.getContent().length);
            baos.write(attachmentDataSource.getContent());
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
        return null;
    }
    
    private KualiRuleService getKualiRuleService() {
        return getService(KualiRuleService.class);
    }
    
    
    /**
     * 
     * This method is for audit rule to forward to the page that the audit error fix is clicked.
     * This is an example for budget audit error forward from proposal to budget page.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward personnel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String forward = getForwardToBudgetUrl(form);
        // TODO : what if forward is null
        forward = StringUtils.replace(forward, "budgetSummary.do?", "budgetPersonnel.do?auditActivated=true&");
        
        return new ActionForward(forward, true);
    }
    
    /**
     * 
     * This method gets called upon clicking of resubmit(replace sponsor) button on Proposal Actions page.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward resubmit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("resubmit");
    }

    /**
     * 
     * This method is for budget expense rule when 'fix' is clicked.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward budgetExpenses(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String forward = getForwardToBudgetUrl(form);
        String methodToCallAttribute = (String)request.getAttribute("methodToCallAttribute");
        String viewBudgetPeriodParam = null;
        if (StringUtils.isNotBlank(methodToCallAttribute)) {
            int idx = methodToCallAttribute.indexOf("&viewBudgetPeriod=");
            if (idx > 0) {
                viewBudgetPeriodParam = "viewBudgetPeriod=" + methodToCallAttribute.substring(methodToCallAttribute.indexOf("=", idx)+1,methodToCallAttribute.indexOf(".", idx))+"&";
            }
        }
        forward = StringUtils.replace(forward, "budgetSummary.do?", "budgetExpenses.do?auditActivated=true&");
        if (viewBudgetPeriodParam != null) {
            forward = StringUtils.replace(forward, "budgetExpenses.do?", "budgetExpenses.do?"+viewBudgetPeriodParam); 
        }
        return new ActionForward(forward, true);
    }
    
    /**
     * 
     * This method is to forward to budget summary page for audit errors.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String forward = getForwardToBudgetUrl(form);
        forward = StringUtils.replace(forward, "budgetSummary.do?", "budgetSummary.do?auditActivated=true&");
        
        return new ActionForward(forward, true);
    }
    
    public ActionForward saveProposalActions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return save(mapping, form, request, response);
    }

    /**
     * 
     * This is a helper method to set up the forward to budget url for budget audit error.
     * @param form
     * @return
     */
    private String getForwardToBudgetUrl(ActionForm form) {
        ProposalDevelopmentForm pdForm = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument pdDoc = pdForm.getProposalDevelopmentDocument();
        BudgetDocument budgetDocument = null;
        String forward = null;
        try {
            for (BudgetVersionOverview budgetVersion: pdDoc.getBudgetVersionOverviews()) {
                if (budgetVersion.isFinalVersionFlag()) {
                    DocumentService documentService = KraServiceLocator.getService(DocumentService.class);
                    budgetDocument = (BudgetDocument) documentService.getByDocumentHeaderId(budgetVersion.getDocumentNumber());
                }
            }
            Long routeHeaderId = budgetDocument.getDocumentHeader().getWorkflowDocument().getRouteHeaderId();
            forward = buildForwardUrl(routeHeaderId);
        } catch (Exception e) {
            LOG.info("forward to budgetsummary "+e.getStackTrace());
            //TODO what is the forward here
        }
        return forward;

    }

}
    
    
    
    
    
