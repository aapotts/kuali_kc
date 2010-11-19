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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.kuali.kra.meeting.CommitteeScheduleMinute;
import org.kuali.kra.meeting.MinuteEntryType;

/**
 * This class defines functions that need to be implemented in a "bean" that needs to provide support for reviewer comments.
 */
public class ReviewCommentsBean implements Serializable {

    private static final long serialVersionUID = -9167144271091192973L;

    private String errorPropertyName;
    
    private CommitteeScheduleMinute newReviewComment;
    private List<CommitteeScheduleMinute> reviewComments;
    private List<CommitteeScheduleMinute> deletedReviewComments;
    
    /**
     * Constructs a ReviewerCommentsBean.
     */
    public ReviewCommentsBean(String errorPropertyName) {
        this.errorPropertyName = errorPropertyName;
        
        this.newReviewComment = new CommitteeScheduleMinute();
        this.newReviewComment.setMinuteEntryTypeCode(MinuteEntryType.PROTOCOL);
        this.reviewComments = new ArrayList<CommitteeScheduleMinute>();
        this.deletedReviewComments = new ArrayList<CommitteeScheduleMinute>();
    }
    
    public void setErrorPropertyName(String errorPropertyName) {
        this.errorPropertyName = errorPropertyName;
    }

    public String getErrorPropertyName() {
        return errorPropertyName;
    }
    
    public CommitteeScheduleMinute getNewReviewComment() {
        return newReviewComment;
    }
    
    public void setNewReviewComment(CommitteeScheduleMinute newReviewComment) {
        this.newReviewComment = newReviewComment;
    }
    
    public List<CommitteeScheduleMinute> getReviewComments() {
        return reviewComments;
    }
    
    public void setReviewComments(List<CommitteeScheduleMinute> reviewComments) {
        this.reviewComments = reviewComments;
    }
    
    public List<CommitteeScheduleMinute> getDeletedReviewComments() {
        return deletedReviewComments;
    }
    
    public void setDeletedReviewComments(List<CommitteeScheduleMinute> deletedReviewComments) {
        this.deletedReviewComments = deletedReviewComments;
    }
    
}