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
package org.kuali.kra.award.web.struts.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.kra.award.AwardForm;
import org.kuali.kra.award.AwardLockService;
import org.kuali.kra.award.AwardNumberService;
import org.kuali.kra.award.AwardTemplateSyncService;
import org.kuali.kra.award.awardhierarchy.AwardHierarchy;
import org.kuali.kra.award.awardhierarchy.AwardHierarchyService;
import org.kuali.kra.award.awardhierarchy.AwardHierarchyBean;
import org.kuali.kra.award.document.AwardDocument;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.award.home.AwardComment;
import org.kuali.kra.award.home.approvedsubawards.AwardApprovedSubaward;
import org.kuali.kra.award.paymentreports.ReportClass;
import org.kuali.kra.award.paymentreports.awardreports.AwardReportTerm;
import org.kuali.kra.award.paymentreports.awardreports.AwardReportTermRecipient;
import org.kuali.kra.award.paymentreports.closeout.CloseoutReportTypeValuesFinder;
import org.kuali.kra.bo.versioning.VersionStatus;
import org.kuali.kra.budget.web.struts.action.BudgetParentActionBase;
import org.kuali.kra.infrastructure.AwardRoleConstants;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.rice.shim.UniversalUser;
import org.kuali.kra.service.AwardDirectFandADistributionService;
import org.kuali.kra.service.AwardReportsService;
import org.kuali.kra.service.AwardSponsorTermService;
import org.kuali.kra.service.KraAuthorizationService;
import org.kuali.kra.service.KraWorkflowService;
import org.kuali.kra.service.VersionHistoryService;
import org.kuali.kra.timeandmoney.document.TimeAndMoneyDocument;
import org.kuali.kra.web.struts.action.AuditActionHelper;
import org.kuali.rice.kew.util.KEWConstants;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.rule.event.KualiDocumentEvent;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.DocumentService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.service.KualiRuleService;
import org.kuali.rice.kns.service.PessimisticLockService;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.KNSConstants;
import org.kuali.rice.kns.util.MessageList;
import org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase;
import org.kuali.rice.kns.web.ui.KeyLabelPair;

/**
 * 
 * This class represents base Action class for all the Award pages.
 */
public class AwardAction extends BudgetParentActionBase {
    protected static final String AWARD_ID_PARAMETER_NAME = "awardId";
    private static final String AWARD_NUMBER_SERVICE = "awardNumberService";
    
    /**
     * @see org.kuali.core.web.struts.action.KualiDocumentActionBase#docHandler(
     * org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, 
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward docHandler(ActionMapping mapping, ActionForm form
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        AwardForm awardForm = (AwardForm) form;        
        ActionForward forward = handleDocument(mapping, form, request, response, awardForm);
        awardForm.initializeFormOrDocumentBasedOnCommand();
        
        return forward;
    }
    
    /**
     * @see org.kuali.kra.web.struts.action.KraTransactionalDocumentActionBase#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = super.execute(mapping, form, request, response);
                
        new AuditActionHelper().auditConditionally((AwardForm)form);
        
        return actionForward;
    }

    /**
     * This method populates the AwardHierarchy data
     * @param form
     */
    protected void populateAwardHierarchy(ActionForm form) {
        AwardForm awardForm = (AwardForm)form;
        List<String> order = new ArrayList<String>();
        AwardHierarchyBean helperBean = awardForm.getAwardHierarchyBean();
        AwardHierarchy rootNode = helperBean.getRootNode();
        Map<String, AwardHierarchy> awardHierarchyNodes = helperBean.getAwardHierarchy(rootNode, order);
        awardForm.setAwardHierarchyNodes(awardHierarchyNodes);
        awardForm.setRootAwardNumber(rootNode.getRootAwardNumber());
        awardForm.setOrder(order);
    }
    
    /**
     * @see org.kuali.kra.web.struts.action.KraTransactionalDocumentActionBase#save(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO: JF Are all of these saves in a single transaction?
        ActionForward forward = mapping.findForward(Constants.MAPPING_BASIC);
        AwardForm awardForm = (AwardForm) form;
        
        Award award = awardForm.getAwardDocument().getAward();
        checkAwardNumber(award);
        
        if (isValidSave(awardForm)) {
            boolean savingNewAward = award.getAwardId() == null;
            
            forward = super.save(mapping, form, request, response);
            if (awardForm.getMethodToCall().equals("save") && awardForm.isAuditActivated()) {
                forward = mapping.findForward(Constants.MAPPING_AWARD_ACTIONS_PAGE);
            }
            
            boolean newAwardSaved = savingNewAward && award.getAwardId() != null; 
            if(newAwardSaved) {
                getVersionHistoryService().createVersionHistory(award, VersionStatus.PENDING, GlobalVariables.getUserSession().getPrincipalName());
            }

            AwardHierarchyBean bean = awardForm.getAwardHierarchyBean();
            if(bean.saveHierarchyChanges()) {
                List<String> order = new ArrayList<String>();
                awardForm.setAwardHierarchyNodes(bean.getAwardHierarchy(bean.getRootNode().getAwardNumber(), order));
                awardForm.setOrder(order);
            }
        }

        return forward;
    }
    
    /**
     * This method returns the award associated with the AwardDocument on the AwardForm
     * @return
     */
    protected Award getAward(ActionForm form) {
        return getAwardDocument(form).getAward(); 
    }
    
    /**
     * This method returns the AwardDocument
     * 
     * @param form
     * @return
     */
    protected AwardDocument getAwardDocument(ActionForm form) {
        return ((AwardForm) form).getAwardDocument();
    }
    
    /**
     * This method sets an award number on an award if the award number hasn't been initialized yet.
     * @param award
     */
    private void checkAwardNumber(Award award) {
        if (Award.DEFAULT_AWARD_NUMBER.equals(award.getAwardNumber())) {
            AwardNumberService awardNumberService = getAwardNumberService();
            String awardNumber = awardNumberService.getNextAwardNumber();
            award.setAwardNumber(awardNumber);
        }
        if (Award.DEFAULT_AWARD_NUMBER.equals(award.getAwardAmountInfos().get(0).getAwardNumber())) {
            award.getAwardAmountInfos().get(0).setAwardNumber(award.getAwardNumber());
        }
        for(AwardApprovedSubaward approvedSubaward : award.getAwardApprovedSubawards()) {
            if(Award.DEFAULT_AWARD_NUMBER.equals(approvedSubaward.getAwardNumber())) {
                approvedSubaward.setAwardNumber(award.getAwardNumber());
            }
        }
        for(AwardComment comment : award.getAwardComments()) {
            comment.setAward(award);
        }
    }
    
    /**
     * 
     * This method is a helper method to retrieve AwardNumberService.
     * @return
     */
    protected AwardNumberService getAwardNumberService() {
        return KraServiceLocator.getService(AwardNumberService.class);
    }
    
    /**
     * @see org.kuali.kra.web.struts.action.KraTransactionalDocumentActionBase#initialDocumentSave(org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase)
     *
     * TODO JF: Handle initial save
     * 
     * One of these conditions exist when this method is called:
       1) This is a new award, created from the "Create Award" portal action. A new root node needs to be created
          a) prevAwardNumber and prevRootAwardNumber are null
          b) awardHierarchyBean.rootNodes.size() == 0
       2) This is a new award created from a hierarchy action. The node for this award should exist on the hierarchy bean
          a) prevAwardNumber and prevRootAwardNumber are ?
          b) awardHierarchyBean.rootNodes.size() == ?
     */
    @Override
    protected void initialDocumentSave(KualiDocumentFormBase form) throws Exception {
        AwardForm awardForm = (AwardForm) form;
        
        createInitialAwardUsers(awardForm.getAwardDocument().getAward());
        populateStaticCloseoutReports(awardForm);
        if(!awardForm.getAwardDocument().isDocumentSaveAfterVersioning()) {
            createDefaultAwardHierarchy(awardForm);            
        }
    }

    // TODO JF: Integrate behavior into bean; i.e. bean should create the new node
    protected void createDefaultAwardHierarchy(AwardForm awardForm) {
        String awardNumber = awardForm.getAwardDocument().getAward().getAwardNumber();
        AwardHierarchy newNode = new AwardHierarchy();
        newNode.setAwardNumber(awardNumber);
        newNode.setParentAwardNumber(determineParentAwardNumber(awardForm));
        newNode.setRootAwardNumber(determineRootAwardNumber(awardForm));
        newNode.setOriginatingAwardNumber(awardNumber);
        if(newNode.isRootNode()) {
            awardForm.getAwardHierarchyBean().setRootNode(newNode);
        }
    }

    /**
     * Create the original set of Award Users for a new Award Document.
     * The creator the award is assigned to the AWARD_MODIFIER role.
     *
     * @param doc
     */
    protected void createInitialAwardUsers(Award award) {
        UniversalUser user = new UniversalUser(GlobalVariables.getUserSession().getPerson());
        String username = user.getPersonUserIdentifier();
        KraAuthorizationService kraAuthService = KraServiceLocator.getService(KraAuthorizationService.class);
        kraAuthService.addRole(username, AwardRoleConstants.AWARD_MODIFIER.getAwardRole(), award);
    }

    /**
     *
     * This method populates the initial static AwardCloseout reports upon the creation of an Award.
     *
     * @param form
     */
    protected void populateStaticCloseoutReports(AwardForm form){
        CloseoutReportTypeValuesFinder closeoutReportTypeValuesFinder = new CloseoutReportTypeValuesFinder();

        form.getAwardCloseoutBean().addAwardCloseoutStaticItems(closeoutReportTypeValuesFinder.getKeyValues());
    }

    protected AwardHierarchyService getAwardHierarchyService(){
        return KraServiceLocator.getService(AwardHierarchyService.class);
    }

    /**
     * Can the Award be saved?  This method is normally overridden by
     * a subclass in order to invoke business rules to verify that the
     * Award can be saved.
     * @param awardForm the Award Form
     * @return true if the award can be saved; otherwise false
     */
    protected boolean isValidSave(AwardForm awardForm) {
        return true;
    }

    /**
     * Use the Kuali Rule Service to apply the rules for the given event.
     * @param event the event to process
     * @return true if success; false if there was a validation error
     */
    protected final boolean applyRules(KualiDocumentEvent event) {
        return getKualiRuleService().applyRules(event);
    }

    /**
     *
     * This method builds the string for the ActionForward
     * @param forwardPath
     * @param docIdRequestParameter
     * @return
     */
    public String buildForwardStringForActionListCommand(String forwardPath, String docIdRequestParameter){
        StringBuilder sb = new StringBuilder();
        sb.append(forwardPath);
        sb.append("?");
        sb.append(KNSConstants.PARAMETER_DOC_ID);
        sb.append("=");
        sb.append(docIdRequestParameter);
        return sb.toString();
    }


    /**
     *
     * This method gets called upon navigation to Awards tab.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward home(ActionMapping mapping, ActionForm form
            , HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward(Constants.MAPPING_AWARD_HOME_PAGE);
    }

    /**
     *
     * This method gets called upon navigation to Contacts tab.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward contacts(ActionMapping mapping, ActionForm form
            , HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward(Constants.MAPPING_AWARD_CONTACTS_PAGE);
    }

    /**
     *
     * This method gets called upon navigation to Commitments tab.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward commitments(ActionMapping mapping, ActionForm form
            , HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward(Constants.MAPPING_AWARD_COMMITMENTS_PAGE);
    }

    /**
     *
     * This method gets called upon navigation to Time & Money tab.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward timeAndMoney(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
        AwardForm awardForm = (AwardForm) form;
        DocumentService documentService = KraServiceLocator.getService(DocumentService.class);
        KraWorkflowService kraWorkflowService = KraServiceLocator.getService(KraWorkflowService.class);
        boolean createNewTimeAndMoneyDocument = Boolean.TRUE;

        populateAwardHierarchy(form);

        Award award = awardForm.getAwardDocument().getAward();
        if(isNewAward(awardForm) && !(award.getBeginDate() == null)){
            AwardDirectFandADistributionService awardDirectFandADistributionService = getAwardDirectFandADistributionService();
            awardForm.getAwardDocument().getAward().setAwardDirectFandADistributions
                                (awardDirectFandADistributionService.
                                        generateDefaultAwardDirectFandADistributionPeriods(awardForm.getAwardDocument().getAward()));
        }

        Map<String, Object> fieldValues = new HashMap<String, Object>();
        String rootAwardNumber = awardForm.getAwardHierarchyNodes().get(award.getAwardNumber()).getRootAwardNumber();
        fieldValues.put("rootAwardNumber", rootAwardNumber);
        //fieldValues.put("sequenceNumber", award.getSequenceNumber());
        BusinessObjectService businessObjectService =  KraServiceLocator.getService(BusinessObjectService.class);

        List<TimeAndMoneyDocument> timeAndMoneyDocuments = (List<TimeAndMoneyDocument>)businessObjectService.findMatching(TimeAndMoneyDocument.class, fieldValues);
        TimeAndMoneyDocument timeAndMoneyDocument = null;

        for(TimeAndMoneyDocument t : timeAndMoneyDocuments){
            timeAndMoneyDocument = (TimeAndMoneyDocument) documentService.getByDocumentHeaderId(t.getDocumentNumber());
            timeAndMoneyDocument.setAwardNumber(award.getAwardNumber());
            timeAndMoneyDocument.setAward(award);
            if(!kraWorkflowService.isInWorkflow(timeAndMoneyDocument)){
                createNewTimeAndMoneyDocument = Boolean.FALSE;
                break;
            }
        }

        if(createNewTimeAndMoneyDocument){
            timeAndMoneyDocument = (TimeAndMoneyDocument) documentService.getNewDocument(TimeAndMoneyDocument.class);
            timeAndMoneyDocument.getDocumentHeader().setDocumentDescription("timeandmoney document");
            timeAndMoneyDocument.setRootAwardNumber(rootAwardNumber);
            timeAndMoneyDocument.setAwardNumber(award.getAwardNumber());
            timeAndMoneyDocument.setAward(award);
        }

        documentService.saveDocument(timeAndMoneyDocument);

        Long routeHeaderId = timeAndMoneyDocument.getDocumentHeader().getWorkflowDocument().getRouteHeaderId();

        String forward = buildForwardUrl(routeHeaderId);
        return new ActionForward(forward, true);

    }

    /**
     * This method tests if the award is new by checking the size of AwardDirectFandADistributions on the Award.
     * @param awardForm
     * @return
     */
    public boolean isNewAward(AwardForm awardForm) {
        return awardForm.getAwardDocument().getAward().getAwardDirectFandADistributions().size() == 0;
    }

    /**
     *
     * This method is a helper method to retrieve AwardSponsorTermService.
     * @return
     */
    protected AwardDirectFandADistributionService getAwardDirectFandADistributionService() {
        return KraServiceLocator.getService(AwardDirectFandADistributionService.class);
    }

    /**
     *
     * This method gets called upon navigation to Payment, Reports and Terms tab.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @SuppressWarnings("all")
    public ActionForward paymentReportsAndTerms(ActionMapping mapping, ActionForm form
            , HttpServletRequest request, HttpServletResponse response) {
        AwardForm awardForm = (AwardForm) form;

        setReportsAndTermsOnAwardForm(awardForm);

        return mapping.findForward(Constants.MAPPING_AWARD_PAYMENT_REPORTS_AND_TERMS_PAGE);
    }

    @SuppressWarnings("unchecked")
    protected void setReportsAndTermsOnAwardForm(AwardForm awardForm) {
        AwardSponsorTermService awardSponsorTermService = getAwardSponsorTermService();
        List<KeyLabelPair> sponsorTermTypes = awardSponsorTermService.retrieveSponsorTermTypesToAwardFormForPanelHeaderDisplay();
        awardForm.getSponsorTermFormHelper().setSponsorTermTypes(sponsorTermTypes);
        awardForm.getSponsorTermFormHelper().setNewSponsorTerms(awardSponsorTermService.getEmptyNewSponsorTerms(sponsorTermTypes));

        AwardReportsService awardReportsService = KraServiceLocator.getService(AwardReportsService.class);
        Map<String,Object> initializedObjects = awardReportsService.initializeObjectsForReportsAndPayments(
                                                    awardForm.getAwardDocument().getAward());
        awardForm.setReportClasses((List<KeyLabelPair>) initializedObjects.get(
                                      Constants.REPORT_CLASSES_KEY_FOR_INITIALIZE_OBJECTS));
        awardForm.getAwardReportsBean().setNewAwardReportTerms((List<AwardReportTerm>) initializedObjects.get(
                                          Constants.NEW_AWARD_REPORT_TERMS_LIST_KEY_FOR_INITIALIZE_OBJECTS));
        awardForm.getAwardReportsBean().setNewAwardReportTermRecipients((List<AwardReportTermRecipient>) initializedObjects.get(
                                                    Constants.NEW_AWARD_REPORT_TERM_RECIPIENTS_LIST_KEY_FOR_INITIALIZE_OBJECTS));
        awardForm.setReportClassForPaymentsAndInvoices((ReportClass) initializedObjects.get(
                                                        Constants.REPORT_CLASS_FOR_PAYMENTS_AND_INVOICES_PANEL));

    }

    /**
     *
     * This method is a helper method to retrieve AwardSponsorTermService.
     * @return
     */
    protected AwardSponsorTermService getAwardSponsorTermService() {
        return KraServiceLocator.getService(AwardSponsorTermService.class);
    }

    /**
     *
     * This method gets called upon navigation to Special Review tab.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward specialReview(ActionMapping mapping, ActionForm form
            , HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward(Constants.MAPPING_AWARD_SPECIAL_REVIEW_PAGE);
    }

    /**
     *
     * This method gets called upon navigation to Special Review tab.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward customData(ActionMapping mapping, ActionForm form
            , HttpServletRequest request, HttpServletResponse response) {
        AwardForm awardForm = (AwardForm) form;
        return awardForm.getCustomDataHelper().awardCustomData(mapping, form, request, response);
    }

    /**
     *
     * This method gets called upon navigation to Custom Data tab.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward questions(ActionMapping mapping, ActionForm form
            , HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward(Constants.MAPPING_AWARD_QUESTIONS_PAGE);
    }

    /**
     *
     * This method gets called upon navigation to Questions tab.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward permissions(ActionMapping mapping, ActionForm form
            , HttpServletRequest request, HttpServletResponse response) {
        ((AwardForm)form).getPermissionsHelper().prepareView();
        return mapping.findForward(Constants.MAPPING_AWARD_PERMISSIONS_PAGE);
    }

    /**
     *
     * This method gets called upon navigation to Permissions tab.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward notesAndAttachments(ActionMapping mapping, ActionForm form
            , HttpServletRequest request, HttpServletResponse response) {
        AwardForm awardForm = (AwardForm) form;
        awardForm.getAwardCommentBean().setAwardCommentScreenDisplayTypesOnForm();
        return mapping.findForward(Constants.MAPPING_AWARD_NOTES_AND_ATTACHMENTS_PAGE);
    }

    /**
     *
     * This method gets called upon navigation to Award Actions tab.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward awardActions(ActionMapping mapping, ActionForm form
            , HttpServletRequest request, HttpServletResponse response) {

        populateAwardHierarchy(form);

        return mapping.findForward(Constants.MAPPING_AWARD_ACTIONS_PAGE);
    }


    /**
     *
     * This method gets called upon navigation to Awards tab.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward budgetVersions(ActionMapping mapping, ActionForm form
            , HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward(Constants.MAPPING_AWARD_BUDGET_VERSIONS_PAGE);
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @param awardForm
     * @return
     * @throws Exception
     */
    ActionForward handleDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                  HttpServletResponse response, AwardForm awardForm) throws Exception {
        String command = awardForm.getCommand();
        ActionForward forward;
        if (KEWConstants.ACTIONLIST_INLINE_COMMAND.equals(command)) {
            String docIdRequestParameter = request.getParameter(KNSConstants.PARAMETER_DOC_ID);
            Document retrievedDocument = getDocumentService().getByDocumentHeaderId(docIdRequestParameter);
            awardForm.setDocument(retrievedDocument);
            request.setAttribute(KNSConstants.PARAMETER_DOC_ID, docIdRequestParameter);
            ActionForward baseForward = mapping.findForward(Constants.MAPPING_COPY_PROPOSAL_PAGE);
            forward = new ActionForward(buildForwardStringForActionListCommand(
                    baseForward.getPath(),docIdRequestParameter));
        } else {
        forward = super.docHandler(mapping, form, request, response);
        }
        awardForm.getAwardDocument().populateCustomAttributes();
        return forward;
    }

    /**
     *
     * @return
     */
    @Override
    protected DocumentService getDocumentService() {
        return KNSServiceLocator.getDocumentService();
    }

    /**
     *
     * This method is a helper method to retrieve KualiRuleService.
     * @return
     */
    protected KualiRuleService getKualiRuleService() {
        return KraServiceLocator.getService(KualiRuleService.class);
    }

    public ActionForward syncAwardTemplate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception{
        AwardTemplateSyncService awardTemplateSyncService = KraServiceLocator.getService(AwardTemplateSyncService.class);
        AwardForm awardForm = (AwardForm)form;
        AwardDocument awardDocument = awardForm.getAwardDocument();
        String syncPropertyName = getSyncPropertyName(request);

//        AwardTemplateSyncEvent awardTemplateSyncEvent =
//            new AwardTemplateSyncEvent("Award Sync","document.award.awardTemplate",awardForm.getDocument());
//        boolean success = true;
//        try{
//            awardForm.getAwardDocument().validateBusinessRules(awardTemplateSyncEvent);
//        }catch(ValidationException vEx){
//            success = false;
//        }
        boolean success = (syncPropertyName!=null?
                        awardTemplateSyncService.syncToAward(awardDocument, syncPropertyName):
                            awardTemplateSyncService.syncToAward(awardDocument));

        return mapping.findForward(Constants.MAPPING_AWARD_BASIC);
    }

    /**
     * Parses the method to call attribute to pick off the property name of award object
     * which should have a sync action performed on it.
     *
     * @param request
     * @return
     */
    protected String getSyncPropertyName(HttpServletRequest request) {
        String syncPropertyName = null;
        String parameterName = (String) request.getAttribute(KNSConstants.METHOD_TO_CALL_ATTRIBUTE);
        if (StringUtils.isNotBlank(parameterName) && parameterName.indexOf(".syncPropertyName")!=-1) {
            syncPropertyName = StringUtils.substringBetween(parameterName, ".syncPropertyName", ".anchor");
        }
        return syncPropertyName;
    }

    @Override
    protected PessimisticLockService getPessimisticLockService() {
        return KraServiceLocator.getService(AwardLockService.class);
    }

    /**
     * @return
     */
    protected VersionHistoryService getVersionHistoryService() {
        return KraServiceLocator.getService(VersionHistoryService.class);
    }

    private String determineRootAwardNumber(AwardForm awardForm) {
        String prevRootAwardNumber = awardForm.getPrevRootAwardNumber();
        return prevRootAwardNumber != null ? prevRootAwardNumber : awardForm.getAwardDocument().getAward().getAwardNumber();
    }

    private String determineParentAwardNumber(AwardForm awardForm) {
        String prevAwardNumber = awardForm.getPrevAwardNumber();
        return prevAwardNumber != null ? prevAwardNumber : Constants.AWARD_HIERARCHY_DEFAULT_PARENT_OF_ROOT;
    }
}
