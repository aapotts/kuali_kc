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
package org.kuali.kra.irb.actions;

import static org.kuali.kra.infrastructure.Constants.MAPPING_BASIC;
import static org.kuali.rice.kns.util.KNSConstants.QUESTION_INST_ATTRIBUTE_NAME;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.HeaderTokenizer;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.kra.authorization.ApplicationTask;
import org.kuali.kra.bo.AttachmentFile;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KeyConstants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.TaskName;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.ProtocolAction;
import org.kuali.kra.irb.ProtocolDocument;
import org.kuali.kra.irb.ProtocolForm;
import org.kuali.kra.irb.actions.amendrenew.CreateAmendmentEvent;
import org.kuali.kra.irb.actions.amendrenew.ProtocolAmendRenewService;
import org.kuali.kra.irb.actions.assigncmtsched.ProtocolAssignCmtSchedBean;
import org.kuali.kra.irb.actions.assigncmtsched.ProtocolAssignCmtSchedEvent;
import org.kuali.kra.irb.actions.assigncmtsched.ProtocolAssignCmtSchedService;
import org.kuali.kra.irb.actions.assignreviewers.ProtocolAssignReviewersBean;
import org.kuali.kra.irb.actions.assignreviewers.ProtocolAssignReviewersEvent;
import org.kuali.kra.irb.actions.assignreviewers.ProtocolAssignReviewersService;
import org.kuali.kra.irb.actions.copy.ProtocolCopyService;
import org.kuali.kra.irb.actions.delete.ProtocolDeleteService;
import org.kuali.kra.irb.actions.grantexemption.ProtocolGrantExemptionBean;
import org.kuali.kra.irb.actions.grantexemption.ProtocolGrantExemptionService;
import org.kuali.kra.irb.actions.notifyirb.ProtocolNotifyIrbService;
import org.kuali.kra.irb.actions.request.ProtocolRequestBean;
import org.kuali.kra.irb.actions.request.ProtocolRequestEvent;
import org.kuali.kra.irb.actions.request.ProtocolRequestService;
import org.kuali.kra.irb.actions.submit.ProtocolSubmission;
import org.kuali.kra.irb.actions.submit.ProtocolSubmitAction;
import org.kuali.kra.irb.actions.submit.ProtocolSubmitActionEvent;
import org.kuali.kra.irb.actions.submit.ProtocolSubmitActionService;
import org.kuali.kra.irb.actions.withdraw.ProtocolWithdrawService;
import org.kuali.kra.irb.auth.ProtocolTask;
import org.kuali.kra.irb.noteattachment.ProtocolAttachmentBase;
import org.kuali.kra.web.struts.action.AuditActionHelper;
import org.kuali.kra.web.struts.action.KraTransactionalDocumentActionBase;
import org.kuali.kra.web.struts.action.StrutsConfirmation;
import org.kuali.rice.kns.web.struts.action.AuditModeAction;

/**
 * The set of actions for the Protocol Actions tab.
 */
public class ProtocolProtocolActionsAction extends ProtocolAction implements AuditModeAction {

    private static final Log LOG = LogFactory.getLog(ProtocolProtocolActionsAction.class);

    private static final String PROTOCOL_TAB = "protocol";
    private static final String CONFIRM_SUBMIT_FOR_REVIEW_KEY = "confirmSubmitForReview";
    private static final String CONFIRM_ASSIGN_CMT_SCHED_KEY = "confirmAssignCmtSched";
    
    private static final String NOT_FOUND_SELECTION = "The attachment was not found for selection ";

    private static final String CONFIRM_DELETE_PROTOCOL_KEY = "confirmDeleteProtocol";

    /** signifies that a response has already be handled therefore forwarding to obtain a response is not needed. */
    private static final ActionForward RESPONSE_ALREADY_HANDLED = null;
    private static final String SUBMISSION_ID = "submissionId";
    
    /**
     * @see org.kuali.kra.web.struts.action.KraTransactionalDocumentActionBase#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward actionForward = super.execute(mapping, form, request, response);

        ((ProtocolForm) form).getActionHelper().prepareView();

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
            ProtocolCopyService protocolCopyService = KraServiceLocator.getService(ProtocolCopyService.class);
            String newDocId = protocolCopyService.copyProtocol(protocolForm.getDocument()).getDocumentNumber();

            // Switch over to the new protocol document and
            // go to the Protocol tab web page.

            protocolForm.setDocId(newDocId);
            loadDocument(protocolForm);
            protocolForm.getProtocolHelper().prepareView();

            return mapping.findForward(PROTOCOL_TAB);
        }

        return mapping.findForward(Constants.MAPPING_BASIC);
    }


    /** {@inheritDoc} */
    public ActionForward activate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return new AuditActionHelper().setAuditMode(mapping, (ProtocolForm) form, true);
    }

    /** {@inheritDoc} */
    public ActionForward deactivate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
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
     * @throws Exception
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
    public ActionForward submitForReview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionForward forward = mapping.findForward(Constants.MAPPING_BASIC);

        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolTask task = new ProtocolTask(TaskName.SUBMIT_PROTOCOL, protocolForm.getProtocolDocument().getProtocol());
        if (isAuthorized(task)) {
            ProtocolSubmitAction submitAction = protocolForm.getActionHelper().getProtocolSubmitAction();
            if (applyRules(new ProtocolSubmitActionEvent(protocolForm.getProtocolDocument(), submitAction))) {
                if (isCommitteeMeetingAssignedMaxProtocols(submitAction.getCommitteeId(), submitAction.getScheduleId())) {
                    return confirm(buildSubmitForReviewConfirmationQuestion(mapping, form, request, response),
                            CONFIRM_SUBMIT_FOR_REVIEW_KEY, "");
                }
                getProtocolActionService().submitToIrbForReview(protocolForm.getProtocolDocument().getProtocol(), submitAction);

                forward = super.route(mapping, form, request, response);
            }
        }

        return forward;
    }

    private ProtocolSubmitActionService getProtocolActionService() {
        return KraServiceLocator.getService(ProtocolSubmitActionService.class);
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
     * @return the destination (always the original Protocol Document web page that caused this action to be invoked)
     * @throws Exception
     * @see KraTransactionalDocumentActionBase#confirm(StrutsQuestion, String, String)
     */
    public ActionForward confirmSubmitForReview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Object question = request.getParameter(QUESTION_INST_ATTRIBUTE_NAME);

        if (CONFIRM_SUBMIT_FOR_REVIEW_KEY.equals(question)) {
            ProtocolForm protocolForm = (ProtocolForm) form;
            ProtocolSubmitAction submitAction = protocolForm.getActionHelper().getProtocolSubmitAction();
            getProtocolActionService().submitToIrbForReview(protocolForm.getProtocolDocument().getProtocol(), submitAction);
        }

        return mapping.findForward(MAPPING_BASIC);
    }

    private boolean isCommitteeMeetingAssignedMaxProtocols(String committeeId, String scheduleId) {
        return false;
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

            return mapping.findForward(PROTOCOL_TAB);
        }

        return mapping.findForward(MAPPING_BASIC);
    }

    private ProtocolWithdrawService getProtocolWithdrawService() {
        return KraServiceLocator.getService(ProtocolWithdrawService.class);
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
                getProtocolRequestService().submitRequest(protocolForm.getProtocolDocument().getProtocol(),
                        closeEnrollmentRequestBean);
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
                getProtocolRequestService().submitRequest(protocolForm.getProtocolDocument().getProtocol(),
                        reopenEnrollmentRequestBean);
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
                getProtocolRequestService()
                        .submitRequest(protocolForm.getProtocolDocument().getProtocol(), dataAnalysisRequestBean);
            }
        }
        return mapping.findForward(MAPPING_BASIC);
    }

    private ProtocolRequestService getProtocolRequestService() {
        return KraServiceLocator.getService(ProtocolRequestService.class);
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
        return mapping.findForward(MAPPING_BASIC);
    }

    private ProtocolNotifyIrbService getProtocolNotifyIrbService() {
        return KraServiceLocator.getService(ProtocolNotifyIrbService.class);
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

            protocolForm.getProtocolHelper().prepareView();
            return mapping.findForward(PROTOCOL_TAB);
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
            String newDocId = getProtocolAmendRenewService().createRenewal(protocolForm.getProtocolDocument());
            // Switch over to the new protocol document and
            // go to the Protocol tab web page.

            protocolForm.setDocId(newDocId);
            loadDocument(protocolForm);

            protocolForm.getProtocolHelper().prepareView();
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

            protocolForm.getProtocolHelper().prepareView();
            return mapping.findForward(PROTOCOL_TAB);
        }
        return mapping.findForward(MAPPING_BASIC);
    }

    private ProtocolAmendRenewService getProtocolAmendRenewService() {
        return KraServiceLocator.getService(ProtocolAmendRenewService.class);
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

    private ProtocolDeleteService getProtocolDeleteService() {
        return KraServiceLocator.getService(ProtocolDeleteService.class);
    }

    /**
     * Print one of the various protocol "documents".
     * 
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
        System.out.println("Print Tag: " + protocolForm.getActionHelper().getPrintTag());
        return mapping.findForward(MAPPING_BASIC);
    }

    /**
     * When the "filter" button is pressed in the History sub-panel, we only need to redraw the page. The results will changed based
     * upon the contents of the Data Range Filter.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward filterHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward(MAPPING_BASIC);
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

        final int selectedIndex = getSelectedLine(request);
        final ProtocolAttachmentBase attachment = protocolForm.getProtocolDocument().getProtocol().getAttachmentProtocol(
                selectedIndex);

        if (attachment == null) {
            LOG.info(NOT_FOUND_SELECTION + selectedIndex);
            // may want to tell the user the selection was invalid.
            return mapping.findForward(Constants.MAPPING_BASIC);
        }

        final AttachmentFile file = attachment.getFile();
        this.streamToResponse(file.getData(), getValidHeaderString(file.getName()), getValidHeaderString(file.getType()), response);

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
        ((ProtocolForm) form).setDocument(getDocumentService().getByDocumentHeaderId(
                protocolSubmission.getProtocol().getProtocolDocument().getDocumentNumber()));
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
        ProtocolTask task = new ProtocolTask(TaskName.ASSIGN_TO_COMMITTEE_SCHEDULE, protocolForm.getProtocolDocument().getProtocol());
        if (isAuthorized(task)) {
            ProtocolAssignCmtSchedBean actionBean = protocolForm.getActionHelper().getAssignCmtSchedBean();
            if (applyRules(new ProtocolAssignCmtSchedEvent(protocolForm.getProtocolDocument(), actionBean))) {
                if (isCommitteeMeetingAssignedMaxProtocols(actionBean.getNewCommitteeId(), actionBean.getNewScheduleId())) {
                    return confirm(buildAssignToCmtSchedConfirmationQuestion(mapping, form, request, response),
                            CONFIRM_ASSIGN_CMT_SCHED_KEY, "");
                }
                getProtocolAssignCmtSchedService().assignToCommitteeAndSchedule(protocolForm.getProtocolDocument().getProtocol(), actionBean);
            }
        }

        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    /*
     * Builds the confirmation question to verify if the user wants to assign the protocol to the committee.
     */
    private StrutsConfirmation buildAssignToCmtSchedConfirmationQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return buildParameterizedConfirmationQuestion(mapping, form, request, response, CONFIRM_ASSIGN_CMT_SCHED_KEY,
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
     * @return the destination (always the original Protocol Document web page that caused this action to be invoked)
     * @throws Exception
     * @see KraTransactionalDocumentActionBase#confirm(StrutsQuestion, String, String)
     */
    public ActionForward confirmAssignCmtSched(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Object question = request.getParameter(QUESTION_INST_ATTRIBUTE_NAME);

        if (CONFIRM_ASSIGN_CMT_SCHED_KEY.equals(question)) {
            ProtocolForm protocolForm = (ProtocolForm) form;
            ProtocolAssignCmtSchedBean actionBean = protocolForm.getActionHelper().getAssignCmtSchedBean();
            getProtocolAssignCmtSchedService().assignToCommitteeAndSchedule(protocolForm.getProtocolDocument().getProtocol(), actionBean);
        }

        return mapping.findForward(MAPPING_BASIC);
    }

    private ProtocolAssignCmtSchedService getProtocolAssignCmtSchedService() {
        return KraServiceLocator.getService(ProtocolAssignCmtSchedService.class);
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

        ProtocolForm protocolForm = (ProtocolForm) form;
        ProtocolTask task = new ProtocolTask(TaskName.ASSIGN_REVIEWERS, protocolForm.getProtocolDocument().getProtocol());
        if (isAuthorized(task)) {
            ProtocolAssignReviewersBean actionBean = protocolForm.getActionHelper().getProtocolAssignReviewersBean();
            if (applyRules(new ProtocolAssignReviewersEvent(protocolForm.getProtocolDocument(), actionBean))) {
                getProtocolAssignReviewersService().assignReviewers(protocolForm.getProtocolDocument().getProtocol(), actionBean.getReviewers());
            }
        }

        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    private ProtocolAssignReviewersService getProtocolAssignReviewersService() {
        return KraServiceLocator.getService(ProtocolAssignReviewersService.class);
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
        ProtocolGrantExemptionBean actionBean = protocolForm.getActionHelper().getProtocolGrantExemptionBean();
        actionBean.getReviewComments().addNewComment();
        
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
        actionBean.getReviewComments().deleteComment(getLineToDelete(request));
        
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
        actionBean.getReviewComments().moveUp(getLineToDelete(request));
        
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
        actionBean.getReviewComments().moveDown(getLineToDelete(request));
        
        return mapping.findForward(Constants.MAPPING_BASIC);
    }
    
    private ProtocolGrantExemptionService getProtocolGrantExemptionService() {
        return KraServiceLocator.getService(ProtocolGrantExemptionService.class);
    }
}
