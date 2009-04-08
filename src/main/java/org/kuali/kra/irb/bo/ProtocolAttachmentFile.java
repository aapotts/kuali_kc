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

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.apache.struts.upload.FormFile;
import org.kuali.kra.bo.KraPersistableBusinessObjectBase;

/**
 * Represents a Protocol Attachment File.
 */
public class ProtocolAttachmentFile extends KraPersistableBusinessObjectBase {
    
    private static final long serialVersionUID = 8999619585664343780L;

    private Long id;
    private String name;
    private String type;
    private byte[] data;
    
    /**
     * empty ctor to satisfy JavaBean convention.
     */
    public ProtocolAttachmentFile() {
        super();
    }
    
    /**
     * Convenience ctor to set the relevant properties of this class.
     * 
     * <p>
     * This ctor does not validate any of the properties.
     * </p>
     * 
     * @param name the name.
     * @param type the type.
     * @param data the data.
     */
    private ProtocolAttachmentFile(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = (data == null) ? null : data.clone();
    }
    
    /**
     * factory method creating an instance from a {@link FormFile FormFile}.
     * @param formFile the {@link FormFile FormFile}
     * @return an instance
     * @throws NullPointerException if the formfile is null.
     * @throws CreateException if unable to create from FormFile.
     */
    public static final ProtocolAttachmentFile createFromFormFile(FormFile formFile) {
        
        if (formFile == null) {
            throw new NullPointerException("the formFile is null");
        }
        
        try {
            return new ProtocolAttachmentFile(formFile.getFileName(), formFile.getContentType(), formFile.getFileData());
        } catch (IOException e) {
            throw new CreateException(e);
        }
    }
    
    /**
     * Gets the the Protocol Attachment File id.
     * @return the Protocol Attachment File id.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Sets the the Protocol Attachment File id.
     * @param id the Protocol Attachment File id.
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets the Protocol Attachment File name.
     * @return the Protocol Attachment File name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Sets the Protocol Attachment File name.
     * @param name the Protocol Attachment File name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the Protocol Attachment File type.
     * @return the Protocol Attachment File type
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Sets the Protocol Attachment File type.
     * @param type the Protocol Attachment File type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Gets the Protocol Attachment File data.
     * @return the Protocol Attachment File data
     */
    public byte[] getData() {
        return this.data;
    }
    
    /**
     * Sets the Protocol Attachment File data.
     * @param data the Protocol Attachment File data
     */
    public void setData(byte[] data) {
        this.data = data;
    }
    
    /** {@inheritDoc} */
    @Override 
    protected LinkedHashMap<String, Object> toStringMapper() {
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        hashMap.put("id", this.getId());
        hashMap.put("name", this.getName());
        hashMap.put("contentType", this.getType());
        return hashMap;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.data);
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ProtocolAttachmentFile)) {
            return false;
        }
        ProtocolAttachmentFile other = (ProtocolAttachmentFile) obj;
        if (!Arrays.equals(this.data, other.data)) {
            return false;
        }
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!this.type.equals(other.type)) {
            return false;
        }
        return true;
    }
    
    /**
     * Exception thrown when unable to create instance from static factory.
     */
    public static class CreateException extends RuntimeException {
        
        /**
         * Wraps caused-by Throwable.
         * @param t the Throwable
         */
        public CreateException(Throwable t) {
            super(t);
        }
    }
}
