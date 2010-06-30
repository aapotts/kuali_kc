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
package org.kuali.kra.irb.onlinereview;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.kra.committee.bo.CommitteeMembership;
import org.kuali.kra.committee.service.CommitteeService;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.ProtocolDocument;
import org.kuali.kra.irb.ProtocolOnlineReviewDocument;
import org.kuali.kra.irb.actions.assignreviewers.ProtocolAssignReviewersService;
import org.kuali.kra.irb.actions.submit.ProtocolReviewer;
import org.kuali.kra.irb.actions.submit.ProtocolSubmission;
import org.kuali.kra.service.KraAuthorizationService;
import org.kuali.rice.kew.exception.WorkflowException;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kim.service.IdentityManagementService;
import org.kuali.rice.kim.service.PersonService;
import org.kuali.rice.kns.bo.DocumentHeader;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.DocumentService;
import org.kuali.rice.kns.workflow.service.KualiWorkflowDocument;
import org.kuali.rice.kns.workflow.service.WorkflowDocumentService;

public class ProtocolOnlineReviewServiceImpl implements ProtocolOnlineReviewService {

    
    private BusinessObjectService businessObjectService;
    private DocumentService documentService;
    private KraAuthorizationService kraAuthorizationService;
    private ProtocolAssignReviewersService protocolAssignReviewersService;
    private IdentityManagementService identityManagementService;
    private CommitteeService committeeService;

    private PersonService personService;
    
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ProtocolOnlineReviewServiceImpl.class);
    
    /**
     * @see org.kuali.kra.irb.onlinereview.ProtocolOnlineReviewService#assignOnlineReviewer(org.kuali.kra.irb.Protocol, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public ProtocolOnlineReviewDocument assignOnlineReviewer(Protocol protocol, 
                                                             String personId,
                                                             String documentDescription,
                                                             String documentExplanation,
                                                             String documentOrganizationDocumentNumber,
                                                             String principalId ) throws WorkflowException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Assigning online reviewer [%s] to protocol [%s].", personId, protocol.getProtocolNumber()));
        }
        ProtocolSubmission submission = protocolAssignReviewersService.getCurrentSubmission(protocol);
        
        if (submission == null) { 
            throw new IllegalStateException(String.format("Cannot assign online reviewers, there is no current submission for the protocol %s.", protocol.getProtocolNumber()));
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Current submission for protocol %s is %s.", protocol.getProtocolNumber(), submission.getSubmissionNumber()));
        }
        ProtocolOnlineReviewDocument reviewDocument = createNewProtocolOnlineReviewDocument(protocol, submission, personId, documentDescription, documentExplanation, documentOrganizationDocumentNumber, principalId);
        //save the document, should save the BOs as well.
        documentService.saveDocument(reviewDocument);
        reviewDocument.getProtocolOnlineReview().refresh();
        
        //irb admin needs to see these documents before they go final.
        reviewDocument.getDocumentHeader().getWorkflowDocument().setReceiveFutureRequests();
        reviewDocument.getDocumentHeader().getWorkflowDocument().setDoNotReceiveFutureRequests();
        documentService.routeDocument(reviewDocument, "Submitted into workflow by ProtocolOnlineReviewServiceImpl when review was created.", new java.util.ArrayList());
        documentService.approveDocument(reviewDocument, "", new java.util.ArrayList());
        return reviewDocument;
    }

    public void submitOnlineReviwToWorkflow(ProtocolOnlineReviewDocument protocolReviewDocument) {
        //TODO: complete method or remove from interface.
    }
    
    /**
     * Create a new ProtocolReviewDocument.  Handles creating the workflow document, and the underlying ProtocolReview BO linking the protocol, submission, and reviewer.
     * 
     * @param protocol The protocol 
     * @param submission The submission the review is for.
     * @param reviewer The reviewer being assigned.
     * @param documentDescription the description for the created document
     * @param documentExplanation the explanation for the created document
     * @param documentOrganizationNumber the organizationNumber for the created document
     * @param principalId The principalId to use when creating the workflow document. Usually this should be the principal of the user creating the review.
     * @return
     * @throws WorkflowException
     */
    private ProtocolOnlineReviewDocument createNewProtocolOnlineReviewDocument( Protocol protocol, 
                                                                                ProtocolSubmission submission, 
                                                                                String personId, 
                                                                                String documentDescription,
                                                                                String documentExplanation,
                                                                                String documentOrganizationDocumentNumber,
                                                                                String principalId ) 
        throws WorkflowException {
        
        Person reviewerPerson = personService.getPerson(personId);
        
        ProtocolOnlineReviewDocument protocolReviewDocument;
        
        Person person = personService.getPerson(principalId);
        KualiWorkflowDocument workflowDocument = getWorkflowDocumentService().createWorkflowDocument(PROTOCOL_ONLINE_REVIEW_DOCUMENT_TYPE, person);
        
        DocumentHeader docHeader = new DocumentHeader();
        docHeader.setWorkflowDocument(workflowDocument);
        docHeader.setDocumentNumber(workflowDocument.getRouteHeaderId().toString());
        protocolReviewDocument = new ProtocolOnlineReviewDocument();
        protocolReviewDocument.setDocumentNumber(docHeader.getDocumentNumber());
        protocolReviewDocument.setDocumentHeader(docHeader);
        
        protocolReviewDocument.getProtocolOnlineReview().setProtocol(protocol);
        protocolReviewDocument.getProtocolOnlineReview().setProtocolId(protocol.getProtocolId());
        
        protocolReviewDocument.getProtocolOnlineReview().setProtocolSubmission(submission);
        protocolReviewDocument.getProtocolOnlineReview().setSubmissionIdFk(submission.getSubmissionId());
        protocolReviewDocument.getProtocolOnlineReview().setProtocolOnlineReviewStatusCode(ProtocolOnlineReviewStatus.SAVED_STATUS_CD);
        protocolReviewDocument.getProtocolOnlineReview().setDateRequested(new java.sql.Date((new java.util.Date()).getTime()));
        
        ProtocolReviewer reviewer = getOrCreateProtocolReviewerByPersonIdAndSubmissionId(personId, protocol, submission);
        if (reviewer.getProtocolOnlineReviews().size() > 0) {
            throw new IllegalStateException(String.format( "Reviewer %s already has an assigned OnlineReview for (protocolId=%s,submissionId=%s)",protocol.getProtocolId(),submission.getSubmissionId()));
        }
        
        protocolReviewDocument.getProtocolOnlineReview().setProtocolReviewerId(reviewer.getProtocolReviewerId());
        protocolReviewDocument.getProtocolOnlineReview().setProtocolReviewer(reviewer);
        
        docHeader.setDocumentDescription(documentDescription);
        docHeader.setOrganizationDocumentNumber(documentOrganizationDocumentNumber);
        docHeader.setExplanation(documentExplanation);
        return protocolReviewDocument;
    }
    
    
    
    /**
     * Gets the online review for the protocol,submission, and person - or it will create a new one and return it.
     * @param personId
     * @param protocol
     * @param submission
     * @return
     */
    private ProtocolReviewer getOrCreateProtocolReviewerByPersonIdAndSubmissionId(String personId, Protocol protocol, ProtocolSubmission submission) {
        
        Long protocolId = protocol.getProtocolId();
        Long submissionId = submission.getSubmissionId();
       
        Map<String,Object> keyMap = new HashMap<String,Object>();
        keyMap.put("personId", personId);
        keyMap.put("protocolId", protocolId);
        keyMap.put("submissionIdFk", submissionId);
        ProtocolReviewer result;
       
        List<ProtocolReviewer> reviewers = (List<ProtocolReviewer>)businessObjectService.findMatching(ProtocolReviewer.class, keyMap);
        if (reviewers.size() == 1) {
            result =  reviewers.get(0);
        } else if (reviewers.size() == 0) {
            result = new ProtocolReviewer();
            result.setPersonId(personId);
            result.setProtocolId(protocolId);
            result.setSubmissionIdFk(submissionId);
            result.setProtocolNumber(protocol.getProtocolNumber());
            result.setProtocol(protocol);
            result.setSubmissionNumber(submission.getSubmissionNumber());
            result.setSequenceNumber(1);
            //TODO:FIX
            result.setReviewerTypeCode("1");
            businessObjectService.save(result);
           
            result.refresh();
        } else {
            throw new IllegalStateException(String.format( "More than 1 ProtocolReview record for (personId=%s,protocolId=%s,submissionId=%s), do not know what to do.",personId,protocolId,submissionId));
        }
        return result;		
    }
    
    /**
     * @see org.kuali.kra.irb.onlinereview.ProtocolOnlineReviewService#getProtocolReviewDocumentsForCurrentSubmission(org.kuali.kra.irb.Protocol)
     */
    public List<ProtocolOnlineReviewDocument> getProtocolReviewDocumentsForCurrentSubmission(Protocol protocol) {
       
        List<ProtocolOnlineReviewDocument> onlineReviewDocuments = new ArrayList<ProtocolOnlineReviewDocument>();
        ProtocolSubmission submission = protocolAssignReviewersService.getCurrentSubmission(protocol);
        
        List<ProtocolOnlineReview> reviews = findProtocolOnlineReviews(protocol.getProtocolId(), 
                                                                       submission.getSubmissionId(),
                                                                       null, 
                                                                       null, 
                                                                       null, 
                                                                       false, 
                                                                       null, 
                                                                       false, 
                                                                       null, 
                                                                       false); 
                                                                       
        
        for (ProtocolOnlineReview review : reviews) {
            review.refresh();
            onlineReviewDocuments.add(review.getProtocolOnlineReviewDocument());
        }
        
        return onlineReviewDocuments;
    }

    
    /**
     * @see org.kuali.kra.irb.onlinereview.ProtocolOnlineReviewService#getAvailableCommitteeMembersForCurrentSubmission(org.kuali.kra.irb.Protocol)
     */
    public List<CommitteeMembership> getAvailableCommitteeMembersForCurrentSubmission(Protocol protocol) {
        ProtocolSubmission submission = protocolAssignReviewersService.getCurrentSubmission(protocol);
        if (LOG.isDebugEnabled()) { 
            LOG.debug(String.format("Fetching available committee members for protocol %s, submission %s", protocol.getProtocolNumber(), 
                    submission.getSubmissionNumber()));
        }
        return getCommitteeService().getAvailableMembers(submission.getCommitteeId(), submission.getScheduleId());
    }
    
    
    /**
     * This method...
     * @param protocolId
     * @param submissionIdFk
     * @param protocolReviewerId
     * @param protocolOnlineReviewStatusCode
     * @param protocolOnlineReviewDeterminationRecommendationCode
     * @param protocolOnlineReviewDeterminationRecommendationCodeEnable
     * @param dateDue
     * @param dateDueEnable
     * @param dateRequested
     * @param dateRequestedEnable
     * @return
     */
    public List<ProtocolOnlineReview> findProtocolOnlineReviews(Long protocolId,
                                                                 Long submissionIdFk,
                                                                 Long protocolReviewerId,
                                                                 String protocolOnlineReviewStatusCode,
                                                                 String protocolOnlineReviewDeterminationRecommendationCode,
                                                                 boolean protocolOnlineReviewDeterminationRecommendationCodeEnable,
                                                                 Date dateDue,
                                                                 boolean dateDueEnable,
                                                                 Date dateRequested,
                                                                 boolean dateRequestedEnable) {
        
        Map<String,Object> hashMap = new HashMap<String,Object>();
        if (protocolId != null) { 
            hashMap.put("protocolId", protocolId);
        }
        if (submissionIdFk != null) { 
            hashMap.put("submissionIdFk", submissionIdFk);
        }
        if (protocolReviewerId != null) { 
            hashMap.put("protocolReviewerId", protocolReviewerId);
        }
        if (protocolOnlineReviewStatusCode != null) { 
            hashMap.put("protocolOnlineReviewStatusCode", protocolOnlineReviewStatusCode);
        }
        if (protocolOnlineReviewDeterminationRecommendationCodeEnable) { 
            hashMap.put("protocolOnlineReviewDeterminationRecommendationCode", protocolOnlineReviewDeterminationRecommendationCode);
        }
        if (dateRequestedEnable) {
            hashMap.put("dateRequested", dateDue);
        }
        if (dateDueEnable) { 
            hashMap.put("dateDue", dateDue);
        }
        
        List<ProtocolOnlineReview> results = (List<ProtocolOnlineReview>) getBusinessObjectService().findMatchingOrderBy(ProtocolOnlineReview.class, hashMap, "dateRequested", false );
       
        return results;
        
    }
    
    
    /**
     * @see org.kuali.kra.irb.onlinereview.ProtocolOnlineReviewService#getOnlineReviewersForProtocolSubmission(java.lang.Long)
     */
    public List<ProtocolOnlineReview> getOnlineReviewersForProtocolSubmission(Long submissionId) {
        List<ProtocolOnlineReview> reviews = new ArrayList<ProtocolOnlineReview>();
        ProtocolSubmission submission = getBusinessObjectService().findBySinglePrimaryKey(ProtocolSubmission.class, submissionId);
        if (submission != null) {
            reviews = submission.getProtocolOnlineReviews();
        }
        return reviews;
    }
    
    
    /*
     * Getters and setters for needed services.
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }
    public DocumentService getDocumentService() {
        return documentService;
    }
    public void setKraAuthorizationService(KraAuthorizationService kraAuthorizationService) {
        this.kraAuthorizationService = kraAuthorizationService;
    }
    public KraAuthorizationService getKraAuthorizationService() {
        return kraAuthorizationService;
    }
    public void setProtocolAssignReviewersService(ProtocolAssignReviewersService protocolAssignReviewersService) {
        this.protocolAssignReviewersService = protocolAssignReviewersService;
    }
    public ProtocolAssignReviewersService getProtocolAssignReviewersService() {
        return protocolAssignReviewersService;
    }
    /**
     * Gets the workflowDocumentService attribute. 
     * @return Returns the workflowDocumentService.
     */
    public WorkflowDocumentService getWorkflowDocumentService() {
        return KraServiceLocator.getService(WorkflowDocumentService.class);
    }
    /**
     * Gets the personService attribute. 
     * @return Returns the personService.
     */
    public PersonService getPersonService() {
        return personService;
    }
    /**
     * Sets the personService attribute value.
     * @param personService The personService to set.
     */
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }
    /**
     * Gets the identityManagementService attribute. 
     * @return Returns the identityManagementService.
     */
    public IdentityManagementService getIdentityManagementService() {
        return identityManagementService;
    }
    /**
     * Sets the identityManagementService attribute value.
     * @param identityManagementService The identityManagementService to set.
     */
    public void setIdentityManagementService(IdentityManagementService identityManagementService) {
        this.identityManagementService = identityManagementService;
    }
    
    /**
     * Gets the committeeService attribute. 
     * @return Returns the committeeService.
     */
    public CommitteeService getCommitteeService() {
        return committeeService;
    }
    /**
     * Sets the committeeService attribute value.
     * @param committeeService The committeeService to set.
     */
    public void setCommitteeService(CommitteeService committeeService) {
        this.committeeService = committeeService;
    }

    
    /**
     * Get a protocol by the document number.
     * @param documentNumber
     * @return
     */
    public Protocol getProtocolByDocumentNumber(String documentNumber)  {
        try {
            return ((ProtocolDocument) documentService.getByDocumentHeaderId(documentNumber)).getProtocol();
        } catch (WorkflowException e) {
           LOG.error(String.format( "Workflow exception thrown while attempting to load ProtocolDocument %s:%s", documentNumber, e.getMessage()));
           throw new RuntimeException(String.format( "Could not load ProtocolDocument %s", e), e);
        }
    }
    
    /**
     * Get a protocol by the protocol number.
     * @param protocolNumber
     * @return
     */
    public Protocol getProtocolByProtocolNumber(String protocolNumber) {
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put(Constants.PROPERTY_PROTOCOL_NUMBER,protocolNumber);
        return ((List<Protocol>)businessObjectService.findMatching(Protocol.class, fieldMap)).get(0);
    }
    
    
    /**
     * Get all of the ProtocolOnlineReview BOs for the protocol and submission.
     * @param protocolId
     * @param submissionId
     * @return
     */
    public List<ProtocolOnlineReview> getProtocolOnlineReviews(Long protocolId, Long submissionId) {
        
        List<ProtocolOnlineReview> reviews = findProtocolOnlineReviews(protocolId, 
                submissionId,
                null, 
                null, 
                null, 
                false, 
                null, 
                false, 
                null, 
                false); 
        
        return reviews;
    }

    /**
     * @see org.kuali.kra.irb.onlinereview.ProtocolOnlineReviewService#getProtocolReviewsForCurrentSubmission(java.lang.String)
     */
    public List<ProtocolOnlineReview> getProtocolReviewsForCurrentSubmission(String protocolNumber) {
        Protocol protocol = getProtocolByProtocolNumber(protocolNumber);
        return getProtocolOnlineReviews(protocol.getProtocolId() ,protocolAssignReviewersService.getCurrentSubmission(protocol).getSubmissionId());
    }

    /**
     * @see org.kuali.kra.irb.onlinereview.ProtocolOnlineReviewService#isUserAnOnlineReviewerOfProtocol(java.lang.String, org.kuali.kra.irb.Protocol)
     */
    public boolean isUserAnOnlineReviewerOfProtocol(String principalId, Protocol protocol) {
        
        boolean result = false;
        ProtocolSubmission submission = protocolAssignReviewersService.getCurrentSubmission(protocol);
        if (submission != null) {
            List<ProtocolOnlineReview> reviews = getProtocolOnlineReviews(protocol.getProtocolId(), submission.getSubmissionId());
            for (ProtocolOnlineReview review : reviews) {
                if (StringUtils.equals(review.getProtocolReviewer().getPersonId(),principalId)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * @see org.kuali.kra.irb.onlinereview.ProtocolOnlineReviewService#isProtocolInStateToBeReviewed(org.kuali.kra.irb.Protocol)
     */
    public boolean isProtocolInStateToBeReviewed(Protocol protocol) {
        boolean result = false;
        ProtocolSubmission submission = protocolAssignReviewersService.getCurrentSubmission(protocol);
        if( submission != null ) {
            result = true;
        }
        return result;
    }

   
    
}
