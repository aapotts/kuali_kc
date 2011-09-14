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
package org.kuali.kra.negotiations.web.struts.form;

import java.io.Serializable;
import java.util.Date;

import org.kuali.kra.bo.AttachmentFile;
import org.kuali.kra.negotiations.bo.NegotiationActivity;
import org.kuali.kra.negotiations.bo.NegotiationActivityAttachment;

/**
 * Form helper to manage activities and attachments.
 */
public class NegotiationActivityHelper implements Serializable {
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 8707454680500119142L;
    private NegotiationForm form;
    private NegotiationActivity newActivity;
    
    /**
     * 
     * Constructs a NegotiationActivityHelper.java.
     * @param form
     */
    public NegotiationActivityHelper(NegotiationForm form) {
        this.form = form;
    }
    
    /**
     * Add the newActivity to the negotiation on the form also set as updated so during the
     * execute phase timestamps and last user update is updated.
     */
    public void addActivity() {
        //TODO: Add validations
        newActivity.setCreateDate(new java.sql.Date(new Date().getTime()));
        //set as updated so action code elsewhere will update references and modified timestamps
        newActivity.setUpdated(true);
        form.getNegotiationDocument().getNegotiation().getActivities().add(newActivity);
        newActivity = new NegotiationActivity();
    }
    
    /**
     * Set the restricted flag on the activity at the activityIndex.
     * @param restricted
     * @param activityIndex
     */
    public void setRestrictedActivity(Boolean restricted, int activityIndex) {
        getActivity(activityIndex).setRestricted(restricted);
    }
    
    /**
     * Find the activity at the specified index on the negotiation. If the index is -1,
     * the newActivity is returned.
     * @param activityIndex
     * @return
     */
    protected NegotiationActivity getActivity(int activityIndex) {
        if (activityIndex == -1) {
            return newActivity;
        } else {
            return form.getNegotiationDocument().getNegotiation().getActivities().get(activityIndex);
        }     
    }
    
    /**
     * Add the newAttachment on the activity found at activityIndex.
     * @param activityIndex
     */
    public void addAttachment(int activityIndex) {
        //TODO add attachment validations
        NegotiationActivity activity = getActivity(activityIndex);
        NegotiationActivityAttachment attachment = activity.getNewAttachment();
        attachment.setFile(AttachmentFile.createFromFormFile(attachment.getNewFile()));
        attachment.setActivity(activity);
        activity.add(attachment);
        activity.setNewAttachment(new NegotiationActivityAttachment());
    }
    
    /**
     * Delete the attachment on the activity at the specified indexes.
     * 
     * @param activityIndex
     * @param attachmentIndex
     */
    public void deleteAttachment(int activityIndex, int attachmentIndex) {
        NegotiationActivity activity = getActivity(activityIndex);
        activity.getAttachments().remove(attachmentIndex);
    }
    
    /**
     * Set the specified attachment's restricted flag.
     * @param restricted
     * @param activityIndex
     * @param attachmentIndex
     */
    public void setRestrictedAttachment(Boolean restricted, int activityIndex, int attachmentIndex) {
        NegotiationActivity activity = getActivity(activityIndex);
        NegotiationActivityAttachment attachment = activity.getAttachments().get(attachmentIndex);
        attachment.setRestricted(restricted);
    }

    public NegotiationForm getForm() {
        return form;
    }
    public void setForm(NegotiationForm form) {
        this.form = form;
    }
    public NegotiationActivity getNewActivity() {
        return newActivity;
    }
    public void setNewActivity(NegotiationActivity newActivity) {
        this.newActivity = newActivity;
    }
    
    

}
