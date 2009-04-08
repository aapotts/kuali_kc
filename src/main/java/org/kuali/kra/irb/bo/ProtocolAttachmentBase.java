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
package org.kuali.kra.irb.bo;

import java.util.LinkedHashMap;

import org.apache.struts.upload.FormFile;
import org.kuali.kra.bo.KraPersistableBusinessObjectBase;

/**
 * This is the base class for all Protocol Attachments.
 */
public abstract class ProtocolAttachmentBase extends KraPersistableBusinessObjectBase {

    private static final long serialVersionUID = -2519574730475246022L;

    private Long id;
    
    private Long protocolId;
    private Protocol protocol;
    
    private String typeCode;
    private ProtocolAttachmentType type;
    
    private Integer attachmentVersionNumber;
    private Integer documentId;
    
    private Long fileId;
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
     * Convenience ctor to set the protocol id.
     * 
     * <p>
     * This ctor does not validate any of the properties.
     * </p>
     * 
     * @param protocolId the protocol Id.
     */
    public ProtocolAttachmentBase(final Long protocolId) {
        this.protocolId = protocolId;
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
     * @return the Protocol id
     */
    public Long getProtocolId() {
        return this.protocolId;
    }
    
    /**
     * Sets the Protocol id.
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
     * Sets the Protocol.
     * @param protocol the Protocol
     */
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }
    
    /**
     * Gets the Protocol Attachment Base Type code.
     * @return the Protocol Attachment Base Type code
     */
    public String getTypeCode() {
        return this.typeCode;
    }
    
    /**
     * Sets the Protocol Attachment Base Type code.
     * @param typeCode the Protocol Attachment Base Type code
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
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
     * Gets the Protocol Attachment Base File Id.
     * @return the Protocol Attachment Base File Id
     */
    public Long getFileId() {
        return this.fileId;
    }
    
    /**
     * Sets the Protocol Attachment Base File Id.
     * @param fileId the Protocol Attachment Base File Id
     */
    public void setFileId(Long fileId) {
        this.fileId = fileId;
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
    
    /** {@inheritDoc} */
    @Override 
    protected LinkedHashMap<String, Object> toStringMapper() {
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        hashMap.put("attachmentVersionNumber", this.getAttachmentVersionNumber());
        hashMap.put("description", this.getDescription());
        hashMap.put("documentId", this.getDocumentId());
        hashMap.put("fileId", this.getFileId());
        hashMap.put("id", this.getId());
        hashMap.put("protocolId", this.getProtocolId());
        hashMap.put("typeCode", this.getTypeCode());
        return hashMap;
    }
}
