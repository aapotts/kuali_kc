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

import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.infrastructure.TaskName;
import org.kuali.kra.irb.Protocol;
import org.kuali.kra.irb.ProtocolDocument;
import org.kuali.kra.irb.ProtocolForm;
import org.kuali.kra.irb.auth.ProtocolTask;
import org.kuali.kra.service.TaskAuthorizationService;
import org.kuali.rice.kns.util.GlobalVariables;

/**
 * This is the "Helper" class for ProtocolNoteAndAttachment.
 */
public class ProtocolNotepadHelper {
    
    private final TaskAuthorizationService authService;
    
    private final ProtocolForm form;
    
    private ProtocolNotepad newProtocolNotepad;
    
    private boolean modifyNotepads;
    
    /**
     * Constructs a helper setting the dependencies to default values.
     * @param form the form
     * @throws IllegalArgumentException if the form is null
     */
    public ProtocolNotepadHelper(final ProtocolForm form) {
        this(form, KraServiceLocator.getService(TaskAuthorizationService.class));
    }
    
    /**
     * Constructs a helper.
     * @param form the form
     * @param authService the authService
     * @throws IllegalArgumentException if the form or authService is null
     */
    ProtocolNotepadHelper(final ProtocolForm form,
        final TaskAuthorizationService authService) {
        
        if (form == null) {
            throw new IllegalArgumentException("the form was null");
        }
        
        if (authService == null) {
            throw new IllegalArgumentException("the authService was null");
        }
        
        this.form = form;
        this.authService = authService;
    }
    
    /**
     * Prepare the tab for viewing.
     */
    public void prepareView() {
        this.initializePermissions();
    }
    
    /**
     * Initialize the permissions for viewing/editing the Custom Data web page.
     */
    private void initializePermissions() {
        this.modifyNotepads = this.canModifyProtocolNotepads();
    }
    
    /**
     * Checks if Protocol Notepads can be modified.
     * @return true if can be modified false if cannot
     */
    private boolean canModifyProtocolNotepads() {
        final ProtocolTask task = new ProtocolTask(TaskName.MODIFY_PROTOCOL_NOTEPADS, this.getProtocol());
        return this.authService.isAuthorized(this.getUserIdentifier(), task);
    }
    
    /**
     * Get the userName of the user for the current session.
     * @return the current session's userName
     */
    private String getUserIdentifier() {
        return GlobalVariables.getUserSession().getPrincipalId();
    }
    
    /**
     * Get the Protocol.
     * @return the Protocol
     * @throws IllegalArgumentException if the {@link ProtocolDocument ProtocolDocument}
     * or {@link Protocol Protocol} is {@code null}.
     */
    public Protocol getProtocol() {

        if (this.form.getDocument() == null) {
            throw new IllegalArgumentException("the document is null");
        }
        
        if (this.form.getDocument().getProtocol() == null) {
            throw new IllegalArgumentException("the protocol is null");
        }

        return this.form.getDocument().getProtocol();
    }
    
    /**
     * Gets the new attachment protocol.  This method will not return null.
     * Also, The ProtocolAttachmentProtocol should have a valid protocol Id at this point.
     * 
     * @return the new attachment protocol
     */
    public ProtocolNotepad getNewProtocolNotepad() {
        if (this.newProtocolNotepad == null) {
            this.initProtocolNotepad();
        }
        
        return this.newProtocolNotepad;
    }

    /**
     * Sets the new protocol notepad.
     * @param newProtocolNotepad the new protocol notepad
     */
    public void setNewProtocolNotepad(final ProtocolNotepad newProtocolNotepad) {
        this.newProtocolNotepad = newProtocolNotepad;
    }
    
    /**
     * returns whether a protocol can be modified.
     * @return true if modification is allowed false if not.
     */
    public boolean isModifyNotepads() {
        return this.modifyNotepads;
    }

    /**
     * sets whether a protocol can be modified.
     * @param modifyNotepads true if modification is allowed false if not.
     */
    public void setModifyNotepads(final boolean modifyNotepads) {
        this.modifyNotepads = modifyNotepads;
    }
    
    /**
     * initializes a new attachment protocol setting the protocol id.
     */
    private void initProtocolNotepad() {
        this.setNewProtocolNotepad(new ProtocolNotepad(this.getProtocol()));
    }
}
