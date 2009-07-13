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
package org.kuali.kra.irb.noteattachment;

import java.sql.Timestamp;
import java.util.LinkedHashMap;

import org.apache.ojb.broker.PersistenceBroker;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.actions.submit.ProtocolSubmission;

/**
 * This class represents the Protocol Attachment Notification.
 */
public class ProtocolAttachmentNotification extends ProtocolAttachmentBase {

    private static final long serialVersionUID = -7115904344245464654L;
    
    private Timestamp actionDate;
    private String comments;
    
    private ProtocolSubmission submission;
    private Long submissionId;
    
    /**
     * empty ctor to satisfy JavaBean convention.
     */
    public ProtocolAttachmentNotification() {
        super();
    }
    
    /**
     * Convenience ctor to set the protocol.
     * 
     * <p>
     * This ctor does not validate any of the properties.
     * </p>
     * 
     * @param protocol the protocol.
     */
    public ProtocolAttachmentNotification(final Protocol protocol) {
        super(protocol);
    }
    
    /**
     * Sets the {@link #getActionDate() actionDate} from the {@link #getUpdateTimestamp() updateTimestamp}
     * if the action date is {@code null}.
     * {@inheritDoc}
     */
    @Override
    public void beforeUpdate(PersistenceBroker persistenceBroker) {
        super.beforeUpdate(persistenceBroker);
        
        //this assumes that super.beforeUpdate(persistenceBroker); sets the time
        this.setActionDateFromUpdateTimeStamp();
    }
    
    /**
     * Sets the {@link #getActionDate() actionDate} from the {@link #getUpdateTimestamp() updateTimestamp}
     * if the action date is {@code null}.
     * {@inheritDoc}
     */
    @Override
    public void beforeInsert(PersistenceBroker persistenceBroker) {
        super.beforeInsert(persistenceBroker);
        
        //this assumes that super.beforeUpdate(persistenceBroker); sets the time
        this.setActionDateFromUpdateTimeStamp();
    }
    
    
    /** sets the action date from the update timestamp if action date is {@code null}. */
    private void setActionDateFromUpdateTimeStamp() {
        if (this.getActionDate() == null) {
            this.setActionDate(this.getUpdateTimestamp());
        }
    }
    
    /**
     * Gets the Protocol Attachment Notification action date.
     * @return the Protocol Attachment Notification action date
     */
    public Timestamp getActionDate() {
        return this.actionDate;
    }

    /**
     * Sets the Protocol Attachment Notification action date.
     * @param actionDate the Protocol Attachment Notification action date
     */
    public void setActionDate(Timestamp actionDate) {
        this.actionDate = actionDate;
    }

    /**
     * Gets the Protocol Attachment Notification Comments.
     * @return the Protocol Attachment Notification Comments
     */
    public String getComments() {
        return this.comments;
    }
    
    /**
     * Sets the Protocol Attachment Notification comments.
     * @param comments the Protocol Attachment Notification comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    /**
     * Gets the submission attribute. 
     * @return Returns the submission.
     */
    public ProtocolSubmission getSubmission() {
        return this.submission;
    }

    /**
     * Sets the submission attribute value.
     * @param submission The submission to set.
     */
    public void setSubmission(ProtocolSubmission submission) {
        this.submission = submission;
    }

    /**
     * Gets the submissionId attribute. 
     * @return Returns the submissionId.
     */
    public Long getSubmissionId() {
        return this.submissionId;
    }

    /**
     * Sets the submissionId attribute value.
     * @param submissionId The submissionId to set.
     */
    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }
    
    /** {@inheritDoc} */
    @Override
    public String getAttachmentDescription() {
        return "Notification Attachment";
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean supportsVersioning() {
        return false;
    }

    /** {@inheritDoc} */
    @Override 
    protected LinkedHashMap<String, Object> toStringMapper() {
        final LinkedHashMap<String, Object> hashMap = super.toStringMapper();

        hashMap.put(PropertyName.COMMENTS.getPropertyName(), this.getComments());
        hashMap.put(PropertyName.ACTION_DATE.getPropertyName(), this.getActionDate());

        return hashMap;
    }
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.actionDate == null) ? 0 : this.actionDate.hashCode());
        result = prime * result + ((this.comments == null) ? 0 : this.comments.hashCode());
        result = prime * result + ((this.submissionId == null) ? 0 : this.submissionId.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final ProtocolAttachmentNotification other = (ProtocolAttachmentNotification) obj;
        if (this.actionDate == null) {
            if (other.actionDate != null) {
                return false;
            }
        } else if (!this.actionDate.equals(other.actionDate)) {
            return false;
        }
        if (this.comments == null) {
            if (other.comments != null) {
                return false;
            }
        } else if (!this.comments.equals(other.comments)) {
            return false;
        }
        if (this.submissionId == null) {
            if (other.submissionId != null) {
                return false;
            }
        } else if (!this.submissionId.equals(other.submissionId)) {
            return false;
        }
        return true;
    }



    /**
     * Contains all the property names in this class.
     */
    public static enum PropertyName {
        COMMENTS("comments"), ACTION_DATE("actionDate");
        
        private final String name;
        
        /**
         * Sets the enum properties.
         * @param name the name.
         */
        PropertyName(final String name) {
            this.name = name;
        }
        
        /**
         * Gets the property name.
         * @return the the property name.
         */
        public String getPropertyName() {
            return this.name;
        }
        
        /**
         * Gets the {@link #getPropertyName() propertyName()}.
         * @return {@link #getPropertyName() propertyName()}
         */
        @Override
        public String toString() {
            return this.name;
        }
    }
}
