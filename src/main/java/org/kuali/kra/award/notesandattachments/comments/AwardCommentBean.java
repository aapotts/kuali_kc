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
package org.kuali.kra.award.notesandattachments.comments;

import java.io.Serializable;
import java.util.List;

import org.kuali.kra.award.AwardForm;
import org.kuali.kra.bo.CommentType;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.service.AwardCommentService;

/**
 * This class contains comment helper fields and methods for form and action classes
 */
public class AwardCommentBean implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -8505814106872342691L;
    private List<CommentType> awardCommentScreenDisplayTypes;
    private AwardForm form;
    
    /**
     * 
     * Constructs a AwardReportsBean.java.
     */
    public AwardCommentBean() {
        
    }
    
    /**
     * 
     * Constructs a AwardCommentBean.java.
     * @param form
     */
    public AwardCommentBean(AwardForm form) {
        this.form = form;
    }
    
    /**
     * Gets the awardCommentScreenDisplayTypes attribute. 
     * @return Returns the awardCommentScreenDisplayTypes.
     */
    public List<CommentType> getAwardCommentScreenDisplayTypes() {
        return awardCommentScreenDisplayTypes;
    }

    /**
     * Sets the awardCommentScreenDisplayTypes attribute value.
     * @param awardCommentScreenDisplayTypes The awardCommentScreenDisplayTypes to set.
     */
    public void setAwardCommentScreenDisplayTypes(List<CommentType> awardCommentScreenDisplayTypes) {
        this.awardCommentScreenDisplayTypes = awardCommentScreenDisplayTypes;
    }

    /**
     * Gets the form attribute. 
     * @return Returns the form.
     */
    public AwardForm getForm() {
        return form;
    }
    
    /**
     * Sets the form attribute value.
     * @param form The form to set.
     */
    public void setForm(AwardForm form) {
        this.form = form;
    }
    
    //
    public void setAwardCommentScreenDisplayTypesOnForm() {
        AwardCommentService awardCommentService = getAwardCommentService();
        //List<CommentType> commentTypes = awardCommentService.retrieveCommentTypes();
        //setAwardCommentScreenDisplayTypes(commentTypes);
        setAwardCommentScreenDisplayTypes(awardCommentService.retrieveCommentTypes());
    }
    
    /**
     * 
     * This method is a helper method to retrieve AwardSponsorTermService.
     * @return
     */
    protected AwardCommentService getAwardCommentService() {
        return KraServiceLocator.getService(AwardCommentService.class);
    }

}
