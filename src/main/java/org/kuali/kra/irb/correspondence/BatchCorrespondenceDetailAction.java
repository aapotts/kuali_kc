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
package org.kuali.kra.irb.correspondence;

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
import org.kuali.kra.committee.bo.CommitteeSchedule;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.PermissionConstants;
import org.kuali.kra.meeting.MeetingForm;
import org.kuali.rice.core.util.RiceConstants;
import org.kuali.rice.kns.exception.AuthorizationException;
import org.kuali.rice.kns.question.ConfirmationQuestion;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.KNSConstants;
import org.kuali.rice.kns.util.RiceKeyConstants;
import org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase;

public class BatchCorrespondenceDetailAction extends KualiDocumentActionBase {
    // signifies that a response has already be handled therefore forwarding to obtain a response is not needed. 
//    private static final ActionForward RESPONSE_ALREADY_HANDLED = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {

        // Check and initialize permissions
//        if (getProtocolCorrespondenceTemplateAuthorizationService().hasPermission(PermissionConstants.MODIFY_CORRESPONDENCE_TEMPLATE)) {
//            ((ProtocolCorrespondenceTemplateForm) form).setReadOnly(false);
//        } else if (getProtocolCorrespondenceTemplateAuthorizationService().hasPermission(PermissionConstants.VIEW_CORRESPONDENCE_TEMPLATE)) {
//            ((ProtocolCorrespondenceTemplateForm) form).setReadOnly(true);
//        } else {
//            throw new AuthorizationException(GlobalVariables.getUserSession().getPerson().getPrincipalName(), 
//                    findMethodToCall(form, request), this.getClass().getSimpleName());
//        }
        
        // initialize form on initial page load and on page reload to erase any old user data
//        if (StringUtils.equals(request.getParameter("init"), "true")
//                || StringUtils.equals((String) request.getAttribute("methodToCallAttribute"), "methodToCall.reload.y")) {
//            ProtocolCorrespondenceTemplateForm templateForm = new ProtocolCorrespondenceTemplateForm();
//            ((ProtocolCorrespondenceTemplateForm) form).setCorrespondenceTypes(templateForm.getCorrespondenceTypes());
//            ((ProtocolCorrespondenceTemplateForm) form).setNewDefaultCorrespondenceTemplates(templateForm.getNewDefaultCorrespondenceTemplates());
//            ((ProtocolCorrespondenceTemplateForm) form).setNewCorrespondenceTemplates(templateForm.getNewCorrespondenceTemplates());
//            ((ProtocolCorrespondenceTemplateForm) form).setDeletedCorrespondenceTemplates(templateForm.getDeletedCorrespondenceTemplates());
//            ((ProtocolCorrespondenceTemplateForm) form).setTabStates(new HashMap<String, String>());
//        }
        
        return super.execute(mapping, form, request, response);
    }
    
    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, String> fieldValues = new HashMap<String, String>();
        fieldValues.put("batchCorrespondenceTypeCode", ((BatchCorrespondenceDetailForm) form).getBatchCorrespondence().getBatchCorrespondenceTypeCode());
        BatchCorrespondence batchCorrespondence = (BatchCorrespondence) getBusinessObjectService().findByPrimaryKey(BatchCorrespondence.class, fieldValues);
        ((BatchCorrespondenceDetailForm) form).setBatchCorrespondence(batchCorrespondence);

        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * This method is to get the protocol correspondence template service.
     * @return ProtocolCorrespondenceTemplateService
     */
//    private ProtocolCorrespondenceTemplateService getProtocolCorrespondenceTemplateService() {
//        return (ProtocolCorrespondenceTemplateService) KraServiceLocator.getService("protocolCorrespondenceTemplateService");
//    }
    
    /**
     * This method returns the index of the selected correspondence type.
     * @param request
     * @return index
     */
    protected int getSelectedCorrespondenceType(HttpServletRequest request) {
        int index = -1;
        String parameterName = (String) request.getAttribute(KNSConstants.METHOD_TO_CALL_ATTRIBUTE);
        if (StringUtils.isNotBlank(parameterName)) {
            index = Integer.parseInt(StringUtils.substringBetween(parameterName, "correspondenceType[", "]"));
        }
        return index;
    }

    /**
     * This method returns the index of the selected correspondence template.
     * @param request
     * @return index
     */
    protected int getSelectedCorrespondenceTemplate(HttpServletRequest request) {
        int index = -1;
        String parameterName = (String) request.getAttribute(KNSConstants.METHOD_TO_CALL_ATTRIBUTE);
        if (StringUtils.isNotBlank(parameterName)) {
            index = Integer.parseInt(StringUtils.substringBetween(parameterName, "correspondenceTemplate[", "]"));
        }
        return index;
    }

    /**
     * 
     * This method is called when saving the correspondence templates.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return action forward
     * @throws Exception
     */
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {

        // Check modify permission
//        if (!getProtocolCorrespondenceTemplateAuthorizationService().hasPermission(PermissionConstants.MODIFY_CORRESPONDENCE_TEMPLATE)) {
//            throw new AuthorizationException(GlobalVariables.getUserSession().getPerson().getPrincipalName(), 
//                    findMethodToCall(form, request), this.getClass().getSimpleName());
//        }
        
//        ProtocolCorrespondenceTemplateForm correspondenceTemplateForm = (ProtocolCorrespondenceTemplateForm) form;
//        List<ProtocolCorrespondenceType> protocolCorrespondenceTypes = correspondenceTemplateForm.getCorrespondenceTypes();
//        boolean rulePassed = new ProtocolCorrespondenceTemplateRule().processSaveProtocolCorrespondenceTemplateRules(protocolCorrespondenceTypes);
//        if (rulePassed) {
//            getProtocolCorrespondenceTemplateService().saveProtocolCorrespondenceTemplates(protocolCorrespondenceTypes, 
//                correspondenceTemplateForm.getDeletedCorrespondenceTemplates());
//            correspondenceTemplateForm.setDeletedCorrespondenceTemplates(new ArrayList<ProtocolCorrespondenceTemplate>());
//            GlobalVariables.getMessageList().add(RiceKeyConstants.MESSAGE_SAVED);
//        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * 
     * This method is called when reloading the correspondence templates.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return action forward
     * @throws Exception
     */
    @Override
    public ActionForward reload(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        GlobalVariables.getMessageList().add(RiceKeyConstants.MESSAGE_RELOADED);
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * 
     * This method is called when closing the correspondence templates.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return action forward
     * @throws Exception
     */
    @Override
    public ActionForward close(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        ActionForward actionForward = mapping.findForward(KNSConstants.MAPPING_PORTAL);
        
//        if (getProtocolCorrespondenceTemplateAuthorizationService().hasPermission(PermissionConstants.MODIFY_CORRESPONDENCE_TEMPLATE)) {
//            if (!StringUtils.equals(request.getParameter(KNSConstants.QUESTION_INST_ATTRIBUTE_NAME), KNSConstants.DOCUMENT_SAVE_BEFORE_CLOSE_QUESTION)) {
//                // Ask question whether to save before close
//                actionForward = this.performQuestionWithoutInput(mapping, form, request, response, KNSConstants.DOCUMENT_SAVE_BEFORE_CLOSE_QUESTION, 
//                        getKualiConfigurationService().getPropertyString(RiceKeyConstants.QUESTION_SAVE_BEFORE_CLOSE), 
//                        KNSConstants.CONFIRMATION_QUESTION, KNSConstants.MAPPING_CLOSE, "");
//            } else if (StringUtils.equals(request.getParameter(KNSConstants.QUESTION_CLICKED_BUTTON), ConfirmationQuestion.YES)) {
//                // Validate document
//                ProtocolCorrespondenceTemplateForm correspondenceTemplateForm = (ProtocolCorrespondenceTemplateForm) form;
//                List<ProtocolCorrespondenceType> protocolCorrespondenceTypes = correspondenceTemplateForm.getCorrespondenceTypes();
//                boolean rulePassed = new ProtocolCorrespondenceTemplateRule().processSaveProtocolCorrespondenceTemplateRules(protocolCorrespondenceTypes);
//                if (!rulePassed) {
//                    // Reload document if errors exist 
//                    actionForward = mapping.findForward(RiceConstants.MAPPING_BASIC);                    
//                } else {
//                    // Save document
//                    getProtocolCorrespondenceTemplateService().saveProtocolCorrespondenceTemplates(protocolCorrespondenceTypes, 
//                        correspondenceTemplateForm.getDeletedCorrespondenceTemplates());
//                    correspondenceTemplateForm.setDeletedCorrespondenceTemplates(new ArrayList<ProtocolCorrespondenceTemplate>());
//                    actionForward = mapping.findForward(KNSConstants.MAPPING_PORTAL);
//                }
//            }
//        }
        
        return actionForward;
    }
    
    /**
     * 
     * This method is called when canceling the correspondence templates.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return action forward
     * @throws Exception
     */
    @Override
    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        ActionForward actionForward;
        
        if (StringUtils.equals(request.getParameter(KNSConstants.QUESTION_INST_ATTRIBUTE_NAME), KNSConstants.DOCUMENT_CANCEL_QUESTION)) {
            if (StringUtils.equals(request.getParameter(KNSConstants.QUESTION_CLICKED_BUTTON), ConfirmationQuestion.YES)) {
                // Cancel document and return to portal if cancel has been confirmed
                actionForward = mapping.findForward(KNSConstants.MAPPING_PORTAL);
            } else {
                // Reload document if cancel has been aborted 
                actionForward = mapping.findForward(RiceConstants.MAPPING_BASIC);
            }
        } else {
            // Ask question to confirm cancel
            actionForward = performQuestionWithoutInput(mapping, form, request, response, KNSConstants.DOCUMENT_CANCEL_QUESTION, 
                    getKualiConfigurationService().getPropertyString("document.question.cancel.text"), KNSConstants.CONFIRMATION_QUESTION, 
                    KNSConstants.MAPPING_CANCEL, "");
        }

        return actionForward;
    }
    
//    private ProtocolCorrespondenceTemplateAuthorizationService getProtocolCorrespondenceTemplateAuthorizationService() {
//        return KraServiceLocator.getService(ProtocolCorrespondenceTemplateAuthorizationService.class);
//    }

}
