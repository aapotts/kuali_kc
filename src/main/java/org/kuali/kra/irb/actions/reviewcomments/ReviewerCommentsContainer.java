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
package org.kuali.kra.irb.actions.reviewcomments;


/**
 * 
 * This class defines functions that need to be implemented in a "bean" that needs to provide support for reviewer comments.
 */
public interface ReviewerCommentsContainer {
    /**
     * 
     * This method needs to return the reviewer comments object held by the bean. Maintained by data entry by the remote user
     * 
     * @return ReviewComments object
     */
    ReviewComments getReviewComments();

    /**
     * 
     * This method sets the reviewer comments object of the bean, pulled from the database or by user input.
     * 
     * @param reviewComments ReviewComments object
     */
    void setReviewComments(ReviewComments reviewComments);
}
