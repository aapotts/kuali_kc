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
package org.kuali.kra.meeting;

import static org.kuali.kra.logging.BufferedLogger.debug;

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
import org.kuali.kra.irb.actions.submit.ProtocolSubmission;
import org.kuali.rice.kns.bo.BusinessObject;
import org.kuali.rice.kns.question.ConfirmationQuestion;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.DictionaryValidationService;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.KNSConstants;
import org.kuali.rice.kns.web.struts.action.KualiAction;
import org.kuali.rice.kns.web.struts.form.KualiForm;

/**
 * 
 * This class is for all meeting actions.  A couple of methods, which are for text area update, 
 *  are copied from KraTransactionalDocumentActionBase.
 */
public class MeetingAction extends KualiAction {
    private static final String CLOSE_QUESTION = "Would you like to save meeting data before close it ?";

    private static final String CLOSE_QUESTION_ID = "meeting.close.question";
    private static final String NEW_SCHEDULE_MINUTE_ERROR_PATH = "meetingHelper.newCommitteeScheduleMinute";
    private static final String NEW_OTHER_ACTION_ERROR_PATH = "meetingHelper.newOtherAction";
    /**
     * 
     * This method is for the initial load of meeting page. It is called when 'maintain' button of committee schedule is clicked.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, String> fieldValues = new HashMap<String, String>();
        fieldValues.put("id", request.getParameter("scheduleId"));
        CommitteeSchedule commSchedule = (CommitteeSchedule) getBusinessObjectService().findByPrimaryKey(CommitteeSchedule.class,
                fieldValues);
        ((MeetingForm) form).getMeetingHelper().populateFormHelper(commSchedule, Integer.parseInt(request.getParameter("lineNum")));
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * 
     * This method is called when 'meeting detail' tab is clicked.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward meeting(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * 
     * This method is to save committee schedule when the 'save' button on meeting detail page is clicked.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        CommitteeSchedule committeeSchedule = ((MeetingForm) form).getMeetingHelper().getCommitteeSchedule();
        if (isValidToSave(committeeSchedule, ((MeetingForm) form).getMeetingHelper().getMemberPresentBeans())) {
            ((MeetingForm) form).getMeetingHelper().populateAttendancePreSave();
            getMeetingService().SaveMeetingDetails(committeeSchedule, ((MeetingForm) form).getMeetingHelper().getDeletedBos());
            ((MeetingForm) form).getMeetingHelper().initDeletedList();
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }


    /*
     * validate required/format of the properties of bo.  also validate business rules.
     */
    private boolean isValidToSave(CommitteeSchedule committeeSchedule, List<MemberPresentBean> memberPresentBeans) {

        GlobalVariables.getMessageMap().addToErrorPath("meetingHelper.committeeSchedule");
        getDictionaryValidationService().validateBusinessObject(committeeSchedule);
        GlobalVariables.getMessageMap().removeFromErrorPath("meetingHelper.committeeSchedule");
        boolean valid = GlobalVariables.getMessageMap().hasNoErrors();
        MeetingDetailsRule meetingDetailsRule = new MeetingDetailsRule();
        valid &= meetingDetailsRule.validateMeetingDetails(committeeSchedule);
        valid &= meetingDetailsRule.validateDuplicateAlternateFor(memberPresentBeans);
        return valid;

    }

    /**
     * 
     * This method is called when 'Meeting Action' Tab is clicked. It is forwarded to 'meetingAction'
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward meetingAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("meetingAction");
    }

    /**
     * 
     * This method is for the 'view' button of protocol submitted. It will be forwarded to protocol action page.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    private ActionForward viewProtocolSubmission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolSubmission protocolSubmission = ((MeetingForm) form).getMeetingHelper().getCommitteeSchedule()
                .getProtocolSubmissions().get(Integer.parseInt(request.getParameter("line")));
        response.sendRedirect("protocolProtocolActions.do?methodToCall=start&protocolId="
                + protocolSubmission.getProtocol().getProtocolId());
        return null;
    }


    private BusinessObjectService getBusinessObjectService() {
        return KraServiceLocator.getService(BusinessObjectService.class);
    }

    private MeetingService getMeetingService() {
        return KraServiceLocator.getService(MeetingService.class);
    }

    /**
     * 
     * This method is for updatetextarea tag. Since meeting management is not a trx doc, so it has to implement this method here.
     * this is copied from KraTransactionalDocumentActionBase
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward kraUpdateTextArea(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        // parse out the important strings from our methodToCall parameter
        String fullParameter = (String) request.getAttribute(KNSConstants.METHOD_TO_CALL_ATTRIBUTE);

        // parse textfieldname:htmlformaction
        String parameterFields = StringUtils.substringBetween(fullParameter, KNSConstants.METHOD_TO_CALL_PARM2_LEFT_DEL,
                KNSConstants.METHOD_TO_CALL_PARM2_RIGHT_DEL);
        debug("fullParameter: ", fullParameter);
        debug("parameterFields: ", parameterFields);
        String[] keyValue = null;
        if (StringUtils.isNotBlank(parameterFields)) {
            String[] textAreaParams = parameterFields.split(KNSConstants.FIELD_CONVERSIONS_SEPARATOR);
            debug("lookupParams: ", textAreaParams);
            for (int i = 0; i < textAreaParams.length; i++) {
                keyValue = textAreaParams[i].split(KNSConstants.FIELD_CONVERSION_PAIR_SEPARATOR);

                debug("keyValue[0]: ", keyValue[0]);
                debug("keyValue[1]: ", keyValue[1]);
            }
        }
        request.setAttribute(org.kuali.kra.infrastructure.Constants.TEXT_AREA_FIELD_NAME, keyValue[0]);
        request.setAttribute(org.kuali.kra.infrastructure.Constants.HTML_FORM_ACTION, keyValue[1]);
        request.setAttribute(org.kuali.kra.infrastructure.Constants.TEXT_AREA_FIELD_LABEL, keyValue[2]);
        request.setAttribute(org.kuali.kra.infrastructure.Constants.VIEW_ONLY, keyValue[3]);
        if (form instanceof KualiForm && StringUtils.isNotEmpty(((KualiForm) form).getAnchor())) {
            request.setAttribute(org.kuali.kra.infrastructure.Constants.TEXT_AREA_FIELD_ANCHOR, ((KualiForm) form).getAnchor());
        }

        return mapping.findForward("kraUpdateTextArea");

    }

    /**
     * 
     * This method to post text area if js is disabled. this is also for updatetextarea tag. this is copied from
     * KraTransactionalDocumentActionBase
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward kraPostTextAreaToParent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * 
     * This method is to add the new other action to other action list. Other action is committee schedule act item.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward addOtherAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        MeetingForm meetingForm = (MeetingForm) form;
        CommScheduleActItem newCommScheduleActItem = meetingForm.getMeetingHelper().getNewOtherAction();
        validateBusinessObject(newCommScheduleActItem, NEW_OTHER_ACTION_ERROR_PATH);
        if (GlobalVariables.getMessageMap().hasNoErrors()) {
            meetingForm.getMeetingHelper().addOtherAction();
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /*
     * Utility methods to validate a bo.
     */
    private void validateBusinessObject(BusinessObject bo, String errorPath) {
        GlobalVariables.getMessageMap().addToErrorPath(errorPath);
        getDictionaryValidationService().validateBusinessObject(bo);
        GlobalVariables.getMessageMap().removeFromErrorPath(errorPath);        
    }
    
    /**
     * 
     * This method is to remove other action from other action list. Also keep in a deleted list, which will be used to call
     * 'bos.delete' to remove them from DB before save.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteOtherAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ((MeetingForm) form).getMeetingHelper().deleteOtherAction(getLineToDelete(request));
        return mapping.findForward(Constants.MAPPING_BASIC);
    }


    /**
     * 
     * This method is for 'close' button.  Confirmation of 'save' is performed.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward close(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Object question = request.getParameter(KNSConstants.QUESTION_INST_ATTRIBUTE_NAME);
        if (question == null) {
            return performQuestionWithoutInput(mapping, form, request, response, CLOSE_QUESTION_ID, CLOSE_QUESTION,
                    KNSConstants.CONFIRMATION_QUESTION, ((MeetingForm) form).getMethodToCall(), "");
        }
        else {
            Object buttonClicked = request.getParameter(KNSConstants.QUESTION_CLICKED_BUTTON);
            if ((CLOSE_QUESTION_ID.equals(question)) && ConfirmationQuestion.YES.equals(buttonClicked)) {
                CommitteeSchedule committeeSchedule = ((MeetingForm) form).getMeetingHelper().getCommitteeSchedule();
                if (isValidToSave(committeeSchedule, ((MeetingForm) form).getMeetingHelper().getMemberPresentBeans())) {
                    ((MeetingForm) form).getMeetingHelper().populateAttendancePreSave();
                    getMeetingService().SaveMeetingDetails(committeeSchedule,
                            ((MeetingForm) form).getMeetingHelper().getDeletedBos());
                    ((MeetingForm) form).getMeetingHelper().initDeletedList();
                }
                else {
                    return mapping.findForward(Constants.MAPPING_BASIC);

                }
            }
        }

        return mapping.findForward(KNSConstants.MAPPING_PORTAL);
    }

    /**
     * 
     * This method is for cancel button
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward(KNSConstants.MAPPING_PORTAL);
    }

    /**
     * 
     * This method is called when markabsent is clicked.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward markAbsent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ((MeetingForm) form).getMeetingHelper().markAbsent(getLineToDelete(request));
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * 
     * This method is to move member absent to member present.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward presentVoting(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        MeetingForm meetingForm = (MeetingForm) form;
        MemberAbsentBean memberAbsentBean = meetingForm.getMeetingHelper().getMemberAbsentBeans().get((getLineToDelete(request)));
        MeetingDetailsRule meetingDetailsRule = new MeetingDetailsRule();
        if (meetingDetailsRule.validateNotAlternateFor(meetingForm.getMeetingHelper().getMemberPresentBeans(), memberAbsentBean)) {
            meetingForm.getMeetingHelper().presentVoting(getLineToDelete(request));
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * 
     * This method is to move member absent to other present.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward presentOther(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        MeetingForm meetingForm = (MeetingForm) form;
        MemberAbsentBean memberAbsentBean = meetingForm.getMeetingHelper().getMemberAbsentBeans().get((getLineToDelete(request)));
        MeetingDetailsRule meetingDetailsRule = new MeetingDetailsRule();
        if (meetingDetailsRule.validateNotAlternateFor(meetingForm.getMeetingHelper().getMemberPresentBeans(), memberAbsentBean)) {
            meetingForm.getMeetingHelper().presentOther(getLineToDelete(request));
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * 
     * This method is to add the selected person/rolodex to otehr present.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward addOtherPresent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        MeetingForm meetingForm = (MeetingForm) form;
        MeetingDetailsRule meetingDetailsRule = new MeetingDetailsRule();
        if (meetingDetailsRule.validateNewOther(meetingForm.getMeetingHelper())) {
            meetingForm.getMeetingHelper().addOtherPresent();
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * 
     * This method is to remove the selected otehr present.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteOtherPresent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ((MeetingForm) form).getMeetingHelper().deleteOtherPresent(getLineToDelete(request));
        return mapping.findForward(Constants.MAPPING_BASIC);
    }


    /**
     * 
     * This method is to add committee schedule minutes.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward addCommitteeScheduleMinute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        MeetingForm meetingForm = (MeetingForm) form;
        CommitteeScheduleMinute newCommitteeScheduleMinute = meetingForm.getMeetingHelper().getNewCommitteeScheduleMinute();
        validateBusinessObject(newCommitteeScheduleMinute, NEW_SCHEDULE_MINUTE_ERROR_PATH);
        boolean valid = GlobalVariables.getMessageMap().hasNoErrors();
        MeetingDetailsRule meetingDetailsRule = new MeetingDetailsRule();
        valid &= meetingDetailsRule.validateProtocolInMinute(newCommitteeScheduleMinute);
        if (valid) {
            meetingForm.getMeetingHelper().addCommitteeScheduleMinute();
        }
        return mapping.findForward("basic");
    }

    /**
     * 
     * This method is to remove Committee Schedule Minute from Committee Schedule Minute list. Also keep in a deleted list, which
     * will be used to call 'bos.delete' to remove them from DB when save.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteCommitteeScheduleMinute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ((MeetingForm) form).getMeetingHelper().deleteCommitteeScheduleMinute(getLineToDelete(request));

        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * override method to handle person/rolodex lookup return.
     * @see org.kuali.rice.kns.web.struts.action.KualiAction#refresh(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (StringUtils.isNotBlank(request.getParameter("refreshCaller"))) {
            if (request.getParameter("refreshCaller").equals("nonOrganizationalRolodexLookupable")) {
                ((MeetingForm) form).getMeetingHelper().getNewOtherPresentBean().getAttendance().setNonEmployeeFlag(true);
            }
            else {
                ((MeetingForm) form).getMeetingHelper().getNewOtherPresentBean().getAttendance().setNonEmployeeFlag(false);
            }
        }
        return super.refresh(mapping, form, request, response);

    }

    /**
     * primarily to sort attendance for every action.
     * @see org.kuali.rice.kns.web.struts.action.KualiAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward forward = super.execute(mapping, form, request, response);
        ((MeetingForm) form).getMeetingHelper().sortAttendances();
// if view protocol is using popup, then need following code        
        String command = request.getParameter("command");
        if (StringUtils.isNotBlank(command) && "viewProtocolSubmission".equals(command)) {
            forward = viewProtocolSubmission(mapping, form, request, response);
        }
        return forward;
    }

    private DictionaryValidationService getDictionaryValidationService() {
        return KraServiceLocator.getService(DictionaryValidationService.class);
    }

}