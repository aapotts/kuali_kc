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
package org.kuali.kra.irb.web.struts.form;

import java.util.List;

import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.irb.bo.Protocol;
import org.kuali.kra.irb.bo.ProtocolAttachmentFile;
import org.kuali.kra.irb.bo.ProtocolAttachmentPersonnel;
import org.kuali.kra.irb.bo.ProtocolAttachmentProtocol;
import org.kuali.kra.irb.document.ProtocolDocument;
import org.kuali.kra.irb.service.ProtocolNotesAndAttachmentsService;

/**
 * This is the "Helper" class so ProtocolNotesAndAttachments.
 */
public class NotesAndAttachmentsHelper {
    
    private final ProtocolNotesAndAttachmentsService notesService;
    
    /**
     * Each Helper must contain a reference to its document form
     * so that it can access the document.
     */
    private final ProtocolForm form;
    
    private ProtocolAttachmentProtocol newAttachmentProtocol;
    private ProtocolAttachmentPersonnel newAttachmentPersonnel;
    private boolean modifyProtocol;

    /**
     * Constructs a helper setting the dependencies to default values.
     * @param form the form
     * @throws NullPointerException if the form is null
     */
    NotesAndAttachmentsHelper(final ProtocolForm form) {
        this(form, KraServiceLocator.getService(ProtocolNotesAndAttachmentsService.class));
    }
    
    /**
     * Constructs a helper.
     * @param form the form
     * @param notesService the notesService
     * @throws NullPointerException if the form or notesService is null
     */
    NotesAndAttachmentsHelper(final ProtocolForm form, final ProtocolNotesAndAttachmentsService notesService) {
        if (form == null) {
            throw new NullPointerException("the form was null");
        }
        
        if (notesService == null) {
            throw new NullPointerException("the notesService was null");
        }
        
        this.form = form;
        this.notesService = notesService;
        
        this.initAttachmentProtocol();
        this.initAttachmentPersonnel();
    }
    
    /**
     * Get the Protocol.
     * @return the Protocol
     * @throws NullPointerException if the {@link ProtocolDocument ProtocolDocument}
     * or {@link Protocol Protocol} is {@code null}.
     */
    private Protocol getProtocol() {

        if (this.form.getDocument() == null) {
            throw new NullPointerException("the document is null");
        }
        
        if (this.form.getDocument().getProtocol() == null) {
            throw new NullPointerException("the protocol is null");
        }

        return this.form.getDocument().getProtocol();
    }
    
    /**
     * Gets the new attachment protocol.
     * @return the new attachment protocol
     */
    public ProtocolAttachmentProtocol getNewAttachmentProtocol() {
        return this.newAttachmentProtocol;
    }

    /**
     * Sets the new attachment protocol.
     * @param newAttachmentProtocol the new attachment protocol
     */
    public void setNewAttachmentProtocol(ProtocolAttachmentProtocol newAttachmentProtocol) {
        this.newAttachmentProtocol = newAttachmentProtocol;
    }

    /**
     * Gets the new attachment personnel.
     * @return the new attachment personnel
     */
    public ProtocolAttachmentPersonnel getNewAttachmentPersonnel() {
        return this.newAttachmentPersonnel;
    }

    /**
     * Sets the new attachment personnel.
     * @param newAttachmentPersonnel the new attachment personnel
     */
    public void setNewAttachmentPersonnel(ProtocolAttachmentPersonnel newAttachmentPersonnel) {
        this.newAttachmentPersonnel = newAttachmentPersonnel;
    }
    
    /**
     * returns whether a protocol can be modified.
     * @return true if modification is allowed false if not.
     */
    public boolean isModifyProtocol() {
        return true;
        //return modifyProtocol;
    }

    /**
     * sets whether a protocol can be modified.
     * @param modifyProtocol true if modification is allowed false if not.
     */
    public void setModifyProtocol(boolean modifyProtocol) {
        this.modifyProtocol = modifyProtocol;
    }
    
    /**
     * Adds the "new" ProtocolAttachmentProtocol to the Protocol Document.
     */
    public void addNewProtocolAttachmentProtocol() {
        
        this.getProtocol().addAttachmentProtocol(this.newAttachmentProtocol);
        
        this.notesService.saveAttatchment(this.newAttachmentProtocol);
        
        this.initAttachmentProtocol();
    }
    
    /**
     * Adds the "new" ProtocolAttachmentPersonnel to the Protocol Document.
     */
    public void addNewProtocolAttachmentPersonnel() {
        this.newAttachmentPersonnel.setType(this.notesService.getTypeFromCode(this.newAttachmentPersonnel.getTypeCode()));
        this.newAttachmentPersonnel.setFile(ProtocolAttachmentFile.createFromFormFile(this.newAttachmentPersonnel.getNewFile()));
        this.newAttachmentProtocol.setAttachmentVersionNumber(Integer.valueOf(1));
        this.newAttachmentProtocol.setDocumentId(Integer.valueOf(1));
        
        this.getProtocol().addAttachmentPersonnel(this.newAttachmentPersonnel);
        
        this.initAttachmentPersonnel();
    }
    
    /**
     * Deletes the "existing" ProtocolAttachmentProtocol from the Protocol Document.
     * If attachmentNumber is not valid then this method does returns false.  This is because
     * the item to delete comes from the client and may not be a valid item.
     * 
     * @param attachmentNumber the item to delete.
     * @return whether a delete successfully executed.
     */
    public boolean deleteExistingAttachmentProtocol(int attachmentNumber) {

        if (!this.validIndexForList(attachmentNumber, this.getProtocol().getAttachmentProtocols())) {
            return false;
        }
        
        this.getProtocol().getAttachmentProtocols().remove(attachmentNumber);
        return true;
    }
    
    /**
     * Retrieves the "existing" ProtocolAttachmentProtocol from the Protocol Document.
     * If attachmentNumber is not valid then this method returns {@code null}.  This is because
     * the item to retrieve comes from the client and may not be a valid item.
     * 
     * @param attachmentNumber the item to delete.
     * @return the ProtocolAttachmentProtocol
     */
    public ProtocolAttachmentProtocol retrieveExistingAttachmentProtocol(int attachmentNumber) {
        
        if (!this.validIndexForList(attachmentNumber, this.getProtocol().getAttachmentProtocols())) {
            return null;
        }
        
        return this.getProtocol().getAttachmentProtocols().get(attachmentNumber);
    }
    
    /**
     * Checks if a given index is valid for a given list. This method returns null if the list is null.
     * 
     * @param index the index
     * @param forList the list
     * @return true if a valid index
     */
    private boolean validIndexForList(int index, List<?> forList) {      
        return forList != null && index >= 0 && index <= forList.size() - 1;
    }
    
    private void initAttachmentProtocol() {
        this.setNewAttachmentProtocol(new ProtocolAttachmentProtocol(this.getProtocol().getProtocolId()));
    }
    
    private void initAttachmentPersonnel() {
        this.setNewAttachmentPersonnel(new ProtocolAttachmentPersonnel(this.getProtocol().getProtocolId()));
    }
}
