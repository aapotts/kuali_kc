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
package org.kuali.kra.irb.web.struts.action;

import static org.kuali.kra.infrastructure.KraServiceLocator.getService;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.core.bo.PersistableBusinessObject;
import org.kuali.core.bo.user.UniversalUser;
import org.kuali.core.document.Document;
import org.kuali.core.lookup.LookupResultsService;
import org.kuali.core.rule.event.KualiDocumentEvent;
import org.kuali.core.service.KualiRuleService;
import org.kuali.core.util.GlobalVariables;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.RoleConstants;
import org.kuali.kra.infrastructure.TaskName;
import org.kuali.kra.irb.document.ProtocolDocument;
import org.kuali.kra.irb.document.authorization.ProtocolTask;
import org.kuali.kra.irb.service.ProtocolAuthorizationService;
import org.kuali.kra.irb.web.struts.form.ProtocolForm;
import org.kuali.kra.web.struts.action.KraTransactionalDocumentActionBase;
import org.kuali.rice.KNSServiceLocator;
import org.kuali.rice.kns.util.KNSConstants;

import edu.iu.uis.eden.clientapp.IDocHandler;

/**
 * The ProtocolAction is the base class for all Protocol actions.  Each derived
 * Action class corresponds to one tab (web page).  The derived Action class handles
 * all user requests for that particular tab (web page).
 */
public abstract class ProtocolAction extends KraTransactionalDocumentActionBase {
    
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ProtocolAction.class);
    
    public ActionForward protocol(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("protocol");
    }

    public ActionForward personnel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("personnel");
    }

    /**
     * @see org.kuali.core.web.struts.action.KualiDocumentActionBase#save(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public final ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {    
        
        ActionForward actionForward = mapping.findForward(Constants.MAPPING_BASIC);
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolDocument doc = protocolForm.getProtocolDocument();
        
        ProtocolTask task = new ProtocolTask(TaskName.MODIFY_PROTOCOL, doc.getProtocol());
        if (isAuthorized(task)) {
            if (isValidSave(protocolForm)) {
                String originalStatus = getDocumentStatus(doc);
                actionForward = super.save(mapping, form, request, response);
                if (isInitialSave(originalStatus)) {
                    initializeProtocolUsers(doc); 
                }
            }
        }

        return actionForward;
    }
    
    /**
     * Can the protocol be saved?  This method is normally overridden by
     * a subclass in order to invoke business rules to verify that the
     * protocol can be saved.
     * @param protocolForm the Protocol Form
     * @return true if the protocol can be saved; otherwise false
     */
    protected boolean isValidSave(ProtocolForm protocolForm) {
        return true;
    }

    /**
     * Get the current status of the document.  
     * @param doc the Protocol Document
     * @return the status (INITIATED, SAVED, etc.)
     */
    private String getDocumentStatus(ProtocolDocument doc) {
        return doc.getDocumentHeader().getWorkflowDocument().getStatusDisplayValue();
    }
    
    /**
     * Is this the initial save of the document?  If there are errors
     * in the document, it won't be saved and thus it cannot be initial
     * successful save.
     * @param status the original status before the save operation
     * @return true if the initial save; otherwise false
     */
    private boolean isInitialSave(String status) {
        return GlobalVariables.getErrorMap().isEmpty() &&
               StringUtils.equals("INITIATED", status);
    }
    
    /**
     * Create the original set of Protocol Users for a new Protocol Document.
     * The creator the protocol is assigned to the PROTOCOL_AGGREGATOR role.
     * @param doc the Protocol Document
     */
    protected void initializeProtocolUsers(ProtocolDocument doc) {
        UniversalUser user = GlobalVariables.getUserSession().getUniversalUser();
        String username = user.getPersonUserIdentifier();
        ProtocolAuthorizationService protocolAuthService = KraServiceLocator.getService(ProtocolAuthorizationService.class);
        protocolAuthService.addRole(username, RoleConstants.PROTOCOL_AGGREGATOR, doc.getProtocol());
    }

    @Override
    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        super.refresh(mapping, form, request, response);
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolDocument protocolDocument = protocolForm.getProtocolDocument();
                     
        // KNS UI hook for lookup resultset, check to see if we are coming back from a lookup
        if (Constants.MULTIPLE_VALUE.equals(protocolForm.getRefreshCaller())) {
            // Multivalue lookup. Note that the multivalue keyword lookup results are returned persisted to avoid using session.
            // Since URLs have a max length of 2000 chars, field conversions can not be done.
            String lookupResultsSequenceNumber = protocolForm.getLookupResultsSequenceNumber();
            
            if (StringUtils.isNotBlank(lookupResultsSequenceNumber)) {
                
                Class lookupResultsBOClass = Class.forName(protocolForm.getLookupResultsBOClassName());
                String userName = GlobalVariables.getUserSession().getUniversalUser().getPersonUniversalIdentifier();
                LookupResultsService service = KraServiceLocator.getService(LookupResultsService.class);
                Collection<PersistableBusinessObject> selectedBOs = service.retrieveSelectedResultBOs(lookupResultsSequenceNumber, lookupResultsBOClass, userName);
                
                processMultipleLookupResults(protocolDocument, lookupResultsBOClass, selectedBOs);
            }
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC );
    }
    
    /**
     * This method must be overridden by a derived class if that derived class has a field that requires a 
     * Lookup that returns multiple values.  The derived class should first check the class of the selected BOs.
     * Based upon the class, the Protocol can be updated accordingly.  This is necessary since there may be
     * more than one multi-lookup on a web page.
     * 
     * @param protocolDocument the Protocol Document
     * @param lookupResultsBOClass the class of the BOs that are returned by the Lookup
     * @param selectedBOs the selected BOs
     */
    protected void processMultipleLookupResults(ProtocolDocument protocolDocument, Class lookupResultsBOClass, Collection<PersistableBusinessObject> selectedBOs) {
        // do nothing
    }
    
    @Override
    public ActionForward headerTab(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.headerTab(mapping, form, request, response);
    }

    /**
     * @see org.kuali.core.web.struts.action.KualiDocumentActionBase#docHandler(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward docHandler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = null;
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        String command = protocolForm.getCommand();
        
        if (IDocHandler.ACTIONLIST_INLINE_COMMAND.equals(command)) {
             String docIdRequestParameter = request.getParameter(KNSConstants.PARAMETER_DOC_ID);
             Document retrievedDocument = KNSServiceLocator.getDocumentService().getByDocumentHeaderId(docIdRequestParameter);
             protocolForm.setDocument(retrievedDocument);
             request.setAttribute(KNSConstants.PARAMETER_DOC_ID, docIdRequestParameter);
             forward = mapping.findForward(Constants.MAPPING_COPY_PROPOSAL_PAGE);
             forward = new ActionForward(forward.getPath()+ "?" + KNSConstants.PARAMETER_DOC_ID + "=" + docIdRequestParameter);  
        } else {
             forward = super.docHandler(mapping, form, request, response);
        }

        if (IDocHandler.INITIATE_COMMAND.equals(protocolForm.getCommand())) {
            protocolForm.getProtocolDocument().initialize();
        }else{
            protocolForm.initialize();
        }
        
        return forward;
    }

    /**
     * Get the Kuali Rule Service.
     * @return the Kuali Rule Service
     */
    private KualiRuleService getKualiRuleService() {
        return getService(KualiRuleService.class);
    }
    
    /**
     * Use the Kuali Rule Service to apply the rules for the given event.
     * @param event the event to process
     * @return true if success; false if there was a validation error
     */
    protected final boolean applyRules(KualiDocumentEvent event) {
        return getKualiRuleService().applyRules(event);
    }
}
