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
package org.kuali.kra.irb.committee.web.struts.action;

import static org.kuali.kra.infrastructure.KraServiceLocator.getService;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.core.bo.PersistableBusinessObject;
import org.kuali.core.document.Document;
import org.kuali.core.lookup.LookupResultsService;
import org.kuali.core.rule.event.KualiDocumentEvent;
import org.kuali.core.service.KualiRuleService;
import org.kuali.core.util.GlobalVariables;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.irb.committee.document.CommitteeDocument;
import org.kuali.kra.irb.committee.web.struts.form.CommitteeForm;
import org.kuali.kra.web.struts.action.KraTransactionalDocumentActionBase;
import org.kuali.rice.KNSServiceLocator;
import org.kuali.rice.kns.util.KNSConstants;

import edu.iu.uis.eden.clientapp.IDocHandler;

/**
 * The CommitteeAction is the base class for all Committee actions.  Each derived
 * Action class corresponds to one tab (web page).  The derived Action classes handle
 * the user requests for a particular tab (web page).
 */
public abstract class CommitteeAction extends KraTransactionalDocumentActionBase {
    
    @SuppressWarnings("unused")
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(CommitteeAction.class);

    /**
     * We override this method to add in support for multi-lookups.
     * 
     * @see org.kuali.core.web.struts.action.KualiDocumentActionBase#refresh(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @SuppressWarnings("unchecked")
    @Override
    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        super.refresh(mapping, form, request, response);
        
        CommitteeForm committeeForm = (CommitteeForm) form;
        CommitteeDocument committeeDocument = committeeForm.getCommitteeDocument();
                     
        // KNS UI hook for lookup resultset, check to see if we are coming back from a lookup
        if (Constants.MULTIPLE_VALUE.equals(committeeForm.getRefreshCaller())) {
            // Multivalue lookup. Note that the multivalue keyword lookup results are returned persisted to avoid using session.
            // Since URLs have a max length of 2000 chars, field conversions can not be done.
            String lookupResultsSequenceNumber = committeeForm.getLookupResultsSequenceNumber();
            
            if (StringUtils.isNotBlank(lookupResultsSequenceNumber)) {
                
                Class lookupResultsBOClass = Class.forName(committeeForm.getLookupResultsBOClassName());
                String userName = GlobalVariables.getUserSession().getUniversalUser().getPersonUniversalIdentifier();
                LookupResultsService service = KraServiceLocator.getService(LookupResultsService.class);
                Collection<PersistableBusinessObject> selectedBOs = service.retrieveSelectedResultBOs(lookupResultsSequenceNumber, lookupResultsBOClass, userName);
                
                processMultipleLookupResults(committeeDocument, lookupResultsBOClass, selectedBOs);
            }
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC );
    }
    
    /**
     * This method must be overridden by a derived class if that derived class has a field that requires a 
     * Lookup that returns multiple values.  The derived class should first check the class of the selected BOs.
     * Based upon the class, the Committee can be updated accordingly.  This is necessary since there may be
     * more than one multi-lookup on a web page.
     * 
     * @param committeeDocument the Committee Document
     * @param lookupResultsBOClass the class of the BOs that are returned by the Lookup
     * @param selectedBOs the selected BOs
     */
    @SuppressWarnings("unchecked")
    protected void processMultipleLookupResults(CommitteeDocument committeeDocument, Class lookupResultsBOClass, Collection<PersistableBusinessObject> selectedBOs) {
        // do nothing
    }

    /**
     * @see org.kuali.core.web.struts.action.KualiDocumentActionBase#docHandler(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward docHandler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = null;
        
        CommitteeForm committeeForm = (CommitteeForm) form;
        String command = committeeForm.getCommand();
        
        if (IDocHandler.ACTIONLIST_INLINE_COMMAND.equals(command)) {
             String docIdRequestParameter = request.getParameter(KNSConstants.PARAMETER_DOC_ID);
             Document retrievedDocument = KNSServiceLocator.getDocumentService().getByDocumentHeaderId(docIdRequestParameter);
             committeeForm.setDocument(retrievedDocument);
             request.setAttribute(KNSConstants.PARAMETER_DOC_ID, docIdRequestParameter);
             forward = mapping.findForward(Constants.MAPPING_BASIC);
             forward = new ActionForward(forward.getPath()+ "?" + KNSConstants.PARAMETER_DOC_ID + "=" + docIdRequestParameter);  
        } 
        else {
             forward = super.docHandler(mapping, form, request, response);
        }

        if (IDocHandler.INITIATE_COMMAND.equals(committeeForm.getCommand())) {
            committeeForm.getCommitteeDocument().initialize();
        } 
        else {
            committeeForm.initialize();
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
