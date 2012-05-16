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
package org.kuali.kra.iacuc.onlinereview;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.kra.committee.bo.CommitteeMembership;
import org.kuali.kra.committee.service.CommitteeService;
import org.kuali.kra.iacuc.IacucProtocolOnlineReviewDocument;
import org.kuali.kra.iacuc.actions.submit.IacucProtocolReviewer;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.irb.actions.submit.ProtocolSubmissionStatus;
import org.kuali.kra.protocol.Protocol;
import org.kuali.kra.protocol.ProtocolDocument;
import org.kuali.kra.protocol.ProtocolOnlineReviewDocument;
import org.kuali.kra.protocol.actions.submit.ProtocolReviewer;
import org.kuali.kra.protocol.actions.submit.ProtocolSubmission;
import org.kuali.kra.protocol.onlinereview.ProtocolOnlineReview;
import org.kuali.kra.protocol.onlinereview.ProtocolOnlineReviewStatus;
import org.kuali.kra.protocol.personnel.ProtocolPerson;
import org.kuali.kra.service.KraAuthorizationService;
import org.kuali.kra.service.KraWorkflowService;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.bo.AdHocRouteRecipient;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.workflow.service.WorkflowDocumentService;

public class IacucProtocolOnlineReviewServiceImpl implements IacucProtocolOnlineReviewService {
    private static final Log LOG = LogFactory.getLog(IacucProtocolOnlineReviewServiceImpl.class);
    private BusinessObjectService businessObjectService;
    private DocumentService documentService;
    private KraAuthorizationService kraAuthorizationService;
    private IdentityService identityManagementService;
    private CommitteeService committeeService;
    private KraWorkflowService kraWorkflowService;
    private WorkflowDocumentService workflowDocumentService;

    private String reviewerApproveNodeName;
    private String irbAdminApproveNodeName;

    @SuppressWarnings("unchecked")
    private PersonService personService;

    @Override
    public List<CommitteeMembership> getAvailableCommitteeMembersForCurrentSubmission(Protocol protocol) {
        List<CommitteeMembership> results = new ArrayList<CommitteeMembership>();

        ProtocolSubmission submission = protocol.getProtocolSubmission();
        submission.refreshReferenceObject("protocolOnlineReviews");
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Fetching available committee members for protocol %s, submission %s",
                    protocol.getProtocolNumber(), submission.getSubmissionNumber()));
        }

        List<ProtocolOnlineReview> currentReviews = submission.getProtocolOnlineReviews();
        List<CommitteeMembership> committeeMembers = committeeService.getAvailableMembers(submission.getCommitteeId(),
                submission.getScheduleId());
        // TODO: Make this better.
        // should run this for loop to exclude protocol personnel
//        if (CollectionUtils.isNotEmpty(currentReviews)) {
            for (CommitteeMembership member : committeeMembers) {
                boolean found = false;
                for (ProtocolOnlineReview review : currentReviews) {
                    if (review.getProtocolReviewer().isProtocolReviewerFromCommitteeMembership(member) && review.isActive()) {
                        found = true;
                        break;
                    }
                }
                if (!found && !isProtocolPersonnel(protocol, member)) {
                    results.add(member);
                }
            }
//        }
//        else {
//            results.addAll(committeeMembers);
//        }
        return results;
    }

    public List<ProtocolOnlineReviewDocument> getProtocolReviewDocumentsForCurrentSubmission(Protocol protocol) {
        List<ProtocolOnlineReviewDocument> onlineReviewDocuments = new ArrayList<ProtocolOnlineReviewDocument>();
        ProtocolSubmission submission = protocol.getProtocolSubmission();
        List<ProtocolOnlineReview> reviews = findProtocolOnlineReviews(protocol.getProtocolId(), submission.getSubmissionId());
        for (ProtocolOnlineReview review : reviews) {
            if (review.isActive()) {
                review.refresh();
                try {
                    onlineReviewDocuments.add((ProtocolOnlineReviewDocument) (documentService.getByDocumentHeaderId(review
                            .getProtocolOnlineReviewDocument().getDocumentNumber())));
                }
                catch (WorkflowException e) {
                    throw new RuntimeException(String.format(
                            "Could not load ProtocolOnlineReview docuemnt %s due to WorkflowException: %s", review
                                    .getProtocolOnlineReviewDocument().getDocumentNumber(), e.getMessage()), e);
                }
            }
        }
        return onlineReviewDocuments;
    }

    public String getProtocolOnlineReviewDocumentDescription(String protocolNumber, String piName) {
        final int fieldLimit = 40;
        int pilen = piName != null ? piName.length() : 0;
        int pnlen = protocolNumber != null ? protocolNumber.length() : 0;
        int ttlLength = pilen + pnlen + ONLINE_REVIEW_DOCUMENT_DESCRIPTION_FORMAT.length() - 4;
        String piNameToUse = piName;

        if (ttlLength > fieldLimit && piName != null) {
            int charsToTrim = ttlLength - fieldLimit;
            piNameToUse = piName.substring(0, Math.max(piName.length() - charsToTrim - 1, 0));
        }
        String init = String.format(ONLINE_REVIEW_DOCUMENT_DESCRIPTION_FORMAT, piNameToUse, protocolNumber);
        if (init.length() > fieldLimit) {
            return init.substring(0, fieldLimit - 1);
        }
        else {
            return init;
        }

    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    public void setKraAuthorizationService(KraAuthorizationService kraAuthorizationService) {
        this.kraAuthorizationService = kraAuthorizationService;
    }


    public void setIdentityManagementService(IdentityService identityManagementService) {
        this.identityManagementService = identityManagementService;
    }

    public void setCommitteeService(CommitteeService committeeService) {
        this.committeeService = committeeService;
    }

    private List<String> getPersonnelIds(Protocol protocol) {
        List<String> PersonnelIds = new ArrayList<String>();
        for (ProtocolPerson person : protocol.getProtocolPersons()) {
            if (StringUtils.isNotBlank(person.getPersonId())) {
                PersonnelIds.add(person.getPersonId());
            }
            else {
                PersonnelIds.add(person.getRolodexId().toString());
            }
        }

        return PersonnelIds;
    }

    private boolean isProtocolPersonnel(Protocol protocol, CommitteeMembership member) {
        return getPersonnelIds(protocol).contains(member.getPersonId());
    }

    @SuppressWarnings("unchecked")
    protected List<ProtocolOnlineReview> findProtocolOnlineReviews(Long protocolId, Long submissionIdFk) {
        List<ProtocolOnlineReview> reviews = new ArrayList<ProtocolOnlineReview>();
        if (protocolId != null && submissionIdFk != null) {
            Map<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("protocolId", protocolId);
            hashMap.put("submissionIdFk", submissionIdFk);
            reviews.addAll(businessObjectService.findMatchingOrderBy(IacucProtocolOnlineReview.class, hashMap, "dateRequested",
                    false));
        }
        return reviews;
    }

    public boolean isProtocolReviewer(String personId, boolean nonEmployeeFlag, ProtocolSubmission protocolSubmission) {
        boolean isReviewer = false;

        if (protocolSubmission != null) {
            for (ProtocolOnlineReview review : protocolSubmission.getProtocolOnlineReviews()) {
                if (review.getProtocolReviewer().isPersonIdProtocolReviewer(personId, nonEmployeeFlag) && review.isActive()) {
                    isReviewer = true;
                    break;
                }
            }
        }


        return isReviewer;
    }

    public boolean isProtocolInStateToBeReviewed(Protocol protocol) {
        boolean isReviewable = false;
        ProtocolSubmission submission = protocol.getProtocolSubmission();
        if (submission != null) {
            try {
                isReviewable = StringUtils.isNotEmpty(submission.getScheduleId());
                isReviewable &= (StringUtils.equals(submission.getSubmissionStatusCode(),
                        ProtocolSubmissionStatus.SUBMITTED_TO_COMMITTEE) || StringUtils.equals(
                        submission.getSubmissionStatusCode(), ProtocolSubmissionStatus.IN_AGENDA));
                // TODO : uncomment following when IACUC doc wkflw is ready
//                ProtocolDocument protocolDocument = (ProtocolDocument) documentService.getByDocumentHeaderId(protocol
//                        .getProtocolDocument().getDocumentNumber());
//                isReviewable &= kraWorkflowService.isDocumentOnNode(protocolDocument, Constants.IACUC_PROTOCOL_IRBREVIEW_ROUTE_NODE_NAME);
            }
//            catch (WorkflowException e) {
                catch (Exception e) {
                String errorString = String.format(
                        "WorkflowException checking route node for creating new ProtocolOnlineReviewDocument " + "for protocol %s",
                        submission.getProtocolNumber());
                LOG.error(errorString, e);
                throw new RuntimeException(errorString, e);
            }
        }
        return isReviewable;
    }

    public void setKraWorkflowService(KraWorkflowService kraWorkflowService) {
        this.kraWorkflowService = kraWorkflowService;
    }

    @Override
    public ProtocolOnlineReviewDocument createAndRouteProtocolOnlineReviewDocument(ProtocolSubmission protocolSubmission, 
            ProtocolReviewer protocolReviewer,
            String documentDescription,
            String documentExplanation,
            String documentOrganizationDocumentNumber,
            String documentRouteAnnotation,
            boolean initialApproval,
            Date dateRequested,
            Date dateDue, 
            String principalId) {
        ProtocolOnlineReviewDocument document = null;

        try {
            if (LOG.isDebugEnabled()) {
                String protocolNumber = protocolSubmission.getProtocolNumber();
                Integer submissionNumber = protocolSubmission.getSubmissionNumber();
                LOG.debug(String.format("Assigning online reviewer [%s] to protocol [%s].", protocolReviewer, protocolNumber));
                LOG.debug(String.format("Current submission for protocol %s is %s.", protocolNumber, submissionNumber));
            }

            document = createProtocolOnlineReviewDocument(protocolSubmission, protocolReviewer, documentDescription,
                    documentExplanation, documentOrganizationDocumentNumber, dateRequested, dateDue, principalId);

            documentService.routeDocument(document, "Review Requested by PI during protocol submission.",
                    new ArrayList<AdHocRouteRecipient>());

            if (initialApproval) {
                documentService.approveDocument(document, "", new ArrayList<AdHocRouteRecipient>());
            }
        }
        catch (WorkflowException e) {
            String errorString = String.format(
                    "WorkflowException creating new ProtocolOnlineReviewDocument for reviewer %s, protocol %s",
                    protocolReviewer.getPersonId(), protocolSubmission.getProtocolNumber());
            LOG.error(errorString, e);
            throw new RuntimeException(errorString, e);
        }

        return document;
    }

    @Override
    public ProtocolReviewer createProtocolReviewer(String principalId, boolean nonEmployeeFlag, String reviewerTypeCode,
            ProtocolSubmission protocolSubmission) {
        IacucProtocolReviewer reviewer = new IacucProtocolReviewer();
        reviewer.setProtocolIdFk(protocolSubmission.getProtocolId());
        reviewer.setSubmissionIdFk(protocolSubmission.getSubmissionId());
        reviewer.setProtocolNumber(protocolSubmission.getProtocolNumber());
        reviewer.setSequenceNumber(protocolSubmission.getSequenceNumber());
        reviewer.setSubmissionNumber(protocolSubmission.getSubmissionNumber());
        if (!nonEmployeeFlag) {
            reviewer.setPersonId(principalId);
        }
        else {
            reviewer.setRolodexId(Integer.parseInt(principalId));
        }
        reviewer.setNonEmployeeFlag(nonEmployeeFlag);
        reviewer.setReviewerTypeCode(reviewerTypeCode);

        businessObjectService.save(reviewer);

        return reviewer;
    }

    protected ProtocolOnlineReviewDocument createProtocolOnlineReviewDocument(ProtocolSubmission protocolSubmission,
            ProtocolReviewer protocolReviewer, String documentDescription, String documentExplanation,
            String documentOrganizationDocumentNumber, Date dateRequested, Date dateDue, String principalId)
            throws WorkflowException {

        IacucProtocolOnlineReviewDocument protocolReviewDocument;

        Person person = personService.getPerson(principalId);
        WorkflowDocument workflowDocument = workflowDocumentService.createWorkflowDocument(IACUC_PROTOCOL_ONLINE_REVIEW_DOCUMENT_TYPE,
                person);

        DocumentHeader docHeader = new DocumentHeader();
        docHeader.setWorkflowDocument(workflowDocument);
        docHeader.setDocumentNumber(workflowDocument.getDocumentId().toString());
        protocolReviewDocument = new IacucProtocolOnlineReviewDocument();
        protocolReviewDocument.setDocumentNumber(docHeader.getDocumentNumber());
        protocolReviewDocument.setDocumentHeader(docHeader);


        protocolReviewDocument.getProtocolOnlineReview().setProtocol(protocolSubmission.getProtocol());


        protocolReviewDocument.getProtocolOnlineReview().setProtocolId(protocolSubmission.getProtocolId());

        protocolReviewDocument.getProtocolOnlineReview().setProtocolSubmission(protocolSubmission);
        protocolReviewDocument.getProtocolOnlineReview().setSubmissionIdFk(protocolSubmission.getSubmissionId());
        protocolReviewDocument.getProtocolOnlineReview().setProtocolOnlineReviewStatusCode(
                ProtocolOnlineReviewStatus.SAVED_STATUS_CD);
        protocolReviewDocument.getProtocolOnlineReview().setDateRequested(
                dateRequested == null ? new Date((new java.util.Date()).getTime()) : dateRequested);
        protocolReviewDocument.getProtocolOnlineReview().setDateDue(dateDue);

        protocolReviewDocument.getProtocolOnlineReview().setProtocolReviewerId(protocolReviewer.getProtocolReviewerId());
        protocolReviewDocument.getProtocolOnlineReview().setProtocolReviewer(protocolReviewer);

        docHeader.setDocumentDescription(documentDescription);
        docHeader.setOrganizationDocumentNumber(documentOrganizationDocumentNumber);
        docHeader.setExplanation(documentExplanation);

        documentService.saveDocument(protocolReviewDocument);
        return protocolReviewDocument;
    }

    public void setWorkflowDocumentService(WorkflowDocumentService workflowDocumentService) {
        this.workflowDocumentService = workflowDocumentService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

}
