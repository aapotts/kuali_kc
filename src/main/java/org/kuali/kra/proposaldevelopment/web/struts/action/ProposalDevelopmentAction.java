/*
 * Copyright 2006-2009 The Kuali Foundation
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
package org.kuali.kra.proposaldevelopment.web.struts.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.kra.bo.CustomAttributeDocValue;
import org.kuali.kra.bo.CustomAttributeDocument;
import org.kuali.kra.bo.DocumentNextvalue;
import org.kuali.kra.budget.web.struts.action.BudgetParentActionBase;
import org.kuali.kra.budget.web.struts.action.BudgetTDCValidator;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.RoleConstants;
import org.kuali.kra.proposaldevelopment.bo.AttachmentDataSource;
import org.kuali.kra.proposaldevelopment.bo.Narrative;
import org.kuali.kra.proposaldevelopment.bo.ProposalColumnsToAlter;
import org.kuali.kra.proposaldevelopment.bo.ProposalCopyCriteria;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.proposaldevelopment.hierarchy.ProposalHierarcyActionHelper;
import org.kuali.kra.proposaldevelopment.service.KeyPersonnelService;
import org.kuali.kra.proposaldevelopment.service.NarrativeService;
import org.kuali.kra.proposaldevelopment.service.ProposalDevelopmentService;
import org.kuali.kra.proposaldevelopment.service.ProposalPersonBiographyService;
import org.kuali.kra.proposaldevelopment.service.ProposalRoleTemplateService;
import org.kuali.kra.proposaldevelopment.web.struts.form.ProposalDevelopmentForm;
import org.kuali.kra.s2s.bo.S2sOppForms;
import org.kuali.kra.s2s.service.PrintService;
import org.kuali.kra.s2s.service.S2SService;
import org.kuali.kra.service.KraAuthorizationService;
import org.kuali.kra.web.struts.action.AuditActionHelper;
import org.kuali.rice.kew.exception.WorkflowException;
import org.kuali.rice.kew.util.KEWConstants;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kns.bo.Note;
import org.kuali.rice.kns.bo.PersistableBusinessObject;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.util.AuditCluster;
import org.kuali.rice.kns.util.AuditError;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.KNSConstants;
import org.kuali.rice.kns.util.KNSPropertyConstants;
import org.kuali.rice.kns.util.ObjectUtils;
import org.kuali.rice.kns.util.WebUtils;
import org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase;
import org.kuali.rice.kns.web.struts.form.KualiForm;

public class ProposalDevelopmentAction extends BudgetParentActionBase {
    private static final String PROPOSAL_NARRATIVE_TYPE_GROUP = "proposalNarrativeTypeGroup";
    private static final String DELIVERY_INFO_DISPLAY_INDICATOR = "deliveryInfoDisplayIndicator";
    private static final Log LOG = LogFactory.getLog(ProposalDevelopmentAction.class);
    private ProposalHierarcyActionHelper hierarchyHelper;
    
    /**
     * @see org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase#docHandler(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward docHandler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = null;
        
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        String command = proposalDevelopmentForm.getCommand();
        
        if (KEWConstants.ACTIONLIST_INLINE_COMMAND.equals(command)) {
            loadDocumentInForm(request, proposalDevelopmentForm);
            forward = mapping.findForward(Constants.MAPPING_COPY_PROPOSAL_PAGE);
            forward = new ActionForward(forward.getPath()+ "?" + KNSConstants.PARAMETER_DOC_ID + "=" + request.getParameter(KNSConstants.PARAMETER_DOC_ID));  
        } else {
             forward = super.docHandler(mapping, form, request, response);
        }

        if (KEWConstants.INITIATE_COMMAND.equals(proposalDevelopmentForm.getCommand())) {
            proposalDevelopmentForm.getDocument().initialize();
        }else{
            proposalDevelopmentForm.initialize();
        }
        
        return forward;
    }

    protected ProposalHierarcyActionHelper getHierarchyHelper() {
        if (hierarchyHelper == null) {
            hierarchyHelper = new ProposalHierarcyActionHelper();
        }
        return hierarchyHelper;
    }
    

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        ActionForward actionForward = super.execute(mapping, form, request, response);
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument document = proposalDevelopmentForm.getDocument();
         String keywordPanelDisplay = this.getParameterService().getParameterValue(
                    ProposalDevelopmentDocument.class, Constants.KEYWORD_PANEL_DISPLAY);        
            request.getSession().setAttribute(Constants.KEYWORD_PANEL_DISPLAY, keywordPanelDisplay);
            // TODO: not sure it's should be here - for audit error display.
            // ES: Still do not know how exactly *how* this should be done, 
            // but I added a check to only call the auditConditionally when the audit error 
            // map is empty - otherwise the display during a submit 
            //check to see if the audit errors are filled out.  This happens on a submit
            //that fails.
            
            if( GlobalVariables.getAuditErrorMap().isEmpty())
                new AuditActionHelper().auditConditionally(proposalDevelopmentForm);
            proposalDevelopmentForm.setProposalDataOverrideMethodToCalls(this.constructColumnsToAlterLookupMTCs(proposalDevelopmentForm.getDocument().getDevelopmentProposal().getProposalNumber()));
//            if (proposalDevelopmentForm.isAuditActivated()) {
//                if (document != null && 
//                    document.getDevelopmentProposal().getS2sOpportunity() != null ) {
//                    getService(S2SService.class).validateApplication(document);            
//                }
//            }

            //if(isPrincipalInvestigator){
            //}
            
            /*if(proposalDevelopmentForm.getDocument().getSponsorCode()!=null){
                proposalDevelopmentForm.setAdditionalDocInfo1(new KeyLabelPair("datadictionary.Sponsor.attributes.sponsorCode.label",proposalDevelopmentForm.getDocument().getSponsorCode()));
            }
            if(proposalDevelopmentForm.getDocument().getPrincipalInvestigator()!=null){
                proposalDevelopmentForm.setAdditionalDocInfo2(new KeyLabelPair("${Document.DataDictionary.ProposalDevelopmentDocument.attributes.sponsorCode.label}",proposalDevelopmentForm.getDocument().getPrincipalInvestigator().getFullName()));
            }*/
    
            // setup any Proposal Development System Parameters that will be needed
            
            ((ProposalDevelopmentForm)form).getProposalDevelopmentParameters().put(DELIVERY_INFO_DISPLAY_INDICATOR, this.getParameterService().retrieveParameter(Constants.MODULE_NAMESPACE_PROPOSAL_DEVELOPMENT, Constants.PARAMETER_COMPONENT_DOCUMENT, DELIVERY_INFO_DISPLAY_INDICATOR));
            ((ProposalDevelopmentForm)form).getProposalDevelopmentParameters().put(PROPOSAL_NARRATIVE_TYPE_GROUP, this.getParameterService().retrieveParameter(Constants.MODULE_NAMESPACE_PROPOSAL_DEVELOPMENT, Constants.PARAMETER_COMPONENT_DOCUMENT, PROPOSAL_NARRATIVE_TYPE_GROUP));
            
            if(document.getDevelopmentProposal().getS2sOpportunity()!=null && document.getDevelopmentProposal().getS2sOpportunity().getS2sOppForms()!=null){
                Collections.sort(document.getDevelopmentProposal().getS2sOpportunity().getS2sOppForms(),new S2sOppFormsComparator2());
                Collections.sort(document.getDevelopmentProposal().getS2sOpportunity().getS2sOppForms(),new S2sOppFormsComparator1());
            }
         return actionForward;
    }

   /**
     * Do nothing.  Used when the Proposal is in view-only mode.  Instead of saving
     * the proposal when the tab changes, we simply do nothing.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward nullOp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * @see org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase#loadDocument(KualiDocumentFormBase)
     */
    @Override
    protected void loadDocument(KualiDocumentFormBase kualiDocumentFormBase) throws WorkflowException {
        super.loadDocument(kualiDocumentFormBase);
        getKeyPersonnelService().populateDocument(((ProposalDevelopmentForm) kualiDocumentFormBase).getDocument());
    }

    @Override
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // We will need to determine if the proposal is being saved for the first time.

        final ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        final ProposalDevelopmentDocument doc = proposalDevelopmentForm.getDocument();
       
		updateProposalDocument(proposalDevelopmentForm);
        ActionForward forward = super.save(mapping, form, request, response);
        // If validation is turned on, take the user to the proposal actions page (which contains the validation panel, which auto-expands)
        if (proposalDevelopmentForm.isAuditActivated()) {
            forward = mapping.findForward(Constants.MAPPING_PROPOSAL_ACTIONS);
        }
        
        doc.getDevelopmentProposal().updateProposalNumbers();

        proposalDevelopmentForm.setFinalBudgetVersion(getFinalBudgetVersion(doc.getBudgetDocumentVersions()));
        setBudgetStatuses(doc);

        //if not on budget page
        if ("ProposalDevelopmentBudgetVersionsAction".equals(proposalDevelopmentForm.getActionName())) {
            GlobalVariables.getErrorMap().addToErrorPath(KNSConstants.DOCUMENT_PROPERTY_NAME + ".proposal");

            final BudgetTDCValidator tdcValidator = new BudgetTDCValidator(request);
            tdcValidator.validateGeneratingErrorsAndWarnings(doc);
        }

        return forward;
    }

    protected void updateProposalDocument(ProposalDevelopmentForm pdForm) {
        ProposalDevelopmentDocument pdDocument = pdForm.getDocument();
        ProposalDevelopmentDocument updatedDocCopy = getProposalDoc(pdDocument.getDocumentNumber());
        
        //For Budget Lock region, this is the only way in which a Proposal Document might get updated
        if(StringUtils.isNotEmpty(pdForm.getActionName()) && !pdForm.getActionName().equalsIgnoreCase("ProposalDevelopmentBudgetVersionsAction" )) {
            if(updatedDocCopy != null && updatedDocCopy.getVersionNumber() > pdDocument.getVersionNumber()) {
                  //refresh the reference
                pdDocument.setBudgetDocumentVersions(updatedDocCopy.getBudgetDocumentVersions());
                pdDocument.getDevelopmentProposal().setBudgetStatus(updatedDocCopy.getDevelopmentProposal().getBudgetStatus());
                try {
                    fixVersionNumbers(updatedDocCopy, pdDocument, new ArrayList<Object>());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                pdDocument.setVersionNumber(updatedDocCopy.getVersionNumber());
                pdDocument.getDocumentHeader().setVersionNumber(updatedDocCopy.getDocumentHeader().getVersionNumber());
                int noteIndex = 0;
                for(Object note: pdDocument.getDocumentHeader().getBoNotes()) {
                    Note updatedNote = updatedDocCopy.getDocumentHeader().getBoNote(noteIndex);
                    ((Note) note).setVersionNumber(updatedNote.getVersionNumber());
                    noteIndex++;
                }
                for(DocumentNextvalue documentNextValue : pdDocument.getDocumentNextvalues()) {
                    DocumentNextvalue updatedDocumentNextvalue = updatedDocCopy.getDocumentNextvalueBo(documentNextValue.getPropertyName());
                    if(updatedDocumentNextvalue != null) {
                        documentNextValue.setVersionNumber(updatedDocumentNextvalue.getVersionNumber());
                    }
                }
            }
            pdForm.setDocument(pdDocument);
        }
        
    }
    
    private boolean isPropertyGetterMethod(Method method, Method methods[]) {
        if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
            String setterName = method.getName().replaceFirst("get", "set");
            for (Method m : methods) {
                if (m.getName().equals(setterName)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    private void fixVersionNumbers(Object srcObject, Object object, List<Object> list) throws Exception {
        Class[] setterParamTypes = {Long.class};
        if (object != null && object instanceof PersistableBusinessObject) {
            if (list.contains(object)) return;
            list.add(object);
            
            Method getterMethod = object.getClass().getMethod("getVersionNumber");
            if(getterMethod != null) {
                Long currentVersionNumber = null;
                if(srcObject != null) 
                    currentVersionNumber = (Long) getterMethod.invoke(srcObject, new Object[]{});
                else
                    currentVersionNumber = (Long) getterMethod.invoke(object, new Object[]{});
                
                Method setterMethod = object.getClass().getMethod("setVersionNumber", setterParamTypes);
                if(currentVersionNumber != null) {
                    setterMethod.invoke(object, currentVersionNumber);
                }
            }
            
            Method[] methods = object.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (isPropertyGetterMethod(method, methods)) {
                    Object srcValue = null;
                    if(srcObject != null) {
                        srcValue = method.invoke(srcObject);
                    }
                    Object value = method.invoke(object);
                    if (value != null && value instanceof Collection) {
                        Collection c = (Collection) value;
                        Object[] srcC = c.toArray();
                        if(srcValue != null) {
                            srcC = ((Collection) srcValue).toArray();
                        } 
                        
                        Iterator iter = c.iterator();
                        int count = 0;
                        while (iter.hasNext()) {
                            Object srcEntry = null;
                            if(srcC.length > count) 
                                srcEntry = srcC[count];
                            Object entry = iter.next();
                            fixVersionNumbers(srcEntry, entry, list);
                            count++;
                        }
                    } else {
                        fixVersionNumbers(srcValue, value, list);
                    }   
                }
            }
        }
    }

    protected ProposalDevelopmentDocument getProposalDoc(String pdDocumentNumber) {
        BusinessObjectService boService = KraServiceLocator.getService(BusinessObjectService.class);
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put("documentNumber", pdDocumentNumber);
        ProposalDevelopmentDocument newCopy = (ProposalDevelopmentDocument) boService.findByPrimaryKey(ProposalDevelopmentDocument.class, keyMap);
        return newCopy;
    }
    
    public ActionForward proposal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward(Constants.PROPOSAL_PAGE);
    }

    /**
     * Action called to forward to a new KeyPersonnel tab.
     * 
     * @param mapping 
     * @param form
     * @param request
     * @param response
     * @return ActionForward instance for forwarding to the tab.
     */
    public ActionForward keyPersonnel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProposalDevelopmentForm pdform = (ProposalDevelopmentForm) form;
        getKeyPersonnelService().populateDocument(pdform.getDocument());
              
        // Let this be taken care of in KeyPersonnelAction execute() method
        if (this instanceof ProposalDevelopmentKeyPersonnelAction) {
            LOG.info("forwarding to keyPersonnel action");
            return mapping.findForward(Constants.KEY_PERSONNEL_PAGE);
        }

        new ProposalDevelopmentKeyPersonnelAction().prepare(form, request);

        return mapping.findForward(Constants.KEY_PERSONNEL_PAGE);
    }

    public ActionForward specialReview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward(Constants.SPECIAL_REVIEW_PAGE);
    }

    public ActionForward questions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward(Constants.QUESTIONS_PAGE);
    }
    
    public ActionForward permissions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward(Constants.PERMISSIONS_PAGE);
    }
    
    public ActionForward hierarchy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ProposalDevelopmentForm pdForm = (ProposalDevelopmentForm)form;
        pdForm.setHierarchyProposalSummaries(getHierarchyHelper().getHierarchySummaries(pdForm.getDocument().getDevelopmentProposal().getProposalNumber()));
        return mapping.findForward(Constants.HIERARCHY_PAGE);
    }
    
    public ActionForward grantsGov(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward(Constants.GRANTS_GOV_PAGE);
    }

    public ActionForward budgetVersions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final ProposalDevelopmentForm pdForm = (ProposalDevelopmentForm) form;
        final String headerTabCall = getHeaderTabDispatch(request);
        if(StringUtils.isEmpty(headerTabCall)) {
            pdForm.getDocument().refreshPessimisticLocks();
        }        
        pdForm.setFinalBudgetVersion(getFinalBudgetVersion(pdForm.getDocument().getBudgetDocumentVersions()));
        setBudgetStatuses(pdForm.getDocument());
        
        final BudgetTDCValidator tdcValidator = new BudgetTDCValidator(request);
        tdcValidator.validateGeneratingWarnings(pdForm.getDocument());
        
        return mapping.findForward(Constants.PD_BUDGET_VERSIONS_PAGE);
    }

    @SuppressWarnings("unchecked")
    public ActionForward abstractsAttachments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        // TODO temporarily to set up proposal person- remove this once keyperson is completed and htmlunit testing fine
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument doc = proposalDevelopmentForm.getDocument();
        doc.getDevelopmentProposal().populateNarrativeRightsForLoggedinUser();

        /*
         * Save the current set of narratives.  In some cases, a user can view the
         * narrative panel info, but is not allowed to change it.  We will make a
         * copy of the original narratives to use for comparison when a save occurs.
         * If a user attempted to change a narrative they were not authorized to,
         * then an error will be posted.
         */
        List<Narrative> narratives = (List<Narrative>) ObjectUtils.deepCopy((Serializable) doc.getDevelopmentProposal().getNarratives());
        proposalDevelopmentForm.setNarratives(narratives);
        KraServiceLocator.getService(ProposalPersonBiographyService.class).setPersonnelBioTimeStampUser(doc.getDevelopmentProposal().getPropPersonBios());
        List<Narrative> narrativeList = new ArrayList<Narrative> ();
        narrativeList.addAll(doc.getDevelopmentProposal().getNarratives());
        narrativeList.addAll(doc.getDevelopmentProposal().getInstituteAttachments());
        KraServiceLocator.getService(NarrativeService.class).setNarrativeTimeStampUser(narrativeList);

        return mapping.findForward(Constants.ATTACHMENTS_PAGE);
    }

    public ActionForward customData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SortedMap<String, List<CustomAttributeDocument>> customAttributeGroups = new TreeMap<String, List<CustomAttributeDocument>>();

        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument doc = proposalDevelopmentForm.getDocument();

        Map<String, CustomAttributeDocument> customAttributeDocuments = doc.getCustomAttributeDocuments();
        String documentNumber = doc.getDocumentNumber();
        for(Map.Entry<String, CustomAttributeDocument> customAttributeDocumentEntry:customAttributeDocuments.entrySet()) {
            CustomAttributeDocument customAttributeDocument = customAttributeDocumentEntry.getValue();
            Map<String, Object> primaryKeys = new HashMap<String, Object>();
            primaryKeys.put(KNSPropertyConstants.DOCUMENT_NUMBER, documentNumber);
            primaryKeys.put(Constants.CUSTOM_ATTRIBUTE_ID, customAttributeDocument.getCustomAttributeId());

            CustomAttributeDocValue customAttributeDocValue = (CustomAttributeDocValue) KraServiceLocator.getService(BusinessObjectService.class).findByPrimaryKey(CustomAttributeDocValue.class, primaryKeys);
            if (customAttributeDocValue != null) {
                customAttributeDocument.getCustomAttribute().setValue(customAttributeDocValue.getValue());
                proposalDevelopmentForm.getCustomAttributeValues().put("id" + customAttributeDocument.getCustomAttributeId().toString(), new String[]{customAttributeDocValue.getValue()});
            }

            String groupName = customAttributeDocument.getCustomAttribute().getGroupName();
            List<CustomAttributeDocument> customAttributeDocumentList = customAttributeGroups.get(groupName);

            if (customAttributeDocumentList == null) {
                customAttributeDocumentList = new ArrayList<CustomAttributeDocument>();
                customAttributeGroups.put(groupName, customAttributeDocumentList);
            }
            customAttributeDocumentList.add(customAttributeDocument);
        }

        ((ProposalDevelopmentForm)form).setCustomAttributeGroups(customAttributeGroups);

        return mapping.findForward(Constants.CUSTOM_ATTRIBUTES_PAGE);
    }

    public ActionForward actions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
        ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
        if (proposalDevelopmentForm.getDocument().getDocumentNumber() == null) {
            // If entering this action from copy link on doc search
            loadDocumentInForm(request, proposalDevelopmentForm);
        }
        ProposalDevelopmentDocument proposalDevelopmentDocument = proposalDevelopmentForm.getDocument();
        PrintService printService = KraServiceLocator.getService(PrintService.class);
        printService.populateSponsorForms(proposalDevelopmentForm.getSponsorFormTemplates(), proposalDevelopmentDocument.getDevelopmentProposal().getSponsorCode());
        return mapping.findForward(Constants.PROPOSAL_ACTIONS_PAGE);
    }
    
    /**
    *
    * This method gets called upon navigation to Medusa tab.
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @return
    */
   public ActionForward medusa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
       ProposalDevelopmentForm proposalDevelopmentForm = (ProposalDevelopmentForm) form;
       ProposalDevelopmentDocument document = (ProposalDevelopmentDocument) proposalDevelopmentForm.getDocument();
       String proposalNumber = document.getDevelopmentProposal().getProposalNumber();
       proposalDevelopmentForm.getMedusaBean().setMedusaViewRadio("0");
       proposalDevelopmentForm.getMedusaBean().setModuleName("DP");
       proposalDevelopmentForm.getMedusaBean().setModuleIdentifier(Long.valueOf(proposalNumber));
       return mapping.findForward(Constants.MAPPING_PROPOSAL_MEDUSA_PAGE);
   }

    /**
     * This method processes an auditMode action request
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward to forward to ("auditMode")
     */
    public ActionForward auditMode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        new AuditActionHelper().auditConditionally((ProposalDevelopmentForm) form);
        return mapping.findForward("auditMode");
    }
    
    /**
     * Grabs the <code>{@link KeyPersonnelService} from Spring!
     * 
     * @return KeyPersonnelService
     */
    protected KeyPersonnelService getKeyPersonnelService() {
        return KraServiceLocator.getService(KeyPersonnelService.class);
    }
    
    /**
     * @see org.kuali.kra.web.struts.action.KraTransactionalDocumentActionBase#initialDocumentSave(org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase)
     */
    @Override
    protected void initialDocumentSave(KualiDocumentFormBase form) throws Exception {
        ProposalDevelopmentForm pdForm = (ProposalDevelopmentForm) form;
        ProposalDevelopmentDocument doc = pdForm.getDocument();
        initializeProposalUsers(doc);
    }
    
   /**
    * Create the original set of Proposal Users for a new Proposal Development Document.
    * The creator the proposal is assigned to the AGGREGATOR role.
    */
    protected void initializeProposalUsers(ProposalDevelopmentDocument doc) {
        
        // Assign the creator of the proposal to the AGGREGATOR role.
        
        String userId = GlobalVariables.getUserSession().getPrincipalId();
        KraAuthorizationService kraAuthService = KraServiceLocator.getService(KraAuthorizationService.class);
        if (!kraAuthService.hasRole(userId, doc, RoleConstants.AGGREGATOR))
            kraAuthService.addRole(userId, RoleConstants.AGGREGATOR, doc);
        
        // Add the users defined in the role templates for the proposal's lead unit
        
        ProposalRoleTemplateService proposalRoleTemplateService = KraServiceLocator.getService(ProposalRoleTemplateService.class);
        proposalRoleTemplateService.addUsers(doc);
    } 
    
    /**
     * Get the name of the action.  Every Proposal Action class has the
     * naming convention of
     * 
     *      ProposalDevelopment<name>Action
     * 
     * This method extracts the <name> from the above class name.
     * 
     * @return the action's name
     */
    protected String getActionName() {
        String name = getClass().getSimpleName();
        int endIndex = name.lastIndexOf("Action");
        return name.substring(19, endIndex);
    }
    
    protected void loadDocumentInForm(HttpServletRequest request, ProposalDevelopmentForm proposalDevelopmentForm)
    throws WorkflowException {
        String docIdRequestParameter = request.getParameter(KNSConstants.PARAMETER_DOC_ID);
        ProposalDevelopmentDocument retrievedDocument = (ProposalDevelopmentDocument)KNSServiceLocator.getDocumentService().getByDocumentHeaderId(docIdRequestParameter);
        proposalDevelopmentForm.setDocument(retrievedDocument);
        request.setAttribute(KNSConstants.PARAMETER_DOC_ID, docIdRequestParameter);
        
        // Set lead unit on form when copying a document. This is needed so the lead unit shows up on the "Copy to New Document" panel under Proposal Actions.
        ProposalCopyCriteria cCriteria = proposalDevelopmentForm.getCopyCriteria();
        if (cCriteria != null) {
            cCriteria.setOriginalLeadUnitNumber(retrievedDocument.getDevelopmentProposal().getOwnedByUnitNumber());
        }
    }
    
    /**
     * Overriding headerTab to customize how clearing tab state works on PDForm.
     */
    @Override
    public ActionForward headerTab(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ((KualiForm) form).setTabStates(new HashMap<String, String>());
        return super.headerTab(mapping, form, request, response);
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
        super.save(mapping, form, request, response);
        ProposalDevelopmentDocument proposalDevelopmentDocument = proposalDevelopmentForm.getDocument();
        boolean grantsGovErrorExists = false;

        AttachmentDataSource attachmentDataSource = KraServiceLocator.getService(S2SService.class).printForm(proposalDevelopmentDocument);
        if(attachmentDataSource==null || attachmentDataSource.getContent()==null){
            //KRACOEUS-3300 - there should be GrantsGov audit errors in this case, grab them and display them as normal errors on
            //the GrantsGov forms tab so we don't need to turn on auditing
            Iterator<String> iter = GlobalVariables.getAuditErrorMap().keySet().iterator();
            while (iter.hasNext()) {
               String errorKey = (String) iter.next();
                AuditCluster auditCluster = (AuditCluster)GlobalVariables.getAuditErrorMap().get(errorKey);
                if(StringUtils.equalsIgnoreCase(auditCluster.getCategory(),Constants.GRANTSGOV_ERRORS)){
                    grantsGovErrorExists = true;
                    for (Object error : auditCluster.getAuditErrorList()) {
                        AuditError auditError = (AuditError)error;
                        GlobalVariables.getErrorMap().putError("grantsGovFormValidationErrors", auditError.getMessageKey(), auditError.getParams());
                    }
                }
            }
        }
        if(grantsGovErrorExists){
            GlobalVariables.getErrorMap().putError("grantsGovFormValidationErrors", KeyConstants.VALIDATTION_ERRORS_BEFORE_GRANTS_GOV_SUBMISSION);
            return mapping.findForward(Constants.GRANTS_GOV_PAGE);
        }
        if(attachmentDataSource==null || attachmentDataSource.getContent()==null){
            return mapping.findForward(Constants.MAPPING_PROPOSAL_ACTIONS);
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
    
    
    /**
     * This method produces a list of strings containg the methodToCall to be registered for each of the 
     * ProposalColumnsToAlter lookup buttons that can be rendered on the Proposal Data Override tab. The execute method in this class
     * puts this list into the form.  The Proposal Data Override tag file then calls registerEditableProperty on each when rendering the tab.
     * 
     * @param proposalNumber The proposal number for which we are generating the list for.
     * @return Possible editable properties that can be called from the page.
     */
    public List<String> constructColumnsToAlterLookupMTCs(String proposalNumber) {
        Map<String,Object> filterMap = new HashMap<String,Object>();
        ProposalDevelopmentService proposalDevelopmentService = KraServiceLocator.getService(ProposalDevelopmentService.class);
        Collection<ProposalColumnsToAlter> proposalColumnsToAlterCollection = (KraServiceLocator.getService(BusinessObjectService.class).findMatching(ProposalColumnsToAlter.class, filterMap));
        
        List<String> mtcReturn = new ArrayList<String>();
        
        for( ProposalColumnsToAlter pcta : proposalColumnsToAlterCollection ) {
            if( pcta.getHasLookup() ) {
                Map<String, Object> primaryKeys = new HashMap<String, Object>();
                primaryKeys.put("columnName", pcta.getColumnName());
                Object fieldValue = proposalDevelopmentService.getProposalFieldValueFromDBColumnName(proposalNumber, pcta.getColumnName());
                String displayAttributeName = pcta.getLookupReturn();
                String displayLookupReturnValue = proposalDevelopmentService.getDataOverrideLookupDisplayReturnValue(pcta.getLookupClass());
                mtcReturn.add("methodToCall.performLookup.(!!"+pcta.getLookupClass()+"!!).((("+displayLookupReturnValue+":newProposalChangedData.changedValue,"+displayAttributeName+":newProposalChangedData.displayValue))).((##)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).anchorProposalDataOverride");
            }
        }
        return mtcReturn;
    }
}

class S2sOppFormsComparator1 implements Comparator<S2sOppForms> {
    public int compare(S2sOppForms s2sOppForms1, S2sOppForms s2sOppForms2) {
        return  s2sOppForms2.getAvailable().compareTo(s2sOppForms1.getAvailable());
    }
  }

class S2sOppFormsComparator2 implements Comparator<S2sOppForms> {
    public int compare(S2sOppForms s2sOppForms1, S2sOppForms s2sOppForms2) {
        return  s2sOppForms2.getMandatory().compareTo(s2sOppForms1.getMandatory());
    }
  }
