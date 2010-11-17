/*
 * Copyright 2005-2010 The Kuali Foundation
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
package org.kuali.kra.irb.actions;

import static org.kuali.kra.infrastructure.Constants.MAPPING_BASIC;
import static org.kuali.rice.kns.util.KNSConstants.QUESTION_INST_ATTRIBUTE_NAME;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.HeaderTokenizer;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.kra.authorization.ApplicationTask;
import org.kuali.kra.bo.AttachmentFile;
import org.kuali.kra.committee.bo.Committee;
import org.kuali.kra.committee.bo.CommitteeSchedule;
import org.kuali.kra.committee.service.CommitteeService;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.TaskName;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.ProtocolAction;
import org.kuali.kra.irb.ProtocolDocument;
import org.kuali.kra.irb.ProtocolForm;
import org.kuali.kra.irb.actions.acknowledgement.IrbAcknowledgementBean;
import org.kuali.kra.irb.actions.acknowledgement.IrbAcknowledgementService;
import org.kuali.kra.irb.actions.amendrenew.CreateAmendmentEvent;
import org.kuali.kra.irb.actions.amendrenew.CreateRenewalEvent;
import org.kuali.kra.irb.actions.amendrenew.ModifyAmendmentSectionsEvent;
import org.kuali.kra.irb.actions.amendrenew.ProtocolAmendRenewService;
import org.kuali.kra.irb.actions.approve.ProtocolApproveBean;
import org.kuali.kra.irb.actions.approve.ProtocolApproveEvent;
import org.kuali.kra.irb.actions.approve.ProtocolApproveService;
import org.kuali.kra.irb.actions.assignagenda.ProtocolAssignToAgendaBean;
import org.kuali.kra.irb.actions.assignagenda.ProtocolAssignToAgendaEvent;
import org.kuali.kra.irb.actions.assignagenda.ProtocolAssignToAgendaService;
import org.kuali.kra.irb.actions.assigncmtsched.ProtocolAssignCmtSchedBean;
import org.kuali.kra.irb.actions.assigncmtsched.ProtocolAssignCmtSchedEvent;
import org.kuali.kra.irb.actions.assigncmtsched.ProtocolAssignCmtSchedService;
import org.kuali.kra.irb.actions.assignreviewers.ProtocolAssignReviewersBean;
import org.kuali.kra.irb.actions.assignreviewers.ProtocolAssignReviewersEvent;
import org.kuali.kra.irb.actions.assignreviewers.ProtocolAssignReviewersService;
import org.kuali.kra.irb.actions.copy.ProtocolCopyService;
import org.kuali.kra.irb.actions.correction.AdminCorrectionBean;
import org.kuali.kra.irb.actions.correction.AdminCorrectionService;
import org.kuali.kra.irb.actions.correction.ProtocolAdminCorrectionEvent;
import org.kuali.kra.irb.actions.decision.CommitteeDecision;
import org.kuali.kra.irb.actions.decision.CommitteeDecisionAbstainerEvent;
import org.kuali.kra.irb.actions.decision.CommitteeDecisionEvent;
import org.kuali.kra.irb.actions.decision.CommitteeDecisionRecuserEvent;
import org.kuali.kra.irb.actions.decision.CommitteeDecisionService;
import org.kuali.kra.irb.actions.decision.CommitteePerson;
import org.kuali.kra.irb.actions.defer.ProtocolDeferService;
import org.kuali.kra.irb.actions.delete.ProtocolDeleteService;
import org.kuali.kra.irb.actions.expediteapproval.ProtocolExpediteApprovalService;
import org.kuali.kra.irb.actions.genericactions.ProtocolGenericActionBean;
import org.kuali.kra.irb.actions.genericactions.ProtocolGenericActionService;
import org.kuali.kra.irb.actions.grantexemption.ProtocolGrantExemptionBean;
import org.kuali.kra.irb.actions.grantexemption.ProtocolGrantExemptionService;
import org.kuali.kra.irb.actions.history.ProtocolHistoryFilterDatesEvent;
import org.kuali.kra.irb.actions.modifysubmission.ProtocolModifySubmissionBean;
import org.kuali.kra.irb.actions.modifysubmission.ProtocolModifySubmissionEvent;
import org.kuali.kra.irb.actions.modifysubmission.ProtocolModifySubmissionService;
import org.kuali.kra.irb.actions.noreview.ProtocolReviewNotRequiredBean;
import org.kuali.kra.irb.actions.noreview.ProtocolReviewNotRequiredEvent;
import org.kuali.kra.irb.actions.noreview.ProtocolReviewNotRequiredService;
import org.kuali.kra.irb.actions.notifyirb.ProtocolActionAttachment;
import org.kuali.kra.irb.actions.notifyirb.ProtocolNotifyIrbService;
import org.kuali.kra.irb.actions.print.ProtocolActionPrintEvent;
import org.kuali.kra.irb.actions.print.ProtocolPrintType;
import org.kuali.kra.irb.actions.print.ProtocolPrintingService;
import org.kuali.kra.irb.actions.request.ProtocolRequestBean;
import org.kuali.kra.irb.actions.request.ProtocolRequestEvent;
import org.kuali.kra.irb.actions.request.ProtocolRequestService;
import org.kuali.kra.irb.actions.responseapproval.ProtocolResponseApprovalEvent;
import org.kuali.kra.irb.actions.responseapproval.ProtocolResponseApprovalRule;
import org.kuali.kra.irb.actions.responseapproval.ProtocolResponseApprovalService;
import org.kuali.kra.irb.actions.reviewcomments.ProtocolAddReviewCommentEvent;
import org.kuali.kra.irb.actions.reviewcomments.ReviewCommentsBean;
import org.kuali.kra.irb.actions.reviewcomments.ReviewCommentsService;
import org.kuali.kra.irb.actions.risklevel.ProtocolAddRiskLevelEvent;
import org.kuali.kra.irb.actions.risklevel.ProtocolRiskLevelBean;
import org.kuali.kra.irb.actions.risklevel.ProtocolUpdateRiskLevelEvent;
import org.kuali.kra.irb.actions.submit.ProtocolReviewerBean;
import org.kuali.kra.irb.actions.submit.ProtocolSubmission;
import org.kuali.kra.irb.actions.submit.ProtocolSubmitAction;
import org.kuali.kra.irb.actions.submit.ProtocolSubmitActionEvent;
import org.kuali.kra.irb.actions.submit.ProtocolSubmitActionService;
import org.kuali.kra.irb.actions.undo.UndoLastActionBean;
import org.kuali.kra.irb.actions.undo.UndoLastActionService;
import org.kuali.kra.irb.actions.withdraw.ProtocolWithdrawService;
import org.kuali.kra.irb.auth.GenericProtocolAuthorizer;
import org.kuali.kra.irb.auth.ProtocolTask;
import org.kuali.kra.irb.correspondence.ProtocolCorrespondence;
import org.kuali.kra.irb.noteattachment.ProtocolAttachmentBase;
import org.kuali.kra.irb.noteattachment.ProtocolAttachmentPersonnel;
import org.kuali.kra.irb.noteattachment.ProtocolAttachmentProtocol;
import org.kuali.kra.irb.noteattachment.ProtocolAttachmentService;
import org.kuali.kra.irb.noteattachment.ProtocolNotepad;
import org.kuali.kra.irb.summary.AttachmentSummary;
import org.kuali.kra.irb.summary.ProtocolSummary;
import org.kuali.kra.meeting.CommitteeScheduleMinute;
import org.kuali.kra.meeting.MinuteEntryType;
import org.kuali.kra.printing.Printable;
import org.kuali.kra.printing.print.AbstractPrint;
import org.kuali.kra.printing.util.PrintingUtils;
import org.kuali.kra.proposaldevelopment.bo.AttachmentDataSource;
import org.kuali.kra.service.TaskAuthorizationService;
import org.kuali.kra.web.struts.action.AuditActionHelper;
import org.kuali.kra.web.struts.action.KraTransactionalDocumentActionBase;
import org.kuali.kra.web.struts.action.StrutsConfirmation;
import org.kuali.rice.kns.question.ConfirmationQuestion;
import org.kuali.rice.kns.service.DateTimeService;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.KNSConstants;
import org.kuali.rice.kns.web.struts.action.AuditModeAction;

/**
 * The set of actions for the Protocol Actions tab.
 */
public class ProtocolProtocolActionsAction extends ProtocolAction implements AuditModeAction {

    private static final Log LOG = LogFactory.getLog(ProtocolProtocolActionsAction.class);
    private static final String CONFIRM_NO_DELETE = "";
    private static final String CONFIRM_DELETE_ACTION_ATT = "confirmDeleteActionAttachment";
    
    private static final String PROTOCOL_TAB = "protocol";
    private static final String CONFIRM_SUBMIT_FOR_REVIEW_KEY = "confirmSubmitForReview";
    private static final String CONFIRM_ASSIGN_TO_AGENDA_KEY = "confirmAssignToAgenda";
    private static final String CONFIRM_ASSIGN_CMT_SCHED_KEY = "confirmAssignCmtSched";
    private static final String CONIFRM_REMOVE_REVIEWER_KEY="confirmRemoveReviewer";
    private static final String CONFIRM_REMOVE_EXISTING_REVIEWS_KEY="confirmRemoveExistingReviews";
    private static final String SCHEDULE_CHANGE_REMOVE_ONLINE_REVIEW_ANNOTATION="Online Review removed due to submission committee or schedule change.";
    
    
    private static final String NOT_FOUND_SELECTION = "The attachment was not found for selection ";

    private static final String CONFIRM_DELETE_PROTOCOL_KEY = "confirmDeleteProtocol";

    /** signifies that a response has already be handled therefore forwarding to obtain a response is not needed. */
    private static final ActionForward RESPONSE_ALREADY_HANDLED = null;
    private static final String SUBMISSION_ID = "submissionId";
     
    
    private static final Map<String, String> PRINTTAG_MAP = new HashMap<String, String>() {
        {
            put("summary", "PROTOCOL_SUMMARY_VIEW_REPORT");
            put("full", "PROTOCOL_FULL_PROTOCOL_REPORT");
            put("history", "PROTOCOL_PROTOCOL_HISTORY_REPORT");
            put("comments", "PROTOCOL_REVIEW_COMMENTS_REPORT");
    }};

    
    /** {@inheritDoc} */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        ActionForward actionForward = super.execute(mapping, form, request, response);
        ProtocolForm protocolForm =  (ProtocolForm) form;
        protocolForm.getActionHelper().prepareView();
        // submit action may change "submission details", so re-initializa it
        protocolForm.getActionHelper().initSubmissionDetails();
        return actionForward;
    }

    /**
     * Invoked when the "copy protocol" button is clicked.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward copyProtocol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {


        ProtocolForm protocolForm = (ProtocolForm) form;

        ApplicationTask task = new ApplicationTask(TaskName.CREATE_PROTOCOL);
        if (isAuthorized(task)) {
            String newDocId = getProtocolCopyService().copyProtocol(protocolForm.getDocument()).getDocumentNumber();

            // Switch over to the new protocol document and
            // go to the Protocol tab web page.

            protocolForm.setDocId(newDocId);
            loadDocument(protocolForm);
            protocolForm.getActionHelper().setCurrentSubmissionNumber(-1);
            protocolForm.getProtocolHelper().prepareView();
            protocolForm.getActionHelper().prepareCommentsView();

            return mapping.findForward(PROTOCOL_TAB);
        }

        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /** {@inheritDoc} */
    public ActionForward activate(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        return new AuditActionHelper().setAuditMode(mapping, (ProtocolForm) form, true);
    }

    /** {@inheritDoc} */
    public ActionForward deactivate(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        return new AuditActionHelper().setAuditMode(mapping, (ProtocolForm) form, false);
    }

    /**
     * Refreshes the page. We only need to redraw the page. This method is used when JavaScript is disabled. During a review
     * submission action, the user will have to refresh the page. For example, after a committee is selected, the page needs to be
     * refreshed so that the available scheduled dates for that committee can be displayed in the drop-down menu for the scheduled
     * dates. Please see ProtocolSubmitAction.prepareView() for how the Submit for Review works on a refresh.
     * 
     * @param mapping the mapping associated with this action.
     * @param form the Protocol form.
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the name of the HTML page to display
     * @throws Exception doesn't ever really happen
     */
    public ActionForward refreshPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * Submit a protocol for review.
     * 
     * @param mapping the mapping associated with this action.
     * @param form the Protocol form.
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the name of the HTML page to display
     * @throws Exception
     */
    public ActionForward submitForReview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = mapping.findForward(Constants.MAPPING_BASIC);

        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolDocument protocolDocument = protocolForm.getProtocolDocument();
        protocolForm.setAuditActivated(true);
        ProtocolTask task = new ProtocolTask(TaskName.SUBMIT_PROTOCOL, protocolDocument.getProtocol());
        if (isAuthorized(task)) {
            ProtocolSubmitAction submitAction = protocolForm.getActionHelper().getProtocolSubmitAction();            
            if (applyRules(new ProtocolSubmitActionEvent(protocolDocument, submitAction))) {
                AuditActionHelper auditActionHelper = new AuditActionHelper();
                if (auditActionHelper.auditUnconditionally(protocolDocument)) {
                    if (isCommitteeMeetingAssignedMaxProtocols(submitAction.getNewCommitteeId(), submitAction.getNewScheduleId())) {
                        forward = confirm(buildSubmitForReviewConfirmationQuestion(mapping, form, request, response), CONFIRM_SUBMIT_FOR_REVIEW_KEY, "");
                    } else {
                        forward = submitForReviewAndRedirect(mapping, form, request, response);
                    }
                } else {
                    GlobalVariables.getMessageMap().clearErrorMessages();
                    GlobalVariables.getMessageMap().putError("datavalidation", KeyConstants.ERROR_WORKFLOW_SUBMISSION,  new String[] {});
                }
            }
        }

        return forward;
    }
    
    private boolean isCommitteeMeetingAssignedMaxProtocols(String committeeId, String scheduleId) {
        boolean isMax = false;
        
        Committee committee = getCommitteeService().getCommitteeById(committeeId);
        if (committee != null) {
            CommitteeSchedule schedule = getCommitteeService().getCommitteeSchedule(committee, scheduleId);
            if (schedule != null) {
                int currentSubmissionCount = (schedule.getProtocolSubmissions() == null) ? 0 : schedule.getProtocolSubmissions().size();
                int maxSubmissionCount = schedule.getMaxProtocols();
                isMax = currentSubmissionCount >= maxSubmissionCount;
            }
        }
        
        return isMax;
    }

    /*
     * Builds the confirmation question to verify if the user wants to submit the protocol for review.
     */
    private StrutsConfirmation buildSubmitForReviewConfirmationQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return buildParameterizedConfirmationQuestion(mapping, form, request, response, CONFIRM_SUBMIT_FOR_REVIEW_KEY,
                KeyConstants.QUESTION_PROTOCOL_CONFIRM_SUBMIT_FOR_REVIEW);
    }

    /**
     * Method dispatched from <code>{@link KraTransactionalDocumentActionBase#confirm(StrutsQuestion, String, String)}</code> for
     * when a "yes" condition is met.
     * 
     * @param mapping The mapping associated with this action.
     * @param form The Protocol form.
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the destination
     * @throws Exception
     * @see KraTransactionalDocumentActionBase#confirm(StrutsQuestion, String, String)
     */
    public ActionForward confirmSubmitForReview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws Exception {
        
        ActionForward forward = mapping.findForward(Constants.MAPPING_BASIC);
        
        Object question = request.getParameter(QUESTION_INST_ATTRIBUTE_NAME);
        if (CONFIRM_SUBMIT_FOR_REVIEW_KEY.equals(question)) {
            forward = submitForReviewAndRedirect(mapping, form, request, response);
        }

        return forward;
    }
    
    /**
     * Submits the Protocol for review and calculates the redirect back to the portal page, adding in the proper parameters for displaying a message to the
     * user upon successful submission.
     * 
     * @param mapping The mapping associated with this action.
     * @param form The Protocol form.
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the destination
     * @throws Exception
     */
    private ActionForward submitForReviewAndRedirect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolDocument protocolDocument = protocolForm.getProtocolDocument();
        ProtocolSubmitAction submitAction = protocolForm.getActionHelper().getProtocolSubmitAction();
        
        getProtocolSubmitActionService().submitToIrbForReview(protocolDocument.getProtocol(), submitAction);
        protocolForm.getActionHelper().getAssignCmtSchedBean().init();
        
        super.route(mapping, protocolForm, request, response);
        
        return createSuccessfulSubmitRedirect("Protocol", protocolDocument.getProtocol().getProtocolNumber(), request, mapping, protocolForm);
    }

    /**
     * Withdraw a previously submitted protocol.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward withdrawProtocol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_WITHDRAW, protocolForm.getProtocolDocument().getProtocol());
        if (isAuthorized(task)) {
            ProtocolDocument pd = getProtocolWithdrawService().withdraw(protocolForm.getProtocolDocument().getProtocol(),
                    protocolForm.getActionHelper().getProtocolWithdrawBean());

            protocolForm.setDocId(pd.getDocumentNumber());
            loadDocument(protocolForm);
            protocolForm.getProtocolHelper().prepareView();
            
            recordProtocolActionSuccess("Withdraw");

            return mapping.findForward(PROTOCOL_TAB);
        }

        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Request a close of the protocol.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward closeRequestProtocol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_CLOSE, protocolForm.getProtocolDocument().getProtocol());
        if (isAuthorized(task)) {
            ProtocolRequestBean closeRequestBean = protocolForm.getActionHelper().getProtocolCloseRequestBean();
            if (applyRules(new ProtocolRequestEvent(protocolForm.getProtocolDocument(),
                Constants.PROTOCOL_CLOSE_REQUEST_PROPERTY_KEY, closeRequestBean))) {
                getProtocolRequestService().submitRequest(protocolForm.getProtocolDocument().getProtocol(), closeRequestBean);
            
                recordProtocolActionSuccess("Request to Close");
            }
        }
        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Request a suspension of a protocol.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward suspendRequestProtocol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_SUSPENSION, protocolForm.getProtocolDocument().getProtocol());
        if (isAuthorized(task)) {
            ProtocolRequestBean suspendRequestBean = protocolForm.getActionHelper().getProtocolSuspendRequestBean();
            if (applyRules(new ProtocolRequestEvent(protocolForm.getProtocolDocument(),
                Constants.PROTOCOL_SUSPEND_REQUEST_PROPERTY_KEY, suspendRequestBean))) {
                getProtocolRequestService().submitRequest(protocolForm.getProtocolDocument().getProtocol(), suspendRequestBean);
            
                recordProtocolActionSuccess("Request for Suspension");
            }
        }
        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Request a close of enrollment for a protocol.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward closeEnrollmentRequestProtocol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_CLOSE_ENROLLMENT, protocolForm.getProtocolDocument()
                .getProtocol());
        if (isAuthorized(task)) {
            ProtocolRequestBean closeEnrollmentRequestBean = protocolForm.getActionHelper().getProtocolCloseEnrollmentRequestBean();
            if (applyRules(new ProtocolRequestEvent(protocolForm.getProtocolDocument(),
                Constants.PROTOCOL_CLOSE_ENROLLMENT_REQUEST_PROPERTY_KEY, closeEnrollmentRequestBean))) {
                getProtocolRequestService().submitRequest(protocolForm.getProtocolDocument().getProtocol(), closeEnrollmentRequestBean);
            
                recordProtocolActionSuccess("Request to Close Enrollment");
            }
        }
        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Request to re-open enrollment for a protocol.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward reopenEnrollmentRequestProtocol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_REOPEN_ENROLLMENT, protocolForm.getProtocolDocument()
                .getProtocol());
        if (isAuthorized(task)) {
            ProtocolRequestBean reopenEnrollmentRequestBean = protocolForm.getActionHelper()
                    .getProtocolReOpenEnrollmentRequestBean();
            if (applyRules(new ProtocolRequestEvent(protocolForm.getProtocolDocument(),
                Constants.PROTOCOL_REOPEN_ENROLLMENT_REQUEST_PROPERTY_KEY, reopenEnrollmentRequestBean))) {
                getProtocolRequestService().submitRequest(protocolForm.getProtocolDocument().getProtocol(), reopenEnrollmentRequestBean);
            
                recordProtocolActionSuccess("Request to Re-open Enrollment");
            }
        }
        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Request for data analysis only for a protocol.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward dataAnalysisOnlyRequestProtocol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_DATA_ANALYSIS, protocolForm.getProtocolDocument()
                .getProtocol());
        if (isAuthorized(task)) {
            ProtocolRequestBean dataAnalysisRequestBean = protocolForm.getActionHelper().getProtocolDataAnalysisRequestBean();
            if (applyRules(new ProtocolRequestEvent(protocolForm.getProtocolDocument(),
                Constants.PROTOCOL_DATA_ANALYSIS_REQUEST_PROPERTY_KEY, dataAnalysisRequestBean))) {
                getProtocolRequestService().submitRequest(protocolForm.getProtocolDocument().getProtocol(), dataAnalysisRequestBean);
            
                recordProtocolActionSuccess("Request for Data Analysis Only");
            }
        }
        return mapping.findForward(MAPPING_BASIC);
    }
    
    
    public ActionForward terminateRequestProtocol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REQUEST_TERMINATE, protocolForm.getProtocolDocument()
                .getProtocol());
        if (isAuthorized(task)) {
            ProtocolRequestBean terminateRequestBean = protocolForm.getActionHelper().getProtocolTerminateRequestBean();
            if (applyRules(new ProtocolRequestEvent(protocolForm.getProtocolDocument(),
                Constants.PROTOCOL_TERMINATE_REQUEST_PROPERTY_KEY, terminateRequestBean))) {
                getProtocolRequestService().submitRequest(protocolForm.getProtocolDocument().getProtocol(), terminateRequestBean);
            
                recordProtocolActionSuccess("Request for Termination");
            }
        }
        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Notify the IRB office.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward notifyIrbProtocol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        getProtocolNotifyIrbService().submitIrbNotification(protocolForm.getProtocolDocument().getProtocol(),
                protocolForm.getActionHelper().getProtocolNotifyIrbBean());
        LOG.info("notifyIrbProtocol "+ protocolForm.getProtocolDocument().getDocumentNumber());

        recordProtocolActionSuccess("Notify IRB");
        
        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Create an Amendment.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward createAmendment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;

        ProtocolTask task = new ProtocolTask(TaskName.CREATE_PROTOCOL_AMMENDMENT, protocolForm.getProtocolDocument().getProtocol());
        if (isAuthorized(task)) {
            if (!applyRules(new CreateAmendmentEvent(protocolForm.getProtocolDocument(), Constants.PROTOCOL_CREATE_AMENDMENT_KEY,
                protocolForm.getActionHelper().getProtocolAmendmentBean()))) {
                return mapping.findForward(MAPPING_BASIC);
            }

            String newDocId = getProtocolAmendRenewService().createAmendment(protocolForm.getProtocolDocument(),
                    protocolForm.getActionHelper().getProtocolAmendmentBean());
            // Switch over to the new protocol document and
            // go to the Protocol tab web page.

            protocolForm.setDocId(newDocId);
            loadDocument(protocolForm);
            protocolForm.getActionHelper().setCurrentSubmissionNumber(-1);
            protocolForm.getProtocolHelper().prepareView();
            
            recordProtocolActionSuccess("Create Amendment");
            
            return mapping.findForward(PROTOCOL_TAB);
        }
        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Create an Amendment.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward modifyAmendmentSections(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        Protocol protocol = protocolForm.getProtocolDocument().getProtocol();
        
        ProtocolTask task = new ProtocolTask(TaskName.MODIFY_PROTOCOL_AMMENDMENT_SECTIONS, protocol);
        if (isAuthorized(task)) {
            if (!applyRules(new ModifyAmendmentSectionsEvent(protocolForm.getProtocolDocument(), Constants.PROTOCOL_CREATE_AMENDMENT_KEY,
                protocolForm.getActionHelper().getProtocolAmendmentBean()))) {
                return mapping.findForward(MAPPING_BASIC);
            }

            getProtocolAmendRenewService().updateAmendmentRenewal(protocolForm.getProtocolDocument(), 
                    protocolForm.getActionHelper().getProtocolAmendmentBean());
            
            return save(mapping, protocolForm, request, response);
        }
            
        return mapping.findForward(MAPPING_BASIC);
    }
    
    /**
     * Create a Renewal without an Amendment.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward createRenewal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolTask task = new ProtocolTask(TaskName.CREATE_PROTOCOL_RENEWAL, protocolForm.getProtocolDocument().getProtocol());
        if (isAuthorized(task)) {
            if (!applyRules(new CreateRenewalEvent(protocolForm.getProtocolDocument(),
                    Constants.PROTOCOL_CREATE_RENEWAL_SUMMARY_KEY, protocolForm.getActionHelper().getRenewalSummary()))) {
                    return mapping.findForward(MAPPING_BASIC);
                }
            String newDocId = getProtocolAmendRenewService().createRenewal(protocolForm.getProtocolDocument(),((ProtocolForm) form).getActionHelper().getRenewalSummary());
            // Switch over to the new protocol document and
            // go to the Protocol tab web page.

            protocolForm.setDocId(newDocId);
            loadDocument(protocolForm);

            protocolForm.getActionHelper().setCurrentSubmissionNumber(-1);
            protocolForm.getProtocolHelper().prepareView();
            
            recordProtocolActionSuccess("Create Renewal without Amendment");
            
            // Form fields copy needed to support modifyAmendmentSections
            protocolForm.getActionHelper().getProtocolAmendmentBean().setSummary(protocolForm.getActionHelper().getRenewalSummary());
            
            return mapping.findForward(PROTOCOL_TAB);
        }
        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Create a Renewal with an Amendment.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward createRenewalWithAmendment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolTask task = new ProtocolTask(TaskName.CREATE_PROTOCOL_RENEWAL, protocolForm.getProtocolDocument().getProtocol());
        if (isAuthorized(task)) {
            if (!applyRules(new CreateAmendmentEvent(protocolForm.getProtocolDocument(),
                Constants.PROTOCOL_CREATE_RENEWAL_WITH_AMENDMENT_KEY, protocolForm.getActionHelper()
                        .getProtocolRenewAmendmentBean()))) {
                return mapping.findForward(MAPPING_BASIC);
            }

            String newDocId = getProtocolAmendRenewService().createRenewalWithAmendment(protocolForm.getProtocolDocument(),
                    protocolForm.getActionHelper().getProtocolRenewAmendmentBean());
            // Switch over to the new protocol document and
            // go to the Protocol tab web page.

            protocolForm.setDocId(newDocId);
            loadDocument(protocolForm);

            protocolForm.getActionHelper().setCurrentSubmissionNumber(-1);
            protocolForm.getProtocolHelper().prepareView();
            
            recordProtocolActionSuccess("Create Renewal with Amendment");
            
            // Form fields copy needed to support modifyAmendmentSections
            protocolForm.getActionHelper().setProtocolAmendmentBean(protocolForm.getActionHelper().getProtocolRenewAmendmentBean());
            
            return mapping.findForward(PROTOCOL_TAB);
        }
        return mapping.findForward(MAPPING_BASIC);
    }
    
    /**
     * Delete a Protocol/Amendment/Renewal. Remember that amendments and renewals are simply protocol documents that were copied
     * from a protocol.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteProtocol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_AMEND_RENEW_DELETE, protocolForm.getProtocolDocument().getProtocol());
        if (isAuthorized(task)) {
            return confirm(buildDeleteProtocolConfirmationQuestion(mapping, form, request, response), CONFIRM_DELETE_PROTOCOL_KEY,
                    "");

        }
        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * If the user confirms the deletion, then delete the protocol.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward confirmDeleteProtocol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Object question = request.getParameter(QUESTION_INST_ATTRIBUTE_NAME);
        if (CONFIRM_DELETE_PROTOCOL_KEY.equals(question)) {
            ProtocolForm protocolForm = (ProtocolForm) form;
            getProtocolDeleteService().delete(protocolForm.getProtocolDocument().getProtocol(),
                    protocolForm.getActionHelper().getProtocolDeleteBean());
            
            recordProtocolActionSuccess("Delete Protocol, Amendment, or Renewal");
        }
        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Build the question to ask the user. Ask if they really want to delete the protocol.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    private StrutsConfirmation buildDeleteProtocolConfirmationQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolDocument doc = ((ProtocolForm) form).getDocument();
        String protocolNumber = doc.getProtocol().getProtocolNumber();
        return buildParameterizedConfirmationQuestion(mapping, form, request, response, CONFIRM_DELETE_PROTOCOL_KEY,
                KeyConstants.QUESTION_DELETE_PROTOCOL_CONFIRMATION, protocolNumber);
    }


    /**
     * 
     * This method is to view protocol attachment at protocol actions/print
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewProtocolAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        int selected = getSelectedLine(request);
        ProtocolAttachmentProtocol attachment = protocolForm.getProtocolDocument().getProtocol().getActiveAttachmentProtocols().get(selected);
        return printAttachmentProtocol(mapping, response, attachment);

    }
    
    public ActionForward viewProtocolPersonnelAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        int selected = getSelectedLine(request);
        ProtocolAttachmentPersonnel personAttach = protocolForm.getProtocolDocument().getProtocol().getAttachmentPersonnel(selected);
        return printAttachmentProtocol(mapping, response, personAttach);

    }
  
    /**
     * 
     * This method is to print protocol reports
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward printProtocolDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolForm protocolForm = (ProtocolForm) form;
        Protocol protocol = protocolForm.getProtocolDocument().getProtocol();
        ActionForward forward = mapping.findForward(MAPPING_BASIC);
        ActionHelper actionHelper = protocolForm.getActionHelper();
        StringBuffer fileName = new StringBuffer().append("Protocol-");
        if (applyRules(new ProtocolActionPrintEvent(protocolForm.getProtocolDocument(), actionHelper.getSummaryReport(),
            actionHelper.getFullReport(), actionHelper.getHistoryReport(), actionHelper.getReviewCommentsReport()))) {
            ProtocolPrintType printType = ProtocolPrintType.PROTOCOL_FULL_PROTOCOL_REPORT;
            String reportName = protocol.getProtocolNumber()+"-"+printType.getReportName();
            AttachmentDataSource dataStream = getProtocolPrintingService().print(reportName,getPrintReportArtifacts(protocolForm, fileName));
            if (dataStream.getContent() != null) {
                dataStream.setFileName(fileName.toString());
                PrintingUtils.streamToResponse(dataStream, response);
                forward = null;
            }
        }


        return forward;
    }
    private Map<Class,Object> getReportOptions(ProtocolForm protocolForm, ProtocolPrintType printType) {
        Map<Class,Object> reportParameters = new HashMap<Class, Object>();
        ProtocolSummaryPrintOptions summaryOptions = protocolForm.getActionHelper().getProtocolSummaryPrintOptions();
        if(printType.equals(ProtocolPrintType.PROTOCOL_FULL_PROTOCOL_REPORT)){
            summaryOptions.setActions(true);
            summaryOptions.setAmendmentRenewalHistory(true);
            summaryOptions.setAmmendmentRenewalSummary(true);
            summaryOptions.setAreaOfResearch(true);
            summaryOptions.setAttachments(true);
            summaryOptions.setCorrespondents(true);
            summaryOptions.setDocuments(true);
            summaryOptions.setFundingSource(true);
            summaryOptions.setInvestigator(true);
            summaryOptions.setNotes(true);
            summaryOptions.setOrganizaition(true);
            summaryOptions.setProtocolDetails(true);
            summaryOptions.setReferences(true);
            summaryOptions.setRiskLevel(true);
            summaryOptions.setRoles(true);
            summaryOptions.setSpecialReview(true);
            summaryOptions.setStudyPersonnels(true);
            summaryOptions.setSubjects(true);
        }
        reportParameters.put(ProtocolSummaryPrintOptions.class, summaryOptions);
        return reportParameters;
    }

    /*
     * set up all artifacts and filename
     */
    private List<Printable> getPrintReportArtifacts(ActionForm form, StringBuffer fileName) {
        ProtocolForm protocolForm = (ProtocolForm) form;
        Boolean printSummary = protocolForm.getActionHelper().getSummaryReport();
        Boolean printFull = protocolForm.getActionHelper().getFullReport();
        Boolean printHistory = protocolForm.getActionHelper().getHistoryReport();
        Boolean printReviewComments = protocolForm.getActionHelper().getReviewCommentsReport();
        List<Printable> printableArtifactList = new ArrayList<Printable>();
        if (printSummary) {
            Map reportParameters = getReportOptions(protocolForm,ProtocolPrintType.PROTOCOL_SUMMARY_VIEW_REPORT);
            printableArtifactList.add(getPrintableArtifacts(protocolForm.getProtocolDocument().getProtocol(), "summary", fileName,reportParameters));
            protocolForm.getActionHelper().setSummaryReport(false);
        }
        if (printFull) {
            Map reportParameters = getReportOptions(protocolForm,ProtocolPrintType.PROTOCOL_FULL_PROTOCOL_REPORT);
            printableArtifactList.add(getPrintableArtifacts(protocolForm.getProtocolDocument().getProtocol(), "full", fileName,reportParameters));
            protocolForm.getActionHelper().setFullReport(false);
        }
        if (printHistory) {
            Map reportParameters = getReportOptions(protocolForm,ProtocolPrintType.PROTOCOL_PROTOCOL_HISTORY_REPORT);
            printableArtifactList.add(getPrintableArtifacts(protocolForm.getProtocolDocument().getProtocol(), "history", fileName,reportParameters));
            protocolForm.getActionHelper().setHistoryReport(false);
        }
        if (printReviewComments) {
            Map reportParameters = getReportOptions(protocolForm,ProtocolPrintType.PROTOCOL_REVIEW_COMMENTS_REPORT);
            printableArtifactList
                    .add(getPrintableArtifacts(protocolForm.getProtocolDocument().getProtocol(), "comments", fileName,reportParameters));
            protocolForm.getActionHelper().setReviewCommentsReport(false);
        }
        fileName.append("report.pdf");
        return printableArtifactList;
    }
    
    /*
     * This is to view attachment if attachment is seleccted in print panel.
     */
    private ActionForward printAttachmentProtocol(ActionMapping mapping, HttpServletResponse response, ProtocolAttachmentBase attachment) throws Exception {

        if (attachment == null) {
            return mapping.findForward(Constants.MAPPING_BASIC);
        }

        final AttachmentFile file = attachment.getFile();
        this.streamToResponse(file.getData(), getValidHeaderString(file.getName()), getValidHeaderString(file.getType()), response);

        return RESPONSE_ALREADY_HANDLED;
    }

    /**
     * Filters the actions shown in the History sub-panel, first validating the dates before filtering and refreshing the page.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward filterHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolForm protocolForm = (ProtocolForm) form;
        Date startDate = protocolForm.getActionHelper().getFilteredHistoryStartDate();
        Date endDate = protocolForm.getActionHelper().getFilteredHistoryEndDate();
        
        if (applyRules(new ProtocolHistoryFilterDatesEvent(protocolForm.getDocument(), startDate, endDate))) {
            protocolForm.getActionHelper().initFilterDatesView();
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Shows all of the actions in the History sub-panel.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward resetHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ProtocolForm protocolForm = (ProtocolForm) form;
        protocolForm.getActionHelper().setFilteredHistoryStartDate(null);
        protocolForm.getActionHelper().setFilteredHistoryEndDate(null);
        protocolForm.getActionHelper().initFilterDatesView();
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * Load a Protocol summary into the summary sub-panel. The protocol summary to load corresponds to the currently selected
     * protocol action in the History sub-panel.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward loadProtocolSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolForm protocolForm = (ProtocolForm) form;
        org.kuali.kra.irb.actions.ProtocolAction action = protocolForm.getActionHelper().getSelectedProtocolAction();
        if (action != null) {
            protocolForm.getActionHelper().setCurrentSequenceNumber(action.getSequenceNumber());
        }
        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * View an attachment via the Summary sub-panel.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewAttachmentProtocol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolSummary protocolSummary = protocolForm.getActionHelper().getProtocolSummary();
        int selectedIndex = getSelectedLine(request);
        AttachmentSummary attachmentSummary = protocolSummary.getAttachments().get(selectedIndex);
        
        ProtocolAttachmentProtocol attachment = getProtocolAttachmentService().getAttachment(ProtocolAttachmentProtocol.class, 
                attachmentSummary.getAttachmentId());
        AttachmentFile file = attachment.getFile();
        streamToResponse(file.getData(), getValidHeaderString(file.getName()), getValidHeaderString(file.getType()), response);
        
        return RESPONSE_ALREADY_HANDLED;
    }

    /**
     * Go to the previous summary.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewPreviousProtocolSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        actionHelper.setCurrentSequenceNumber(actionHelper.getCurrentSequenceNumber() - 1);
        ((ProtocolForm) form).getActionHelper().initSummaryDetails();

        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Go to the next summary.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewNextProtocolSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        actionHelper.setCurrentSequenceNumber(actionHelper.getCurrentSequenceNumber() + 1);
        ((ProtocolForm) form).getActionHelper().initSummaryDetails();
        
        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * 
     * This method to load previous submission for display
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewPreviousSubmission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        actionHelper.setCurrentSubmissionNumber(actionHelper.getPrevSubmissionNumber());
        protocolForm.getActionHelper().initSubmissionDetails();
        return mapping.findForward(MAPPING_BASIC);
    }
    
    /**
     * 
     * This method is to load next submission for display
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewNextSubmission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        actionHelper.setCurrentSubmissionNumber(actionHelper.getNextSubmissionNumber());
        protocolForm.getActionHelper().initSubmissionDetails();
        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * Quotes a string that follows RFC 822 and is valid to include in an http header.
     * 
     * <p>
     * This really should be a part of {@link org.kuali.rice.kns.util.WebUtils WebUtils}.
     * <p>
     * 
     * For example: without this method, file names with spaces will not show up to the client correctly.
     * 
     * <p>
     * This method is not doing a Base64 encode just a quoted printable character otherwise we would have to set the encoding type
     * on the header.
     * <p>
     * 
     * @param s the original string
     * @return the modified header string
     */
    private String getValidHeaderString(String s) {
        return MimeUtility.quote(s, HeaderTokenizer.MIME);
    }


    /**
     * 
     * This method is to render protocol action page when 'view' is clicked in meeting page, Protocol submitted panel.
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
        fieldValues.put(SUBMISSION_ID, request.getParameter(SUBMISSION_ID));
        ProtocolSubmission protocolSubmission = (ProtocolSubmission) getBusinessObjectService().findByPrimaryKey(ProtocolSubmission.class, fieldValues);
        protocolSubmission.getProtocol().setProtocolSubmission(protocolSubmission);
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        protocolForm.setDocId(protocolSubmission.getProtocol().getProtocolDocument().getDocumentNumber());
        loadDocument(protocolForm);
        protocolForm.initialize();
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    
    /**
     * 
     * This method...
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward assignToAgenda(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolTask task = new ProtocolTask(TaskName.ASSIGN_TO_AGENDA, protocolForm.getProtocolDocument().getProtocol());
        
        if (isAuthorized(task)) {
            ProtocolAssignToAgendaBean actionBean = protocolForm.getActionHelper().getAssignToAgendaBean();
            if (applyRules(new ProtocolAssignToAgendaEvent(protocolForm.getProtocolDocument(), actionBean))) {               
                getProtocolAssignToAgendaService().assignToAgenda(protocolForm.getProtocolDocument().getProtocol(), actionBean);
                saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
                
                recordProtocolActionSuccess("Assign to Agenda");
            }
        }

        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward protocolReviewNotRequired(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_REVIEW_NOT_REQUIRED, protocolForm.getProtocolDocument().getProtocol());
        if (isAuthorized(task)) {
            ProtocolReviewNotRequiredBean actionBean = protocolForm.getActionHelper().getProtocolReviewNotRequiredBean();
            if (applyRules(new ProtocolReviewNotRequiredEvent(protocolForm.getProtocolDocument(), actionBean))) {
                KraServiceLocator.getService(ProtocolReviewNotRequiredService.class).reviewNotRequired(protocolForm.getProtocolDocument(), actionBean);
            
                recordProtocolActionSuccess("Review Not Required");
            }
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward addAssignToAgendaReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getAssignToAgendaBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_ASSIGN_TO_AGENDA_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    } 
    
    public ActionForward deleteAssignToAgendaReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolAssignToAgendaBean actionBean = protocolForm.getActionHelper().getAssignToAgendaBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveUpAssignToAgendaReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolAssignToAgendaBean actionBean = protocolForm.getActionHelper().getAssignToAgendaBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveDownAssignToAgendaReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolAssignToAgendaBean actionBean = protocolForm.getActionHelper().getAssignToAgendaBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    
    /**
     * Assign a protocol to a committee/schedule.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward assignCommitteeSchedule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolForm protocolForm = (ProtocolForm) form;
        final String callerString = "assignCommitteeSchedule";
        ProtocolTask task = new ProtocolTask(TaskName.ASSIGN_TO_COMMITTEE_SCHEDULE, protocolForm.getProtocolDocument().getProtocol());
       
        if (isAuthorized(task)) {
            ProtocolAssignCmtSchedBean actionBean = protocolForm.getActionHelper().getAssignCmtSchedBean();
            if (applyRules(new ProtocolAssignCmtSchedEvent(protocolForm.getProtocolDocument(), actionBean))) {
                
                if( protocolForm.getProtocolDocument().getProtocol().getProtocolSubmission() != null) {
                    boolean performAssignment = false;
                    Object question = request.getParameter(KNSConstants.QUESTION_INST_ATTRIBUTE_NAME);
                    Object buttonClicked = request.getParameter(KNSConstants.QUESTION_CLICKED_BUTTON);
                
                    if ((CONFIRM_REMOVE_EXISTING_REVIEWS_KEY.equals(question) || actionBean.scheduleHasChanged() && getProtocolOnlineReviewService().getProtocolReviewDocumentsForCurrentSubmission(protocolForm.getProtocolDocument().getProtocol()).size()>0)) {
                        //There are existing reviews and we are changing schedules
                        //need to verify with the user that they want to remove the existing reviews before proceeding.
                        if (question==null || !CONFIRM_REMOVE_EXISTING_REVIEWS_KEY.equals(question)) {
                            return performQuestionWithoutInput(mapping, form, request, response, CONFIRM_REMOVE_EXISTING_REVIEWS_KEY,
                                    getKualiConfigurationService().getPropertyString(KeyConstants.QUESTION_CONFIRM_SCHEDULE_CHANGE_REMOVE_EXISTING_REVIEWS), KNSConstants.CONFIRMATION_QUESTION, callerString, "" );
                        } else if (ConfirmationQuestion.YES.equals(buttonClicked)) {
                               getProtocolOnlineReviewService().removeOnlineReviews(protocolForm.getProtocolDocument().getProtocol().getProtocolSubmission(),SCHEDULE_CHANGE_REMOVE_ONLINE_REVIEW_ANNOTATION);
                        } else {
                            return mapping.findForward(Constants.MAPPING_BASIC);
                        }
                    }
                
                    if (isCommitteeMeetingAssignedMaxProtocols(actionBean.getNewCommitteeId(), actionBean.getNewScheduleId())) {
                        //There are existing reviews and we are changing schedules
                        //need to verify with the user that they want to remove the existing reviews before proceeding.
                        if (question==null || !CONFIRM_ASSIGN_CMT_SCHED_KEY.equals(question)) {
                            return performQuestionWithoutInput(mapping, form, request, response, CONFIRM_ASSIGN_CMT_SCHED_KEY,
                                    getKualiConfigurationService().getPropertyString(KeyConstants.QUESTION_PROTOCOL_CONFIRM_SUBMIT_FOR_REVIEW), KNSConstants.CONFIRMATION_QUESTION, callerString, "" );
                        } else if (ConfirmationQuestion.YES.equals(buttonClicked)) {
                            performAssignment = true;
                        } else {
                            //nothing to do, answered no.
                        }
                    } else {
                        performAssignment = true;
                    }
    
                    if (performAssignment) {
                        getProtocolAssignCmtSchedService().assignToCommitteeAndSchedule(protocolForm.getProtocolDocument().getProtocol(), actionBean);
                        recordProtocolActionSuccess("Assign to Committee and Schedule");
                    }
                    ((ProtocolForm)form).getActionHelper().prepareView();
                }
            }
        }

        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
     /**
     * 
     * Builds the confirmation question to verify if the user wants to assign the protocol to the committee.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    private StrutsConfirmation buildAssignToAgendaConfirmationQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return buildParameterizedConfirmationQuestion(mapping, form, request, response, CONFIRM_ASSIGN_TO_AGENDA_KEY,
                KeyConstants.QUESTION_PROTOCOL_CONFIRM_SUBMIT_FOR_REVIEW);
    }
   
    public ActionForward confirmAssignToAgenda(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Object question = request.getParameter(QUESTION_INST_ATTRIBUTE_NAME);

        if (CONFIRM_ASSIGN_TO_AGENDA_KEY.equals(question)) {
            ProtocolForm protocolForm = (ProtocolForm) form;
            ProtocolAssignToAgendaBean actionBean = protocolForm.getActionHelper().getAssignToAgendaBean();
            getProtocolAssignToAgendaService().assignToAgenda(protocolForm.getProtocolDocument().getProtocol(), actionBean);
        }

        return mapping.findForward(MAPPING_BASIC);
    }
    
    /**
     * Assign a protocol to some reviewers.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward assignReviewers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionForward forward = mapping.findForward(Constants.MAPPING_BASIC);
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolTask task = new ProtocolTask(TaskName.ASSIGN_REVIEWERS, protocolForm.getProtocolDocument().getProtocol());
        String callerString = String.format("assignReviewers");
        Object question = request.getParameter(KNSConstants.QUESTION_INST_ATTRIBUTE_NAME);
        
        if (isAuthorized(task)) {
            ProtocolAssignReviewersBean actionBean = protocolForm.getActionHelper().getProtocolAssignReviewersBean();
            if (applyRules(new ProtocolAssignReviewersEvent(protocolForm.getProtocolDocument(), actionBean))) {
                boolean processRequest = true;
                
                if (GlobalVariables.getMessageMap().hasWarnings()) {
                    if (question == null) {
                        // ask question if not already asked
                        forward = performQuestionWithoutInput(mapping, form, request, response, 
                                                                CONIFRM_REMOVE_REVIEWER_KEY, 
                                                                getKualiConfigurationService().getPropertyString(KeyConstants.MESSAGE_REMOVE_REVIEWERS_WITH_COMMENTS), 
                                                                KNSConstants.CONFIRMATION_QUESTION, 
                                                                callerString, 
                                                                "");
                        processRequest = false;
                    }
                    else {
                        Object buttonClicked = request.getParameter(KNSConstants.QUESTION_CLICKED_BUTTON);
                        if ((KNSConstants.DOCUMENT_DISAPPROVE_QUESTION.equals(question)) && ConfirmationQuestion.NO.equals(buttonClicked)) {
                            // if no button clicked just reload the doc
                            processRequest = false;
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("User declined to confirm the request, not processing.");
                            }
                        }
                    }
                
                }
                
                if (processRequest) {
                    ProtocolSubmission submission = protocolForm.getProtocolDocument().getProtocol().getProtocolSubmission();
                    List<ProtocolReviewerBean> beans = actionBean.getReviewers();
                    getProtocolAssignReviewersService().assignReviewers(submission, beans);
                    //clear the warnings before rendering the page.
                    GlobalVariables.getMessageMap().getWarningMessages().clear();
                    
                    recordProtocolActionSuccess("Assign Reviewers");
                }
            }
        }

        return forward;
    }
    
    /**
     * Grant an exemption to a protocol.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward grantExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGrantExemptionBean actionBean = protocolForm.getActionHelper().getProtocolGrantExemptionBean();
        getProtocolGrantExemptionService().grantExemption(protocolForm.getProtocolDocument().getProtocol(), actionBean);
        saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
        
        recordProtocolActionSuccess("Grant Exemption");
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward irbAcknowledgement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        IrbAcknowledgementBean actionBean = protocolForm.getActionHelper().getIrbAcknowledgementBean();
        getIrbAcknowledgementService().irbAcknowledgement(protocolForm.getProtocolDocument().getProtocol(), actionBean);
        saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
        
        recordProtocolActionSuccess("IRB Acknowledgement");
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    

    /**
     * Add a review comment to a grant exemption request.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addGrantExemptionReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getProtocolGrantExemptionBean().getReviewCommentsBean();

        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_GRANT_EXEMPTION_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
        
    public ActionForward addIrbAcknowledgementReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getIrbAcknowledgementBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_IRB_ACKNOWLEDGEMENT_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * Delete a review comment from a grant exemption request.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteGrantExemptionReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGrantExemptionBean actionBean = protocolForm.getActionHelper().getProtocolGrantExemptionBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward deleteIrbAcknowledgementReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        IrbAcknowledgementBean actionBean = protocolForm.getActionHelper().getIrbAcknowledgementBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Move a review comment up one in a grant exemption request.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward moveUpGrantExemptionReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGrantExemptionBean actionBean = protocolForm.getActionHelper().getProtocolGrantExemptionBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveUpIrbAcknowledgementReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        IrbAcknowledgementBean actionBean = protocolForm.getActionHelper().getIrbAcknowledgementBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Move a review comment down one in a grant exemption request.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward moveDownGrantExemptionReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGrantExemptionBean actionBean = protocolForm.getActionHelper().getProtocolGrantExemptionBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveDownIrbAcknowledgementReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        IrbAcknowledgementBean actionBean = protocolForm.getActionHelper().getIrbAcknowledgementBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Expedite Approval.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward expediteApproval(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolApproveBean actionBean = protocolForm.getActionHelper().getProtocolExpediteApprovalBean();
        getProtocolExpediteApprovalService().grantExpeditedApproval(protocolForm.getProtocolDocument().getProtocol(), actionBean);
        saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
        
        recordProtocolActionSuccess("Expedited Approval");
        return mapping.findForward(KNSConstants.MAPPING_PORTAL);            
    }
    
    /**
     * Add a review comment to a grant exemption request.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addExpediteApprovalReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getProtocolExpediteApprovalBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_EXPEDITE_APPROVAL_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Delete a review comment from a grant exemption request.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteExpediteApprovalReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolApproveBean actionBean = protocolForm.getActionHelper().getProtocolExpediteApprovalBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Move a review comment up one in a grant exemption request.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward moveUpExpediteApprovalReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolApproveBean actionBean = protocolForm.getActionHelper().getProtocolExpediteApprovalBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Move a review comment down one in a grant exemption request.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward moveDownExpediteApprovalReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolApproveBean actionBean = protocolForm.getActionHelper().getProtocolExpediteApprovalBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    
    /**
     * Approves a protocol for a Response Review Type.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward approveResponse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolDocument document = protocolForm.getProtocolDocument();
        ProtocolApproveBean actionBean = protocolForm.getActionHelper().getProtocolResponseApprovalBean();
        
        if (applyRules(new ProtocolResponseApprovalEvent<ProtocolResponseApprovalRule>(document, actionBean))) {
            getProtocolResponseApprovalService().approveResponse(document.getProtocol(), actionBean);
            saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
            
            recordProtocolActionSuccess("Response Approval");
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Adds a Review Comment to a protocol in a Response Approval action.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addResponseApprovalReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getProtocolResponseApprovalBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_RESPONSE_APPROVAL_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Deletes a Review Comment to a protocol in a Response Approval action.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteResponseApprovalReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolApproveBean actionBean = protocolForm.getActionHelper().getProtocolResponseApprovalBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Moves a Review Comment up one in a protocol in a Response Approval action.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward moveUpResponseApprovalReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolApproveBean actionBean = protocolForm.getActionHelper().getProtocolResponseApprovalBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Moves a Review Comment down one in a protocol in a Response Approval action.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward moveDownResponseApprovalReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolApproveBean actionBean = protocolForm.getActionHelper().getProtocolResponseApprovalBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Disapproves a protocol.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward disapproveAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        
        if (hasPermission(TaskName.DISAPPROVE_PROTOCOL, protocolForm.getProtocolDocument().getProtocol())) {
            ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolDisapproveBean();
            getProtocolGenericActionService().disapprove(protocolForm.getProtocolDocument().getProtocol(), actionBean);
            saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
            
            recordProtocolActionSuccess("Disapprove");
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Adds a Review Comment to a protocol in a Disapprove action.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addDisapproveActionReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getProtocolDisapproveBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_DISAPPROVE_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Deletes a Review Comment to a protocol in a Disapprove action.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteDisapproveActionReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolDisapproveBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Moves a Review Comment up one in a protocol in a Disapprove action.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward moveUpDisapproveActionReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolDisapproveBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Moves a Review Comment down one in a protocol in a Disapprove action.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward moveDownDisapproveActionReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolDisapproveBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }  
    
    /**
     * Returns the protocol to the PI for specific minor revisions.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward returnForSMR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ActionForward forward = mapping.findForward(Constants.MAPPING_BASIC);
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        if (hasPermission(TaskName.RETURN_FOR_SMR, protocolForm.getProtocolDocument().getProtocol())) {
            ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolSMRBean();
            
            ProtocolDocument newDocument = getProtocolGenericActionService().returnForSMR(protocolForm.getProtocolDocument().getProtocol(), actionBean);
            
            saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());            
            protocolForm.setDocId(newDocument.getDocumentNumber());
            loadDocument(protocolForm);
            protocolForm.getProtocolHelper().prepareView();
            
            recordProtocolActionSuccess("Return for Specific Minor Revisions");
            
            forward = mapping.findForward(PROTOCOL_TAB);
        }
        
        return forward;
    }
    
    /**
     * Adds a Review Comment to a protocol in an SMR action.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addSMRReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getProtocolSMRBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_SMR_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Deletes a Review Comment to a protocol in an SMR action.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteSMRReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolSMRBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Moves a Review Comment up one in a protocol in an SMR action.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward moveUpSMRReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolSMRBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Moves a Review Comment down one in a protocol in an SMR action.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward moveDownSMRReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolSMRBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }  
    
    /**
     * Returns the protocol to the PI for substantial revisions.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward returnForSRR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ActionForward forward = mapping.findForward(Constants.MAPPING_BASIC);
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        if (hasPermission(TaskName.RETURN_FOR_SRR, protocolForm.getProtocolDocument().getProtocol())) {
            ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolSRRBean();
            
            ProtocolDocument newDocument = getProtocolGenericActionService().returnForSRR(protocolForm.getProtocolDocument().getProtocol(), actionBean);
            
            saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
            protocolForm.setDocId(newDocument.getDocumentNumber());
            loadDocument(protocolForm);
            protocolForm.getProtocolHelper().prepareView();
            
            recordProtocolActionSuccess("Return for Substantive Revisions Required");
            
            forward = mapping.findForward(PROTOCOL_TAB);
        }
        
        return forward;
    }
    
    /**
     * Adds a Review Comment to a protocol in an SRR action.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addSRRReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getProtocolSRRBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_SRR_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Deletes a Review Comment to a protocol in an SRR action.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteSRRReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolSRRBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Moves a Review Comment up one in a protocol in an SRR action.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward moveUpSRRReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolSRRBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Moves a Review Comment down one in a protocol in an SRR action.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward moveDownSRRReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolSRRBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }  
    
    /**
     * Perform Protocol Approve Action - maps to IRBReview RouteNode 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward approveAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionForward forward = mapping.findForward(Constants.MAPPING_BASIC);
        ProtocolForm protocolForm = (ProtocolForm) form;
        if (hasPermission(TaskName.APPROVE_PROTOCOL, protocolForm.getProtocolDocument().getProtocol())) {
            if (applyRules(new ProtocolApproveEvent(protocolForm.getProtocolDocument(), protocolForm.getActionHelper()
                    .getProtocolApproveBean()))) {
                forward = super.approve(mapping, protocolForm, request, response);
                ProtocolApproveBean actionBean = protocolForm.getActionHelper().getProtocolApproveBean();
                getProtocolApproveService().approve(protocolForm.getProtocolDocument(), actionBean);
                if (protocolForm.getProtocolDocument().getProtocol().isAmendment() || protocolForm.getProtocolDocument().getProtocol().isRenewal()) {
                    forward = mapping.findForward(KNSConstants.MAPPING_PORTAL);
                    
                } 
                saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
            }
        }
        
        return forward;
    }
    
    public ActionForward defer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        if (hasPermission(TaskName.DEFER_PROTOCOL, protocolForm.getProtocolDocument().getProtocol())) {
            ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolDeferBean();
            ProtocolDocument newDocument = getProtocolDeferService().defer(protocolForm.getProtocolDocument().getProtocol(), actionBean);
            
            saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
            
            if(!StringUtils.equals(protocolForm.getProtocolDocument().getDocumentNumber(), newDocument.getDocumentNumber())) {
                protocolForm.setDocId(newDocument.getDocumentNumber());
                loadDocument(protocolForm);
                protocolForm.getProtocolHelper().prepareView();
                
                recordProtocolActionSuccess("Defer");
                
                return mapping.findForward(PROTOCOL_TAB);
            }
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward manageComments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        if (hasPermission(TaskName.PROTOCOL_MANAGE_REVIEW_COMMENTS, protocolForm.getProtocolDocument().getProtocol())) {
            ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolManageReviewCommentsBean();
            saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
            
            recordProtocolActionSuccess("Manage Review Comments");
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    public ActionForward addReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolManageReviewCommentsBean();

        addReviewComment(actionBean.getReviewCommentsBean(), protocolForm.getProtocolDocument(), Constants.PROTOCOL_MANAGE_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    } 
    
    public ActionForward deleteReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolManageReviewCommentsBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveUpReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolManageReviewCommentsBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveDownReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolManageReviewCommentsBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward addApproveActionReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getProtocolApproveBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_APPROVE_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward deleteApproveActionReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolApproveBean actionBean = protocolForm.getActionHelper().getProtocolApproveBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveUpApproveActionReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolApproveBean actionBean = protocolForm.getActionHelper().getProtocolApproveBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveDownApproveActionReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolApproveBean actionBean = protocolForm.getActionHelper().getProtocolApproveBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward reopen(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        
        if (hasGenericPermission(GenericProtocolAuthorizer.REOPEN_PROTOCOL, protocolForm.getProtocolDocument().getProtocol())) {
        
            ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolReopenBean();
            getProtocolGenericActionService().reopen(protocolForm.getProtocolDocument().getProtocol(), actionBean);
            saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
            
            recordProtocolActionSuccess("Re-open Enrollment");
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward addReopenReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getProtocolReopenBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_REOPEN_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward deleteReopenReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form; 
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolReopenBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveUpReopenReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolReopenBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveDownReopenReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolReopenBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward closeEnrollment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        
        if (hasGenericPermission(GenericProtocolAuthorizer.CLOSE_ENROLLMENT_PROTOCOL, protocolForm.getProtocolDocument().getProtocol())) {
        
            ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolCloseEnrollmentBean();
            getProtocolGenericActionService().closeEnrollment(protocolForm.getProtocolDocument().getProtocol(), actionBean);
            saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
            
            recordProtocolActionSuccess("Close Enrollment");
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward addCloseEnrollmentReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getProtocolCloseEnrollmentBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_CLOSE_ENROLLMENT_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward deleteCloseEnrollmentReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolCloseEnrollmentBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveUpCloseEnrollmentReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolCloseEnrollmentBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveDownCloseEnrollmentReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolCloseEnrollmentBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward suspend(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        
        if (hasGenericPermission(GenericProtocolAuthorizer.SUSPEND_PROTOCOL, protocolForm.getProtocolDocument().getProtocol())) {
        
            ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolSuspendBean();
            getProtocolGenericActionService().suspend(protocolForm.getProtocolDocument().getProtocol(), actionBean);
            saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
            
            recordProtocolActionSuccess("Suspend");
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward addSuspendReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getProtocolSuspendBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_SUSPEND_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward deleteSuspendReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolSuspendBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveUpSuspendReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolSuspendBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveDownSuspendReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolSuspendBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward suspendByDsmb(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        
        if (hasGenericPermission(GenericProtocolAuthorizer.SUSPEND_PROTOCOL_BY_DSMB, protocolForm.getProtocolDocument().getProtocol())) {
        
            ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolSuspendByDsmbBean();
            getProtocolGenericActionService().suspendByDsmb(protocolForm.getProtocolDocument().getProtocol(), actionBean);
            saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
            
            recordProtocolActionSuccess("Suspend by DMSB");
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward addSuspendByDsmbReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getProtocolSuspendByDsmbBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_SUSPEND_BY_DMSB_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward deleteSuspendByDsmbReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolSuspendByDsmbBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveUpSuspendByDsmbReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolSuspendByDsmbBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveDownSuspendByDsmbReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolSuspendByDsmbBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward closeProtocol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolForm protocolForm = (ProtocolForm) form;
        if (hasGenericPermission(GenericProtocolAuthorizer.CLOSE_PROTOCOL, protocolForm.getProtocolDocument().getProtocol())) {
            ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolCloseBean();
            getProtocolGenericActionService().close(protocolForm.getProtocolDocument().getProtocol(), actionBean);
            saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
            
            recordProtocolActionSuccess("Close");
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward addCloseReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getProtocolCloseBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_CLOSE_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward deleteCloseReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolCloseBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveUpCloseReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolCloseBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveDownCloseReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolCloseBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward expire(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        if (hasGenericPermission(GenericProtocolAuthorizer.EXPIRE_PROTOCOL, protocolForm.getProtocolDocument().getProtocol())) {
            ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolExpireBean();
            getProtocolGenericActionService().expire(protocolForm.getProtocolDocument().getProtocol(), actionBean);
            saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
            
            recordProtocolActionSuccess("Expire");
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward addExpireReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getProtocolExpireBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_EXPIRE_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward deleteExpireReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolExpireBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveUpExpireReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolExpireBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveDownExpireReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolExpireBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward terminate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        if (hasGenericPermission(GenericProtocolAuthorizer.TERMINATE_PROTOCOL, protocolForm.getProtocolDocument().getProtocol())) {
            ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolTerminateBean();
            getProtocolGenericActionService().terminate(protocolForm.getProtocolDocument().getProtocol(), actionBean);
            saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
            
            recordProtocolActionSuccess("Terminate");
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward addTerminateReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getProtocolTerminateBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_TERMINATE_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward deleteTerminateReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolTerminateBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveUpTerminateReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolTerminateBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveDownTerminateReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolTerminateBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward permitDataAnalysis(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        if (hasGenericPermission(GenericProtocolAuthorizer.PERMIT_DATA_ANALYSIS, protocolForm.getProtocolDocument().getProtocol())) {
            ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolPermitDataAnalysisBean();
            getProtocolGenericActionService().permitDataAnalysis(protocolForm.getProtocolDocument().getProtocol(), actionBean);
            saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());
            
            recordProtocolActionSuccess("Permit Data Analysis Only");
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward addPermitDataAnalysisReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getProtocolPermitDataAnalysisBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_PERMIT_DATA_ANALYSIS_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward deletePermitDataAnalysisReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolPermitDataAnalysisBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveUpPermitDataAnalysisReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolPermitDataAnalysisBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveDownPermitDataAnalysisReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolPermitDataAnalysisBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Adds a Risk Level to a protocol in an Approval action.
     * 
     * @param mapping Struts action mapping
     * @param form Form associated with this action
     * @param request Raw HTTP Request
     * @param response Raw HTTP Response
     * @return The mapping for the next page
     * @throws Exception
     */
    public ActionForward addApproveRiskLevel(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        
        addRiskLevel(actionHelper.getProtocolApproveBean().getProtocolRiskLevelBean(), protocolForm.getDocument(), actionHelper.getProtocol(), 
                Constants.PROTOCOL_APPROVAL_ENTER_RISK_LEVEL_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Adds a Risk Level to a protocol in an Expedited Approval action.
     * 
     * @param mapping Struts action mapping
     * @param form Form associated with this action
     * @param request Raw HTTP Request
     * @param response Raw HTTP Response
     * @return The mapping for the next page
     * @throws Exception
     */
    public ActionForward addExpediteApprovalRiskLevel(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        
        addRiskLevel(actionHelper.getProtocolExpediteApprovalBean().getProtocolRiskLevelBean(), protocolForm.getProtocolDocument(), actionHelper.getProtocol(), 
                Constants.PROTOCOL_EXPEDITED_APPROVAL_ENTER_RISK_LEVEL_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Adds a Risk Level to a protocol in an Response Approval action.
     * 
     * @param mapping Struts action mapping
     * @param form Form associated with this action
     * @param request Raw HTTP Request
     * @param response Raw HTTP Response
     * @return The mapping for the next page
     * @throws Exception
     */
    public ActionForward addResponseApprovalRiskLevel(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        
        addRiskLevel(actionHelper.getProtocolResponseApprovalBean().getProtocolRiskLevelBean(), protocolForm.getProtocolDocument(), actionHelper.getProtocol(), 
                Constants.PROTOCOL_EXPEDITED_APPROVAL_ENTER_RISK_LEVEL_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /*
     * Applies add rules to all risk levels from both types of approval actions
     */
    private void addRiskLevel(ProtocolRiskLevelBean protocolRiskLevelBean, ProtocolDocument document, Protocol protocol, String errorPropertyName) {
        if (applyRules(new ProtocolAddRiskLevelEvent(document, errorPropertyName, protocolRiskLevelBean.getNewProtocolRiskLevel()))) {
            protocolRiskLevelBean.addNewProtocolRiskLevel(protocol);
        }
    }
    
    /**
     * Updates a persisted Risk Level in a protocol for an approval action, 
     * moving the persisted risk level to Inactive status and adding a new Active status risk level.
     * 
     * @param mapping Struts action mapping
     * @param form Form associated with this action
     * @param request Raw HTTP Request
     * @param response Raw HTTP Response
     * @return The mapping for the next page
     * @throws Exception
     */
    public ActionForward updateApproveRiskLevel(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();

        updateRiskLevel(actionHelper.getProtocolApproveBean().getProtocolRiskLevelBean(), protocolForm.getProtocolDocument(), 
                actionHelper.getProtocol(), getSelectedLine(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Updates a persisted Risk Level in a protocol for an expedited approval action, 
     * moving the persisted risk level to Inactive status and adding a new Active status risk level.
     * 
     * @param mapping Struts action mapping
     * @param form Form associated with this action
     * @param request Raw HTTP Request
     * @param response Raw HTTP Response
     * @return The mapping for the next page
     * @throws Exception
     */
    public ActionForward updateExpediteApprovalRiskLevel(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();

        updateRiskLevel(actionHelper.getProtocolExpediteApprovalBean().getProtocolRiskLevelBean(), protocolForm.getProtocolDocument(), 
                actionHelper.getProtocol(), getSelectedLine(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Updates a persisted Risk Level in a protocol for a Response Approval action, 
     * moving the persisted risk level to Inactive status and adding a new Active status risk level.
     * 
     * @param mapping Struts action mapping
     * @param form Form associated with this action
     * @param request Raw HTTP Request
     * @param response Raw HTTP Response
     * @return The mapping for the next page
     * @throws Exception
     */
    public ActionForward updateResponseApprovalRiskLevel(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();

        updateRiskLevel(actionHelper.getProtocolResponseApprovalBean().getProtocolRiskLevelBean(), protocolForm.getProtocolDocument(), 
                actionHelper.getProtocol(), getSelectedLine(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /*
     * Applies update rules to all risk levels from both types of approval actions
     */
    private void updateRiskLevel(ProtocolRiskLevelBean protocolRiskLevelBean, ProtocolDocument document, Protocol protocol, int lineNumber) {
        if (applyRules(new ProtocolUpdateRiskLevelEvent(document, Constants.PROTOCOL_UPDATE_RISK_LEVEL_KEY, lineNumber))) {
            protocolRiskLevelBean.updateProtocolRiskLevel(protocol.getProtocolRiskLevels().get(lineNumber));
        }
    }
    
    /**
     * Deletes a Risk Level from a protocol in an Approval action.
     * 
     * @param mapping Struts action mapping
     * @param form Form associated with this action
     * @param request Raw HTTP Request
     * @param response Raw HTTP Response
     * @return The mapping for the next page
     * @throws Exception
     */
    public ActionForward deleteApproveRiskLevel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        
        deleteRiskLevel(actionHelper.getProtocolApproveBean().getProtocolRiskLevelBean(), actionHelper.getProtocol(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * Deletes a Risk Level from a protocol in an Expedited Approval action.
     * 
     * @param mapping Struts action mapping
     * @param form Form associated with this action
     * @param request Raw HTTP Request
     * @param response Raw HTTP Response
     * @return The mapping for the next page
     * @throws Exception
     */
    public ActionForward deleteExpediteApprovalRiskLevel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        
        deleteRiskLevel(actionHelper.getProtocolExpediteApprovalBean().getProtocolRiskLevelBean(), actionHelper.getProtocol(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    
    /**
     * Deletes a Risk Level from a protocol in a Response Approval action.
     * 
     * @param mapping Struts action mapping
     * @param form Form associated with this action
     * @param request Raw HTTP Request
     * @param response Raw HTTP Response
     * @return The mapping for the next page
     * @throws Exception
     */
    public ActionForward deleteResponseApprovalRiskLevel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        
        deleteRiskLevel(actionHelper.getProtocolResponseApprovalBean().getProtocolRiskLevelBean(), actionHelper.getProtocol(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /*
     * Deletes the risk level from the protocol for both types of approval actions
     */
    private void deleteRiskLevel(ProtocolRiskLevelBean protocolRiskLevelBean, Protocol protocol, int lineNumber) {
        protocolRiskLevelBean.deleteProtocolRiskLevel(protocol, lineNumber);
    }
    
    /**
     * Open ProtocolDocument in Read/Write mode for Admin Correction
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward openProtocolForAdminCorrection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionForward forward = mapping.findForward(MAPPING_BASIC);
        
        ProtocolTask task = new ProtocolTask(TaskName.PROTOCOL_ADMIN_CORRECTION, protocolForm.getProtocolDocument().getProtocol());
        if (isAuthorized(task)) {
            if (applyRules(new ProtocolAdminCorrectionEvent(protocolForm.getProtocolDocument(), protocolForm.getActionHelper()
                        .getProtocolAdminCorrectionBean()))) {
                protocolForm.getProtocolDocument().getProtocol().setCorrectionMode(true); 
                protocolForm.getProtocolHelper().prepareView();
                
                recordProtocolActionSuccess("Make Administrative Correction");
                
                return mapping.findForward(PROTOCOL_TAB);
            }
        }
        
        return forward;  
    }

    public ActionForward submitAdminCorrection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolDocument protocolDocument = protocolForm.getProtocolDocument();
        protocolForm.setAuditActivated(true);

        AuditActionHelper auditActionHelper = new AuditActionHelper();
        if (auditActionHelper.auditUnconditionally(protocolDocument)) {
            protocolDocument.getProtocol().setCorrectionMode(false); 
            AdminCorrectionBean adminCorrectionBean = protocolForm.getActionHelper().getProtocolAdminCorrectionBean();
            protocolForm.getProtocolDocument().updateProtocolStatus(ProtocolActionType.ADMINISTRATIVE_CORRECTION, adminCorrectionBean.getComments());
             
            AdminCorrectionService adminCorrectionService = KraServiceLocator.getService(AdminCorrectionService.class);
            adminCorrectionService.sendCorrectionNotification(protocolDocument.getProtocol(), adminCorrectionBean);
            
            recordProtocolActionSuccess("Submit Administrative Correction");
            protocolForm.setAuditActivated(false);
        } else {
            GlobalVariables.getMessageMap().clearErrorMessages();
            GlobalVariables.getMessageMap().putError("datavalidation", KeyConstants.ERROR_ADMIN_CORRECTION_SUBMISSION,  new String[] {});
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);  
    }
    
    public ActionForward undoLastAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolDocument protocolDocument = protocolForm.getProtocolDocument();
        UndoLastActionBean undoLastActionBean = protocolForm.getActionHelper().getUndoLastActionBean();
        UndoLastActionService undoLastActionService = KraServiceLocator.getService(UndoLastActionService.class);
        ProtocolDocument updatedDocument = undoLastActionService.undoLastAction(protocolDocument, undoLastActionBean);  
        
        recordProtocolActionSuccess("Undo Last Action");
        
        if(!updatedDocument.getDocumentNumber().equals(protocolForm.getDocId())) { 
            protocolForm.setDocId(updatedDocument.getDocumentNumber());
            loadDocument(protocolForm);
            protocolForm.getProtocolHelper().prepareView();
            return mapping.findForward(PROTOCOL_TAB);
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward submitCommitteeDecision(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        if (applyRules(new CommitteeDecisionEvent(protocolForm.getProtocolDocument(), protocolForm.getActionHelper().getCommitteeDecision()))){
            CommitteeDecision actionBean = protocolForm.getActionHelper().getCommitteeDecision();
            getCommitteeDecisionService().processCommitteeDecision(protocolForm.getProtocolDocument().getProtocol(), actionBean);
            saveReviewComments(protocolForm, actionBean.getReviewCommentsBean());

            recordProtocolActionSuccess("Record Committee Decision");
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward addCommitteeDecisionReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getCommitteeDecision().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_COMMITTEE_DECISION_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward deleteCommitteeDecisionReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        CommitteeDecision actionBean = protocolForm.getActionHelper().getCommitteeDecision();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveUpCommitteeDecisionReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        CommitteeDecision actionBean = protocolForm.getActionHelper().getCommitteeDecision();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveDownCommitteeDecisionReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        CommitteeDecision actionBean = protocolForm.getActionHelper().getCommitteeDecision();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward addAbstainer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolForm protocolForm = (ProtocolForm) form;
        CommitteeDecision decision = protocolForm.getActionHelper().getCommitteeDecision();
        if (applyRules(new CommitteeDecisionAbstainerEvent(protocolForm.getProtocolDocument(), decision))){
            decision.getAbstainers().add(decision.getNewAbstainer());
            decision.setNewAbstainer(new CommitteePerson());
            decision.setAbstainCount(decision.getAbstainCount() + 1);
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward deleteAbstainer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        CommitteeDecision decision = protocolForm.getActionHelper().getCommitteeDecision();
        CommitteePerson person = decision.getAbstainers().get(getLineToDelete(request));
        if (person != null) {
            decision.getAbstainersToDelete().add(person);
            decision.getAbstainers().remove(getLineToDelete(request));
            decision.setAbstainCount(decision.getAbstainCount() - 1);
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward addRecused(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        CommitteeDecision decision = protocolForm.getActionHelper().getCommitteeDecision();
        if (applyRules(new CommitteeDecisionRecuserEvent(protocolForm.getProtocolDocument(), decision))) {
            decision.getRecused().add(decision.getNewRecused());
            decision.setNewRecused(new CommitteePerson());
            decision.setRecusedCount(decision.getRecusedCount() + 1);
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward modifySubmsionAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolModifySubmissionBean bean = protocolForm.getActionHelper().getProtocolModifySubmissionBean();
        if (applyRules(new ProtocolModifySubmissionEvent(protocolForm.getProtocolDocument(), bean))) {
            KraServiceLocator.getService(ProtocolModifySubmissionService.class).modifySubmisison(protocolForm.getProtocolDocument(), bean);
        
            recordProtocolActionSuccess("Modify Submission Request");
        }
        return mapping.findForward(Constants.MAPPING_BASIC);        
    }
    
    public ActionForward deleteRecused(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        CommitteeDecision decision = protocolForm.getActionHelper().getCommitteeDecision();
        CommitteePerson person = decision.getRecused().get(getLineToDelete(request));
        if (person != null) {
            decision.getRecusedToDelete().add(person);
            decision.getRecused().remove(getLineToDelete(request));
            decision.setRecusedCount(decision.getRecusedCount() - 1);
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    private Printable getPrintableArtifacts(Protocol protocol, String reportType, StringBuffer fileName,Map reportParameters) {
        ProtocolPrintType printType = ProtocolPrintType.valueOf(PRINTTAG_MAP.get(reportType));

        AbstractPrint printable = (AbstractPrint)getProtocolPrintingService().getProtocolPrintable(printType);
        printable.setPrintableBusinessObject(protocol);
        printable.setReportParameters(reportParameters);
        fileName.append(reportType).append("-");
        return printable;
    }

    private ProtocolPrintingService getProtocolPrintingService() {
        return KraServiceLocator.getService(ProtocolPrintingService.class);
    }

    private void addReviewComment(ReviewCommentsBean reviewCommentsBean, ProtocolDocument document, String errorPropertyName) throws Exception {
        CommitteeScheduleMinute newReviewComment = reviewCommentsBean.getNewReviewComment();
        List<CommitteeScheduleMinute> reviewComments = reviewCommentsBean.getReviewComments();
        Protocol protocol = reviewCommentsBean.getProtocol();
        
        if (applyRules(new ProtocolAddReviewCommentEvent(document, errorPropertyName, newReviewComment))) {
            getReviewCommentsService().addReviewComment(newReviewComment, reviewComments, protocol);
            
            reviewCommentsBean.setNewReviewComment(new CommitteeScheduleMinute(MinuteEntryType.PROTOCOL));
        }
    }
    
    private void deleteReviewComment(ReviewCommentsBean reviewCommentsBean, int index) throws Exception {
        List<CommitteeScheduleMinute> reviewComments = reviewCommentsBean.getReviewComments();
        List<CommitteeScheduleMinute> deletedReviewComments = reviewCommentsBean.getDeletedReviewComments();
        
        getReviewCommentsService().deleteReviewComment(reviewComments, index, deletedReviewComments);
    }
    
    private void moveUpReviewComment(ReviewCommentsBean reviewCommentsBean, int index) throws Exception {
        List<CommitteeScheduleMinute> reviewComments = reviewCommentsBean.getReviewComments();
        Protocol protocol = reviewCommentsBean.getProtocol();
        
        getReviewCommentsService().moveUpReviewComment(reviewComments, protocol, index);
    }
    
    private void moveDownReviewComment(ReviewCommentsBean reviewCommentsBean, int index) throws Exception {
        List<CommitteeScheduleMinute> reviewComments = reviewCommentsBean.getReviewComments();
        Protocol protocol = reviewCommentsBean.getProtocol();
        
        getReviewCommentsService().moveDownReviewComment(reviewComments, protocol, index);
    }
    
    private boolean hasPermission(String taskName, Protocol protocol) {
        ProtocolTask task = new ProtocolTask(taskName, protocol);
        return getTaskAuthorizationService().isAuthorized(GlobalVariables.getUserSession().getPrincipalId(), task);
    }
    
    private boolean hasGenericPermission(String genericActionName, Protocol protocol) {
        ProtocolTask task = new ProtocolTask(TaskName.GENERIC_PROTOCOL_ACTION, protocol, genericActionName);
        return getTaskAuthorizationService().isAuthorized(GlobalVariables.getUserSession().getPrincipalId(), task);
    }
    
    private ProtocolAttachmentService getProtocolAttachmentService() {
        return KraServiceLocator.getService(ProtocolAttachmentService.class);
    }
    
    private TaskAuthorizationService getTaskAuthorizationService() {
        return KraServiceLocator.getService(TaskAuthorizationService.class);
    }
    
    private ProtocolGenericActionService getProtocolGenericActionService() {
        return KraServiceLocator.getService(ProtocolGenericActionService.class);
    }
    
    public ProtocolCopyService getProtocolCopyService() {
        return KraServiceLocator.getService(ProtocolCopyService.class);
    }
    
    private ProtocolSubmitActionService getProtocolSubmitActionService() {
        return KraServiceLocator.getService(ProtocolSubmitActionService.class);
    }
    
    private ProtocolWithdrawService getProtocolWithdrawService() {
        return KraServiceLocator.getService(ProtocolWithdrawService.class);
    }
    
    private ProtocolRequestService getProtocolRequestService() {
        return KraServiceLocator.getService(ProtocolRequestService.class);
    }
    
    private ProtocolNotifyIrbService getProtocolNotifyIrbService() {
        return KraServiceLocator.getService(ProtocolNotifyIrbService.class);
    }
    
    private ProtocolAmendRenewService getProtocolAmendRenewService() {
        return KraServiceLocator.getService(ProtocolAmendRenewService.class);
    }
    
    private ProtocolDeleteService getProtocolDeleteService() {
        return KraServiceLocator.getService(ProtocolDeleteService.class);
    }
    
    private ProtocolAssignCmtSchedService getProtocolAssignCmtSchedService() {
        return KraServiceLocator.getService(ProtocolAssignCmtSchedService.class);
    }
    
    private ProtocolAssignToAgendaService getProtocolAssignToAgendaService() {
        return KraServiceLocator.getService(ProtocolAssignToAgendaService.class);
    }
    
    private ProtocolAssignReviewersService getProtocolAssignReviewersService() {
        return KraServiceLocator.getService(ProtocolAssignReviewersService.class);
    }
    
    private ProtocolGrantExemptionService getProtocolGrantExemptionService() {
        return KraServiceLocator.getService(ProtocolGrantExemptionService.class);
    }
    
    private IrbAcknowledgementService getIrbAcknowledgementService() {
        return KraServiceLocator.getService(IrbAcknowledgementService.class);
    }
    
    private ProtocolExpediteApprovalService getProtocolExpediteApprovalService() {
        return KraServiceLocator.getService(ProtocolExpediteApprovalService.class);
    }
    
    private ProtocolResponseApprovalService getProtocolResponseApprovalService() {
        return KraServiceLocator.getService(ProtocolResponseApprovalService.class);
    }
    
    private ProtocolApproveService getProtocolApproveService() {
        return KraServiceLocator.getService(ProtocolApproveService.class);
    }
    
    private CommitteeService getCommitteeService() {
        return KraServiceLocator.getService(CommitteeService.class);
    }
    
    private CommitteeDecisionService getCommitteeDecisionService() {
        return KraServiceLocator.getService("protocolCommitteeDecisionService");
    }
    
    private ReviewCommentsService getReviewCommentsService() {
        return KraServiceLocator.getService(ReviewCommentsService.class);
    }
    
    private ProtocolDeferService getProtocolDeferService() {
        return KraServiceLocator.getService(ProtocolDeferService.class);
    }

    public ActionForward addDeferReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ActionHelper actionHelper = protocolForm.getActionHelper();
        ReviewCommentsBean actionBean = actionHelper.getProtocolDeferBean().getReviewCommentsBean();
        
        addReviewComment(actionBean, protocolForm.getProtocolDocument(), Constants.PROTOCOL_DEFER_ENTER_REVIEW_COMMENTS_KEY);
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward deleteDeferReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolDeferBean();
        
        deleteReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveUpDeferReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolDeferBean();
        
        moveUpReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward moveDownDeferReviewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolGenericActionBean actionBean = protocolForm.getActionHelper().getProtocolDeferBean();
        
        moveDownReviewComment(actionBean.getReviewCommentsBean(), getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /**
     * 
     * This method is to add a file to notify irb 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addNotifyIrbAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (((ProtocolForm) form).getActionHelper().validFile(((ProtocolForm) form).getActionHelper().getProtocolNotifyIrbBean().getNewActionAttachment(), "protocolNotifyIrbBean")) {
            LOG.info("addNotifyIrbAttachment " +((ProtocolForm) form).getActionHelper().getProtocolNotifyIrbBean().getNewActionAttachment().getFile().getFileName()
                    + ((ProtocolForm) form).getProtocolDocument().getDocumentNumber());
            ((ProtocolForm) form).getActionHelper().addNotifyIrbAttachment();
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * 
     * This method view a file added to notify irb panel
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewNotifyIrbAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return this.viewAttachment(mapping, (ProtocolForm) form, request, response);
    }

    /*
     * utility to view file 
     */
    private ActionForward viewAttachment(ActionMapping mapping, ProtocolForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        int selection = this.getSelectedLine(request);
        ProtocolActionAttachment attachment = form.getActionHelper().getProtocolNotifyIrbBean().getActionAttachments().get(
                selection);

        if (attachment == null) {
            LOG.info(NOT_FOUND_SELECTION + selection);
            // may want to tell the user the selection was invalid.
            return mapping.findForward(Constants.MAPPING_BASIC);
        }

        this.streamToResponse(attachment.getFile().getFileData(), getValidHeaderString(attachment.getFile().getFileName()),
                getValidHeaderString(attachment.getFile().getContentType()), response);

        return RESPONSE_ALREADY_HANDLED;
    }

    /**
     * 
     * This method to delete a file added in norify irb panel
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteNotifyIrbAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return confirmDeleteAttachment(mapping, (ProtocolForm) form, request, response, ((ProtocolForm) form).getActionHelper().getProtocolNotifyIrbBean().getActionAttachments());
    }

    /*
     * confirmation question for delete norify irb file or request attachment file
     */
    private ActionForward confirmDeleteAttachment(ActionMapping mapping, ProtocolForm form, HttpServletRequest request,
            HttpServletResponse response, List<ProtocolActionAttachment> attachments) throws Exception {

        int selection = this.getSelectedLine(request);
        ProtocolActionAttachment attachment = attachments.get(selection);

        if (attachment == null) {
            LOG.info(NOT_FOUND_SELECTION + selection);
            // may want to tell the user the selection was invalid.
            return mapping.findForward(Constants.MAPPING_BASIC);
        }

        StrutsConfirmation confirm = buildParameterizedConfirmationQuestion(mapping, form, request, response,
                CONFIRM_DELETE_ACTION_ATT, KeyConstants.QUESTION_DELETE_ATTACHMENT_CONFIRMATION, "", attachment
                        .getFile().getFileName());

        return confirm(confirm, CONFIRM_DELETE_ACTION_ATT, CONFIRM_NO_DELETE);
    }


    /**
     * 
     * method when confirm to delete notify irb file or request action attachment
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward confirmDeleteActionAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        int selection = this.getSelectedLine(request);
        String actionTypeCode = getRequestActionType(request);

        if (StringUtils.isBlank(actionTypeCode)) {
            ((ProtocolForm) form).getActionHelper().getProtocolNotifyIrbBean().getActionAttachments().remove(selection);
        } else {
            ((ProtocolForm) form).getActionHelper().getActionTypeRequestBeanMap(actionTypeCode).getActionAttachments().remove(selection);
        }
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /**
     * 
     * This method is to view the submission doc displayed in history panel
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewSubmissionDoc(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ProtocolForm protocolForm = (ProtocolForm) form;
        int actionIndex = getSelectedLine(request);
        int attachmentIndex = getSelectedAttachment(request);
        org.kuali.kra.irb.actions.ProtocolAction protocolAction = protocolForm.getActionHelper().getProtocol().getProtocolActions().get(actionIndex);
        ProtocolSubmissionDoc attachment = protocolAction.getProtocolSubmissionDocs().get(attachmentIndex);

        if (attachment == null) {
            LOG.info(NOT_FOUND_SELECTION + "protocolAction: " + actionIndex + ", protocolSubmissionDoc: " + attachmentIndex);
            // may want to tell the user the selection was invalid.
            return mapping.findForward(Constants.MAPPING_BASIC);
        }

        this.streamToResponse(attachment.getDocument(), getValidHeaderString(attachment.getFileName()), getValidHeaderString(attachment.getContentType()), response);

        return RESPONSE_ALREADY_HANDLED;
    }
    
    /**
     * 
     * This method is to view correspondences in history panel.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewActionCorrespondence(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        ProtocolForm protocolForm = (ProtocolForm) form;
        int actionIndex = getSelectedLine(request);
        int attachmentIndex = getSelectedAttachment(request);
        org.kuali.kra.irb.actions.ProtocolAction protocolAction = protocolForm.getActionHelper().getProtocol().getProtocolActions().get(actionIndex);
        ProtocolCorrespondence attachment = protocolAction.getProtocolCorrespondences().get(attachmentIndex);

        if (attachment == null) {
            LOG.info(NOT_FOUND_SELECTION + "protocolAction: " + actionIndex + ", protocolCorrespondence: " + attachmentIndex);
            // may want to tell the user the selection was invalid.
            return mapping.findForward(Constants.MAPPING_BASIC);
        }

        this.streamToResponse(attachment.getCorrespondence(), StringUtils.replace(attachment.getProtocolCorrespondenceType().getDescription(), " ", "") + ".pdf", 
                Constants.PDF_REPORT_CONTENT_TYPE, response);

        return RESPONSE_ALREADY_HANDLED;
    }
    
    /*
     * utility to get "actionidx;atachmentidx"
     */
    private int getSelectedAttachment(HttpServletRequest request) {
        int selectedAttachment = -1;
        String parameterName = (String) request.getAttribute(KNSConstants.METHOD_TO_CALL_ATTRIBUTE);
        if (StringUtils.isNotBlank(parameterName)) {
            String attachmentNumber = StringUtils.substringBetween(parameterName, ".attachment", ".");
            selectedAttachment = Integer.parseInt(attachmentNumber);
        }

        return selectedAttachment;
    }
    
    /**
     * Saves the review comments to the database and performs refresh and cleanup.
     * @param protocolForm
     * @param actionBean
     * @return
     * @throws Exception
     */
    private void saveReviewComments(ProtocolForm protocolForm, ReviewCommentsBean actionBean) throws Exception { 
        getReviewCommentsService().saveReviewComments(actionBean.getReviewComments(), actionBean.getDeletedReviewComments());           
        actionBean.setDeletedReviewComments(new ArrayList<CommitteeScheduleMinute>());
        protocolForm.getActionHelper().prepareCommentsView();
    }

    
    /**
     * 
     * This method is to add attachment for several request actions.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addRequestAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String actionTypeCode = getRequestActionType(request);
        ProtocolRequestBean requestBean = ((ProtocolForm) form).getActionHelper().getActionTypeRequestBeanMap(actionTypeCode);
        if (((ProtocolForm) form).getActionHelper().validFile(requestBean.getNewActionAttachment(), requestBean.getBeanName())) {
            // add this log to trace if there is any further issue
            LOG.info("addRequestAttachment "+ actionTypeCode + " " +requestBean.getNewActionAttachment().getFile().getFileName()
                      + ((ProtocolForm) form).getProtocolDocument().getDocumentNumber());
            ((ProtocolForm) form).getActionHelper().addRequestAttachment(actionTypeCode);
        }
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

    /*
     * utility method to get the actiontypecode from the request methodtocall param
     */
    private String getRequestActionType(HttpServletRequest request) {
        String parameterName = (String) request.getAttribute(KNSConstants.METHOD_TO_CALL_ATTRIBUTE);
        String actionTypeCode = "";
        if (StringUtils.isNotBlank(parameterName)) {
            actionTypeCode = StringUtils.substringBetween(parameterName, ".actionType", ".");
        }

        return actionTypeCode;
    }

    /**
     * 
     * This method view the selected attachment from the request action panel
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewRequestAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String actionTypeCode = getRequestActionType(request);
        int selection = this.getSelectedLine(request);
        ProtocolRequestBean requestBean = ((ProtocolForm) form).getActionHelper().getActionTypeRequestBeanMap(actionTypeCode);
        ProtocolActionAttachment attachment = requestBean.getActionAttachments().get(selection);

        if (attachment == null) {
            LOG.info(NOT_FOUND_SELECTION + selection);
            // may want to tell the user the selection was invalid.
            return mapping.findForward(Constants.MAPPING_BASIC);
        }

        this.streamToResponse(attachment.getFile().getFileData(), getValidHeaderString(attachment.getFile().getFileName()),
                getValidHeaderString(attachment.getFile().getContentType()), response);

        return RESPONSE_ALREADY_HANDLED;
    }

    /**
     * 
     * This method is to delete the selected request action attachment
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward deleteRequestAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String actionTypeCode = getRequestActionType(request);
        ProtocolRequestBean requestBean = ((ProtocolForm) form).getActionHelper().getActionTypeRequestBeanMap(actionTypeCode);
        return confirmDeleteAttachment(mapping, (ProtocolForm) form, request, response, requestBean.getActionAttachments());
    }
    
    private void recordProtocolActionSuccess(String protocolActionName) {
        GlobalVariables.getMessageList().add(KeyConstants.MESSAGE_PROTOCOL_ACTION_SUCCESSFULLY_COMPLETED, protocolActionName);
    }
    
    /**
     * Method called when adding a protocol note.
     * 
     * @param mapping the action mapping
     * @param form the form.
     * @param request the request.
     * @param response the response.
     * @return an action forward.
     * @throws Exception if there is a problem executing the request.
     */
    public ActionForward addNote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        ((ProtocolForm) form).getNotesAttachmentsHelper().addNewNote();
        ((ProtocolForm) form).getNotesAttachmentsHelper().setManageNotesOpen(true);
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    public ActionForward saveNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        Protocol protocol = ((ProtocolForm) form).getProtocolDocument().getProtocol();
        //protocolForm.getNotesAttachmentsHelper().processSave();
        for(ProtocolNotepad note : protocol.getNotepads()) {
            if (StringUtils.isBlank(note.getUpdateUserFullName())) {
                note.setUpdateUserFullName(GlobalVariables.getUserSession().getPerson().getName());
                note.setUpdateTimestamp(KraServiceLocator.getService(DateTimeService.class).getCurrentTimestamp());
            }
            note.setEditable(false);
        }
        getBusinessObjectService().save(protocol.getNotepads());
        return mapping.findForward(Constants.MAPPING_BASIC);
    }

}