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

import java.util.Collection;
import java.util.LinkedHashMap;

import org.apache.struts.upload.FormFile;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.ProtocolAssociate;

/**
 * This is the base class for all Protocol Attachments.
 */
public abstract class ProtocolAttachmentBase extends ProtocolAssociate {

    private static final long serialVersionUID = -2519574730475246022L;

    private Long id;
    
    private Long protocolId;
    private Protocol protocol;
    
    private ProtocolAttachmentType type;
    
    private Integer attachmentVersionNumber;
    private Integer documentId;
    
    private ProtocolAttachmentFile file;
    private transient FormFile newFile;
    
    private String description;
    
    /**
     * empty ctor to satisfy JavaBean convention.
     */
    public ProtocolAttachmentBase() {
        super();
    }
    
    /**
     * Convenience ctor to set the protocol, protocol id and the protocolNumber from the passed in protocol.
     * 
     * <p>
     * This ctor does not validate any of the properties.
     * </p>
     * 
     * @param protocol the protocol.
     */
    public ProtocolAttachmentBase(final Protocol protocol) {
        this.protocol = protocol;
        this.initProtocolInfo(protocol);
    }
    
    /**
     * Gets the Protocol Attachment Base id.
     * @return the Protocol Attachment Base id
     */
    public Long getId() {
        return this.id;
    }
    
    /**
     * Sets the Protocol Attachment Base id.
     * @param id the Protocol Attachment Base id
     */
    public void setId(Long id) {
        this.id = id;
    }
       
    /**
     * Gets the Protocol id.
     * @return the Protocol
     */
    public Long getProtocolId() {
        return this.protocolId;
    }
    
    /**
     * Sets the Protocol & and the protocolNumber from the passed in protocol.
     * @param protocolId the Protocol id
     */
    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }
    
    /**
     * Gets the Protocol.
     * @return the Protocol
     */
    public Protocol getProtocol() {
        return this.protocol;
    }
    
    /**
     * Sets the Protocol & and the protocolNumber from the passed in protocol.
     * @param protocol the Protocol
     */
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
        this.initProtocolInfo(protocol);
    }
    
    /**
     * Gets the Protocol Attachment Base Type.
     * @return the Protocol Attachment Base Type
     */
    public ProtocolAttachmentType getType() {
        return this.type;
    }
    
    /**
     * Sets the Protocol Attachment Base Type.
     * @param type the Protocol Attachment Base Type
     */
    public void setType(ProtocolAttachmentType type) {
        this.type = type;
    }
    
    /**
     * Gets the Protocol Attachment Base Version Number.
     * @return the Protocol Attachment Base Version Number
     */
    public Integer getAttachmentVersionNumber() {
        return this.attachmentVersionNumber;
    }
    
    /**
     * Sets the Protocol Attachment Base Attachment Version Number.
     * @param attachmentVersionNumber the Protocol Attachment Base Attachment Version Number
     */
    public void setAttachmentVersionNumber(Integer attachmentVersionNumber) {
        this.attachmentVersionNumber = attachmentVersionNumber;
    }
    
    /**
     * Gets the Protocol Attachment Base Document Id.
     * @return the Protocol Attachment Base Document Id
     */
    public Integer getDocumentId() {
        return this.documentId;
    }
    
    /**
     * Sets the Protocol Attachment Base Document Id.
     * @param documentId the Protocol Attachment Base Document Id
     */
    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }
    
    /**
     * Gets the Protocol Attachment Base File.
     * @return the Protocol Attachment Base File
     */
    public ProtocolAttachmentFile getFile() {
        return this.file;
    }
    
    /**
     * Sets the Protocol Attachment Base File.
     * @param file the Protocol Attachment Base File
     */
    public void setFile(ProtocolAttachmentFile file) {
        this.file = file;
    }
    
    /**
     * Gets the Protocol Attachment Base Description.
     * @return the Protocol Attachment Base Description
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * Sets the Protocol Attachment Base Description.
     * @param description the Protocol Attachment Base Description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Gets the Protocol Attachment Base New File.
     * @return the Protocol Attachment Base New File
     */
    public FormFile getNewFile() {
        return this.newFile;
    }

    /**
     * Sets the Protocol Attachment Base New File.
     * @param newFile the Protocol Attachment Base New File
     */
    public void setNewFile(FormFile newFile) {
        this.newFile = newFile;
    }
    
    /**
     * The group code that the Protocol Attachment belongs to.
     * The group code relates to {@link ProtocolAttachmentGroup ProtocolAttachmentGroup}.
     * 
     * @return the group code.
     */
    //this would be just as good as a static method except they cannot be abstract.
    public abstract String getGroupCode();
    
    /** {@inheritDoc} */
    @Override 
    protected LinkedHashMap<String, Object> toStringMapper() {
        LinkedHashMap<String, Object> hashMap = super.toStringMapper();
        hashMap.put(PropertyName.ATTACHMENT_VERSION.getPropertyName(), this.getAttachmentVersionNumber());
        hashMap.put(PropertyName.DESCRIPTION.getPropertyName(), this.getDescription());
        hashMap.put(PropertyName.DOCUMENT_ID.getPropertyName(), this.getDocumentId());
        hashMap.put(PropertyName.FILE.getPropertyName(), this.getFile());
        hashMap.put(PropertyName.ID.getPropertyName(), this.getId());
        hashMap.put(PropertyName.TYPE.getPropertyName(), this.getType());
        hashMap.put(PropertyName.GROUP_CODE.getPropertyName(), this.getGroupCode());
        return hashMap;
    }
    
    /**
     * Sets the protocol id and protocolNumber from the passed in protocol.
     * @param aProtocol the Protocol
     */
    private void initProtocolInfo(Protocol aProtocol) {
        this.setProtocolId(aProtocol.getProtocolId());
        this.setProtocolNumber(aProtocol.getProtocolNumber());
    }
    
    /**
     * Adds an attachment to a Collection.
     * @param <T> the type of attachment
     * @param attachment the attachment.
     * @param toCollection the Collection.
     * @throws IllegalArgumentException if the attachment or the list is null.
     */
    public static <T extends ProtocolAttachmentBase> void addAttachmentToCollection(T attachment, Collection<T> toCollection) {
        if (attachment == null) {
            throw new IllegalArgumentException("the attachment is null");
        }
        
        if (toCollection == null) {
            throw new IllegalArgumentException("the toList is null");
        }
        
        toCollection.add(attachment);
    }
    
    /**
     * Contains all the property names in this class.
     */
    public static enum PropertyName {
        ATTACHMENT_VERSION("attachmentVersionNumber"), DESCRIPTION("description"), DOCUMENT_ID("documentId"),
        FILE("file"), ID("id"), PROTOCOL_ID("protocolId"), TYPE("type"), GROUP_CODE("groupCode");
        
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
