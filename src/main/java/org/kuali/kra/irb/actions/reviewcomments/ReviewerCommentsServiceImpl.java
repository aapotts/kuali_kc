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
package org.kuali.kra.irb.actions.reviewcomments;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.kra.committee.bo.Committee;
import org.kuali.kra.committee.bo.CommitteeSchedule;
import org.kuali.kra.committee.service.CommitteeScheduleService;
import org.kuali.kra.committee.service.CommitteeService;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.ProtocolFinderDao;
import org.kuali.kra.irb.actions.submit.ProtocolReviewer;
import org.kuali.kra.irb.actions.submit.ProtocolSubmission;
import org.kuali.kra.irb.onlinereview.ProtocolOnlineReview;
import org.kuali.kra.meeting.CommitteeScheduleMinute;
import org.kuali.kra.meeting.MinuteEntryType;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.util.GlobalVariables;

/**
 * 
 * This class takes care of the persistence for Reviewer comments.
 */
public class ReviewerCommentsServiceImpl implements ReviewerCommentsService {
    
    private BusinessObjectService businessObjectService;
    
    private CommitteeScheduleService committeeScheduleService;
    private CommitteeService committeeService;
    private ProtocolFinderDao protocolFinderDao;
   
    /**
     * Set the Business Object Service.
     * @param businessObjectService BusinessObjectService
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
    
    /**
     * This method sets the committee schedule service.
     * @param committeeScheduleService CommitteeScheduleService
     */
    public void setCommitteeScheduleService(CommitteeScheduleService committeeScheduleService) {
        this.committeeScheduleService = committeeScheduleService;
    }
    
    /**
     * {@inheritDoc}
     * @see org.kuali.kra.irb.actions.reviewcomments.ReviewerCommentsService#getReviewerComments(java.lang.String, int)
     */
    public List<CommitteeScheduleMinute> getReviewerComments(String protocolNumber, int submissionNumber) {
        ArrayList<CommitteeScheduleMinute> reviewerComments = new ArrayList<CommitteeScheduleMinute>();
        
        List<ProtocolSubmission> protocolSubmissions = protocolFinderDao.findProtocolSubmissions(protocolNumber, submissionNumber);
        
        for (ProtocolSubmission protocolSubmission : protocolSubmissions) {
            if (protocolSubmission.getCommitteeScheduleMinutes() != null) {
                for (CommitteeScheduleMinute minute : protocolSubmission.getCommitteeScheduleMinutes()) {
                    String minuteEntryTypeCode = minute.getMinuteEntryTypeCode();
                    // need to check current minute entry; otherwise may have minutes from previous version comittee
                    if (MinuteEntryType.PROTOCOL.equals(minuteEntryTypeCode) && isCurrentMinuteEntry(minute)) {
                        reviewerComments.add(minute);
                    }
                }
            }
        }
        
        return reviewerComments;
    }
    
    /**
     * 
     * @see org.kuali.kra.irb.actions.reviewcomments.ReviewerCommentsService#getProtocolReviewers(java.lang.String, int)
     */
    public List<ProtocolReviewer> getProtocolReviewers(String protocolNumber, int submissionNumber) {
        List<ProtocolReviewer> reviewers = new ArrayList<ProtocolReviewer>();

        List<ProtocolSubmission> protocolSubmissions = protocolFinderDao.findProtocolSubmissions(protocolNumber, submissionNumber);

        for (ProtocolSubmission protocolSubmission : protocolSubmissions) {
            if (CollectionUtils.isNotEmpty(protocolSubmission.getProtocolReviewers())) {
                reviewers.addAll(protocolSubmission.getProtocolReviewers());
            }
        }

        return reviewers;
    }

    /*
     * when version committee, the minutes also versioned.  This is to get the current one.
     */
    private boolean isCurrentMinuteEntry(CommitteeScheduleMinute minute) {
        minute.refreshReferenceObject("committeeSchedule");
        if (minute.getCommitteeSchedule() != null) {
            Committee committee = committeeService.getCommitteeById(minute.getCommitteeSchedule().getCommittee().getCommitteeId());
            return committee.getId().equals(minute.getCommitteeSchedule().getCommittee().getId());
        } else {
            // if scheduleid is 999999999
            return true;
        }
    }
    
    /** {@inheritDoc} */
    public void persistReviewerComments(ReviewerComments reviewComments, Protocol protocol) {
        int nextEntryNumber = 0;
        this.businessObjectService.delete(reviewComments.getCommentsToDelete());
        reviewComments.resetComentsToDelete();
        for (CommitteeScheduleMinute minute : reviewComments.getComments()) {
            minute.setEntryNumber(nextEntryNumber);
            boolean doUpdate = false;
            if (minute.getCommScheduleMinutesId() != null) {
                CommitteeScheduleMinute existing = this.committeeScheduleService.getCommitteeScheduleMinute(minute.getCommScheduleMinutesId());
                doUpdate = !minute.equals(existing);
            } else {
                //brand new review comment / minute entry, set some cool stuff
                minute.setMinuteEntryTypeCode(MinuteEntryType.PROTOCOL);
                ProtocolSubmission protocolSubmission = getSubmission(protocol);
                minute.setSubmissionIdFk(protocolSubmission.getSubmissionId());
                minute.setProtocolIdFk(protocolSubmission.getProtocolId());
                if (protocolSubmission.getScheduleIdFk() != null) {
                    minute.setScheduleIdFk(protocolSubmission.getScheduleIdFk());
                } else {
                    minute.setScheduleIdFk(CommitteeSchedule.DEFAULT_SCHEDULE_ID);
                }
                minute.setCreateUser(GlobalVariables.getUserSession().getPrincipalName());
                minute.setUpdateUser(GlobalVariables.getUserSession().getPrincipalName());
                doUpdate = true;
            }
            if (doUpdate) {
                this.businessObjectService.save(minute);
            }
            nextEntryNumber++;
        }
    }

    /*
     * if this is IRB acknowledgement and loaded from protocol submission or notification.
     */
    private ProtocolSubmission getSubmission(Protocol protocol) {
        ProtocolSubmission protocolSubmission = protocol.getProtocolSubmission();
        if (protocol.getNotifyIrbSubmissionId() != null) {
            // not the current submission, then check programically
            for (ProtocolSubmission submission : protocol.getProtocolSubmissions()) {
                if (submission.getSubmissionId().equals(protocol.getNotifyIrbSubmissionId())) {
                    protocolSubmission = submission;
                    break;
                }
            }
        }
        return protocolSubmission;
    
    }
    
    public void persistReviewerComments(ReviewerComments reviewComments, Protocol protocol, ProtocolOnlineReview protocolOnlineReview) {
        //set the protocolOnlineReviewIdFk for each of the comments.
        for (CommitteeScheduleMinute minute : reviewComments.getComments()) {
           minute.setProtocolOnlineReview(protocolOnlineReview);
           minute.setProtocolOnlineReviewIdFk(protocolOnlineReview.getProtocolOnlineReviewId());
        }
        persistReviewerComments(reviewComments, protocol );
    }


    public void setProtocolFinderDao(ProtocolFinderDao protocolFinderDao) {
        this.protocolFinderDao = protocolFinderDao;
    }

    public void setCommitteeService(CommitteeService committeeService) {
        this.committeeService = committeeService;
    }
}
