/*
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.kra.common.committee.bo;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.protocol.correspondence.BatchCorrespondenceBase;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.krad.service.SequenceAccessorService;

/**
 * 
 * This class implements the CommitteeBatchCorrespondenceBase business object.
 */
public abstract class CommitteeBatchCorrespondenceBase extends KraPersistableBusinessObjectBase implements Comparable<CommitteeBatchCorrespondenceBase> {

    private static final long serialVersionUID = 1L;

    private String committeeBatchCorrespondenceId;

    private String committeeId;

    private String batchCorrespondenceTypeCode;

    private Timestamp batchRunDate;

    private Date timeWindowStart;

    private Date timeWindowEnd;

    private List<CommitteeBatchCorrespondenceDetailBase> committeeBatchCorrespondenceDetails;

    private BatchCorrespondenceBase batchCorrespondence;

    private CommitteeBase committee;

    private transient int finalActionCounter;

    private transient DateTimeService dateTimeService;

    /**
     * Constructs a CommitteeBatchCorrespondenceBase.java.
     */
    public CommitteeBatchCorrespondenceBase() {
        setCommitteeBatchCorrespondenceDetails(new ArrayList<CommitteeBatchCorrespondenceDetailBase>());
    }

    /**
     * Constructs a CommitteeBatchCorrespondenceBase.java for a new request.
     * (The committeeBatchCorrespondenceId is set to the next SEQ_COMMITTEE_ID sequence number and 
     *  the batchRunDate is set to the current date.)
     * @param batchCorrespondenceTypeCode 
     * @param committeeId
     * @param startDate
     * @param endDate
     */
    public CommitteeBatchCorrespondenceBase(String batchCorrespondenceTypeCode, String committeeId, Date startDate, Date endDate) {
        this();
        setCommitteeBatchCorrespondenceId(KraServiceLocator.getService(SequenceAccessorService.class).getNextAvailableSequenceNumber("SEQ_COMMITTEE_ID").toString());
        setCommitteeId(committeeId);
        setBatchCorrespondenceTypeCode(batchCorrespondenceTypeCode);
        setBatchRunDate(getDateTimeService().getCurrentTimestamp());
        setTimeWindowStart(startDate);
        setTimeWindowEnd(endDate);
        setFinalActionCounter(0);
    }

    public String getCommitteeBatchCorrespondenceId() {
        return committeeBatchCorrespondenceId;
    }

    public void setCommitteeBatchCorrespondenceId(String committeeBatchCorrespondenceId) {
        this.committeeBatchCorrespondenceId = committeeBatchCorrespondenceId;
    }

    public String getCommitteeId() {
        return committeeId;
    }

    public void setCommitteeId(String committeeId) {
        this.committeeId = committeeId;
    }

    public String getBatchCorrespondenceTypeCode() {
        return batchCorrespondenceTypeCode;
    }

    public void setBatchCorrespondenceTypeCode(String batchCorrespondenceTypeCode) {
        this.batchCorrespondenceTypeCode = batchCorrespondenceTypeCode;
    }

    public Timestamp getBatchRunDate() {
        return batchRunDate;
    }

    public void setBatchRunDate(Timestamp batchRunDate) {
        this.batchRunDate = batchRunDate;
    }

    public String getFormattedBatchRunDate() {
        return new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_PATTERN).format(batchRunDate);
    }

    public String getFormattedBatchRunTime() {
        return new SimpleDateFormat(Constants.DEFAULT_TIME_FORMAT_PATTERN).format(batchRunDate);
    }

    public Date getTimeWindowStart() {
        return timeWindowStart;
    }

    public void setTimeWindowStart(Date timeWindowStart) {
        this.timeWindowStart = timeWindowStart;
    }

    public String getFormattedTimeWindowStart() {
        return new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_PATTERN).format(timeWindowStart);
    }

    public Date getTimeWindowEnd() {
        return timeWindowEnd;
    }

    public void setTimeWindowEnd(Date timeWindowEnd) {
        this.timeWindowEnd = timeWindowEnd;
    }

    public String getFormattedTimeWindowEnd() {
        return new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_PATTERN).format(timeWindowEnd);
    }

    public List<CommitteeBatchCorrespondenceDetailBase> getCommitteeBatchCorrespondenceDetails() {
        return committeeBatchCorrespondenceDetails;
    }

    public void setCommitteeBatchCorrespondenceDetails(List<CommitteeBatchCorrespondenceDetailBase> committeeBatchCorrespondenceDetails) {
        this.committeeBatchCorrespondenceDetails = committeeBatchCorrespondenceDetails;
    }

    public BatchCorrespondenceBase getBatchCorrespondence() {
        return batchCorrespondence;
    }

    public void setBatchCorrespondence(BatchCorrespondenceBase batchCorrespondence) {
        this.batchCorrespondence = batchCorrespondence;
    }

    public CommitteeBase getCommittee() {
        return committee;
    }

    public void setCommittee(CommitteeBase committee) {
        this.committee = committee;
    }

    public int compareTo(CommitteeBatchCorrespondenceBase arg) {
        int timeWindowStartDiff = this.getTimeWindowStart().compareTo(arg.getTimeWindowStart());
        if (timeWindowStartDiff != 0) {
            return timeWindowStartDiff;
        }
        int timeWindowEndDiff = this.getTimeWindowEnd().compareTo(arg.getTimeWindowEnd());
        if (timeWindowEndDiff != 0) {
            return timeWindowEndDiff;
        }
        int batchRunDateDiff = this.getBatchRunDate().compareTo(arg.getBatchRunDate());
        if (batchRunDateDiff != 0) {
            return batchRunDateDiff;
        }
        return this.getCommitteeBatchCorrespondenceId().compareTo(arg.getCommitteeBatchCorrespondenceId());
    }

    public int getFinalActionCounter() {
        return finalActionCounter;
    }

    public void setFinalActionCounter(int finalActionCounter) {
        this.finalActionCounter = finalActionCounter;
    }

    private DateTimeService getDateTimeService() {
        if (this.dateTimeService == null) {
            dateTimeService = KraServiceLocator.getService(DateTimeService.class);
        }
        return this.dateTimeService;
    }
}
